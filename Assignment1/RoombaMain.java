/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 1 - Reflex Agent
  Due: February 5, 2018
*/

// RoombaMain handles starting the simulation, keeping track of time steps,
// updating roomba memory, and printing to a log file

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class RoombaMain{

  // main which handles running the entire simulation
  public static void main(String[] args) throws FileNotFoundException, 
                                                InterruptedException {

    // simulation variables
    int step = 0;
    int score = 0; 
    int iterations;
    Scanner prompt = new Scanner(System.in);

    // For ease in log printing
    String[] actionList = {"VACCUUM UP","GO FORWARD","TURN LEFT","TURN RIGHT","SHUT DOWN"};

    // Intro speech
    System.out.println("\nWelcome to the Roomba Reflex Agent simulation" + 
                       "\nThis simulation tests two types of reflex agents " + 
                       "within a simple environment. \nOne agent utilizes a " +
                       "small memory, and one does not. \n\nTo begin, enter " +
                       "the name of the map building file:");

    // Scanner for map building
    File f = new File(prompt.next());
    while (!f.exists()){
      System.out.println("File not found - enter filename:");
      f = new File(prompt.next());
    }

    // Builds map
    System.out.println("\nBuilding map...\n");
    RoombaMap board = new RoombaMap(new Scanner(f));

    // Asks if Roomba will have memory or not
    String answer;
    do {
      System.out.println("Do you want your roomba to have memory? (y/n)");
      answer = prompt.next();
    } while (!answer.equals("y") && !answer.equals("n"));
    Roomba bot = new Roomba(answer.equals("y"));

    // Iterations before roomba stops
    System.out.println("\nHow many iterations?");
    iterations = prompt.nextInt();

    // Output printstream
    PrintStream ps = new PrintStream(new File("prog1_log.txt"));

    // Sets up log file
    ps.println("Time    <B Du Df Dr Db Dl Gu Gf Gr Gb Gl>" + 
               "\t\tAction\t\t\tScore");
    ps.println("-----------------------------------------" + 
               "\t\t------\t\t\t-----");
    ps.println("0       <0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0>\t\tNA\t\t\t0");

    // Main loop
    int count = -1;
    while (count < iterations && bot.isTurnedOn){

      // Prints score first
      System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\nScore: " + score + "\n");

      // Increment counter
      count++;

      // Prints board
      board.printDisplay();

      // Makin moves
      int move = bot.returnNextAction(board.makePerceptVector());

      // If robot has memory, add last perception to memory
      if (bot.withMemory){
        bot.memory.add(move);
        if (bot.memory.size() > 3){
            bot.memory.removeLast();
        }
      }

      // Prints move
      System.out.println("Last Action: " + actionList[move-1]);

      // Dirt is sucked up
      if (move == 1){
        score += 100;

      // Dirt is not sucked up
      } else {
        score--;
      }

      // Prints to log file
      ps.print(count + "\t");

      // Prints perception vector
      boolean[] temp = board.makePerceptVector();
      ps.print("<");
      for (int i = 0; i < 10; i++){
        if (temp[i]){
          ps.print("1, ");
        } else {
          ps.print("0, ");
        }
      }
      if (temp[10]){
        ps.print("1>\t\t");
      } else {
        ps.print("0>\t\t");
      }

      ps.print(actionList[move-1] + "\t\t"); // Prints action 
      ps.println(score); // Prints score

      // Makin moves
      board.nextAction(move);

      // Wait half a second between moves
      TimeUnit.MILLISECONDS.sleep(500);
    } 

    // Penalizes if the bot is not on goal space by the end
    if (!bot.isTurnedOn){
      score -= 1000;
    }
  }
}