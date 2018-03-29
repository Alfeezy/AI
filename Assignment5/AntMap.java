/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 5 - ACO
  Due: March 30, 2018
*/

import java.util.*;

// AntMap is a weighted bidirectional map implementation that holds
// all nodes and edge values, and a map from node to location name
public class AntMap{

  // fields
  public Map<Integer, String> nodes; // maps int values to location name
  public Edge[][] edges; // holds all edges

  /** constructor
   *
   * @param: int size - amount of nodes in the graph
   * @pre: size > 0 
   * @post: AntMap is instanciated
   */
  public AntMap(int size){
    nodes  = new HashMap<Integer, String>();
    edges = new Edge[size][5];
  }

  /**
   *
   */
  public void runTour(Ant a){

  }
}