/*

    CLASS       : BaoBoard
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Defines an abstract Bao player, which is fully implemented
                  by HumanPlayer and AI Player

 */

public class BaoBoard {

private int[][] board = new int[4][8];

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



public void sow(Player player, int numCapturedSeeds, Direction direction) {

  int netDirection = (direction.ordinal() == 0) ? 1 : -1;
  int offset = 2;
  int position;

  position = (direction == Direction.LEFT) ? 0 : 7;

  if (player == Player.PLAYER_2) {
    position = 7-position;
  }

  // Player 2 has a reversed perspective.

  if (player == Player.PLAYER_2) {
    netDirection *= -1;
    offset = 1;
  }

  while (numCapturedSeeds > 0) {

    board[offset][position] += 1;
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
}

public void printBoard() {
  for (int [] array:board) {
    for (int x:array) {
      System.out.print("[" + x + "]");
    }
    System.out.println("");
  }
}
}
