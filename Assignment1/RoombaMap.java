/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 1 - Reflex Agent
  Due: February 5, 2018
*/

// RoombaMap
//
// RoombaMap handles the bulk of the simulation, such as keeping track of all
// coordinates and states of objects, and map and log updating and printing

import java.util.*;
import java.io.*;
import java.awt.*;

public class RoombaMap {

  // Fields
  char[][] grid; // The map of spaces that the game occurs on
  int size; // Size of the map, map has (size) x (size) spaces
  Point roombaLoc; // Roomba location
  Point goal; // Goal location
  LinkedList<Point> dirtPiles; // Stores locations of dirt piles
  LinkedList<Point> furniture; // Stores locations of furniture
  Direction compass; // Compass aids in rotation and char -> vec translation

  // Constructor; Builds a map using a specified map file
  //
  // Parameter: input -- Scanner on map file
  // Pre-condition: input is reading a properly formatted map file
  // Post-Condition: grid is fully and properly constructed
  //
  public RoombaMap (Scanner input){

    int x, y; // Temp variables

    // Creates a two dimensional array of spaces
    size = input.nextInt();
    grid = new char[size][size];

    // Creates linked lists
    dirtPiles = new LinkedList<Point>();
    furniture = new LinkedList<Point>();

    // Fills the entire board with blank state (' ')
    for (char[] row : grid){
      Arrays.fill(row, ' ');
    }

    roombaLoc = new Point(0,0); // Sets the roomba's location
    grid[0][0] = '^';
    buildCompass(); // Builds the compass, roomba starts pointing up

    // Stores number of dirt piles and furniture
    int dirtAmt = input.nextInt();
    int furnAmt = input.nextInt();

    // State of goal space set to 'G'
    x = input.nextInt();
    y = input.nextInt();
    grid[x][y] = 'G';
    goal = new Point(x,y);

    // State of furniture spaces set to 'X'
    for (int i = 0; i < furnAmt; i++){
      x = input.nextInt();
      y = input.nextInt();
      grid[x][y] = 'X';

      // Stores on list of furniture coordinates
      furniture.add(new Point(x,y));
    }

    // State of dirt pile spaces set to '#'
    for (int i = 0; i < dirtAmt; i++){
      x = input.nextInt();
      y = input.nextInt();
      grid[x][y] = '#';

      // Stores on list of dirt pile coordinates
      dirtPiles.add(new Point(x,y));
    }
  }

  // Returns the percept vector for current state:
  // <T, Du, Df, Dr, Db, Dl, Gu, Gf, Gr, Gb, Gl>
  // where: u = under, f = front, b = back, l = left, r = right
  //
  // Parameter: none
  // Pre-condition: compass and roombaLoc hold accurate values
  // Post-condition: perception vector is returned
  //
  public boolean[] makePerceptVector(){

    boolean[] percepts = new boolean[11];

    // i0 : T - false if nothing is in front of roomba
    //          true if there is furniture or wall in front of roomba
    Point test = new Point(roombaLoc);
    test.translate((int)compass.dir.getX(), (int)compass.dir.getY());
    percepts[0] = furniture.contains(test) || (int)test.getX() < 0 ||
                  (int)test.getX() > size - 1 || 
                  (int)test.getY() < 0 ||
                  (int)test.getY() > size - 1;

    // i1 : Du - false if dirt is not under roomba
    //           true if dirt is under roomba
    percepts[1] = dirtPiles.contains(roombaLoc);

    // i2-5 : Dx - false if dirt is not around roomba
    //             true is dirt is around roomba
    for (int i = 2; i < 6; i++){
      test = new Point(roombaLoc);
      test.translate((int)compass.dir.getX(), (int)compass.dir.getY());
      percepts[i] = dirtPiles.contains(test);
      compass = compass.right;
    }

    // i6 : Gu - false if goal is not under roomba
    //           true if goal is under roomba
    percepts[6] = roombaLoc.equals(goal);

    // i7-10 : Gx - false if goal is not around roomba
    //            - true is goal is around roomba
    for (int i = 7; i < 11; i++){
      test = new Point(roombaLoc);
      test.translate((int)compass.dir.getX(), (int)compass.dir.getY());
      percepts[i] = test.equals(goal);
      compass = compass.right;
    }

    // Returns percept vector
    return percepts;
  }

