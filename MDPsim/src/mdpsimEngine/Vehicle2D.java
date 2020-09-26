package mdpsimEngine;

import mdpsimEngine.Action2D.Action;

public class Vehicle2D extends Object2D{
	private boolean angleindicator;
	private double maximumspeed;
	private double traverse;
	private double rotationalmomentum;
	
	public Vehicle2D(Circle2D circle, Vector2D position, Vector2D velocity, Vector2D acceleration, boolean isstatic, boolean reverse, double maximumspeed, double traverse) {
		super(circle, position, velocity, acceleration, isstatic);
		this.angleindicator = reverse;
		this.maximumspeed = maximumspeed;
		this.traverse = traverse;
		this.rotationalmomentum = 0;
	}
	
	public Vehicle2D (Circle2D circle, Vector2D position, Vector2D velocity, Vector2D acceleration, Vector2D direction, boolean isstatic, boolean reverse, double maximumspeed, double traverse) {
		super(circle, position, velocity, acceleration, direction, isstatic);
		this.angleindicator = reverse;
		this.maximumspeed = maximumspeed;
		this.traverse = traverse;
		this.rotationalmomentum = 0;
	}
	
	@Override
	public void update(double timestep, Action2D action) {
		if (action != null) {
			if (action.action() == Action.ACCELERATE) {
				this.rotationalmomentum = 0;
				if (velocity().length() == 0) {
					if (angleindicator) {
						this.acceleration(this.direction().unit().multiply(action.value()).rotate(0));
					} else {
						this.acceleration(this.direction().unit().multiply(action.value()).rotate(0).multiply(-1));
					}
				} else {
					this.acceleration(this.velocity().unit().multiply(action.value()).rotate(0));
				}
			} else if (action.action() == Action.TURN) {
				rotationalmomentum = action.value();
				if (velocity().length() != 0) {
					this.acceleration(this.velocity().unit().multiply(action.value()).rotate(rotationalmomentum));
				}
			}
		}
		this.processphysics(timestep);
	}
	
	private void processphysics(double timestep) {

		this.prevpos(this.position());
		this.velocity(this.velocity().add(this.acceleration().multiply(timestep)));
		this.position(this.prevpos().add(this.velocity().multiply(timestep).add(this.acceleration().multiply(0.5*Math.pow(timestep, 2)))));
		if (this.velocity().length() == 0) {
			double angle1 = new Vector2D(10,0).angle(this.direction());
			double angle2 = new Vector2D(-10,0).angle(this.direction());
			if (angle1 < angle2) {
				angleindicator = true;
			} else {
				angleindicator = false;
			}
			this.direction(this.direction().rotate(rotationalmomentum*timestep));
		} else {
			if (angleindicator) {
				this.direction(this.prevpos().subtract(this.position()));
			} else {
				this.direction(this.position().subtract(this.prevpos()));
			}
		}
	}
	public boolean angleindicator() {
		return this.angleindicator;
	}
	
	public void angleindicator(boolean angleindicator) {
		this.angleindicator = angleindicator;
	}
	
	public double rotationalmomentum() {
		return this.rotationalmomentum;
	}
	
	public void rotationalmomentum(double rotationalmomentum) {
		this.rotationalmomentum = rotationalmomentum;
	}
}

