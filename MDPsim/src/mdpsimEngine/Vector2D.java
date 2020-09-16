package mdpsimEngine;
import java.lang.Math;

public class Vector2D {
	private double x;
	private double y;
	
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void x (double x) {
		this.x = x;
	}
	
	public void y (double y) {
		this.y = y;
	}
	
	public double x() {
		return this.x;
	}
	
	public double y() {
		return this.y;
	}
	
	public Vector2D add(Vector2D component) {
		return new Vector2D(this.x + component.x(), this.y + component.y());
	}
	
	public Vector2D subtract(Vector2D component) {
		return new Vector2D(this.x - component.x(), this.y - component.y());
	}
	
	public Vector2D add(double x, double y) {
		return new Vector2D(this.x + x, this.y + y);
	}
	
	public Vector2D multiply(double multiplier) {
		return new Vector2D(this.x * multiplier, this.y * multiplier);
	}
	
	public Vector2D rotate(double radians) {
		return new Vector2D(this.x*Math.cos(radians) - this.y*Math.sin(radians), this.x*Math.sin(radians) + this.y *Math.cos(radians));
	}
	
	public Vector2D unit() {
		if (this.x == 0 && this.y == 0) {
			return new Vector2D(0,0);
		}
		return this.multiply((double)1/(Math.sqrt(Math.pow(this.x, 2)+Math.pow(this.y, 2))));
	}
	
	public double length(Vector2D vec) {
		return Math.sqrt(Math.pow(vec.x()-this.x, 2) + Math.pow(vec.y()-this.y,2));
	}
	
	public double length() {
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y,2));
	}
	
	public double dot(Vector2D vec) {
		return this.x*vec.x() + this.y*vec.y();
	}
	
	public double angle(Vector2D vec) {
		return Math.acos((double)this.dot(vec)/(this.length(new Vector2D(0,0))*vec.length(new Vector2D(0,0))));
	}
	
	public void print() {
		System.out.println("X :" +this.x+" Y :"+this.y);
	}
	
	public Vector2D flip() {
		return new Vector2D(this.y, this.x);
	}
}
