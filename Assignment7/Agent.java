/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 7 - Q Learning
  Due: May 8, 2018
*/

import java.awt.Point;
import java.util.*;

public class Agent{
  
  // fields
  Point loc;
  Universe map;

  // constructor
  public Agent(Point start){

    // starts at given location
    this.loc = start;

  }

  /** Chooses next action based off of P-Greedy selection
   *
   * @param... State state: state in which the action occurs from
   * @return.. int: choice of movement in the form of an index
   */
  public int chooseActionPGreedy(State state){

    // uses weighted randomness to find next action, adapted from textbook
    double prob;    // temporary probability variable
    int move = Universe.getRandom(0,7);   // temporary move choice 

    /** EXPLORE */
    if (Math.random() < QLearning.EXPL_RATE){
      while (!map.legalMove(loc, move)){
        move = Universe.getRandom(0,7);
        if (move > 3)
          move++;
      }
      return move;
    }

    /** EXPLOIT */
    if (move > 3 && move < 7)
      move++;
    for (int i = 0; i < state.actions.length; i++){
      if (map.legalMove(loc, move)){
        prob = state.actions[i] / state.getSumQ();
        if (prob > Math.random())
          return (move);
      }
      if (move++ == 7) // cycles to beginning of array
        move = 0;
    }

    // nothing has been selected, choose random legal action
    while (!map.legalMove(loc, move)){
      move = Universe.getRandom(0,7);
      if (move > 3)
        move++;
    }
    return move;
  }

  /** Chooses next action based off of Greedy selection
   *
   * @param... State state: state in which the action occurs from
   * @return.. int: choice of movement in the form of an index
   */
  public int chooseActionGreedy(State state){
    int max = 0;   // temp variable

    // finds the action for an unvisited state that had the highest q
    for (int i = 1; i < state.actions.length; i++){

      // new coordinates
      int newX = state.loc.x + QLearning.indexToCoords(i).x; 
      int newY = state.loc.y + QLearning.indexToCoords(i).y;
      if (state.actions[i] > state.actions[max] && map.grid[newY][newX].object != 'X'){
        max = i;   // finds largest unvisited legal
      }
    }

    // illegal move, choose random new move
    while (!map.legalMove(loc, max)){
      max = Universe.getRandom(0,7);
      if (max > 3)
        max++;
    }

    return max;
  }
}