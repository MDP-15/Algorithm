package mdpsimRobot;
import mdpsimEngine.Action2D;
import mdpsimEngine.Action2D.Action;
import mdpsimEngine.Object2D;
import mdpsimEngine.Vector2D;
import java.util.ArrayList;

public class Robot {
	public ArrayList<Sensor> sensors;
	public ArrayList<Double> sensorvalues;
	public double radius;
	public Object2D taggedobject;
	
	public Robot(ArrayList<Sensor> sensors, double radius) {
		this.sensors = sensors;
		this.radius = radius;
		this.sensorvalues = new ArrayList<>();
	}
	
	public void addSensor(Vector2D position, Vector2D direction, double minrange, double maxrange) {
		Vector2D posnormalized = position.multiply((double)position.length(new Vector2D(0,0))/radius);
		Vector2D unitdirection = direction.unit();
		this.sensors.add(new Sensor(posnormalized,unitdirection, minrange, maxrange));
	}
	
	public void updateSensors(ArrayList<Double> values) {
		sensorvalues.clear();
		if (values.size() != sensors.size()) {
			System.out.println("sensor vector incorrectly sized!");
			return;
		}
		for (int a = 0; a < values.size(); a++) {
			double maxrange = sensors.get(a).maxrange();
			double minrange = sensors.get(a).minrange();
			double val = values.get(a);
			if (val >= minrange && val <= maxrange) {
				sensorvalues.add(val);
			} else {
				sensorvalues.add(null);
			}
		}
	}
	
	public void printSensor() {
		for (int a = 0; a < sensorvalues.size(); a++) {
			System.out.print(sensorvalues.get(a)+" ");
		}
		System.out.println();
	}
	
	public ArrayList<Action2D> policyUpdate(){
		ArrayList<Action2D> actions = new ArrayList<Action2D>(0);
		if (sensorvalues.get(2) == null) {
			actions.add(new Action2D(Action.ACCELERATE, 10));
		} else if (sensorvalues.get(2) < 45) {
			actions.add(new Action2D(Action.ACCELERATE, 100));
		} else if (sensorvalues.get(2) > 45) {
			actions.add(new Action2D(Action.ACCELERATE, -100));
		}
		return actions;
	}

}
