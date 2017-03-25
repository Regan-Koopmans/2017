package bao;

import java.util.ArrayList;
import bao.player.PlayerType;
import bao.player.Direction;
import bao.Move;
import bao.MoveType;


/**
* Defines a Bao board, which contains state information, 
* and for which moves can be made by players.
* 
* @author Regan Koopmans
*/

public class BaoBoard {

    private int[][] board = new int[4][8];
    private static boolean[] hasBoard = {true, true};

    public BaoBoard() {

        // Starting state as specified

        board[1][1] = 2;
        board[1][2] = 2;
        board[1][3] = 6;

        board[2][4] = 6;
        board[2][5] = 2;
        board[2][6] = 2;
    }

    public int[][] getBoard() {
        return board;
    }

    public BaoBoard(BaoBoard from) {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 8; y++) {
                board[x][y] = from.getBoard()[x][y];
            }
        }
    }

// Function that abstracts all the following move functions into
// a single entry-point, that takes a move object. Facilitates the
// construction of the game tree. Returns a new BaoBoard representing
// the board after the move was made.

    public BaoBoard makeMove(Move move, PlayerType player) {

        if (move.getMoveType() == MoveType.NamuaCapture) {
            
            int numCaptured = placeSeed(player, move.getLocation());
            sow(player, numCaptured, move.getDirection());

        } else if (move.getMoveType() == MoveType.NamuaTakasa) {

            spread(player,move.getLocation(), move.getDirection());

        } else if (move.getMoveType() == MoveType.MtajiCapture) {

            // spread

        } else if (move.getMoveType() == MoveType.MtajiTakasa) {

            // spread

        }

        return new BaoBoard();
    }

    public ArrayList<Move> getMoves(PlayerType player, int numBankSeeds) {

        ArrayList<Move> moves = new ArrayList<Move>();
        if (numBankSeeds > 0) {

            // Namua Turn

            ArrayList<Integer> numuaCapMoves = getNamuaCapMoves(player);
            if (!numuaCapMoves.isEmpty()) {
                for (Integer location:numuaCapMoves) {

                    moves.add(new Move(location, Direction.LEFT, MoveType.NamuaCapture));
                    moves.add(new Move(location, Direction.RIGHT, MoveType.NamuaCapture));
                
                }
            } else {
                ArrayList<Integer> namuaNonCapMoves = getNamuaNonCapMoves(player);
                for (Integer location:namuaNonCapMoves) {

                    moves.add(new Move(location, Direction.LEFT, MoveType.NamuaTakasa));
                    moves.add(new Move(location, Direction.RIGHT, MoveType.NamuaTakasa));
                }
            }
        
        } else {

            // Mtaji Turn

            ArrayList<Move> mtajiCapMoves = getMtajiCapMoves(player);
            if (!mtajiCapMoves.isEmpty()) {

                moves = mtajiCapMoves;

            } else {
                moves = getMtajiNonCapMoves(player);
            }

        }
        
        return moves;
    }

/** Function that increases a hole contents by one and
* returns any seeds that may have been captured
* by the action.
* 
* @param player The player placing the seed.
* @param position The position at which the player wishes to place the seed.
* @return an integer containing the number of seeds captured.
*/

    public int placeSeed(PlayerType player, int position) {
        int captured = 0;
        if (player == PlayerType.PLAYER_1) {
            board[2][position] += 1;
            captured = board[1][position];
            board[1][position] = 0;
        }
        else {
            board[1][7-position] += 1;
            captured = board[2][7-position];
            board[2][7-position] = 0;
        }
        return captured;
    }

