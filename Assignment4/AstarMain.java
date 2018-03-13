/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 4 - A* Algorithm
  Due: March, 2018
  Worked with Alexander Olesen on implementation ideas
*/

import java.util.*;
import java.io.*;


// AstartMain runs all methods involved with parsing, some heuristic 
// calculation, and running the search algorithm
public class AstarMain{

  // main method
  public static void main(String[] args) throws FileNotFoundException{

    // starting location
    Location start = null;

    // parses the map text file, sets starting value
    start = parsing(Integer.parseInt(args[0]), start);

    // creates a minHeap of expanded locations, adds start
    PriorityQueue<Location> expandedNodes = new PriorityQueue<Location>();

    // current location and temp variable, sets starting values
    Location current = start;
    Location temp;
    start.unvisited = false;
    start.gScore = 0;
    start.calculatefScore();

    // currently on starting node
    current.history = new LinkedList<Location>();
    current.history.add(current);

    // while the goal has not yet been reached and there are possible paths
    while (!current.name.equals("Iron Hills")){

      // current node has now been visited
      current.unvisited = false;

      // expands all neighbor roads that have not been visited
      for (Road r : current.roads){

        // temporary location variable
        temp = r.cross(current);

        // only goes towards unvisited variables
        if (temp.unvisited){

          // calculates gScore, fScore for each neighbor
          temp.calculategScore(r);
          temp.calculatefScore();

          // stores path until that point 
          temp.history = new LinkedList<Location>(current.history);
          temp.history.add(temp);

          // adds this location to a list of possible future location
          // makes a copy to ensure that locations do not interfere
          expandedNodes.add(r.cross(current).copyOf());
        }
      }

      // polls the location with the lowest fScore, is new current node
      current = expandedNodes.poll();
    }

    if (current.name.equals("Iron Hills")){
      System.out.println("\nSuccess!");
      System.out.println("\nPath to end: ");
      System.out.print(current.history.poll().name);
      for (Location l : current.history){
        System.out.print(" -> " + l.name);
      }
    } else {
      System.out.println("\nFailure! No shortest path found");
    }
  }

  /** parsing parses the map file and generates a graph
   *
   * @param: int type -- type of heuristic calculation
   *         Location start -- starting location
   * @pre:   1. input is instanciated on proper and well formatted file
   *         2. start points to null
   * @post:  locations is properly formatted 
   */
  private static Location parsing(int type, Location start) 
                                  throws FileNotFoundException{

    // instanciates scannnes and list of locations
    Scanner input = new Scanner(new File("map.txt"));
    Scanner prompt = new Scanner(System.in);

    // temporary list for accessing later
    LinkedList<Location> locations = new LinkedList<Location>();

    // prompts whether it is winter or not
    String s;
    boolean isWinter = false;
    do {
      System.out.println("Is it winter? (y/n)");
      s = prompt.next();
    } while (!s.equalsIgnoreCase("y") && !s.equalsIgnoreCase("n"));
    if (s.equalsIgnoreCase("y")){
      isWinter = true;
    }

    // while there are fields
    while (input.hasNext()){

      // first input
      String next = input.next();

      // enters new field 
      if (next.equals("*START1")){

        // iterates to next token
        next = input.next();

        // temp variable for naming 
        String temp;

        // iterates through every location 
        while (!next.equals("*END")){

          // creates new location object
          Location l = new Location();

          // handles both 1 and 2 word location names
          l.name = next;
          temp = input.next();
          if (!temp.equals("-")){
            l.name += " " + temp;
            input.next();
          }

          // distance from iron hills
          l.hScore = input.nextInt();

          // adds locations to list
          locations.add(l);

          // go to next line
          next = input.next();
        }

      // creates location links 
      } else if (next.equals("*START2")){

        // iterates to next token
        next = input.next();

        // temp variables
        String name, temp;

        // iterates through every link
        while (!next.equals("*END")){

          // creates new road
          Road r = new Road();

          // determines first location's name
          name = next;
          temp = input.next();
          if (!temp.equals("-")){
            name += " " + temp;
            input.next();
          }

          // finds first location in list
          for (Location l : locations){
            if (l.name.equals(name)){
              r.l1 = l;
            }
          }

          // determines second location's name
          name = input.next();
          temp = input.next();
          if (!temp.equals("-")){
            name += " " + temp;
            input.next();
          }

          // finds second location in list
          for (Location l : locations){
            if (l.name.equals(name)){
              r.l2 = l;
            }
          }

          // calculates the cost of the road
          r.cost = calculateCost(type, r, input, isWinter);

          // connects roads to each location
          r.l1.roads.add(r);
          r.l2.roads.add(r);

          // iterates through list
          next = input.next();

        }
      }
    }

    // prompts for starting location
    while (start == null){
      System.out.println("\nStarting location?");
      String loc = prompt.next();
      for (Location l : locations){
        if (l.name.contains(loc)){
          return l;
        }
      }
      System.out.println();
    }
    // dummy return
    return null;
  }

  /** calculates the cost of each road, function depends on type input
   * 
   * @param: int type -- type of heuristic calculation
   *         ArrayList<Location> locations -- list of all graph nodes
   * @pre:   all nodes are properly instanciated and linked
   * @post:  all edges have a heuristic cost associated with them
   */
  private static int calculateCost(int type, Road r, Scanner input, 
                                   boolean isWinter){

    // stores all values
    int distance = input.nextInt();
    int roadQuality = input.nextInt();
    int risk = input.nextInt();
    int winterTravel = 0;
    if (isWinter){
      winterTravel = input.nextInt();
    } else{
      input.next();
    }

    // Heuristic 1: Purely Distance Dependent
    if (type == 1){
      return distance;
    }

    // Heuristic 2: The Warm Wimps
    //
    // These travellers are hardened to snow, but quiver at the thought 
    // Sauron's army. They are affected by risk level the most, followed
    // by road quality, and finally winter travel.
    else if (type == 2){
      return (int)(distance * 6 * ((double)risk / 100)) + 
             (2*(100 - roadQuality)) + (25 - winterTravel);
    }

    // Heuristic 3: Armed Merchants
    // 
    // These merchants have hired a crew of noble mercenaries to defend their
    // precious cargo. They fear not of most attackers, but must be especially
    // wary of poor road conditions, as their carriages are not very agile. 
    // Winter is also of concern.
    else {
      return (int)(distance * 4 * (double)((100 - roadQuality)/100)) + risk + 
             (4*(25 - winterTravel));
    }
  }
}