package mdpsimRobot;
import mdpsimEngine.Vector2D;

public class Sensor {
	public String name;
	public double minrange;
	public double maxrange;
	public Vector2D position;
	public Vector2D direction;
	
	public Sensor (String name, Vector2D position, Vector2D direction, double minrange, double maxrange) {
		this.name = name;
		this.minrange = minrange;
		this.maxrange = maxrange;
		this.position = position;
		this.direction = direction;
	}
	
	public Vector2D position() {
		return this.position;
	}
	
	public void position(Vector2D pos) {
		this.position = pos;
	}
	
	public Vector2D direction() {
		return this.direction;
	}
	
	public void direction(Vector2D direction) {
		this.direction = direction;
	}
	
	public double maxrange() {
		return this.maxrange;
	}
	
	public void maxrange(double maxrange) {
		this.maxrange = maxrange;
	}
	
	public double minrange() {
		return this.minrange;
	}
	
	public void minrange(double minrange) {
		this.minrange = minrange;
	}
}
