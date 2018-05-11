/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 7 - Q Learning
  Due: May 8, 2018
*/

import java.io.*;
import java.util.*;
import java.awt.Point;

public class QLearning{

  // Simulation Variables
  public static final double ALPHA = 0.75;        // Learning Rate
  public static final double GAMMA = 0.1;        // Discount Factor
  public static final double EPOCHS = 10000;     // Number of Iterations
  public static final double EXPL_RATE = 0.1;    // Rate of Exploration

  /** main method
   *
   * @param.. args[0]: filename of the map building file
   * ........ args[1]: variation type of the simulation
   *          ........ 1. Static Trolls
   *          ........ 2. Variable Learning Rate
   *          ........ 3. Moving Trolls
   * @pre.... args[0] and args[1] exist
   */
  public static void main(String[] args) throws FileNotFoundException{

    // creates map and assigns useful pointers
    Universe map = createUniverse(args[0]);
    Agent agent = map.agent;

    for (int i = 0; i < EPOCHS; i++){
      runTour(map, agent, false, Integer.parseInt(args[1]));
    }

    // final greedy tour
    runTour(map, agent, true, Integer.parseInt(args[1]));
  }

  /** runs a single epoch of the simulation
   *
   * @param.. Universe map: world of the simulation
   * ........ Agent agent: agent that lives in this world
   * ........ boolean greedy: whether it is a greedy choice or not
   * ........ int variation: what variation of the algorithm it is
   * @post... a single epoch is ran 
   */
  private static void runTour(Universe map, Agent agent, 
                              boolean greedy, int variation) 
                              throws FileNotFoundException{

    // output printstream
    PrintStream ps = new PrintStream(new File("output.txt"));

    // resets map 
    map.reset();

    // prints final run before greedy choosing
    if (greedy){
      System.out.println(map.toString());
      ps.println(map.toString());
    }

    // counts rescued ponies
    int rescued = 0;

    // counts total reward of agent
    int reward = 0;

    // breaks loop if our agent has been eaten
    boolean hasBeenEaten = false;

    // runs algorithms until agent is on goal location
    while (!agent.loc.equals(map.escapeLocation.loc) && !hasBeenEaten){

      int move;   // temp variable

      // chooses next move based off of greed
      if (greedy){
        move = agent.chooseActionGreedy(map.grid[agent.loc.y][agent.loc.x]);
      } else {
        move = agent.chooseActionPGreedy(map.grid[agent.loc.y][agent.loc.x]);
      }
      int dx = indexToCoords(move).x;
      int dy = indexToCoords(move).y;

      // updates the previous q value
      if (variation == 2){
        map.grid[agent.loc.y][agent.loc.x].updateVarQ(
                 move, map.grid[agent.loc.y + dy][agent.loc.x + dx]);
      } else {
        map.grid[agent.loc.y][agent.loc.x].updateQ(
                 move, map.grid[agent.loc.y + dy][agent.loc.x + dx]);
      }

      // moves the agent
      agent.loc.translate(dx, dy);

      // the spot has been visited one more time
      map.grid[agent.loc.y][agent.loc.x].visitedcount++;

      // counts the reward of moving to this spot
      reward += map.grid[agent.loc.y][agent.loc.x].reward;

      // checks if the agent is on a pony
      for (int i = 0; i < map.ponies.length; i++){
        if (map.ponies[i].loc.equals(agent.loc)){
          rescued++;
        }
      }

      // moves troll if variation = 3
      if (variation == 3){
        for (State s : map.trolls){
          move = Universe.getRandom(0,7);

          // random space that is a legal move
          while (!map.legalMove(s.loc, move)){
            move = Universe.getRandom(0,7);
            if (move > 3)
              move++;
          }

          s = map.grid[s.loc.y + indexToCoords(move).y][s.loc.x + indexToCoords(move).x];
        }
      }

      // checks if the agent is on a troll
      for (int i = 0; i < map.trolls.length; i++){
        if (map.trolls[i].loc.equals(agent.loc)){
          hasBeenEaten = true;
        }
      }

      // sets the current location's object value to X
      map.grid[agent.loc.y][agent.loc.x].object = 'X';
    }

    // prints final run after greedy choosing
    if (greedy){
      System.out.println("\n" + map.toString());
      System.out.println("rescued ponies: " + rescued);
      System.out.println("total reward: " + reward + "\n");
      ps.println("\n" + map.toString());
      ps.println("rescued ponies: " + rescued);
      ps.println("total reward: " + reward + "\n");
    }
  }

  /** Creates a new universe for the simulation
   * 
   * @param.. String filename: name of the map building file
   * @pre.... file associated with filename is properly formatted
   * @throw.. FileNotFoundException
   */
  private static Universe createUniverse(String filename) 
      throws FileNotFoundException{
    File f = new File(filename);
    if (f.exists()){
      Universe temp = new Universe(f);
      temp.agent.map = temp;
      for (State[] a : temp.grid){
        for (State s : a){
          s.map = temp;
        }
      }
      return temp;
    } else {
      throw new FileNotFoundException();
    }
  }

  /** Helper method to convert coordinates of move to index of action array
   *
   * @param... Point: vector of the change in state
   * @return.. int index: index of the direction 
   */
  public static int coordsToIndex(int dx, int dy){    // works
    return (4 + dx + (3*dy));
  }

  /** Helper method to convert index of action array to coordinates of move 
   *
   * @param... int index: index of the direction
   * @return.. Point: vector of the change in state
   */
  public static Point indexToCoords(int index){   // works
    return new Point((index % 3) - 1, (index/3) - 1);
  }
}
