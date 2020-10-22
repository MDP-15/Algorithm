package mdpsimRobot;

import mdpsimEngine.Line2D;
import mdpsimEngine.Vector2D;

public class ExtendLine extends Line2D {
	public int x;
	public int y;
	
	public ExtendLine(Vector2D start, Vector2D end, int x, int y) {
		super(start, end);
		this.x = x;
		this.y = y;
	}

}
