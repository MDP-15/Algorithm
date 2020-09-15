package mdpsimEngine;

import mdpsimEngine.Action2D.Action;

public class Vehicle2D extends Object2D{
	private boolean reverse;
	private double maximumspeed;
	private double traverse;
	private double rotationalmomentum;
	
	public Vehicle2D(Circle2D circle, Vector2D position, Vector2D velocity, Vector2D acceleration, boolean isstatic, boolean reverse, double maximumspeed, double traverse) {
		super(circle, position, velocity, acceleration, isstatic);
		this.reverse = reverse;
		this.maximumspeed = maximumspeed;
		this.traverse = traverse;
		this.rotationalmomentum = 0;
	}
	
	public Vehicle2D (Circle2D circle, Vector2D position, Vector2D velocity, Vector2D acceleration, Vector2D direction, boolean isstatic, boolean reverse, double maximumspeed, double traverse) {
		super(circle, position, velocity, acceleration, direction, isstatic);
		this.reverse = reverse;
		this.maximumspeed = maximumspeed;
		this.traverse = traverse;
		this.rotationalmomentum = 0;
	}
	
	@Override
	public void update(double timestep, Action2D action) {
		if (action != null) {
			if (action.action() == Action.ACCELERATE) {
				if (velocity().length(new Vector2D(0,0)) == 0) {
					this.acceleration(this.direction().unit().multiply(action.value()).rotate(0));
				} else {
					this.acceleration(this.velocity().unit().multiply(action.value()).rotate(0));
				}
			} else if (action.action() == Action.DECELERATE) {
				if (velocity().length(new Vector2D(0,0)) == 0) {
					this.acceleration(this.direction().unit().multiply(action.value()).multiply(-1).rotate(0));
				} else {
					this.acceleration(this.velocity().unit().multiply(action.value()).multiply(-1).rotate(0));
				}
			} else if (action.action() == Action.TURN) {
				rotationalmomentum = action.value();
				if (velocity().length(new Vector2D(0,0)) == 0) {
				} else {
					this.acceleration(this.velocity().unit().multiply(action.value()).multiply(-1).rotate(rotationalmomentum*timestep));
				}
			}
		}
		this.processphysics(timestep);
	}
	
	private void processphysics(double timestep) {
		System.out.println(reverse);
		this.prevpos(this.position());
		this.velocity(this.velocity().add(this.acceleration().multiply(timestep)));
		this.position(this.prevpos().add(this.velocity().multiply(timestep).add(this.acceleration().multiply(0.5*Math.pow(timestep, 2)))));
		if (reverse) {
			if(this.velocity().length(new Vector2D(0,0)) != 0) {
				this.direction(this.prevpos().subtract(this.position()).rotate(rotationalmomentum*timestep));
			} else {
				this.direction(this.direction().rotate(rotationalmomentum*timestep));
			}
		} else {
			if(this.velocity().length(new Vector2D(0,0)) != 0) {
				this.direction(this.position().subtract(this.prevpos()).rotate(rotationalmomentum*timestep));
			} else {
				this.direction(this.direction().rotate(-rotationalmomentum*timestep));
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

