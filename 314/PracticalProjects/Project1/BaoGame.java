/*

    CLASS       : BaoGame
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Encapsulates the procession of a typical Bao game,
                  including turns and winning conditions, and allows for
                  games of different Human/Computer players.

 */

public class BaoGame {

BaoBoard board = new BaoBoard();

private Boolean hasWon(Player player) {
  int offset;
  if (player == Player.PLAYER_1) {
    offset = 1;
  } else {
    offset = 2;
  }

  int [] array = board.getBoard()[offset];
  for (int item:array) {
    if (item != 0) {
      System.out.println(item + " on " + offset);
      return false;
    }
  }
  return true;
}

public void declareWinner(Player winner) {
  String winnerName = winner == Player.PLAYER_1 ? "Player 1" : "Player 2";
  System.out.println(winnerName + " has won!");
  System.exit();
}

public void start(Boolean isHumanPlayer1, Boolean isHumanPlayer2) {

  BaoPlayer player1 = new HumanPlayer(board, Player.PLAYER_1);
  BaoPlayer player2 = new AIPlayer(board, Player.PLAYER_2, 20);

  Boolean winFlag = false;
  Player winner = null;

  while (!winFlag) {

    System.out.println("\n\tPlayer1\n");
    player1.nextTurn();
    if (hasWon(Player.PLAYER_1)) {
      winFlag = true;
      winner = Player.PLAYER_1;
      continue;
    }
    board.printBoard();
    System.out.println("\n\tPlayer2\n");
    player2.nextTurn();
    board.printBoard();
    if (hasWon(Player.PLAYER_2)) {
      winFlag = true;
      winner = Player.PLAYER_2;
    }
  }
  declareWinner(winner);
}
}
