public abstract class BaoPlayer {

  protected BaoBoard board;
  public int seedsInStock = 22;

  public abstract void nextTurn();

  public BaoPlayer(BaoBoard board) {
    this.board = board;
  }
}
