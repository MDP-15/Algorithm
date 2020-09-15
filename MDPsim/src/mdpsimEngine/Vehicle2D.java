package mdpsimEngine;

import mdpsimEngine.Action2D.Action;

public class Vehicle2D extends Object2D{
	private boolean reverse;
	private double maximumspeed;
	private double traverse;
	
	public Vehicle2D(Circle2D circle, Vector2D position, Vector2D velocity, Vector2D acceleration, boolean isstatic, boolean reverse, double maximumspeed, double traverse) {
		super(circle, position, velocity, acceleration, isstatic);
		this.reverse = reverse;
		this.maximumspeed = maximumspeed;
		this.traverse = traverse;
	}
	
	public Vehicle2D (Circle2D circle, Vector2D position, Vector2D velocity, Vector2D acceleration, Vector2D direction, boolean isstatic, boolean reverse, double maximumspeed, double traverse) {
		super(circle, position, velocity, acceleration, direction, isstatic);
		this.reverse = reverse;
		this.maximumspeed = maximumspeed;
		this.traverse = traverse;
	}
	
	@Override
	public void update(double timestep, Action2D action) {

		if (action == null) {
			action(timestep);
		} else if (action.action() == Action.ACCELERATE) {
			if (velocity().length(new Vector2D(0,0)) == 0) {
				this.acceleration(this.direction().unit().multiply(action.value()));
			} else {
				this.acceleration(this.velocity().unit().multiply(action.value()));
			}
			action(timestep);
		} else if (action.action() == Action.DECELERATE) {
			if (velocity().length(new Vector2D(0,0)) == 0) {
				this.acceleration(this.direction().unit().multiply(action.value()).multiply(-1));
			} else {
				this.acceleration(this.velocity().unit().multiply(action.value()).multiply(-1));
			}
			System.out.println("called");
			action(timestep);
		}
	}
	
	private void action(double timestep) {
		this.prevpos(this.position());
		this.velocity(this.velocity().add(this.acceleration().multiply(timestep)));
		this.position(this.prevpos().add(this.velocity().multiply(timestep).add(this.acceleration().multiply(0.5*Math.pow(timestep, 2)))));
		if (reverse) {
			if(this.velocity().length(new Vector2D(0,0)) != 0) {
				this.direction(this.prevpos().subtract(this.position()));
			}
		} else {
			if(this.velocity().length(new Vector2D(0,0)) != 0) {
				this.direction(this.position().subtract(this.prevpos()));
			}
		}
	}
	public boolean reverse() {
		return this.reverse;
	}
	
	public void reverse(boolean reverse) {
		this.reverse = reverse;
	}
}

