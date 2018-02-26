/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 3 - Genetic Algorithm
  Due: February 5, 2018
*/

import java.util.*;

/** In this simulation, chessboard functions as the individual within
 * a larger population.
 * 
 * The board's state, which functions as the genotype, is represented through
 * an array of integers, where the index of an element represents the x 
 * coordinate on the board, and the value of the element represents the y
 * coordinate on the board.
 * NOTE: Chessboard implements comparable so that native java array sorting 
 * methods may be used
 */

public class Chessboard implements Comparable<Chessboard>{

  // fields
  public int[] board; // state of the board
  public double fitness; // fitness of current chessboard
  public int size; // size of the board

  /** constructor
   *
   * @params: int n -- size of chessboard (n*n squares)
   * @pre: none
   * @post: chessboard is instanciated
   */ 
  public Chessboard(int n){

    // instanciates board
    size = n;
    board = new int[n];
    Arrays.fill(board, -1);

  }

  /** Compares the fitness of two Chessboard objects
   * 
   * @params: Chessboard other -- Chessboard to compare to 
   * @pre: both chessboards have valid fitness values (0 <= n <=  1000)
   * @post: integer representation of comparision is returned
   */
  public int compareTo(Chessboard other){

    // fitness of other is greater than this fitness
    if (this.fitness < other.fitness){
      return -1;

    // fitness of other is less that this fitness
    } else if (this.fitness > other.fitness){
      return 1;

    // other and this have equal fitness
    } else {
      return 0;
    }
  }

  /** Calculates the fitness of a board state
   *
   * @pre: board is instanctiated
   * @post: fitness field holds proper value
   */
  public void calculateFitness(){

    // checks the amount of confilcts
    int conflicts = 0;
    for (int i = 0; i < size - 1; i++){
      for (int j = i + 1; j < size; j++){
        if (board[i] == board[j] + (j-i) || board[i] == board[j] - (j-i)){
          conflicts++;
        }
      }
    }

    // make calculation
    fitness = 1 / (conflicts + 0.001);
  }
}