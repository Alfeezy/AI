/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 7 - Q Learning
  Due: May 8, 2018
*/

import java.util.*;
import java.io.*;
import java.awt.Point;

public class Universe{
  
  // fields
  State[][] grid;        // map of the 
  Agent agent;           // agent
  State escapeLocation;  // escape state 
  State[] trolls;        // states with trolls
  State[] ponies;        // states with ponies
  State[] obstructions;  // states with obstructions

  /** Paramterized constructor
   *
   * @param.. file f: file to read data from
   * @pre.... f is a properly formatted Universe file
   */
  public Universe(File f) throws FileNotFoundException{

    // Stores all Universe information
    Scanner fs = new Scanner(f);    // scanner on file

    // creates map
    int size = fs.nextInt();    // size of board (size x size)
    grid = new State[size][size];

    // create all state action pairs
    for (int i = 0; i < grid.length; i++){
      for (int j = 0; j < grid[i].length; j++){
        grid[j][i] = new State(i, j, 2);
      }
    }

    // sets bad actions (never choose!);
    for (State[] a : grid){
      for (State s : a){
        for(int i = 0; i < s.actions.length; i++){
          if (!legalMove(s.loc, i)){
            s.actions[i] = -1;
          }
        }
      } 
    }

    // counts amount of trolls and ponies
    trolls = new State[fs.nextInt()];    // amount of trolls
    ponies = new State[fs.nextInt()];    // amount of ponies

    // places escape location
    int x = fs.nextInt();
    int y = fs.nextInt();
    escapeLocation = grid[y][x];
    grid[y][x].object = 'E';    // sets printed character
    grid[y][x].reward = 15;     // sets reward

    // places pony locations
    for (int i = 0; i < ponies.length; i++){
      x = fs.nextInt();
      y = fs.nextInt();
      ponies[i] = grid[y][x];
      grid[y][x].object = 'P';  // sets printed character
      grid[y][x].reward = 10;   // sets reward
    }

    // places obstructions, handles if there are none
    ArrayList<State> a = new ArrayList<State>();
    int temp = fs.nextInt();
    String line = fs.nextLine();
    if (temp != -1){    // ensures there are obstructions
      Scanner ls = new Scanner(line);
      a.add(grid[ls.nextInt()][temp]);
      while (ls.hasNext()){
        x = ls.nextInt();
        y = ls.nextInt();
        a.add(grid[y][x]); 
      }
    }
    a.trimToSize();
    obstructions = new State[a.size()];
    obstructions = a.toArray(obstructions);   // stores list as array 
    for (State obstr : obstructions){
      obstr.object = 'O';
    }

    // places troll locations
    for (int i = 0; i < trolls.length; i++){
      x = fs.nextInt();
      y = fs.nextInt();
      trolls[i] = grid[y][x];
      grid[y][x].object = 'T';  // sets printed character
      grid[y][x].reward = -15;   // sets reward
    }

    // agent starts at random location that is not occupied space
    do {
      agent = new Agent(new Point(getRandom(1, size-1), getRandom(1, size-1)));
    } while (grid[agent.loc.y][agent.loc.x].object != '-');
  }

  /** Resets the universe for a new round
   * 
   * @pre.. all fields are properly instanciated
   */ 
  public void reset(){
    
    // resets agent location
    agent.loc = new Point(getRandom(1, grid.length-1), getRandom(1, grid.length-1));

    // resets all characters
    for (State[] a : this.grid){
      for (State s : a){
        s.object = '-';
        s.visitedcount = 0;
      }
    }
    escapeLocation.object = 'E';
    for (State s : ponies){
      s.object = 'P';
    }
    for (State s : trolls){
      s.object = 'T';
    }
    for (State s : obstructions){
      s.object = 'O';
    }
  }

  /** Returns a string representation of this universe
   *
   * @return.. String representation of this universe
   */
  public String toString(){
    String temp = "";

    // adds top line
    temp += "##";     // top left wall
    for (int i = 0; i < this.grid.length; i++){
      temp += " #";   // top wall
    }
    temp += " ##\n";  // top right wall

    // all internal lines
    char swapc = grid[agent.loc.y][agent.loc.x].object;  // swaps 
    grid[agent.loc.y][agent.loc.x].object = 'B';
    for (State[] a : this.grid){
      temp += "##";       // right wall
      for (State s : a){
        temp += " " + s.object;  // space
      }
      temp += " ##\n";    // left wall
    }

    // reverts swap
    grid[agent.loc.y][agent.loc.x].object = swapc;

    // adds bottom line
    temp += "##";     // bottom left wall
    for (int i = 0; i < this.grid.length; i++){
      temp += " #";   // lower wall
    }
    temp += " ##";    // bottom right wall
    return temp;
  }

  public void printStateQ(){
    for (int i = 0; i < grid.length; i++){
      for (int k = 0; k < 3; k++){
        for(int j = 0; j < grid.length; j++){
          System.out.print("[");
          for (int l = 0; l < 3; l++){
            if (Math.round(grid[i][j].actions[l+(3*k)]) > -10) {
              System.out.print(" ");
            }
            System.out.print(" " + Math.round(grid[i][j].actions[l+(3*k)]) + " ");
            if (Math.round(grid[i][j].actions[l+(3*k)]) > -1 && (Math.round(grid[i][j].actions[l+(3*k)]) < 10)) {
              System.out.print(" ");
            }
          }
          System.out.print("]");
        }
        System.out.println();
      }
      System.out.println();
    }
  }

  /** Helper method to ensure that an action is legal
   *
   * @param... Point currloc: current location
   * ......... int action: direction of the action
   * @return.. boolean: the move is legal 
   */ 
  public boolean legalMove(Point currloc, int action){

    // new coordinates
    int newX = currloc.x + QLearning.indexToCoords(action).x; 
    int newY = currloc.y + QLearning.indexToCoords(action).y;

    // is within bounding box
    if (newX > -1 && newX < grid.length && newY > -1 && newY < grid.length){

      // is not curr location, has not been visited, are no obstacles
      return action != 4 && grid[newY][newX].object != 'O';
    }
    return false;
  }

  /** Helper method to get a random int in range, inclusive
   *
   * @param... int min: lower bound for random value
   *       ... int max: upper bound for random value
   * @return.. int: random int value in range
   */
  public static int getRandom(int min, int max){
    return (int)(Math.random() * (max - min + 1)) + min;
  }
}