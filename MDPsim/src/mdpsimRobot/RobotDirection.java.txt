package mdpsimRobot;

import java.io.Serializable;

public enum RobotDirection implements Serializable {
	
	UP,DOWN,LEFT,RIGHT;
	
	public RobotDirection TL() {
		if (this == UP) {
			return LEFT;
		} else if (this == DOWN) {
			return RIGHT;
		} else if (this == LEFT) {
			return DOWN;
		}else if (this == RIGHT) {
			return UP;
		}
		return null;
	}
	
	public RobotDirection TR() {
		if (this == UP) {
			return RIGHT;
		} else if (this == DOWN) {
			return LEFT;
		} else if (this == LEFT) {
			return UP;
		}else if (this == RIGHT) {
			return DOWN;
		}
		return null;
	}
	
	public RobotDirection F1() {
		return this;
	}
	
	public RobotDirection F2() {
		return this;
	}
	
	public RobotDirection F3() {
		return this;
	}
}
