/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 4 - A* Algorithm
  Due: March, 2018
  Worked with Alexander Olesen on implementation ideas
*/

import java.util.*;

// Road Class - serves as a link between Location nodes
//
// Holds the two locations which it connects, and the heuristic cost
// of travelling from one node to another
public class Road{

  // fields
  public Location l1, l2;
  public int cost;

  /** Constructor
   *
   * @param: none
   * @pre: none
   * @post: Road object is constructed
   */
  public Road(){}

  /** Returns the location on the other side of a Road
   *
   * @param: Location l -- location on one end of the road
   * @pre: none
   * @post: Location on other end of the road is returned
   */
  public Location cross(Location l){
    if (l.equals(l1)){
      return l2;
    } else {
      return l1;
    }
  }
}