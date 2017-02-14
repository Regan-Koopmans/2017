/*

    CLASS       : BaoPlayer
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Defines an abstract Bao player, which is fully implemented
                  by HumanPlayer and AI Player

 */

public abstract class BaoPlayer {

protected Player playerType;
protected BaoBoard board;
public int seedsInStock = 22;

// These methods will have different implementations, depending on whether
// a player is human or an artificial intelligence.

public abstract int getLocation();
public abstract Direction getDirection();
public abstract int getCascadeLocation();
public abstract Direction getCascadeDirection();

// The concept of a turn is concrete for both, and hence can be described
// abstractly

public void nextTurn() {

        if (seedsInStock > 0) {

                int location = getLocation();
                int captured = board.placeSeed(playerType, location);
                if (captured > 0) {
                        Direction sowDirection;
                        if (location == 0 || location == 1) {
                                sowDirection = Direction.LEFT;
                        }
                        else if (location == 6 || location == 7) {
                                sowDirection = Direction.RIGHT;
                        }
                        else {
                                sowDirection = getDirection();
                        }
                }
                else {
                        System.out.println("Player did not capture any seeds");
                }
                --seedsInStock;
        }
        else {
                System.out.println("Player has no seeds in stock.");
                System.out.print("Enter location of the pile you wish to cascade: ");
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
