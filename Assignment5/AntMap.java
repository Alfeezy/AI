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
  private Random rand; // for the sake of using one random object

  /** constructor
   *
   * @param: int size - amount of nodes in the graph
   * @pre: size > 0 
   * @post: AntMap is instanciated
   */
  public AntMap(int size){
    nodes  = new HashMap<Integer, String>();
    edges = new Edge[size][5];
    rand = new Random();
  }

  /** runs a tour for a single ant
   *
   * @param: Ant a -- ant to run the tour
   * @pre: AntMap is instanciated
   * @post: Ant has its tour stored in tourmem
   */
  public void runTour(Ant a){

    // resets ant attributes
    a.reset(AntMain.STARTING_NODE);

    // while the goal has not been reached
    while (a.currnode != AntMain.ENDING_NODE){

      // gets next choice for node
      Edge nextnode = getNextChoice(a);

      // backtracks if there are no possible nodes
      if (nextnode == null){
        a.currnode = a.tourMem.pop().cross(a.currnode);
      } else {

        // goes to next node
        a.currnode = nextnode.cross(a.currnode);
        a.unvisitedNodes.remove(a.currnode);
        a.tourMem.push(nextnode);
      }
    }
  }

  /** gives a weighted random choice for next location
   *
   * @param: Ant a -- current ant that is travelling
   * @pre: a is on a valid node
   * @post: valid edge is returned, null is returned if none are found
   */
  private Edge getNextChoice(Ant a){

    // sum of all cost from possible edge connects
    double costSum = 0.0; 
    for (Edge e : edges[a.currnode]){
      if (a.unvisitedNodes.contains(e.cross(a.currnode)))
        costSum += Math.pow(e.weight, AntMain.ALPHA) * Math.pow(e.pheremone, AntMain.BETA);
    }

    // there are no possible options, must backtrack
    if (costSum == 0.0)
      return null;

    // gets next node choice through weighted randomness
    double choice = rand.nextDouble() * costSum;
    double costCount = 0.0;
    for (Edge e : edges[a.currnode]){
      if (a.unvisitedNodes.contains(e.cross(a.currnode))){
        costCount += Math.pow(e.weight, AntMain.ALPHA) * Math.pow(e.pheremone, AntMain.BETA);
        if (costCount >= choice)
          return e;
      }
    }

    // dummy return
    return null;
  }

  /** lays the pheremone for every ant in the population
   *
   * @param: Set<Ant> population -- set of all ants in population
   * @pre: population is instanciated
   * @post: all pheremone is layed for every ant
   */
  public void layPheremone(Set<Ant> population){

    // goes through every ants tour memory
    for (Ant a : population){

      // amount of pheremone to be layed per edge
      double pherPerEdge = a.pherPerEdge(AntMain.Q);

      // lays even pheremon on every edge
      for (Edge e : a.tourMem){
        e.pheremone += pherPerEdge;
      }
    }
  }

  /** evaporates the pheremone for every ant in the population
   *
   * @param: none
   * @pre: none
   * @post: pheremone is evaporated
   */
  public void evapPheremone(){

    for (Edge[] a : edges){
      for (Edge e : a){

        // as there exists a duplicate of every edge in edges, mutliplying 
        // each edge by the sqaure root of RHO twice will lead to a net
        // effect of multiplying each road by RHO
        e.pheremone *= Math.sqrt(AntMain.RHO);
      }
    }
  }
}