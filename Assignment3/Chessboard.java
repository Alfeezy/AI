/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 3 - Genetic Algorithm
  Due: February 5, 2018
*/

// Chessboard holds all of the pieces
// In this simulation, chessboard functions as the individual within
// a larger population. 
// The board's state, which functions as the genotype, is represented through
// an array of integers, where the index of an element represents the x 
// coordinate on the board, and the value of the element represents the y
// coordinate on the baord.
public class Chessboard{

  // fields
  int[] board; // state of the board

  // constructor
  //
  // Parameters: int n -- size of chessboard (n*n squares)
  // Pre-condition: none
  // Post-condition: chessboard is instanciated
  public Chessboard(int n){

    // instanciate board
    board = new int[n];
  }

  // returns the state of the chessboard
  //
  // Parameters: none
  // Pre-condition: board is instanciated and hold correct information
  // Post-condition: board is returned as int[]
  public int[] getBoard(){
    return board;
  }

  // sets the state of the chessboard
  //
  // Parameters: int[] newBoard -- new board state to replace old one
  // Pre-condition: board is instanciated
  // Post-condition: board is updated according to recieved board values
  public void setBoard(int[] iniBoard){
    this.board = iniBoard;
  }
}