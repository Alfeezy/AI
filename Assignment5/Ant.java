/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 5 - ACO
  Due: March 30, 2018
*/

import java.util.*;

// ant class
public class Ant{
  
  // fields
  LinkedList<Edge> tourMem;
  Set<Integer> unvisitedNodes;
  int currnode;


  /** constructor
   *
   * @param: none
   * @pre: none
   * @post: Ant is instanciated
   */
  public Ant(){}

  /** Returns the amount of pheremone per edge to be deposited
   *
   * @param: double quantity -- initial qunatity of pheremone
   * @pre: tourMem holds all edges to goal
   * @post: quantity is returned
   */
  public double pherPerEdge(double quantity){
    double temp = 0.0;
    for (Edge e : tourMem){
      temp += e.weight;
    }
    return temp;
  }

  /** Resets all of this ants memory and node objects
   *
   * @param: int start -- node to start on
   * @pre: start is on a node ini the graph
   * @post: all ants attributes are reset
   */
  public void reset(int start){

    // clears ants tour memory and visited nodes from last generation
    this.tourMem = new LinkedList<Edge>();
    this.unvisitedNodes = new HashSet<Integer>();
    for (int i = 0; i < 25; i++){
      this.unvisitedNodes.add(i);
    }

    // places on starting node
    this.unvisitedNodes.remove(start);
    this.currnode = start;
  }
}