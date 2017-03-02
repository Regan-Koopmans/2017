/*

    CLASS       : BaoBoard
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Defines an abstract Bao player, which is fully implemented
                  by HumanPlayer and AI Player

 */
package bao;

import java.util.ArrayList;

public class BaoBoard {

private int[][] board = new int[4][8];
private boolean[] hasBoard = {true, true};

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

// Function that increases a hole by one and
// returns any seeds that may have been captured
// by the action.

public int placeSeed(Player player, int position) {
  int captured = 0;
  if (player == Player.PLAYER_1) {
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

// Function that returns a list of the available
// moves that will capture enemy seeds. A player
// will have to pick one of these moves, if one
// exists.

public ArrayList<Integer> getCaptureMoves(Player player) {

    ArrayList<Integer> captureMoves = new ArrayList<Integer>();

    int offset = (player == Player.PLAYER_1) ? 1 : 2;
    int self = (player == Player.PLAYER_1) ? 2 : 1;
    int position;
    for (int x = 0; x < 8; ++x) {

	// "If my hole has a seed and their hole has a seed"
        if (board[self][x] != 0 && board[offset][x] != 0) {

            // Player 2 has a reversed perspective.

            position = (player == Player.PLAYER_1) ? x : 7-x;
            captureMoves.add(position);
        }
    }

    return captureMoves;
}

public ArrayList<Integer> getNonCaptureMoves(Player player) {

    ArrayList<Integer> nonCaptureMoves = new ArrayList<Integer>();

    int self = (player == Player.PLAYER_1) ? 2 : 1;
    int position;
    for (int x = 0; x < 8; ++x) {

        if (board[self][x] != 0) {

            // Player 2 has a reversed perspective.

            position = (player == Player.PLAYER_1) ? x : 7-x;
            nonCaptureMoves.add(position);
        }
    }
    return nonCaptureMoves;
}

public void spread(Player player, int location, Direction direction) {
  int offset = (player == Player.PLAYER_1) ? 2 : 1;

}

// Function to determine whether a certain position
// is the "house" hole for a player. This is useful
// for handling the fact that the house disappears
// after it has been sown.

public boolean isHouse(Player player, int position) {
  return true;
}

// This function will sow the seeds in a given direction.
// The function handles wrapping around corners, and
// will call itself recursively if the last seed placed
// captures again. Still need to add "house" semantics.

public void sow(Player player, int numCapturedSeeds, Direction direction) {

  int netDirection = (direction.ordinal() == 0) ? 1 : -1;
  int offset = 2;
  int position;
  int previousPosition = 0;
  boolean sowed = false;

  position = (direction == Direction.LEFT) ? 0 : 7;

  if (player == Player.PLAYER_2) {
    position = 7-position;
  }

  // Player 2 has a reversed perspective.

  if (player == Player.PLAYER_2) {
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

      if (direction == Direction.RIGHT) { offset += 1; }
      else                              { offset -= 1; }

    }
    else if (position < 0) {

      netDirection *= -1;
      position = 0;

      if (direction == Direction.RIGHT) { offset -= 1; }
      else                              { offset += 1; }
    }
    --numCapturedSeeds;
  }

  // If I have sowed this round and I ended on a possible capture

  if (sowed && board[offset][previousPosition] != 0) {

    // adversary is the offset for the enemy

    int adversary = (player == Player.PLAYER_1) ? 1 : 2;

    System.out.println(previousPosition);

    if (board[adversary][previousPosition] != 0) {
      int capture = board[adversary][previousPosition];
      board[adversary][previousPosition] = 0;
      sow(player, capture, direction);
    }
    else {
      System.out.println("Player's turn has ended");
    }
  }
  else {
    System.out.println("Turn ended.");
  }
}

public void printBoard() {
  for (int [] arr:board) {
    for (int x:arr) {
      System.out.print("[" + x + "]");
    }
    System.out.println("");
  }
}
}
