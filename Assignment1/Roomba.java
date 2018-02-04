/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 1 - Reflex Agent
  Due: February 5, 2018
*/

// Version of Roomba: Without Memory
// 
// The roomba takes an input in the form of a perecption vector and returns
// the next move as a function of the perception vector. 
// There are two versions of the function, one with a small location memory 
// and one without.
public class Roomba{

  public boolean withMemory; // Decides if the roomba will have memory
  public LinkedList<Point> memory; // Memory 

  // Constructor; Determines which type of roomba this is
  //
  // Parameter: type -- boolean dicatates if roomba will have memory
  // Pre-condition: none
  // Post-condition: Correct roomba object is instantiated
  public Roomba(boolean type){

    withMemory = type; // type of roomba

    // creates memory object
    if (withMemory)
      memory = new LinkedList<Point>();
  }

  public int returnNextMove(boolean[] percepts){

    if (mode = true){ // Type: with memory
    } else { // Type: without memory
    }

    // dummy return
    return;
  }
}