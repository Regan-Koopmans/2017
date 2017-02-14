public class AIPlayer extends BaoPlayer {
public AIPlayer(BaoBoard board, Player playerType, int depth) {
        super(board, playerType);
}

public int getLocation() {
}

public Direction getDirection() {
}

public int getCascadeLocation() {
        return 0;
}

public Direction getCascadeDirection() {
        return Direction.LEFT;
}
}
