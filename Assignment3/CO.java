/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 3 - Genetic Algorithm
  Due: February 5, 2018
*/

import java.util.*;

/** CO (chessboard operations) is a static class that hold all methods that deal
 * with building initial population, random number generation, parent selection,
 * creating children, mutating individuals, and killing the less fit individuals
 * in a population
 */ 

public class CO{

  /** Builds generation 0 for beginning of simulation
   * 
   * @param: int size -- size of chessboard
   * @pre: args[0] = size
   * @post: population is built
   */
  public static HashSet<Chessboard> buildPopulation(int size){

    // population
    HashSet<Chessboard> population = new HashSet<Chessboard>();

    // creates n*10 boards
    for (int i = 0; i < (size*10); i++){

      // creates a new chessboard
      Chessboard temp = new Chessboard(size);

      // temp index variable
      int index = 0;

      // adds random values that have not yet been selected
      while (index < size){
        int next = getRandom(0, size -1);
        if (!arrayContains(temp.board, next)){
          temp.board[index] = next;
          index++;
        }
      }

      // calculates the fitness of the current board
      temp.calculateFitness();

      // adds to population set
      population.add(temp);

    }

    return population;
  }

  /** Returns random int within a range
   *
   * @param: int min -- min value for random, inclusive
   *         int max -- max value for random, inclusive
   * @pre: min is less than max 
   * @post: random integer is returned
   */
  public static int getRandom(int min, int max){
    Random rand = new Random();
    return min + rand.nextInt(max - min + 1);
  }

  /** Selects the parents and creates the mating pool
   *
   * @param: Set population -- the entire population at current time
   *         int size -- size of chessboard
   * @pre: population size = 10 * SIZE
   * @post: sorted array of parents is returned
   */
  public static Chessboard[] selectParents(Set<Chessboard> population, 
                                           int size){

    // temp variables, 3 random parents and the best
    Chessboard c1, c2, c3, best;

    // instanciates mating pool, ensure there will be even numbers
    Chessboard[] matingPool = new Chessboard[size + (size % 2)];

    // convert set to array 
    Chessboard[] populationArray = new Chessboard[size*10];
    int j = 0;
    for (Chessboard c : population){
      populationArray[j] = c;
      j++;
    }

    // adds best of 3 random parents, can choose a board multiple times
    for (int i = 0; i < size + (size % 2); i++){
      c1 = populationArray[getRandom(0, (10*size) - 1)];
      c2 = populationArray[getRandom(0, (10*size) - 1)];
      c3 = populationArray[getRandom(0, (10*size) - 1)];

      // compares c1 to c2
      if (c1.compareTo(c2) == 1){
        best = c1;
      } else {
        best = c2;
      }

      // compares best of c1 and c2 to c3
      if (c3.compareTo(best) == 1){
        best = c3;
      }

      matingPool[i] = best;
    }

    // sorts from best to worst
    Arrays.sort(matingPool);

    return matingPool;
  }

  /** Creates children from a pool of parents using the crossover method,
   *  pairs parents according to fitness
   *
   * @param: Chessboard[] parents -- array of parents 
   *         int size -- size of chessboard
   * @pre: 1. parents contains an even amount of potential parents
   *       2. parents is sorted from least to most fit 
   * @post: pool of children is returned
   */
  public static HashSet<Chessboard> createChildren(Chessboard[] parents, 
                                                   int size){

    // set of children
    HashSet<Chessboard> children = new HashSet<Chessboard>();

    // run the cycle for every pair of parents
    for (int i = 0; i < parents.length; i += 2){

      // instancites children
      Chessboard child1 = new Chessboard(size);
      Chessboard child2 = new Chessboard(size);

      // chooses random index to cross
      int index = getRandom(0, size);

      // copies up until the chosen index from first parent
      for (int j = 0; j < index; j++){
        child1.board[j] = parents[i].board[j];
        child2.board[j] = parents[i+1].board[j];
      }

      // index
      int currindex = index;

      // copies the rest of unused values
      // from parent 2 to child 1
      for (int j : parents[i+1].board){
        if (!arrayContains(child1.board, j)){
          child1.board[currindex] = j;
          currindex++;
        }
      }

      // resets index
      currindex = index;

      // from parent 1 to child 2
      for (int j : parents[i].board){
        if (!arrayContains(child2.board, j)){
          child2.board[currindex] = j;
          currindex++;
        }
      }

      // calculates fitness
      child1.calculateFitness();
      child2.calculateFitness();

      // add children to set of all children
      children.add(child1);
      children.add(child2);

    }

    // returns set of children
    return children;
  }

  /** Mutates a population with a chance of 10%
   *
   * @param: Set population : population to be mutated
   *         int size : size of chessboard
   * @pre: population contains values
   * @post: population is mutated at a 10% average rate
   */
  public static HashSet<Chessboard> mutate(HashSet<Chessboard> population, 
                                           int size){

    // temporary variables
    int i1, i2, temp;

    // 10% chance for every chessboard in the population
    for (Chessboard c : population){

      // 1 in 10 chance
      if (getRandom(1, 10) == 1){

        // swaps two values at random indices
        i1 = getRandom(0, size - 1);
        i2 = getRandom(0, size - 1);
        temp = c.board[i1];
        c.board[i1] = c.board[i2];
        c.board[i2] = temp;
      }
    }

    // returns mutated population
    return population;

  }

  /** method which kills off the weakest of a population
   * 
   * @param: Set pop -- the oversized population that will be reduced
   *         int newsize -- new size of the population
   * @pre: pop contains at least newsize amount of individuals
   * @post: new population is returned, is of size newsize
   */
  public static HashSet<Chessboard> murderWeaklings(HashSet<Chessboard> pop, 
                                                    int newsize){

    // puts all individuals in an array for sorting purposes
    Chessboard[] popAry = new Chessboard[pop.size()];
    int count = 0;
    for (Chessboard c : pop){
      popAry[count] = c;
      count++;
    }

    // sorts from least to most fit
    Arrays.sort(popAry);

    // resets population
    pop = new HashSet<Chessboard>();

    // copys the newsize largest values
    for (int i = popAry.length - 1; popAry.length - i <= newsize; i--){
      pop.add(popAry[i]);
    }

    return pop;
  }

  /** Contains method for java int arrays (why doesn't this exist)
   *
   * @param: int[] a -- array to be tested on
   *         int x   -- value to be tested for
   * @pre: none
   * @post: boolean stating whether value exists in array or not is returned
   */
  private static boolean arrayContains(int[] a, int x){

    // iterates through every value in array
    for (int i : a){
      if (i == x){
        return true;
      }
    }

    return false;
  }
}