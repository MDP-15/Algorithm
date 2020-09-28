package mdpsimRobot;
import java.util.ArrayList;

import mdpsimEngine.Action2D;
import mdpsimEngine.Action2D.Action;

public class MovementHandler {
	public ArrayList<ArrayList<Double>> sensorhistory;
	public ArrayList<Double> timehistory;
	public ArrayList<Action2D> actionhistory;
	public ArrayList<QueuedAction> queuedactions;
	public RobotAction currentaction;
	public RobotAction desiredaction;
	public double time;
	public ArrayList<RobotAction> actionqueue;

	public MovementHandler (ArrayList<RobotAction> actionqueue) {
		this.sensorhistory = new ArrayList<ArrayList<Double>>(0);
		this.timehistory = new ArrayList<Double>(0);
		this.actionhistory = new ArrayList<Action2D>(0);
		this.queuedactions = new ArrayList<QueuedAction>(0);
		sensorhistory.add(new ArrayList<Double>());
		timehistory.add(0.0);
		actionhistory.add(new Action2D(Action.ACCELERATE, 0));
		this.currentaction = null;
		this.desiredaction = null;
		this.time = 0.0;
		this.actionqueue = actionqueue;
	}
	
	//TR -> turn right at 0.5pi/sec for 1 second
	//TL -> turn left at 0.5pi/sec for 1 second
	public Action2D doNext(double time, ArrayList<Double> sensorreadings) {
		Action2D action = null;
		this.sensorhistory.add(sensorreadings);
		this.timehistory.add(time);
		this.time = time;
		double t = queueLatestTime();
		if (currentaction == null) {
			if (actionqueue.size() > 0) {
				currentaction = actionqueue.remove(0);
				}
			if (currentaction == RobotAction.TR) {
				queuedactions.add(new QueuedAction(Action.TURN, Math.PI/2, t, false));
				queuedactions.add(new QueuedAction(Action.TURN, 0, t + 1,true));
			} else if (currentaction == RobotAction.TL) {
				queuedactions.add(new QueuedAction(Action.TURN, -Math.PI/2, t, false));
				queuedactions.add(new QueuedAction(Action.TURN, 0, t + 1, true));
			} else if (currentaction == RobotAction.F1) {
				queuedactions.add(new QueuedAction(Action.ACCELERATE, 10, t, false));
				queuedactions.add(new QueuedAction(Action.ACCELERATE, -10, t+1, false));
				queuedactions.add(new QueuedAction(Action.STOP, 0, t+2, true));
			} else if (currentaction == RobotAction.F2) {
				queuedactions.add(new QueuedAction(Action.ACCELERATE, 20, t, false));
				queuedactions.add(new QueuedAction(Action.ACCELERATE, -20, t+1, false));
				queuedactions.add(new QueuedAction(Action.STOP, 0, t+2, true));
			} else if (currentaction == RobotAction.F3) {
				queuedactions.add(new QueuedAction(Action.ACCELERATE, 30, t, false));
				queuedactions.add(new QueuedAction(Action.ACCELERATE, -30, t+1, false));
				queuedactions.add(new QueuedAction(Action.STOP, 0, t+2, true));
			}
		}
		try {
			if (time >= ((QueuedAction)queuedactions.get(0)).time) {
				System.out.println(((QueuedAction)queuedactions.get(0)).time);
				action = queuedactions.remove(0);
				System.out.println(action.action());
				if (((QueuedAction) action).flag()) {
					currentaction = null;
				}
			}
		} catch (Exception e) {}
		this.actionhistory.add(action);
		return action;
	}
	
	public double queueLatestTime() {
		if (queuedactions.size() == 0) {
			return time;
		} else {
			return queuedactions.get(queuedactions.size()-1).time;
		}
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
