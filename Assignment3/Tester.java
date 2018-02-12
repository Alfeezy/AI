public class Tester{

  public static void main(String[] args){

    // Tests chessboard initialization
    Chessboard b = new Chessboard(20);
    System.out.println("board initiliazed: " + b.board.toString());

    // Tests getting
    System.out.println(b.getBoard().toString());

    // Tests setting
    int[] bitc = new int[20];
    bitc[1] = 1;
    b.setBoard(bitc);

    System.out.println(b.getBoard().toString());

  }
}