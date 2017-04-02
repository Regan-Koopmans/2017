package bao;

/**
* Enumerates the 4 major classes of moves in the game of Bao. 
*/
public enum MoveType {

	/** A move in which the player captures 1 or more of the opponents seeds, 
	* but a new seed is still introduced to the board.
	*/ 
	NamuaCapture,

	/** A move in which the player sows their seeds, but a new seed is still 
	* introduced to the board. 
	*/
	NamuaTakasa,
	
	/** A move */
	MtajiCapture,
	
	/** A move */
	MtajiTakasa,

}