/*
    Name: Bastien Gliech
    CIS 421 Artificial Intelligence
    Assignment: 1 - Reflex Agent
    Due: February 5, 2018
*/

import java.util.*;
import java.io.*;

public class RoombaMain{

  public static void main(String[] args) throws FileNotFoundException {

    // Scorekeeping
    int score = 0;

    // Scanner on file 
    Scanner input = new Scanner(new File(args[0]));

    // Builds map
    System.out.println("\nBuilding map...");
    RoombaMap board = new RoombaMap (input);
  }
}