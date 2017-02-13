public class BaoBoard {

  private int[][] board = new int[4][8];

  public BaoBoard() {
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

  public void printBoard() {
    for (int [] array:board) {
      for (int x:array) {
        System.out.print("[" + x + "]");
      }
      System.out.println("");
    }
  }
}
