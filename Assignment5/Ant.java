public class Ant{
  
  Queue<Integer> tourMem;
  Set<Integer> unvisitedNodes;

  /** constructor
   *
   * @param: none
   * @pre: none
   * @post: Ant is instanciated
   */
  public Ant(){

    // creates new tour memory
    tourMem = new LinkedList<Integer>();

    // adds all locations to unvisited
    unvisitedNodes = new HashSet<Integer>();
    for (int i = 0; i < 25; i++){
      unvisitedNodes.add(i);
    }

  }
}