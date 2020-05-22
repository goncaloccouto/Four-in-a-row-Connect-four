import java.util.*;

class TreeNode{
  Board root;
  TreeNode[] filhos = new TreeNode[7];
  TreeNode parent;
  double nvisits, totvalue;
  int columnMostValuable;
  double ucbtValue;
  char ultimoJogador;
  int nodesGenerated = 0;

  TreeNode(Board tabuleiroInicial, TreeNode parent){
    this.root = tabuleiroInicial;
    this.nvisits = 0;
    this.totvalue = 0;
    this.parent =  parent;
    this.columnMostValuable = -1;
    this.ucbtValue = 0;
    if(parent != null){
      if(parent.ultimoJogador == 'J' && parent.ultimoJogador == 'C')
        this.ultimoJogador = 'C';
      else if(parent.ultimoJogador == 'C')
        this.ultimoJogador = 'J';
    }
    else
      this.ultimoJogador = 'J';
  }

  Random rand = new Random();
  int contaC = 1;
  int contaO = 1;

  void selectAction(){
    List<TreeNode> visited = new LinkedList<>();
    TreeNode cur = this;
    visited.add(cur);
    while(!cur.isLeaf()){
      cur = cur.select();
      visited.add(cur);
    }
    cur = cur.expand();
    visited.add(cur);
    TreeNode auxForRollOut = new TreeNode(cur.root,null);
    String colunaPoints = checkGamePoints(auxForRollOut.root);
    if(colunaPoints != null){
      int colunaAjogar = Character.getNumericValue(colunaPoints.charAt(1));
      if(colunaPoints.charAt(0) == 'C'){
        auxForRollOut.root.colocaPeca(colunaAjogar,'C');
        auxForRollOut.ultimoJogador = 'C';
      }
      else if(colunaPoints.charAt(0) == 'J'){
        auxForRollOut.root.colocaPeca(colunaAjogar,'C');
        auxForRollOut.ultimoJogador = 'C';
      }
    }
    int value = 0;
    if(colunaPoints==null)
      value = auxForRollOut.rollOut();
    else if(colunaPoints!=null && colunaPoints.charAt(0)=='C')
      value = 1;
    if(colunaPoints==null || (colunaPoints!=null && colunaPoints.charAt(0)=='C')){
      for(TreeNode node : visited){
        if(node.ultimoJogador == 'C')
          node.updateStats(value);
        else
          node.nvisits++;
      }
    }
    this.nodesGenerated += (cur.nodesGenerated + auxForRollOut.nodesGenerated);
  }

  TreeNode expand(){
    char jogada = 'J';
    if(gameState(this.root)!=-1)
      return this;
    else{
      for(int i = 0; i < 7; i++){
        if(this.root.checkIsFullColumn(i)==false){
          Board newtabuleiro = this.root.copyBoard(i);
          if(this.ultimoJogador == 'C'){
            jogada = 'J';
            newtabuleiro.colocaPeca(i,'J');
          }
          else if(this.ultimoJogador == 'J'){
            jogada = 'C';
            newtabuleiro.colocaPeca(i,'C');
          }
          filhos[i] = new TreeNode(newtabuleiro,this);
        }
      }
      int aux = rand.nextInt(7);
      while(filhos[aux] == null){
        aux = rand.nextInt(7);
      }
      filhos[aux].ultimoJogador = jogada;
      this.nodesGenerated++;
      return filhos[aux];
    }
  }

  TreeNode select(){
    TreeNode selected = null;
    double bestValue1 = Double.MIN_VALUE;
    double bestValue2 = Double.MAX_VALUE;
    boolean naoVisitado = false;
    for(TreeNode c : filhos){
      if(c != null){
        if((c.nvisits * 100) < this.nvisits  &&  c.nvisits > 0){
          c.ucbtValue = (c.totvalue/c.nvisits) + (5 * Math.sqrt( (2*Math.log(this.nvisits)) / c.nvisits));
          if(c.ultimoJogador == 'J' || c.ultimoJogador == 'C'){
            if(c.ucbtValue > bestValue1){
              selected = c;
              bestValue1 = c.ucbtValue;
            }
          }
        }
        else
          naoVisitado = true;
      }
      if(naoVisitado == true)
        selected = null;
      if(selected == null){
        int aux = rand.nextInt(7);
        while(filhos[aux] == null)
          aux = rand.nextInt(7);
        selected = filhos[aux];
      }
    }
    return selected;
  }

