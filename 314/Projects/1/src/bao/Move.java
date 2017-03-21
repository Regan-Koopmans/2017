/*

    CLASS       : Move
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Encapsulates a location and direction into a single
    			  object.

 */
package bao;

import bao.player.Direction;
import java.lang.StringBuilder;

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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(location);
		sb.append(",");
		switch(moveType) {
			case NamuaCapture : sb.append("NamuaCapture"); break;
			case NamuaTakasa : sb.append("NamuaTakasa"); break;
			case MtajiCapture : sb.append("MtajiCapture"); break;
			case MtajiTakasa : sb.append("MtajiTakasa"); break;
		}
		sb.append(",");
		sb.append(")");
		return sb.toString();
	}
}