/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 5 - ACO
  Due: March, 2018
*/

import java.util.*;

// Road Class - serves as a link between Location nodes
//
// Holds the two locations which it connects
public class Road{

  // fields
  public Location l1, l2;

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