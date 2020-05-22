import java.util.*;
import java.lang.management.*;

public class QuatroEmLinha{
  public static void main(String args[]){
    Scanner in = new Scanner(System.in);
    int x, y;
    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_YELLOW = "\u001B[33m";
    String ANSI_BLUE = "\u001B[34m";
    String ANSI_PURPLE = "\u001B[35m";
    String ANSI_CYAN = "\u001B[36m";
    System.out.println(ANSI_RED + "  _____ ____  _   _ _   _ ______ _____ _______   ______ ____  _    _ _____   " + ANSI_RESET);
    System.out.println(ANSI_GREEN + " / ____/ __ \\| \\ | | \\ | |  ____/ ____|__   __| |  ____/ __ \\| |  | |  __ \\  " + ANSI_RESET);
    System.out.println(ANSI_YELLOW + "| |   | |  | |  \\| |  \\| | |__ | |       | |    | |__ | |  | | |  | | |__) | " + ANSI_RESET);
    System.out.println(ANSI_BLUE + "| |   | |  | | . ` | . ` |  __|| |       | |    |  __|| |  | | |  | |  _  /  " + ANSI_RESET);
    System.out.println(ANSI_PURPLE + "| |___| |__| | |\\  | |\\  | |___| |____   | |    | |   | |__| | |__| | | \\ \\  " + ANSI_RESET);
    System.out.println(ANSI_CYAN + " \\_____\\____/|_| \\_|_| \\_|______\\_____|  |_|    |_|    \\____/ \\____/|_|  \\_\\ " + ANSI_RESET);
    System.out.println();
    System.out.println();
    Board tabuleiro = new Board(0, 0, 'O', -1);
    tabuleiro.printBoard();
    System.out.println("Insira J para Jogador comecar ou C para computador comecar");
    char firstToPlay = in.next().charAt(0);
    System.out.println("Que algoritmo pretende utilizar?  1- MinMax  2- AlphaBeta  3- MCTS");
    int algoritmo = in.nextInt();
    int aux;
    int jogada;
    if(firstToPlay == 'J'){
      System.out.println("Escolha a coluna");
      jogada = in.nextInt();
      tabuleiro.colocaPeca(jogada, 'J');
      tabuleiro.printBoard();
    }
    while(true){
      System.out.println("Vez do computador: ");
      if(algoritmo == 1){
        long startTime = System.nanoTime();
        MinMax minmax = new MinMax(tabuleiro);
        long endTime = System.nanoTime();
        System.out.println(minmax.getNextMove());
        tabuleiro.colocaPeca(minmax.getNextMove(),'C');
        System.out.println();
        tabuleiro.printBoard();
        System.out.println("Tempo em milisegundos: " + (endTime - startTime) / 1000.0);
        System.out.println("Nos gerados: " + minmax.nodeCounter);
        aux = minmax.whoWins(tabuleiro);
        if(aux == 512){
          System.out.println("O computador ganhou!");
          break;
        }
        else if(aux == 0){
          System.out.println("Empate!");
          break;
        }
        else{
          System.out.println("Escolha a coluna");
          jogada = in.nextInt();
          if(jogada < 0 || jogada > 7 || tabuleiro.checkIsFullColumn(jogada) == true){
            System.out.println("Jogada invalida! Tente novamente outra coluna!");
            jogada = in.nextInt();
          }
          tabuleiro.colocaPeca(jogada,'J');
          tabuleiro.printBoard();
          aux = minmax.whoWins(tabuleiro);
          if(aux == -512){
            System.out.println("Voce ganhou !!!");
            break;
          }
          else if(aux == 0){
            System.out.println("Empate");
            break;
          }
        }
      }
      else if(algoritmo == 2){
        long startTime = System.nanoTime();
        AlphaBeta alphabeta = new AlphaBeta(tabuleiro);
        long endTime = System.nanoTime();
        tabuleiro.colocaPeca(alphabeta.getNextMove(),'C');
        tabuleiro.printBoard();
        System.out.println("Tempo em milisegundos: " + (endTime - startTime) / 1000.0);
        System.out.println("Nos gerados: " + alphabeta.nodeCounter);
        aux = alphabeta.whoWins(tabuleiro);
        if(aux == 512){
          System.out.println("O computador ganhou!");
          break;
        }
        else if(aux == 0){
          System.out.println("Empate!");
          break;
        }
        else{
          System.out.println("Escolha a coluna");
          jogada = in.nextInt();
          if(jogada < 0 || jogada > 7 || tabuleiro.checkIsFullColumn(jogada) == true){
            System.out.println("Jogada invalida! Tente novamente outra coluna!");
            jogada = in.nextInt();
          }
          tabuleiro.colocaPeca(jogada,'J');
          tabuleiro.printBoard();
          aux = alphabeta.whoWins(tabuleiro);
          if(aux == -512){
            System.out.println("Voce ganhou !!!");
            break;
          }
          else if(aux == 0){
            System.out.println("Empate");
            break;
          }
        }
      }
      else if(algoritmo == 3){
        long startTime = System.nanoTime();
        MonteCarlo montecarlo = new MonteCarlo(tabuleiro);
        long endTime = System.nanoTime();
        tabuleiro.colocaPeca(montecarlo.nextMoveColumn,'C');
        tabuleiro.printBoard();
        aux = montecarlo.whoWins();
        if(aux == 512){
          System.out.println("O computador ganhou!");
          break;
        }
        else if(aux == 0){
          System.out.println("Empate!");
          break;
        }
        else{
          System.out.println("Escolha a coluna");
          jogada = in.nextInt();
          if(jogada < 0 || jogada > 7 || tabuleiro.checkIsFullColumn(jogada) == true){
            System.out.println("Jogada invalida! Tente novamente outra coluna!");
            jogada = in.nextInt();
          }
          tabuleiro.colocaPeca(jogada,'J');
          tabuleiro.printBoard();
          System.out.println("Tempo em milisegundos: " + (endTime - startTime) / 1000.0);
          aux = montecarlo.whoWins();
          if(aux == -512){
            System.out.println("Voce ganhou !!!");
            break;
          }
          else if(aux == 0){
            System.out.println("Empate");
            break;
          }
        }
      }
    }
  }
}
