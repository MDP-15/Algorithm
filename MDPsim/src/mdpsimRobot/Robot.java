package mdpsimRobot;
import mdpsimEngine.Object2D;
import mdpsimEngine.Vector2D;
import java.util.ArrayList;

public class Robot {
	public ArrayList<Sensor> sensors;
	public double radius;
	public Object2D taggedobject;
	
	public Robot(ArrayList<Sensor> sensors, double radius) {
		this.sensors = sensors;
		this.radius = radius;
	}
	
	public void addSensor(Vector2D position, Vector2D direction, double minrange, double maxrange) {
		Vector2D posnormalized = position.multiply((double)position.length(new Vector2D(0,0))/radius);
		Vector2D unitdirection = direction.unit();
		this.sensors.add(new Sensor(posnormalized,unitdirection, minrange, maxrange));
	}
}
