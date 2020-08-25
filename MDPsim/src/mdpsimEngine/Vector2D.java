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
		return new Vector2D(this.x + component.x, this.y + component.y);
	}
	
	public Vector2D add(double x, double y) {
		return new Vector2D(this.x + x, this.y + y);
	}
	
	public Vector2D multiply(double multiplier) {
		return new Vector2D(this.x * multiplier, this.y * multiplier);
	}
	
	public Vector2D rotate(double degrees) {
		return new Vector2D(this.x*Math.cos(degrees) - this.y*Math.sin(degrees), this.x*Math.sin(degrees) + this.y *Math.cos(degrees));
	}
	
	public Vector2D unit() {
		return this.multiply(1/Math.sqrt(Math.pow(this.x, 2) +Math.pow(this.y, 2)));
	}
	
	public double length(Vector2D vec) {
		return Math.sqrt(Math.pow(vec.x()-x, 2) + Math.pow(vec.y()-y,2));
	}
}
