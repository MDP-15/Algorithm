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

	public Vector2D start() {
		return super.start;
	}
	
	public void start(Vector2D start) {
		super.start = start;
	}
	
	public Vector2D midpoint() {
		return super.start.add(super.end).multiply(0.5);
	}
	
	public Vector2D end() {
		return super.end;
	}
	
	public void end(Vector2D end) {
		super.end = end;
	}
}
