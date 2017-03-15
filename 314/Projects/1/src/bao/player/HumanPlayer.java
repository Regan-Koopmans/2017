/*

    CLASS       : HumanPlayer
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Defines a concrete BaoPlayer, which is fully implemented
                  by HumanPlayer and AI Player

 */

package bao.player;

import java.util.ArrayList;
import java.util.Scanner;
import bao.BaoBoard;

public class HumanPlayer extends BaoPlayer {

    public HumanPlayer(BaoBoard board, PlayerType playerType) {
        super(board, playerType);
    }
    public int getCaptureLocation(ArrayList<Integer> captureMoves) {
        System.out.println("Capture moves: " + captureMoves);
        while (seedLocation == null) {
            try {
                Thread.currentThread().sleep(500);
            } catch (Exception e) {
                System.exit(0);
            }
        }
        System.out.println("GOT LOCATION : " + seedLocation);
        int returnValue = seedLocation.get();
        seedLocation = null;
        return returnValue;
    }

    public Direction getDirection() {
        while (direction == null) {
            try {
                Thread.currentThread().sleep(500);
                System.out.println("direction");
            } catch (Exception e) {
                System.exit(0);
            }
        }
        System.out.println("GOT DIRECTION : " + direction);
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
        Direction direction = (answer.equals("left")) ? Direction.LEFT :
                              Direction.RIGHT;
        return direction;
    }

    public int getNonCaptureLocation(ArrayList<Integer> nonCaptureMoves) {
        while (takasaLocation == null) {
            try {
                Thread.currentThread().sleep(500);
            } catch (Exception e) {
                System.exit(0);
            }
        }
        int returnValue = takasaLocation.get();
        takasaLocation = null;
        return returnValue;
    }
}