  // Roomba performs an action, adds to memory
  //
  // Parameter: none
  // Pre-condition: action recieved is possible
  // Post-condition: roomba performs one action, map and values updated
  //
  public void nextAction(int move){
    
    switch (move) {

      // Sucks up dirt
      case 1: dirtPiles.remove(roombaLoc);
              break;

      // Moves forward one step
      case 2: roombaLoc.translate((int)compass.dir.getX(), 
                                  (int)compass.dir.getY());
              break;

      // Turns left
      case 3: compass = compass.left;
              break;

      // Turns right
      case 4: compass = compass.right;
              break;
    }
  }

  // Prints the map to a text file
  //
  // Parameter: none
  // Pre-Condition: map exists and object data is properly stored
  // Post-Condition: map is printed
  //
  public void printDisplay(){

    // makes space for new map
    System.out.println();

    // Fills the entire board with blank state (' ')
    for (char[] row : grid){
      Arrays.fill(row, ' ');
    }

    // Preliminary furniture update
    for (Point p : furniture){
      grid[(int)p.getX()][(int)p.getY()] = 'X';
    }

    // Preliminary dirt pile update
    for (Point q : dirtPiles){
      grid[(int)q.getX()][(int)q.getY()] = '#';
    }

    // Preliminary home update
    grid[0][0] = 'H';

    // Preliminary goal update
    grid[(int)goal.getX()][(int)goal.getY()] = 'G';

    // Preliminary roomba update
    grid[(int)roombaLoc.getX()][(int)roombaLoc.getY()] = compass.data;


    // Prints top line
    for (int i = 0; i < size; i++){
      System.out.print("+-");
    }
    System.out.println("+");

    // Prints middle of board
    for (int i = size - 1; i > 0; i--){

      // Prints different spaces, both empty and full
      System.out.print("|" + grid[0][i]);
      for (int j = 1; j < size; j++){
        System.out.print(" " + grid[j][i]);
      }
      System.out.println("|");

      // Prints grid spaces
      for (int j = 0; j < size; j++){
        System.out.print("+ ");
      }
      System.out.println("+");
    }

    // Fencepost on printing spaces
    System.out.print("|" + grid[0][0]);
    for (int i = 1; i < size; i++){
      System.out.print(" " + grid[i][0]);
    }
    System.out.println("|");

    // Prints bottom line
    for (int i = 0; i < size; i++){
      System.out.print("+-");
    }
    System.out.println("+");

    // Prints percept vector on the bottom
    System.out.println("\nT\tDU\tDF\tDR\tDB\tDL\tGU\tGF\tGR\tGB\tGL");
    boolean[] temp = makePerceptVector();
    for (int i = 0; i < 11; i++){
      if (temp[i]){
        System.out.print("1\t");
      } else {
        System.out.print("0\t");
      }
    }
    System.out.println("");
  }

  // Object which stores direction, and points to other directions
  // that are perpendicular to itself
  public class Direction{

    // Fields
    public char data; // ASCII representation of direction
    public Direction right; // Direction at 90 degrees right
    public Direction left;  // Direction at 90 degrees left
    public Point dir; // Point (vector) 1 unit in current direction

    // Default constructor -
    public Direction(char data, Point dir, Direction right){

      // Sets fields
      this.data = data;
      this.right = right;
      this.dir = dir;

    }
  }

  // Builds compass - I'm so sorry
  public void buildCompass(){

    // Links all nodes in rightwards direction
    compass = new Direction(
      '^', new Point(0,1), new Direction(
        '>', new Point(1,0), new Direction(
          'v', new Point(0,-1), new Direction(
            '<', new Point(-1,0), null))));

    // Top "head" node links to itself
    compass.right.right.right.right = compass;

    // Links all nodes leftwards direction
    for (int i = 0; i < 4; i++){
      compass.left = compass.right.right.right;
      compass = compass.right;
    }
  }
}