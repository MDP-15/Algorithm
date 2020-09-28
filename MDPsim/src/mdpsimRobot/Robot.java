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
	
	// robot logic
	public ArrayList<Action2D> policyUpdate(){
		ArrayList<Action2D> actions = new ArrayList<Action2D>(0);
<<<<<<< Updated upstream
<<<<<<< Updated upstream
		if (sensorvalues.get(1) == null) {
			actions.add(new Action2D(Action.ACCELERATE, 10));
		} else if (sensorvalues.get(1) < 10) {
			actions.add(new Action2D(Action.ACCELERATE, 40));
		} else if (sensorvalues.get(1) > 10) {
			actions.add(new Action2D(Action.ACCELERATE, -40));
=======
		if (sensorvalues != null) {
			if(sensorvalues.get(0) <= 15) {
				mh.addAction(RobotAction.TL);
			}
			else if(sensorvalues.get(5) <= 15) {
				mh.addAction(RobotAction.TR);
			}
>>>>>>> Stashed changes
=======
		
		
		if (sensorvalues != null) {
			mh.addAction(RobotAction.TL);
		}	
		if(sensorvalues.get(3) != null && sensorvalues.get(3) > 30) {
			mh.addAction(RobotAction.TL);
		}
		
		if(sensorvalues.get(3) != null && sensorvalues.get(3) < 15) {
			
			//if front sensors >30 move forward 
			//if front sensors <15 TR
			mh.addAction(RobotAction.TR);
		}
		if(sensorvalues.get(0) != null) {
			//System.out.println("INSIDE GET 0");
			if(sensorvalues.get(0) <= 15) {
				System.out.println("TURN LEFT");
				mh.addAction(RobotAction.TL);
			}
		}
		
		if(sensorvalues.get(5) != null) {
			//System.out.println("INSIDE GET 5");
			if(sensorvalues.get(5) <= 15) {
				System.out.println("TURN RIGHT");
				mh.addAction(RobotAction.TR);
			}
>>>>>>> Stashed changes
		}
		actions.add(mh.doNext(time , sensorvalues));
		return actions;
	}

}
