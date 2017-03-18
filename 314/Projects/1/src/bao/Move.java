package bao;

import bao.player.Direction;

public class Move {
	
	private int location;
	private Direction direction;

	public Move(int location, Direction direction) {
		this.location = location;
		this.direction = direction;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public int getLocation() {
		return this.location;
	}
}