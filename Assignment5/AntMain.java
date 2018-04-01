/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 5 - ACO
  Due: March 30, 2018
*/

import java.util.*;
import java.io.*;

// AntMain runs the ACO program, also holds a lot of methods dealing
// with

public class AntMain{

  /** STATIC ACO VARIABLES
   *
   *  ALPHA: Pheremone Strength  -- Constant that determines the strength of 
   *                                pheremone along an edge
   *  BETA: Weight Strength      -- Constant the determines the strength of 
   *                                weight value along an edge
   *  RHO: Pheremone Persistence -- Constant that determines the amount of 
   *                                pheremone that persists every turn
   *  Q: Pheremone Quantity      -- Constant that determines the quantity of 
   *                                of pheremone to be deposited on edge
   *  INIT_PHEREMONE             -- Inital amount of pheremone deposited on
   *                                every edge at start of stimulation
   */
  public static final double ALPHA = 1.0;
  public static final double BETA = 1.0;
  public static final double RHO = 0.65;
  public static final double Q = 100;
  public static final double INIT_PHEREMONE = 20;

  // Starting and ending nodes
  public static final int STARTING_NODE = 0;
  public static final int ENDING_NODE = 24;

  // Type of hueristic cost (see calculateCost() method for descriptions) 
  public static final int TYPE = 1;

  // main
  public static void main(String[] args) throws FileNotFoundException{

    // creates new graph object from input file
    AntMap map = parseMapFile(args[0]);

    // creates population of ants
    Set<Ant> population = createPopulation(Integer.parseInt(args[1]));

    // runs the algorithm
    // for each generation
    for (int i = 0; i < Integer.parseInt(args[2]); i++){

      // for each ant in the generation
      for (Ant a : population){

        // ant does its tour
        map.runTour(a);
      }

      // lays pheremone on all traversed edges
      map.layPheremone(population);

      // evaporates pheremone on all edges
      map.evapPheremone();
    }

    printResults(population, map);
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

    // allocates an edge array of size n for every node,
    // where n = edges coming from nodes.
    for (int i = 0; i < map.edges.length; i++){
      map.edges[i] = new Edge[input.nextInt()];
    }
    input.next();

    // creates all links
    while (input.hasNext()){

      // finds the index of the current edge to be created
      Edge e = new Edge(input.nextInt(), input.nextInt(), calculateCost(input, false));
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

  /** calculates the cost of each edge, function depends on type input
   * 
   * @param: int type -- type of weight calculation
   *         Scanner input -- scanner on 
   *         boolean isWinter -- is it winter?
   * @pre:   all nodes are properly instanciated
   * @post:  all edges have a weight cost associated with them
   */
  private static int calculateCost(Scanner input, boolean isWinter){

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

    // weight 1: Purely Distance Dependent
    if (TYPE == 1){
      return distance;
    }

    // weight 2: The Warm Wimps
    //
    // These travellers are hardened to snow, but quiver at the thought 
    // Sauron's army. They are affected by risk level the most, followed
    // by road quality, and finally winter travel.
    else if (TYPE == 2){
      return (int)(distance * 6 * ((double)risk / 100)) + 
             (2*(100 - roadQuality)) + (25 - winterTravel);
    }

    // weight 3: Armed Merchants
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
  private static Set<Ant> createPopulation(int size){
    Set<Ant> population = new HashSet<Ant>();
    for (int i = 0; i < size; i++){
      population.add(new Ant());
    }

    return population;
  }

  /** Finds the most traversed path on the map
   *
   * @param: 1. Set<Ant> population -- set of all ants
   *         2. AntMap map -- map object
   * @pre: All objects passed in are properly instanciated and formatted
   * @post: Result of runs are printed
   */
  private static void printResults(Set<Ant> population, AntMap map){

    LinkedList<Integer> visited = new LinkedList<Integer>();
    int currnode = STARTING_NODE;
    visited.add(currnode);
    while (currnode != ENDING_NODE){

      LinkedList<Edge> templist = new LinkedList<Edge>();
      for (Edge e : map.edges[currnode]){
        if (!visited.contains(e.cross(currnode))){
          templist.add(e);
        }
      }

      if (templist.size() > 0){
        Edge max = templist.get(0);
        for (Edge e : templist){
          if (e.pheremone > max.pheremone){
            max = e;
          }
        }

        System.out.print(map.nodes.get(currnode) + " -> ");
        currnode = max.cross(currnode);
        visited.add(currnode);

      } else {
        System.out.println("\n");
        System.out.println("No optimal solution found");
        System.exit(0);
      }
    }

    System.out.println(map.nodes.get(ENDING_NODE));
  }
}