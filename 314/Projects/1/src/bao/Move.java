/*

    CLASS       : Move
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Encapsulates a location and direction into a single
    			  object.

 */
package bao;

import bao.player.Direction;

public class Move {
	
	private int location;
	private Direction direction;
	private MoveType moveType;

	public Move(int location, Direction direction, MoveType moveType) {
		this.location = location;
		this.direction = direction;
		this.moveType = moveType;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public int getLocation() {
		return this.location;
	}

	public MoveType getMoveType() {
		return this.moveType;
	}
}