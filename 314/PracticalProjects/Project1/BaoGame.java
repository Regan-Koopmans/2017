
public class BaoGame {
  public enum Player { PLAYER_1, PLAYER_2 }
  BaoBoard board = new BaoBoard();

  private Boolean hasWon(Player player) {
    int offset;
    if (player == Player.PLAYER_1) {
      offset = 2;
    } else {
      offset = 1;
    }

    int [] array = board.getBoard()[offset];
    for (int item:array) {
      if (item != 0) { return false; }
    }
    return true;
  }

  public void start(Boolean isHumanPlayer1, Boolean isHumanPlayer2) {
    BaoPlayer player1 = new HumanPlayer(board);
    BaoPlayer player2 = new AIPlayer(board);
    while (!hasWon(Player.PLAYER_1) && !hasWon(Player.PLAYER_2)) {
      player1.nextTurn();
      player2.nextTurn();
      board.printBoard();
    }
  }
}
