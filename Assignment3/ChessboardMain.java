/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 3 - Genetic Algorithm
  Due: February 5, 2018
*/
  
import java.util.*;
import java.io.*;

/** ChessboardMain is the main program that runs the simulation. All file
 * handling and log writing are handled in this class.
 */

public class ChessboardMain{

  /** Runs the genetic algorithm
   *
   * @param: String[] args -- array of input parameters
   * @pre: args[0] contains an integer <100 and >3
   */
  public static void main(String[] args)throws FileNotFoundException,
                                               IOException{

    // stores size of chessboards and iterations
    int size = Integer.parseInt(args[0]);
    int iterations = Integer.parseInt(args[1]);

    // formats file if it doesn't exits
    File f = new File("log.dat");
    if (!f.exists()){
      PrintStream ps = new PrintStream(f);
      ps.println("Generation  Solution");
      ps.close();
    }

    // intro text
    System.out.println("\n");
    System.out.println("Welcome to the n-queens genetic algorithm. In this\n" +
                       "simulation, a genetic algorithm will determine a \n" +
                       "solution to placing n queens on a chessboard such\n" +
                       "that no two queens may attack each other.\n");
    System.out.println("Board size = " + size + " x " + size);
    System.out.println("Population size = " + size * 10);
    System.out.println("Generations = " + iterations);

    // creates population of size n*10
    HashSet<Chessboard> population = CO.buildPopulation(size);
    HashSet<Chessboard> children;
    Chessboard winner = new Chessboard(size);

    // loop counters and breakers
    boolean solutionFound = false;
    int generation = -1;

    // evolutionary loop
    while (generation < iterations && !solutionFound){

      // increments generation
      generation++;

      // generates children 
      children = CO.createChildren(CO.selectParents(population, size), size);

      // adds children to main population
      for (Chessboard c : children){
        population.add(c);
      }

      // mutates the entire population
      population = CO.mutate(population, size);

      // kills the weaklings
      population = CO.murderWeaklings(population, size*10);

      // tests array to see if there are any solutions
      for (Chessboard c : population){
        if (c.fitness == 1000){
          solutionFound = true;
          winner = c;
        }
      }
    }

    // constructs string for printing purposes
    String s = generation + "\t    ";

    // tests to see if an answer exists
    if (solutionFound){
      s = s + Arrays.toString(winner.board);
    }
    BufferedWriter bw = new BufferedWriter(new FileWriter("log.dat", true));
    bw.write(s);
    bw.newLine();
    bw.close();
  }
}