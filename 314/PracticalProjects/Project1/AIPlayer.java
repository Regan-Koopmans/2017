/*

    CLASS       : AIPlayer
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Defines a concrete BaoPlayer, which uses a game tree
                  to determine moves.

 */

public class AIPlayer extends BaoPlayer {
public AIPlayer(BaoBoard board, Player playerType, int depth) {
  super(board, playerType);
}

public int getLocation() {
  return 0;
}

public Direction getDirection() {
  return Direction.LEFT;
}

public int getCascadeLocation() {
  return 0;
}

public Direction getCascadeDirection() {
  return Direction.LEFT;
}
}
