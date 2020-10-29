package mdpsimRobot;

import mdpsimEngine.Action2D;

public class QueuedAction extends Action2D{
	double time;
	boolean flag;
	
	public QueuedAction(Action action, double value, double time, boolean flag) {
		super(action, value);
		this.time = time;
		this.flag = flag;
	}
	
	public QueuedAction(Action2D action, double time, boolean flag) {
		super(action.action(), action.value());
		this.time = time;
		this.flag = flag;
	}
	
	public boolean flag() {
		return this.flag;
	}
}
