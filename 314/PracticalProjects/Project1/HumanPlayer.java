
import java.util.Scanner;

public class HumanPlayer extends BaoPlayer {
  public HumanPlayer(BaoBoard board) {
    super(board);
  }

  public void nextTurn() {
    Scanner in = new Scanner(System.in);
    in.nextLine();
  }
}
