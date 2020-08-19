package mdpsimEngine;

public class Line2D {
	public Vector2D start;
	public Vector2D end;
	
	public Line2D(Vector2D start, Vector2D end) {
		this.start = start;
		this.end = end;
	}
	
	public Vector2D start() {
		return this.start;
	}
	
	public void start(Vector2D start) {
		this.start = start;
	}
	
	public Vector2D midpoint() {
		return this.start.add(this.end).multiply(0.5);
	}
	
	public Vector2D end() {
		return this.end;
	}
	
	public void end(Vector2D end) {
		this.end = end;
	}
}
