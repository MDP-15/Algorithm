package mdpsimEngine;
import java.util.ArrayList;

public class Object2D {
	private Object object;
	private Vector2D position;
	private Vector2D velocity;
	private boolean isstatic;
	
	public Object2D (Circle2D circle, Vector2D position, Vector2D velocity, boolean isstatic) {
		this.object = circle;
		this.position = position;	
		this.velocity = velocity;
		this.isstatic = isstatic;
	}
	
	public Object2D (Rect2D rect, Vector2D position, Vector2D velocity, boolean isstatic) {
		this.object = rect;
		this.position = position;
		this.velocity = velocity;
		this.isstatic = isstatic;
	}
	
	public Object2D (Line2D line, Vector2D position, Vector2D velocity, boolean isstatic) {
		this.object = line;
		this.position = position;	
		this.velocity = velocity;
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
}
