/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 5 - ACO
  Due: March 30, 2018
*/

import java.util.*;

// bidirectional weighted implementation of graph edge
public class Edge{

  // fields
  public int heuristic; // heuristic cost of this edge
  public int n1, n2; // towards 
  public double pheremone; // pheremone level currently on this edge

  /** constructor
   *
   * @param: int n1 -- first node
   *         int n2 -- seconds node
   * @pre:
   * @post:
   */ 
  public Edge(int n1, int n2, int h){

    // sets heuristic
    this.heuristic = h;

    // sets location indices
    this.n1 = n1;
    this.n2 = n2;

    // lays default initial pheremone level
    this.pheremone = AntMain.INIT_PHEREMONE;
  }

  /** Returns a node index of other side of edge
   *
   * @param: int other -- node to travel from 
   * @pre: other is either n1 or n2
   * @post: index of other node is returned
   */
  public int cross(int other){
    return this.n1 == other ? n2 : n1;
  }
}