/*

    CLASS       : AIPlayer
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Defines a concrete BaoPlayer, which uses a game tree
                  to determine moves.

 */

import java.util.ArrayList;

public class AIPlayer extends BaoPlayer {
public AIPlayer(BaoBoard board, Player playerType, int depth) {
  super(board, playerType);
}

public int getCaptureLocation(ArrayList<Integer> captureMoves) {
  return captureMoves.get(0);
}

public int getNonCaptureLocation(ArrayList<Integer> nonCaptureMoves) {
  return nonCaptureMoves.get(0);
}

public Direction getDirection() {
  System.out.println("AI HAS CHOSEN TO SOW FROM THE LEFT");
  return Direction.LEFT;
}

public int getCascadeLocation() {
  return 0;
}

public Direction getCascadeDirection() {
  return Direction.LEFT;
}
}