  boolean isLeaf(){
    for(int i=0;i<7;i++)
      if(filhos[i] != null)
        return false;
    return true;
  }

  int rollOut(){
    while(gameState(this.root) == -1){
      int column = rand.nextInt(7);
      while(this.root.checkIsFullColumn(column)==true){
        column = rand.nextInt(7);
      }
      Board newtabuleiro = this.root.copyBoard(column);
      if(this.ultimoJogador == 'C'){
        this.root.colocaPeca(column,'J');
        contaO++;
        this.ultimoJogador = 'J';
      }
      else{
        this.root.colocaPeca(column,'C');
        contaC++;
        this.ultimoJogador = 'C';
      }
    }
    if(gameState(this.root) == 512){
      return 1;
    }
    return 0;
  }

  void updateStats(double value){
    nvisits++;
    totvalue += value;
  }

  int checkSecondaryDiagonalPoints(Board tabuleiro, int x, int y){
    int counter=0, counterX=0, counterO=0;
    for(int i=y; i<y+4; i++){
      if(i<7 && x<6){
        if(tabuleiro.matrix[x][i] == 'X')
          counterX++;
        else if (tabuleiro.matrix[x][i] == 'O')
          counterO++;
        counter++;
      }
      x++;
    }

    if(counter == 4){
      if(counterX == 0 && counterO != 0){
        if(counterO == 1)
          return -1;
        else if(counterO == 2)
          return -10;
        else if(counterO == 3)
          return -50;
      }

      if(counterO == 0 && counterX != 0){
        if(counterX == 1)
          return 1;
        else if(counterX == 2)
          return 10;
        else if(counterX == 3)
          return 50;
      }
    }
    return 0;
  }

  int checkMainDiagonalPoints(Board tabuleiro, int x, int y){
    int counter=0, counterX=0, counterO=0;
    for(int i=y; i>y-4; i--){
      if(i>=0 && x<6){
        if(tabuleiro.matrix[x][i] == 'X')
          counterX++;
        else if (tabuleiro.matrix[x][i] == 'O')
          counterO++;
        counter++;
      }
      x++;
    }

    if(counter == 4){
      if(counterX == 0 && counterO != 0){
        if(counterO == 1)
          return -1;
        else if(counterO == 2)
          return -10;
        else if(counterO == 3)
          return -50;
      }

      if(counterO == 0 && counterX != 0){
        if(counterX == 1)
          return 1;
        else if(counterX == 2)
          return 10;
        else if(counterX == 3)
          return 50;
      }
    }
    return 0;
  }

  int checkSecondaryDiagonalWin(Board tabuleiro, int x, int y){
    int counterX=0, counterO=0;
    for (int i=y; i<y+4; i++){
      if(i<7 && x<6){
        if(tabuleiro.matrix[x][i] == 'X')
          counterX++;
        else if(tabuleiro.matrix[x][i] == 'O')
          counterO++;
      }
      x++;
    }
    if(counterX == 4)
      return 512;
    else if(counterO == 4)
      return -512;
    else
      return 0;
  }

  int checkMainDiagonalWin(Board tabuleiro, int x, int y){
    int counterX=0, counterO=0;
    for (int i=y; i>y-4; i--){
      if(i>=0 && x<6){
        if(tabuleiro.matrix[x][i] == 'X')
          counterX++;
        else if(tabuleiro.matrix[x][i] == 'O')
          counterO++;
      }
      x++;
    }
    if(counterX == 4)
      return 512;
    else if(counterO == 4)
      return -512;
    else
      return 0;
  }

  int checkColumnPoints(Board tabuleiro, int x, int y){
    int counter=0, counterX=0, counterO=0;
    for(int i=x; i<x+4; i++){
      if(i<6){
        if(tabuleiro.matrix[i][y] == 'X')
          counterX++;
        else if (tabuleiro.matrix[i][y] == 'O')
          counterO++;
        counter++;
      }
    }

    if(counter == 4){
      if(counterX == 0 && counterO != 0){
        if(counterO == 1)
          return -1;
        else if(counterO == 2)
          return -10;
        else if(counterO == 3)
          return -50;
      }

      if(counterO == 0 && counterX != 0){
        if(counterX == 1)
          return 1;
        else if(counterX == 2)
          return 10;
        else if(counterX == 3)
          return 50;
      }
    }
    return 0;
  }

