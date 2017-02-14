/*

    CLASS       : HumanPlayer
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Defines a concrete BaoPlayer, which is fully implemented
                  by HumanPlayer and AI Player

 */

import java.util.Scanner;

public class HumanPlayer extends BaoPlayer {
public HumanPlayer(BaoBoard board, Player playerType) {
        super(board, playerType);
}


public int getLocation() {
        Scanner in = new Scanner(System.in);
        return in.nextInt();
}

public Direction getDirection() {
        Scanner in = new Scanner(System.in);
        return Direction.LEFT;
}

public int getCascadeLocation() {
        Scanner in = new Scanner(System.in);
        return in.nextInt();
}

public Direction getCascadeDirection() {
        Scanner in = new Scanner(System.in);
        return Direction.LEFT;
}

}
