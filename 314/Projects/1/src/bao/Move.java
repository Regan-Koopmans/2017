
package bao;

import bao.player.Direction;
import java.lang.StringBuilder;

/** 
* A helper class that encapsulates details 
* about a move, namely direction, position and the
* move type.
*/

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

	/** 
	* A helper function to generate a human-readable representation of the Move.
	* @return a string containing the printable version of the Move.
	*/

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
		if (direction == Direction.LEFT) {
			sb.append("LEFT");
		} else {
			sb.append("RIGHT");
		}
		sb.append(")");
		return sb.toString();
	}
}