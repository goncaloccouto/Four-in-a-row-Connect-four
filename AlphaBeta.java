import java.util.*;

class AlphaBeta{
  Board tabuleiro;
  int nextMoveColumn;
  int nodeCounter = 0;

  AlphaBeta(Board tabuleiro){
    this.tabuleiro = tabuleiro;
    nextMoveColumn = -1;
    valueAlphaBeta(tabuleiro,Integer.MIN_VALUE,Integer.MAX_VALUE,true);
  }

  int getNextMove(){
    return nextMoveColumn;
  }

  int valueAlphaBeta(Board tabuleiro, int alpha, int beta, boolean isMax){
    int val = whoWins(tabuleiro);
    if(val != -1){
      if(val == 1)
      return gameUtility(tabuleiro);
      else
      return val;
    }
    nodeCounter++;
    if(isMax == true){
      int best = Integer.MIN_VALUE;
      for(int j = 6; j >= 0; j--){
        for(int i = 5; i >= 0; i--){
          Board newtabuleiro = tabuleiro.copyBoard(j);
          if(tabuleiro.matrix[i][j] == ' '){
            newtabuleiro.colocaPeca(j,'C');
            int best2 = Math.max(best,valueAlphaBeta(newtabuleiro,alpha,beta,!isMax));
            if(best != best2){
              newtabuleiro.custo = best2;
              best = best2;
              if(newtabuleiro.depth == 1 || tabuleiro.depth == 0)
              nextMoveColumn = newtabuleiro.move;
            }
            if(best >= beta)
            return best;
            alpha = Math.max(alpha,best);
            break;
          }
        }
      }
      return best;
    }
    else{
      int best = Integer.MAX_VALUE;
      for(int j = 6; j >= 0; j--){
        for(int i = 5; i >= 0; i--){
          Board newtabuleiro = tabuleiro.copyBoard(j);
          if(tabuleiro.matrix[i][j] == ' '){
            newtabuleiro.colocaPeca(j,'J');
            best = Math.min(best,valueAlphaBeta(newtabuleiro,alpha,beta,!isMax));
            if(best <= alpha)
            return best;
            beta = Math.min(beta,best);
            break;
          }
        }
      }
      return best;
    }
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
    return(0);
  }

  int gameUtility(Board tabuleiro){
    int utility = 0;
    for(int i = 0; i < 6; i++){
      for(int j = 0; j < 7; j++){
        int utilRow = checkRowPoints(tabuleiro, i, j);
        int utilCol = checkColumnPoints(tabuleiro, i, j);
        int utilMainD = checkMainDiagonalPoints(tabuleiro, i, j);
        int utilSecD = checkSecondaryDiagonalPoints(tabuleiro, i, j);
        utility += utilRow + utilCol + utilMainD + utilSecD;
      }
    }
    return utility;
  }

  int whoWins(Board tabuleiro){
    int winner;
    if((winner = gameState(tabuleiro)) != 0) return winner;

    else if(tabuleiro.isBoardFull()) return 0;

    else if(tabuleiro.depth >= 7) return 1;

    else return -1;
  }

}
