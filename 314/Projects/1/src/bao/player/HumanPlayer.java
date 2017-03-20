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
    public int getNamuaCapLoc(ArrayList<Integer> captureMoves) {
        System.out.println("Capture moves: " + captureMoves);
        while (seedLocation == null && inRunningInstance) {
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
        while (direction == null && inRunningInstance) {
            try {
                Thread.currentThread().sleep(500);
            } catch (Exception e) {
                System.exit(0);
            }
        }
        System.out.println("GOT DIRECTION : " + direction);
        Direction returnDirection = direction;
        direction = null;
        return returnDirection;
    }

    public int getNamuaNonCapLoc(ArrayList<Integer> nonCaptureMoves) {
        while (takasaLocation == null && inRunningInstance) {
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

    public int getMtajiCapMove() {
        while (mtajiCapLocation == null && inRunningInstance) {
            try {
                Thread.currentThread().sleep(500);
            } catch (Exception e) {
                System.exit(0);
            }
        }
        int returnValue = mtajiCapLocation.get();
        mtajiCapLocation = null;
        return returnValue;
    }
    public int getMtajiNonCapMove(){
        while (mtajiNonCapLocation == null && inRunningInstance) {
            try {
                Thread.currentThread().sleep(500);
            } catch (Exception e) {
                System.exit(0);
            }
        }
        int returnValue = mtajiNonCapLocation.get();
        mtajiNonCapLocation = null;
        return returnValue;
    }
}
