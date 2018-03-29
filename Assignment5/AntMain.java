/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 5 - ACO
  Due: March 30, 2018
*/

import java.util.*;

// AntMain runs the ACO program, also holds a lot of methods dealing
// with
import java.io.*;
import java.util.*;

public class AntMain{

  /** STATIC ACO VARIABLES
   *
   *  ALPHA: Pheremone Strength  -- Constant that determines the strength of 
   *                                pheremone along an edge
   *  BETA: Heuristic Strength   -- Constant the determines the strength of 
   *                                heuristic value along an edge
   *  RHO: Pheremone Persistence -- Constant that determines the amount of 
   *                                pheremone that persists every turn
   *  Q: Pheremone Quantity      -- Constant that determines the quantity of 
   *                                of pheremone to be deposited on edge
   *  INIT_PHEREMONE             -- Inital amount of pheremone deposited on
   *                                every edge at start of stimulation
   */
  public static final double ALPHA = 1.0;
  public static final double BETA = 1.0;
  public static final double RHO = 0.95;
  public static final double Q = 100;
  public static final double INIT_PHEREMONE = 20;

  // main
  public static void main(String[] args) throws FileNotFoundException{

    // creates new graph object from input file
    AntMap map = parseMapFile(args[0]);

    // creates population of ants
    Set<Ant> population = createPopulation(args[1]);

    // runs the algorithm
    for (int i = 0; i < args[2]; i++){
      for (Ant a : population){
        map.runTour(a);
      }

    }


  }

  /** Parses the specified input file to construct the map
   *
   * @param: String filename -- name of input file
   * @pre: args[0] exists
   * @post: map file is built 
   */
  private static AntMap parseMapFile(String filename) throws FileNotFoundException{

    // ensures file exists, quits program if it does not
    File f = new File(filename);
    if (!f.exists()){
      System.out.println("File does not exist. Exiting Program");
      System.exit(0);
    }
    Scanner input = new Scanner(f);

    // creates a new map object
    AntMap map = new AntMap(input.nextInt());

    // adds all integer to name mappings
    int count = 0;
    input.nextLine();
    String name = input.nextLine();
    while (!name.equals("**")){
      map.nodes.put(count, name);
      count++;
      name = input.nextLine();
    }
    System.out.println(map.nodes.toString());

    // allocates an edge array of size n for every node,
    // where n = edges coming from nodes.
    for (int i = 0; i < map.edges.length; i++){
      map.edges[i] = new Edge[input.nextInt()];
    }
    input.next();

    // creates all links
    while (input.hasNext()){

      // finds the index of the current edge to be created
      Edge e = new Edge(input.nextInt(), input.nextInt(), calculateCost(1, input, false));
      int index = 0;
      while (map.edges[e.n1][index] != null){
        index++;
      }
      map.edges[e.n1][index] = e;
      
      index = 0;
      while (map.edges[e.n2][index] != null){
        index++;
      }
      map.edges[e.n2][index] = e;
    }
    return map;
  }

  /** calculates the cost of each road, function depends on type input
   * 
   * @param: int type -- type of heuristic calculation
   *         Scanner input -- scanner on 
   *         boolean isWinter -- is it winter?
   * @pre:   all nodes are properly instanciated
   * @post:  all edges have a heuristic cost associated with them
   */
  private static int calculateCost(int type, Scanner input, boolean isWinter){

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

  /** creates the ant population
   *
   * @param: int size -- size of the ant population
   * @pre:   args[1] is an integer value > 0
   * @post:  population is created and returned;
   */
  private static Set<Ant> createPopulation(){

  }
}