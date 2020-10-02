package mdpsimEngine;

public class Action2D {
	public enum Action{
		ACCELERATE,
		TURN,
		STOP,
		SCAN,
		CALIBRATE;
	}
	
	private Action action;
	private double value;
	
	public Action2D (Action action, double value) {
		this.action = action;
		this.value = value;
	}
	
	public Action action() {
		return this.action;
	}
	
	public void action(Action action) {
		this.action = action;
	}
	
	public double value() {
		return this.value;
	}
	
	public void value(double value) {
		this.value = value;
	}
}
