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
		actionhistory.add(new Action2D(Action.ACCELERATE, 0));
		this.currentaction = null;
		this.desiredaction = null;
		this.time = 0.0;
	}
	
	//TR -> turn right at 0.5pi/sec for 1 second
	//TL -> turn left at 0.5pi/sec for 1 second
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
				if (ac != Action.TURN && act.value() != Math.PI/2) {
					action = new Action2D(Action.TURN, Math.PI/2);
				} else if (ac == Action.TURN && act.value() != Math.PI/2) {
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
				} else if (ac == Action.TURN && act.value() != -Math.PI/2) {
					action = new Action2D(Action.TURN, -Math.PI/2);
				}
			} else if (currentaction == RobotAction.F1) {
				Action ac;
				try {
					ac = act.action();
				} catch (Exception e) {
					ac = null;
				} if (ac != Action.ACCELERATE) {
					action = new Action2D(Action.ACCELERATE, 10);
				} else if (ac == Action.ACCELERATE && act.value() != 10) {
					action = new Action2D(Action.ACCELERATE, 10);
				}
			}
		} else {
			if (currentaction == RobotAction.TR || currentaction == RobotAction.TL) {
				if (time-actiontime >= 1) {
					action = new Action2D(Action.TURN, 0);							
					currentaction = null;
				} else {
					action = null;
				}
			} else if (currentaction == RobotAction.F1) {
				if (time-actiontime >= 1) {
					if (act.action() == Action.ACCELERATE && act.value() == 10) {
						action = new Action2D(Action.ACCELERATE,-10);
					} else if (act.action() == Action.ACCELERATE && act.value() == -10){
						action = new Action2D(Action.ACCELERATE,0);
						currentaction = null;
					} else {
						action = null;
					}
				}
			}
		}
		this.actionhistory.add(action);
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
