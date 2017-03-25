package bao.player;

/**
* ENUMerates the options PLAYER_1 and PLAYER_2 to enable cleaner,
* more readable control structures.
*
* @author Regan Koopmans
*/

public enum PlayerType { 
	PLAYER_1, PLAYER_2; 

	public static PlayerType opposite(PlayerType input) {
		if (input == PLAYER_1) { return PLAYER_2; }
		else { return PLAYER_1; }
	}
}