  int checkColumnWin(Board tabuleiro, int x, int y){
    int counterX=0, counterO=0;
    for (int i=x; i<x+4; i++){
      if(i<6){
        if(tabuleiro.matrix[i][y] == 'X')
          counterX++;
        else if(tabuleiro.matrix[i][y] == 'O')
          counterO++;
      }
    }
    if(counterX == 4)
      return 512;
    else if(counterO == 4)
      return -512;
    else
      return 0;
  }

  int checkRowPoints(Board tabuleiro, int x, int y){
    int counter=0, counterX=0, counterO=0;
    for(int i=y; i<y+4; i++){
      if(i<7){
        if(tabuleiro.matrix[x][i] == 'X')
          counterX++;
        else if (tabuleiro.matrix[x][i] == 'O')
          counterO++;
        counter++;
      }
    }

    if(counter == 4){
      if(counterX == 0 && counterO != 0){
        if(counterO == 1)
          return -1;
        else if(counterO == 2)
          return -10;
        else if(counterO == 3)
          return -50;
      }

      if(counterO == 0 && counterX != 0){
        if(counterX == 1)
          return 1;
        else if(counterX == 2)
          return 10;
        else if(counterX == 3)
          return 50;
      }
    }
    return 0;
  }

  int checkRowWin(Board tabuleiro, int x, int y){
    int counterX=0, counterO=0;
    for (int i=y; i<y+4; i++){
      if(i<7){
        if(tabuleiro.matrix[x][i] == 'X')
          counterX++;
        else if(tabuleiro.matrix[x][i] == 'O')
          counterO++;
      }
    }
    if(counterX == 4)
      return 512;
    else if(counterO == 4)
      return -512;
    else
      return 0;
  }

  String checkGamePoints(Board tabuleiro){
    String res = "";
    for(int i = 0; i < 6; i++){
      for(int j = 0; j < 7; j++){
        if(checkRowPoints(tabuleiro,i,j) == 50 || checkColumnPoints(tabuleiro,i,j) == 50 || checkMainDiagonalPoints(tabuleiro,i,j)==50 || checkSecondaryDiagonalPoints(tabuleiro,i,j)==50){
          res += "C";
          res += j;
          return res;
        }
        if(checkRowPoints(tabuleiro,i,j)==-50 || checkColumnPoints(tabuleiro,i,j)==-50 || checkMainDiagonalPoints(tabuleiro,i,j)==-50 || checkSecondaryDiagonalPoints(tabuleiro,i,j)==-50){
          res += "J";
          res += j;
          return res;
        }
      }
    }
    return null;
  }

  int gameState(Board tabuleiro){
    int win;
    for(int i = 0; i < 6; i++){
      for(int j = 0; j < 7; j++){
        if((win = checkRowWin(tabuleiro, i, j)) != 0) return win;
        else if((win = checkColumnWin(tabuleiro, i, j)) != 0) return win;
        else if((win = checkSecondaryDiagonalWin(tabuleiro, i, j)) != 0) return win;
        else if((win = checkMainDiagonalWin(tabuleiro, i, j)) != 0) return win;
      }
    }
    if(tabuleiro.isBoardFull() == true)
      return(0);
    return -1;
  }

}

class MonteCarlo{

  Board tabuleiro;
  TreeNode treeNode;
  int nextMoveColumn;
  int nosGerados=0;

  MonteCarlo(Board tabuleiro){
    this.tabuleiro = tabuleiro;
    nextMoveColumn = MonteCarlo_Visited();
  }

  int MonteCarlo_Visited(){
    treeNode = new TreeNode(this.tabuleiro,null);
    int i = 0;
    while(i<10000){
      treeNode.selectAction();
      nosGerados += treeNode.nodesGenerated;
      for(int j=0;j<7;j++){
        if(treeNode.filhos[j] != null){
        }
      }
      i++;
    }
    nextMoveColumn = 6;
    double best = Double.MIN_VALUE;
    for(int j=0;j<7;j++){
      if(treeNode.filhos[j] != null){
        TreeNode c = treeNode.filhos[j];
        double valor = (c.totvalue/c.nvisits) + (5 * Math.sqrt( (2*Math.log(treeNode.nvisits)) / c.nvisits));
        if(valor > best){
          best = valor;
          nextMoveColumn = j;
        }
      }
    }
    return nextMoveColumn;
  }

  int whoWins(){
    int winner;
    if((winner = treeNode.gameState(this.tabuleiro)) != 0) return winner;

    else if(this.tabuleiro.isBoardFull()) return 0;

    else return -1;
  }
}
