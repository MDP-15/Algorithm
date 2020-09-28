package mdpsimRobot;
import java.util.ArrayList;

import mdpsimEngine.Action2D;
import mdpsimEngine.Action2D.Action;

public class MovementHandler {
	public ArrayList<ArrayList<Double>> sensorhistory;
	public ArrayList<Double> timehistory;
	public ArrayList<Action2D> actionhistory;
	public RobotAction currentaction;
	public RobotAction desiredaction;
	public double time;
	
	public MovementHandler () {
		this.sensorhistory = new ArrayList<ArrayList<Double>>(0);
		this.timehistory = new ArrayList<Double>(0);
		this.actionhistory = new ArrayList<Action2D>(0);
		sensorhistory.add(new ArrayList<Double>());
		timehistory.add(0.0);
		actionhistory.add(null);
		this.currentaction = null;
		this.desiredaction = null;
		this.time = 0.0;
	}
	
	public Action2D doNext(double time, ArrayList<Double> sensorreadings) {
		Action2D action = null;
		this.sensorhistory.add(sensorreadings);
		this.timehistory.add(time);
		int actionint = getPreviousActionNumber();
		double actiontime = timehistory.get(actionint);
		Action2D act = actionhistory.get(actionint);
		if (currentaction == null) {
			currentaction = desiredaction;
			desiredaction = null;
			if (currentaction == RobotAction.TR) {
				Action ac;
				try {
					ac = act.action();
				} catch (Exception e) {
					ac = null;
				}
				if (ac != Action.TURN) {
					action = new Action2D(Action.TURN, Math.PI/2);
				}
			} else if (currentaction == RobotAction.TL) {
				Action ac;
				try {
					ac = act.action();
				} catch (Exception e) {
					ac = null;
				}
				if (ac != Action.TURN) {
					action = new Action2D(Action.TURN, -Math.PI/2);
				}
			}
		} else {
			if (currentaction == RobotAction.TR || currentaction == RobotAction.TL) {
					if (time-actiontime > 1) {
						action = new Action2D(Action.TURN, 0);							
						currentaction = null;
					} else {
						action = null;
					}
				}
			}
		actionhistory.add(action);
		return action;
	}
	
	public int getPreviousActionNumber() {
		for (int a = actionhistory.size()-1; a >= 0; a--) {
			if (actionhistory.get(a) != null) {
				return a;
			}
		}
		return 0;
	}
	public boolean addAction(RobotAction action) {
		if (desiredaction == null) {
			desiredaction = action;
			return true;
		} else {
			return false;
		}
	}
}
