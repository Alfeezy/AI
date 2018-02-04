/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 1 - R flex Agent
  Due: February 5, 2018
*/

import java.util.*;
import java.io.*;
import java.awt.*;

/* ****************
 *
 * 
 */

//
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

    roombaLoc = new Point(0,0); // Sets the roomba's location
    grid[0][0] = '^';
    buildCompass(); // Builds the compass, roomba starts pointing up

    // Creates linked lists
    dirtPiles = new LinkedList<Point>();
    furniture = new LinkedList<Point>();

    // Fills the entire board with blank state (' ')
    for (char[] row : grid){
      Arrays.fill(row, ' ');
    }

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

  // Prints the map to a text file
  //
  // Pre-Condition: Map exists and object data is properly stored
  // Post-Condition: Map is printed
  //
  public void printDisplay(){

    // Preliminary furniture update
    for (Point p : furniture){
      grid[(int)p.getX()][(int)p.getY()] = 'X';
    }

    // Preliminary dirt pile update
    for (Point q : dirtPiles){
      grid[(int)q.getX()][(int)q.getY()] = '#';
    }

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
  }

  // Returns the percept vector for current state:
  // <T, Du, Df, Dr, Db, Dl, Gu, Gf, Gr, Gl, Gb>
  // where: u = under, f = front, b = back, l = left, r = right
  public boolean[] makePerceptVector(){

    boolean[] percepts = new boolean[11];

    // i0 : T - false if nothing is in front of roomba
    //          true if there is furniture in front of roomba
    Point test = new Point((int)roombaLoc.getX(), (int)roombaLoc.getY());
    test.move((int)compass.dir.getX(), (int)compass.dir.getY());
    percepts[0] = furniture.contains(test);

    // i1 : Du - false if dirt is not under roomba
    //           true if dirt is under roomba
    percepts[1] = dirtPiles.contains(roombaLoc);

    // i3-6 : Dx - false if dirt is not around roomba
    //             true is dirt is around roomba
    for (int i = 3; i < 7; i++){
      test = roombaLoc;
      test.move((int)compass.dir.getX(), (int)compass.dir.getY());
      percepts[i] = dirtPiles.contains(roombaLoc);
      compass = compass.right;
    }

    // i7 : Gu - false if goal is not under roomba
    //           true if goal is under roomba
    percepts[7] = roombaLoc.equals(goal);

    // i8-11 : Gx - false if goal is not around roomba
    //            - true is goal is around roomba
    for (int i = 8; i < 12; i++){
      test = roombaLoc;
      test.move((int)compass.dir.getX(), (int)compass.dir.getY());
      percepts[i] = roombaLoc.equals(goal);
      compass = compass.right;
    }

    // Returns percept vector
    return percepts;
  }

  // Moves the 
  public void nextMove(){}

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