/** Function that returns a list of the available
* moves that will capture enemy seeds. A player
* will have to pick one of these moves, if one
* exists.
*
* @param player The player requesting to know which moves are 
* avaliable
*
* @return An ArrayList containing integer locations of possible moves.
*/

    public ArrayList<Integer> getNamuaCapMoves(PlayerType player) {

        ArrayList<Integer> captureMoves = new ArrayList<Integer>();

        int offset = (player == PlayerType.PLAYER_1) ? 1 : 2;
        int self = (player == PlayerType.PLAYER_1) ? 2 : 1;
        int position;
        for (int x = 0; x < 8; ++x) {

            // "If my hole has a seed and their hole has a seed"
            if (board[self][x] != 0 && board[offset][x] != 0) {

                // Player 2 has a reversed perspective.

                position = (player == PlayerType.PLAYER_1) ? x : 7-x;
                captureMoves.add(position);
            }
        }
        return captureMoves;
    }

    // TODO: this
    // Function used to get the legal moves in a Mtjai round when
    // there are no legal capture moves.

    public ArrayList<Move> getMtajiNonCapMoves(PlayerType player) {
        ArrayList<Move> moves = new ArrayList<Move>();
        int offset = (player == PlayerType.PLAYER_1) ? 2 : 1;

        for (int x = 0; x < 8; x++) {
            if (board[offset][x] > 1) {
            }
        }
        return moves;
    }

    // TODO: this
    // Function that obtains the moves that would capture in a Mtaji stage turn. If 
    // this ArrayList is empty, this is an indiction that a non-capture (takasa)
    // move is required.

    public ArrayList<Move> getMtajiCapMoves(PlayerType player) {
        ArrayList<Move> moves = new ArrayList<Move>();
        
        int offset = (player == PlayerType.PLAYER_1) ? 2 : 0;
        for (int x = offset; x < offset+2; ++x) {
            for (int y = 0; y < 8; ++y) {
                
                // can only cascade piles that have a non-singleton value

                if (board[x][y] > 1) {
                    System.out.println(board[x][y]);
                }
            }
        }
        return moves;
    }

    public ArrayList<Integer> getNamuaNonCapMoves(PlayerType player) {

        ArrayList<Integer> nonCaptureMoves = new ArrayList<Integer>();
        int self = (player == PlayerType.PLAYER_1) ? 2 : 1;
        int position;
        for (int x = 0; x < 8; ++x) {

            if (board[self][x] != 0) {

                // Player 2 has a reversed perspective.

                position = (player == PlayerType.PLAYER_1) ? x : 7-x;
                nonCaptureMoves.add(position);
            }
        }
        return nonCaptureMoves;
    }

    /** 
    * The function that is called in a Numua Takasa round.
    *
    *
    */
    public void spread(PlayerType player, int location, Direction direction) {
        int offset = (player == PlayerType.PLAYER_1) ? 2 : 1;

        location = (player == PlayerType.PLAYER_1) ? location : 7 - location;

        int numSeed = board[offset][location];
        board[offset][location] = 0;

        int netDirection;
        if (direction == Direction.RIGHT) {
            netDirection = 1;
        }
        else {
            netDirection = -1;
        }
        if (player == PlayerType.PLAYER_2) {
            netDirection *= -1;
        }

        location += netDirection;
        while (numSeed > 0) {
            if (location < 0 || location > 7) {
                location = (location < 0) ? 0 : 7;
                netDirection *= -1;
                offset = (player == PlayerType.PLAYER_1) ? offset + 1: offset - 1;
            }
            System.out.println(offset + "  : " + location);
            board[offset][location] += 1;
            location += netDirection;
            --numSeed;
        }
    }

    public int filledHolesInFrontRow(PlayerType player) {
        int count = 0;
        int offset = (player == PlayerType.PLAYER_1) ? 2 : 1;
        for (int x = 0; x < 8; x++) {
            if (board[offset][x] > 0) {
                count++;
            }
        }
        return count;
    }

/** Function to determine whether a certain position
* is the "house" hole for a player. This is used
* for handling the fact that the house disappears
* after it has been sown.

* @param player The player who is a asking whether their house exists.
* @param position The position at which the player is asking whether their 
*        house exists.
*/
    public boolean isHouse(PlayerType player, int position) {
        return true;
    }

/** This function will sow the seeds in a given direction.
* The function handles wrapping around corners, and
* will call itself recursively if the last seed placed
* captures again. Still need to add "house" semantics.
*
* @param player yep
* @param numCapturedSeeds
* @param direction
*/

    public void sow(PlayerType player, int numCapturedSeeds, Direction direction) {

        int netDirection = (direction.ordinal() == 0) ? 1 : -1;
        int offset = 2;
        int position;
        int previousPosition = 0;
        boolean sowed = false;

        position = (direction == Direction.LEFT) ? 0 : 7;
        if (player == PlayerType.PLAYER_2) {
            position = 7-position;
        }

        // Player 2 has a reversed perspective.

        if (player == PlayerType.PLAYER_2) {
            netDirection *= -1;
            offset = 1;
        }
        sowed = (numCapturedSeeds > 0) ? true : false;
        while (numCapturedSeeds > 0) {

            board[offset][position] += 1;
            previousPosition = position;
            position += netDirection;

            if (position > 7) {
                netDirection *= -1;
                position = 7;

                // Loops round the second row.

                if (direction == Direction.RIGHT) {
                    offset += 1;
                }
                else                              {
                    offset -= 1;
                }
            }
            else if (position < 0) {
                netDirection *= -1;
                position = 0;

                if (direction == Direction.RIGHT) {
                    offset -= 1;
                }
                else {
                    offset += 1;
                }
            }
            --numCapturedSeeds;
        }

        // If I have sowed this round and I ended on a possible capture

        if (sowed && board[offset][previousPosition] != 0) {

            // adversary is the offset for the enemy

            int adversary = (player == PlayerType.PLAYER_1) ? 1 : 2;
            if (board[adversary][previousPosition] != 0) {
                int capture = board[adversary][previousPosition];
                board[adversary][previousPosition] = 0;
                sow(player, capture, direction);
            }
            else {
                // System.out.println("Player's turn has ended");
            }
        } else {
            // System.out.println("Turn ended.");
        }
    }

    /** Function that calculates the total number of seeds
    * that a player has on the board. Used in the evaluation
    * function.
    *
    * @param player The player who is asking how many seeds they have 
    * on the board
    *
    * @return The number of seeds that the given player has on 
    * the board.
    */

    public int seedsOnBoard(PlayerType player) {
        int seedCount = 0;
        int offset = (player == PlayerType.PLAYER_1) ? 2 : 0;
        for (int x = offset; x < offset+2; x++) {
            for (int y = 0; y < 8; y++) {
                seedCount += board[x][y];
            }
        }
        return seedCount;
    }

    /**
    * Helper function to print the board graphically to the console.
    */
    public void printBoard() {
        for (int [] arr:board) {
            for (int x:arr) {
                System.out.print("[" + x + "]");
            }
            System.out.println("");
        }
    }
}
