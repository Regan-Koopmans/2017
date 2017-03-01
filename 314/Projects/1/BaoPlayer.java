/*

    CLASS       : BaoPlayer
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Defines an abstract Bao player, which is fully implemented
                  by HumanPlayer and AI Player

 */

import java.util.ArrayList;

public abstract class BaoPlayer {

protected Player playerType;
protected BaoBoard board;
public int seedsInStock = 22;

// These methods will have different implementations, depending on whether
// a player is human or an artificial intelligence.

public abstract int getCaptureLocation(ArrayList<Integer> captureMoves);
public abstract int getNonCaptureLocation(ArrayList<Integer> nonCaptureMoves);
public abstract Direction getDirection();
public abstract int getCascadeLocation();
public abstract Direction getCascadeDirection();

// The concept of a turn is concrete for both, and hence can be described
// abstractly

public void nextTurn() {

  if (seedsInStock > 0) {
    ArrayList<Integer> captureMoves = board.getCaptureMoves(playerType);
    if (captureMoves.isEmpty()) {
	     // TAKASA
       ArrayList<Integer> nonCaptureMoves = board.getNonCaptureMoves(playerType);
       int location = getNonCaptureLocation(nonCaptureMoves);
    }
    else {
      int location = getCaptureLocation(captureMoves);
      int captured = board.placeSeed(playerType, location);

      if (captured > 0) {
        int sowLocation;
        Direction sowFromDirection;

        // Locations 0-1 and 6-7 automatically sow from the opposite side.

        if (location == 0 || location == 1) { sowFromDirection = Direction.LEFT; }
        else if (location == 6 || location == 7) { sowFromDirection = Direction.RIGHT; }
        else { sowFromDirection = getDirection(); }

        board.sow(playerType, captured, sowFromDirection);

    }
    else { System.out.println("Player did not capture any seeds"); }
    --seedsInStock;
    }
  }
  else {
    System.out.println("Player has no seeds in stock.");
    int cascadeLocation = getCascadeLocation();
    Direction cascadeDirection = getCascadeDirection();

  }
  System.out.println(seedsInStock);
}

public BaoPlayer(BaoBoard board, Player playerType) {
  this.board = board;
  this.playerType = playerType;
}
}
