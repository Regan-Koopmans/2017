
import java.util.Scanner;

public class HumanPlayer extends BaoPlayer {
  public HumanPlayer(BaoBoard board) {
    super(board);
  }

  public void nextTurn() {	  
    textBasedTurn();
    
    // TODO: GUI BASED TURN 
  }

  public void textBasedTurn() {
    Scanner in = new Scanner(System.in);
    System.out.print("Input location [1-8]: ");
    String location = in.nextLine();
    System.out.print("Sow direction [left/right]: ");
    String sowDirection = in.nextLine();
  }
}
