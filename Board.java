import java.util.Scanner;

class Board{
  Scanner scan = new Scanner(System.in);
  char [][] matrix = new char[6][7];
  int depth;
  int custo;
  char pecaJogador;
  char pecaComputador;
  int move;

  Board(int depth, int custo, char pecaJogador,int move){
    for(int i = 0; i < 6; i++){
      for(int j = 0; j < 7; j++){
        matrix[i][j] = ' ';
      }
    }

    this.depth = depth;
    this.custo = custo;
    this.move = move;
    this.pecaJogador = pecaJogador;
    if(pecaJogador == 'X')
    pecaComputador = 'O';
    else
    pecaComputador = 'X';
  }

  char[][] getValue(){
    return this.matrix;
  }

  boolean isBoardFull(){
    for(int j = 0; j < 7; j++){
      if(this.matrix[0][j] == ' '){
        return false;
      }
    }
    return true;
  }

  void colocaPeca(int column, char jogador){
    if(checkIsFullColumn(column) == false){
      for(int i=5;i>=0;i--)
        if(this.matrix[i][column] == ' '){
          if(jogador == 'J')
            this.matrix[i][column] = this.pecaJogador;
          else
            this.matrix[i][column] = this.pecaComputador;
      return;
      }
    }
    else{
      if(jogador == 'J'){
        System.out.println("Coluna cheia! Escolhe outra");
        column = scan.nextInt();
        colocaPeca(column,jogador);
      }
    }
  }

  boolean checkIsFullColumn(int column){
    if(this.matrix[0][column] == ' ')
      return false;
    return true;
  }

  Board copyBoard(int move){
    Board copia = new Board(this.depth+1,this.custo,this.pecaJogador,move);
    for(int i=0;i<6;i++){
      for(int j=0;j<7;j++){
        copia.matrix[i][j] = this.matrix[i][j];
      }
    }
    return copia;
  }

  void printBoard(){
    System.out.println("  0   1   2   3   4   5   6  ");
    for(int i = 0; i < 6; i++){
      System.out.println("-----------------------------");
      System.out.print("| ");
      for(int j = 0; j < 7; j++){
        System.out.printf("%1c", matrix[i][j]);
        System.out.print(" | ");
      }
      System.out.println();
    }
    System.out.println("-----------------------------");
  }
}
