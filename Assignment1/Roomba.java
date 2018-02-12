/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 1 - Reflex Agent
  Due: February 5, 2018
*/

// Roomba
// 
// Roomba takes an input in the form of a perecption vector and returns
// the next move as a function of the perception vector. 
// There are two versions of the function, one with a small location memory 
// and one without.

import java.util.*;
import java.awt.*;

public class Roomba{

  // Fields
  public boolean withMemory; // Decides if the roomba will have memory
  public LinkedList<Integer> memory; // Memory 
  public boolean isTurnedOn; // For ending simulation
  private Random rand; // Random for movement

  // Constructor; Determines which type of roomba this is
  //
  // Parameter: type -- boolean dicatates if roomba will have memory
  // Pre-condition: none
  // Post-condition: correct roomba object is instantiated
  //
  public Roomba(boolean type){

    withMemory = type; // type of roomba
    rand = new Random(); // random for varied data selection
    isTurnedOn = true; // roomba is now alive

    // creates memory object of last 3 moves
    if (withMemory)
      memory = new LinkedList<Integer>();
  }

  // Returns the next logical action determined through chosen algorithm
  // 1 = suck ; 2 = forward 3 = turn left ; 4 = turn right ; 5 = turn off
  //
  // Parameter: percepts -- boolean[] representing all of roomba's perception
  // Pre-condition: percepts is of size 11 and has correct values
  // Post-condition: a move is returned
  //
  public int returnNextAction(boolean[] percepts){

                                // n. condition                    : action
    if (percepts[1]) return 1;  // 1. dirt is under roomba         : suck
    if (percepts[2]) return 2;  // 2. dirt is in front             : forward
    if (percepts[5]) return 3;  // 3. dirt is to the left          : left
                                // 4. dirt is to the right or down : right
    if (percepts[3] || percepts[4]) return 4; 
    if (percepts[6]) {          // 5. goal is under roomba         : turn off
      isTurnedOn = false;
      return 5;
    }
    if (percepts[7]) return 2;  // 6. goal is in front             : forward
    if (percepts[10]) return 3; // 7. goal is to the left          : left
                                // 8. goal is to the right or down : right
    if (percepts[8] || percepts[9]) return 4;

    if (withMemory){
                                // 9. not gone forward in 3 turns  : forward
                                //    && is not facing wall
      if (!memory.contains(2) && !percepts[0]) return 2;
    }

    if (!percepts[0]){         // 10. there is nothing in front    : 2, 3, 4
      int next = rand.nextInt(5);
      if (next == 0){
        return 3; // turn left
      } else if (next == 1){
        return 4; // turn right
      } else {
        return 2; // forward
      }

    } else {                   // 11. there is something in front : 3, 4
      if (rand.nextInt(2) == 0){
        return 3; // turn left
      } else {
        return 4; // turn right 
      }
    }
  }
}