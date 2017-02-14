
import java.util.Scanner;

public class HumanPlayer extends BaoPlayer {
  public HumanPlayer(BaoBoard board) {
    super(board);
  }

  public void nextTurn() {
    textBasedTurn();
  }

  public void textBasedTurn() {
    Scanner in = new Scanner(System.in);
    if (seedsInStock > 0)
    {
      System.out.print("Input location [0-7]: ");
      Integer location = in.nextInt();
      int captured = board.placeSeed(Player.PLAYER_1, location);
      System.out.println("CAPTURED : " + captured);
      if (captured > 0) {

        // Special locations

        if (location == 0 || location == 1) {

        }
        else if (location == 6 || location == 7) {

        }
        else {
          System.out.print("Sow direction [left/right]: ");
          String sowDirection = in.nextLine();
        }
      }
      --seedsInStock;
    }
    System.out.println(seedsInStock);
  }
}
