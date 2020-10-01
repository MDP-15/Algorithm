package mdpsimRobot;
import mdpsimEngine.Action2D;
import mdpsimEngine.Action2D.Action;
import mdpsimEngine.Object2D;
import mdpsimEngine.Vector2D;

import java.text.DecimalFormat;
import java.util.ArrayList;

import mdpsim.MDPSIM;

public class Robot {
	public ArrayList<Sensor> sensors;
	public ArrayList<Double> sensorvalues;
	public double radius;
	public Object2D taggedobject;
	public MovementHandler mh;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	public ArrayList<RobotAction> actionqueue;
	public LogicHandler lh;
	public static boolean oneMovement = false;
	
	public Robot(ArrayList<Sensor> sensors, double radius) {
		this.sensors = sensors;
		this.radius = radius;
		this.sensorvalues = new ArrayList<>();
		this.actionqueue = new ArrayList<RobotAction>(0);
		this.mh = new MovementHandler(actionqueue);
		this.lh = new LogicHandler(15,20,1,1);
		lh.setUnexploredMap(14, 4, 20, 15, RobotDirection.RIGHT);
	}
	
	public void addSensor(String name,Vector2D position, Vector2D direction, double minrange, double maxrange) {
		Vector2D posnormalized = position.multiply((double)position.length(new Vector2D(0,0))/radius);
		Vector2D unitdirection = direction.unit();
		this.sensors.add(new Sensor(name,posnormalized,unitdirection, minrange, maxrange));
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
			} else if (val > maxrange){
				sensorvalues.add(null);
			} else {
				sensorvalues.add(10.0);
			}
		}
	}
	
	public void printSensor() {
		for (int a = 0; a < sensorvalues.size(); a++) {
			System.out.print(sensors.get(a).name+"\t ");
		}
		System.out.println();
		for (int a = 0; a < sensorvalues.size(); a++) {
			if(sensorvalues.get(a) == null) {
				System.out.print(sensorvalues.get(a)+"\t\t ");
			}
			else {
				System.out.print(df2.format(sensorvalues.get(a))+"\t\t ");
			}
			
		}
		System.out.println();
	}
	
	public ArrayList<Action2D> getNextAction(double time){
		ArrayList<Action2D> actions = new ArrayList<Action2D>(0);
		try {
			sensorvalues.get(0);
			//lh.scan(sensorvalues);
		} catch (Exception e) {}
		System.out.println(lh.informationGained(1, 1, RobotDirection.DOWN));
		actions.add(mh.doNext(time , sensorvalues)); // DO NOT TOUCH
		return actions;
	}
}
