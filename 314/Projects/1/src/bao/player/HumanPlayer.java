package bao.player;

import java.util.ArrayList;
import java.util.Scanner;
import bao.BaoBoard;
import bao.Move;
import bao.MoveType;


/**
* Defines a concrete BaoPlayer, which is fully implemented
* by HumanPlayer and AI Player
*
* @author Regan Koopmans
*/

public class HumanPlayer extends BaoPlayer {

    public HumanPlayer(BaoBoard board, PlayerType playerType) {
        super(board, playerType);
    }
    public Move getNamuaCapMove(ArrayList<Integer> captureMoves) {
        
        while (seedLocation == null && inRunningInstance) {
            try {
                Thread.currentThread().sleep(500);
            } catch (Exception e) {
                System.exit(0);
            }
        }
        int returnValue = seedLocation.get();
        Direction direction = (returnValue > 1 && returnValue < 6) ? getDirection() : null;
        seedLocation = null;
        return new Move(returnValue, direction, MoveType.NamuaCapture);
    }

    public Direction getDirection() {
        while (direction == null && inRunningInstance) {
            try {
                Thread.currentThread().sleep(500);
            } catch (Exception e) {
                System.exit(0);
            }
        }
        Direction returnDirection = direction;
        direction = null;
        return returnDirection;
    }

    public Move getNamuaNonCapMove(ArrayList<Integer> nonCaptureMoves) {
        while (takasaLocation == null && inRunningInstance) {
            try {
                Thread.currentThread().sleep(500);
            } catch (Exception e) {
                System.exit(0);
            }
        }
        int returnValue = takasaLocation.get();
        Direction direction = (returnValue > 1 && returnValue < 6) ? getDirection() : null;
        takasaLocation = null;
        return new Move(returnValue, direction, MoveType.NamuaTakasa);
    }

    public Move getMtajiCapMove() {
        while (mtajiCapLocation == null && inRunningInstance) {
            try {
                Thread.currentThread().sleep(500);
            } catch (Exception e) {
                System.exit(0);
            }
        }
        int returnValue = mtajiCapLocation.get();
        Direction direction = getDirection();
        mtajiCapLocation = null;
        return new Move(returnValue, direction, MoveType.MtajiCapture);
    }
    public Move getMtajiNonCapMove(){
        while (mtajiNonCapLocation == null && inRunningInstance) {
            try {
                Thread.currentThread().sleep(500);
            } catch (Exception e) {
                System.exit(0);
            }
        }
        int returnValue = mtajiNonCapLocation.get();
        Direction direction = (returnValue > 1 && returnValue < 6) ? getDirection() : null;
        mtajiNonCapLocation = null;
        return new Move(returnValue, direction, MoveType.MtajiTakasa);
    }
}
