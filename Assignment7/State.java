/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 7 - Q Learning
  Due: May 8, 2018
*/

import java.util.*;
import java.awt.Point;

public class State{

  // fields
  double[] actions;
  int reward;
  int visitedcount;
  Point loc;
  char object;
  Universe map;

  // constructor
  public State(int x, int y, int reward){

    // sets all surrounding q values to optimistic 0.0
    this.actions = new double[9];
    this.loc = new Point(x, y);
    this.object = '-';  // default object: '-'
    this.reward = reward;
    this.visitedcount = 0;
  }

  /** Returns the maximum possible q value obtained from possible actions
   *
   * @pre.....  Q is instanciated and is storing accurate values
   * @return..  max q value is returned
   */
  public double getMaxQ(){
    double max = actions[0];
    for (int i = 1; i < actions.length; i++) {
      if (actions[i] > max){
        max = actions[i];
      }
    }
    return max;
  }

  /** Returns the sum of all q value obtained from possible action
   *
   * @pre.....  Q is instanciated and is storing accurate values
   * @return..  sum of q values is returned
   */
  public double getSumQ(){
    double sum = actions[0];
    for (int i = 1; i < actions.length; i++) {
      if (map.legalMove(this.loc, i))
        sum += actions[i];
    }
    return sum;
  }

  /** Updates the q value for a certain action
   *
   * @param.. int index: index of action for this node
   * @post... this action's q value is updated
   */
  public void updateQ(int index, State s){
    actions[index] += QLearning.ALPHA*(s.reward + (QLearning.GAMMA*s.getMaxQ()) 
                      - actions[index]);
  }

  /** Updates the q value for a certain action, uses variable ALPHA
   *
   * @param.. int index: index of action for this node
   * @post... this action's q value is updated
   */
  public void updateVarQ(int index, State s){
    actions[index] += (QLearning.ALPHA*(1/(1+this.visitedcount))) *
                      (s.reward + (QLearning.GAMMA*s.getMaxQ()) - 
                      actions[index]);
  }
}