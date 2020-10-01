package mdpsimRobot;

import mdpsimEngine.Action2D;
import mdpsimEngine.Action2D.Action;
import mdpsimEngine.Object2D;
import mdpsimEngine.Vector2D;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Stack;

import mdpsim.MDPSIM;

public class Robot {
	public ArrayList<Sensor> sensors;
	public ArrayList<Double> sensorvalues;
	public double radius;
	public Object2D taggedobject;
	public MovementHandler mh;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	public ArrayList<RobotAction> actionqueue;
	public static LogicHandler lh;
	public static boolean oneMovement = false;
	Stack<String> actionstack = new Stack<String>();

	public Robot(ArrayList<Sensor> sensors, double radius) {
		this.sensors = sensors;
		this.radius = radius;
		this.sensorvalues = new ArrayList<>();
		this.actionqueue = new ArrayList<RobotAction>(0);
//		actionqueue.add(RobotAction.TL);
//		actionqueue.add(RobotAction.F1);
//		actionqueue.add(RobotAction.F1);
//		actionqueue.add(RobotAction.F1);
//		actionqueue.add(RobotAction.F1);
//		actionqueue.add(RobotAction.TL);
//		actionqueue.add(RobotAction.TL);
//		actionqueue.add(RobotAction.F1);
//		actionqueue.add(RobotAction.F1);
//		actionqueue.add(RobotAction.F1);
//		actionqueue.add(RobotAction.F1);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.TR);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.F2);
//		actionqueue.add(RobotAction.TL);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.F2);
//		actionqueue.add(RobotAction.TL);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.TR);
//		actionqueue.add(RobotAction.TR);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.F1);
//		actionqueue.add(RobotAction.TL);
//		actionqueue.add(RobotAction.F2);
//		actionqueue.add(RobotAction.TR);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.TR);
//		actionqueue.add(RobotAction.TR);
//		actionqueue.add(RobotAction.TR);
//		actionqueue.add(RobotAction.TL);
//		actionqueue.add(RobotAction.TL);
		this.mh = new MovementHandler(actionqueue);
		this.lh = new LogicHandler(15, 20, 1, 1);
		// lh.parseMDF("000000000000000000000000000000000000010000000000000000000000000000000000000000000000001110111111000000000000000000000000000000000000000000000000010000000000000000000000001110000000000000000000000000000000000000010000000000000000000000001110000000000000000000000010000000000000000000000000000000000000");
		lh.parseMDF(MDPSIM.mdfString);
		//printActions();
	}
	
	

	public void addSensor(String name, Vector2D position, Vector2D direction, double minrange, double maxrange) {
		Vector2D posnormalized = position.multiply((double) position.length(new Vector2D(0, 0)) / radius);
		Vector2D unitdirection = direction.unit();
		this.sensors.add(new Sensor(name, posnormalized, unitdirection, minrange, maxrange));
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
			if (val <= maxrange) {
				sensorvalues.add(val);
			} else {
				sensorvalues.add(null);
			}
		}
	}

	public void printSensor() {
		for (int a = 0; a < sensorvalues.size(); a++) {
			System.out.print(sensors.get(a).name + "\t ");
		}
		System.out.println();
		for (int a = 0; a < sensorvalues.size(); a++) {
			if (sensorvalues.get(a) == null) {
				System.out.print(sensorvalues.get(a) + "\t\t ");
			} else {
				System.out.print(df2.format(sensorvalues.get(a)) + "\t\t ");
			}

		}
		System.out.println();
	}

	// robot logic
	public ArrayList<Action2D> getNextAction(double time) {
		ArrayList<Action2D> actions = new ArrayList<Action2D>(0);
		actions.add(mh.doNext(time, sensorvalues)); // DO NOT TOUCH
		return actions;
	}

	public void robotExploration() {
//		if (!mh.moving) {
//			if (!oneMovement) {
//				if (sensorvalues.get(2) == null && sensorvalues.get(3) == null && sensorvalues.get(4) == null) {
//					oneMovement = true;
//					actionqueue.add(RobotAction.F1);
//				} else if (sensorvalues.get(4) == null && sensorvalues.get(3) == null && sensorvalues.get(2) > 19) {
//					oneMovement = true;
//					actionqueue.add(RobotAction.F1);
//
//				} else if (sensorvalues.get(2) == null && sensorvalues.get(4) == null && sensorvalues.get(3) > 19) {
//					oneMovement = true;
//					actionqueue.add(RobotAction.F1);
//
//				} else if (sensorvalues.get(2) == null && sensorvalues.get(3) == null && sensorvalues.get(4) > 19) {
//					oneMovement = true;
//					actionqueue.add(RobotAction.F1);
//
//				} else if (sensorvalues.get(4) == null && sensorvalues.get(3) == null && sensorvalues.get(2) < 10) {
//					oneMovement = true;
//					actionqueue.add(RobotAction.TL);
//
//				} else if (sensorvalues.get(2) == null && sensorvalues.get(4) == null && sensorvalues.get(3) < 10) {
//					oneMovement = true;
//					actionqueue.add(RobotAction.TL);
//
//				} else if (sensorvalues.get(2) == null && sensorvalues.get(3) == null && sensorvalues.get(4) < 10) {
//					oneMovement = true;
//					actionqueue.add(RobotAction.TL);
//
//				}
//				
//			}
//		}
	}

	public void printActions() {
		System.out.println("Doing fastestPath");
		Node n = lh.computeFastestPath(18, 1, 1, 13, RobotDirection.RIGHT);
		actionstack = new Stack<String>();
		boolean isnull = false;
		while (!isnull) {
			if (n != null) {
				if (n.ra != null) {
					String raction = n.ra.toString();
					System.out.println(raction);
					actionstack.push(raction);
				}
				if (n.prev != null) {
					n = n.prev;
				} else {
					isnull = true;
				}
			}
		}
		
		while (!actionstack.isEmpty()) {
			switch (actionstack.peek()) {
			case "F1":
				actionqueue.add(RobotAction.F1);
				break;
			case "F2":
				actionqueue.add(RobotAction.F2);
				break;
			case "F3":
				actionqueue.add(RobotAction.F3);
				break;
			case "TL":
				actionqueue.add(RobotAction.TL);
				break;
			case "TR":
				actionqueue.add(RobotAction.TR);
				break;
			}
			actionstack.pop();
		}
	}
}
