package mdpsimEngine;

public class Object2D {
	private Object object;
	private Vector2D position;
	private Vector2D prevpos;
	private Vector2D velocity;
	private Vector2D acceleration;
	private boolean isstatic;
	
	public Object2D (Circle2D circle, Vector2D position, Vector2D velocity, Vector2D acceleration,boolean isstatic) {
		this.object = circle;
		this.position = position;
		this.prevpos = position;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.isstatic = isstatic;
	}
	
	public Object2D (Line2D line, Vector2D position, Vector2D velocity, Vector2D acceleration, boolean isstatic) {
		this.object = line;
		this.position = position;
		this.prevpos = position;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.isstatic = isstatic;
	}
	
	public boolean isstatic() {
		return this.isstatic;
	}
	
	public void isstatic(boolean isstatic) {
		this.isstatic = isstatic;
	}
	
	public Class type() {
		return this.object.getClass();
	}
	
	public Vector2D position() {
		return this.position;
	}
	
	public void position(Vector2D position) {
		this.position = position;
	}
	
	public Vector2D velocity() {
		return this.velocity;
	}
	
	public void velocity(Vector2D velocity) {
		this.velocity = velocity;
	}
	
	public Object object() {
		return this.object;
	}
	
	public Vector2D prevpos() {
		return this.prevpos;
	}
	
	public void prevpos(Vector2D prevpos) {
		this.prevpos = prevpos;
	}
	
	public void acceleration(Vector2D accel) {
		this.acceleration = accel;
	}
	
	public Vector2D acceleration() {
		return this.acceleration;
	}
	
	public boolean inside(Vector2D vec) {
		if (this.type() == Circle2D.class) {
			Vector2D pos = this.position();
			double radius = ((Circle2D) this.object()).radius();
			double length = Math.sqrt(Math.pow(pos.x() - vec.x(), 2) + Math.pow(pos.y() - vec.y(), 2));
			if (length <= radius) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
