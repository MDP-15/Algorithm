package mdpsimRobot;
import mdpsimEngine.Vector2D;

public class Sensor {
	public double minrange;
	public double maxrange;
	public Vector2D position;
	public Vector2D direction;
	
	public Sensor (Vector2D position, Vector2D direction, double minrange, double maxrange) {
		this.minrange = minrange;
		this.maxrange = maxrange;
		this.position = position;
		this.direction = direction;
	}
}
