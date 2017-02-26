/*

    CLASS       : HumanPlayer
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Defines a concrete BaoPlayer, which is fully implemented
                  by HumanPlayer and AI Player

 */

import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer extends BaoPlayer {
public HumanPlayer(BaoBoard board, Player playerType) {
  super(board, playerType);
}


public int getLocation(ArrayList<Integer> captureMoves) {
  Scanner in = new Scanner(System.in);
  System.out.print("Enter location to place seed [0-7] : ");
  int x = in.nextInt();
  while (!captureMoves.contains(x)) {
    System.out.println("You are urged to capture when you are able to...");
    board.printBoard();
    System.out.print("Enter location to place seed [0-7] : ");
    x = in.nextInt();
  }
  return x;
}

public Direction getDirection() {
  Scanner in = new Scanner(System.in);
  System.out.print("Enter sowing direction [left/right] : ");
  String answer = in.nextLine();
  Direction direction = (answer.equals("left")) ? Direction.LEFT : Direction.RIGHT;

  if (direction == Direction.LEFT) {
    System.out.println("Sowing from the left");
  }
  else {
    System.out.println("Sowing from the right");
  }
  return direction;
}

public int getCascadeLocation() {
  Scanner in = new Scanner(System.in);
  System.out.print("Enter location to cascade seeds [1-16] : ");
  return in.nextInt();
}

public Direction getCascadeDirection() {
  Scanner in = new Scanner(System.in);
  System.out.println("Enter cascade direction [left/right] : ");
  String answer = in.nextLine();
  Direction direction = (answer == "left") ? Direction.LEFT : Direction.RIGHT;
  return direction;
}

}
