/*

    CLASS       : HumanPlayer
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Defines a concrete BaoPlayer, which is fully implemented
                  by HumanPlayer and AI Player

 */

package bao;
import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer extends BaoPlayer {

public HumanPlayer(BaoBoard board, Player playerType) {
  super(board, playerType);
}


public int getCaptureLocation(ArrayList<Integer> captureMoves) {
  // Scanner in = new Scanner(System.in);
  // System.out.print("Enter location to place seed [0-7] : ");
  // int x = in.nextInt();
  // while (!captureMoves.contains(x)) {
  //   System.out.println("You are urged to capture when you are able to...");
  //   board.printBoard();
  //   System.out.print("Enter location to place seed [0-7] : ");
  //   x = in.nextInt();
  // }
  // return x;
  System.out.println("Capture moves: " + captureMoves);
  while (seedLocation == null || !captureMoves.contains(seedLocation)) {
    try {
      Thread.currentThread().sleep(500);
      System.out.println("location");
    } catch (Exception e) { System.out.println(e); }
  }
  int returnValue = seedLocation;
  seedLocation = null;
  return returnValue;
}

public Direction getDirection() {
  // Scanner in = new Scanner(System.in);
  // System.out.print("Enter sowing direction [left/right] : ");
  // String answer = in.nextLine();
  // Direction direction = (answer.equals("left")) ? Direction.LEFT : Direction.RIGHT;
  //
  // if (direction == Direction.LEFT) {
  //   System.out.println("Sowing from the left");
  // }
  // else {
  //   System.out.println("Sowing from the right");
  // }
  while (direction == null) {
    try {
      Thread.currentThread().sleep(500);
      System.out.println("direction");
    } catch (Exception e) { System.out.println(e); }
  }
  System.out.println(direction);
  Direction returnDirection = direction;
  direction = null;
  return returnDirection;
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

public int getNonCaptureLocation(ArrayList<Integer> nonCaptureMoves) {
  // Scanner in = new Scanner(System.in);
  // System.out.print("Enter location to place seed [0-15] : ");
  // int x = in.nextInt();
  // while (!nonCaptureMoves.contains(x)) {
  //   System.out.println("You must place in a hole that has at least one seed already.");
  //   board.printBoard();
  //   System.out.print("Enter location to place seed [0-15] : ");
  //   x = in.nextInt();
  // }
  while (takasaLocation == null || !nonCaptureMoves.contains(takasaLocation)) {
    try {
      Thread.currentThread().sleep(500);
    } catch (Exception e) { System.out.println(e); }
  }
  int returnValue = takasaLocation;
  takasaLocation = null;
  return returnValue;
}
}
