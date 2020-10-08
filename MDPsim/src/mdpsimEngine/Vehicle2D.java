package mdpsimEngine;
import mdpsimEngine.Action2D.Action;
// ALL ROTATIONS WRT VECTOR2D(0,10)

public class Vehicle2D extends Object2D{
	private double rotationalmomentum;
	
	public Vehicle2D(Circle2D circle, Vector2D position, Vector2D velocity, Vector2D acceleration) {
		super(circle, position, velocity, acceleration, false);
		this.rotationalmomentum = 0;
	}
	
	public Vehicle2D (Circle2D circle, Vector2D position, Vector2D velocity, Vector2D acceleration, Vector2D direction) {
		super(circle, position, velocity, acceleration, direction, false);
		this.rotationalmomentum = 0;
	}
	
	@Override
	public void update(double timestep, Action2D action) {
		if (action != null) {
			if (action.action() == Action.ACCELERATE) {
				this.rotationalmomentum = 0;
				this.acceleration(this.direction().unit().multiply(action.value()).xflip());
			} else if (action.action() == Action.TURN) {
				this.acceleration(new Vector2D(0,0));
				rotationalmomentum = action.value();
			} else if (action.action() == Action.STOP) {
				this.rotationalmomentum = 0;
				this.velocity(new Vector2D(0,0));
				this.acceleration(new Vector2D(0,0));
			} else if (action.action() == Action.CALIBRATE) {
				Vector2D vec = super.position();
				Vector2D dir = super.direction();
				double x = Math.floor((vec.x()-5)/10);
				double y = Math.floor((vec.y()-5)/10);
				this.position(new Vector2D((x*10)+5, (y*10)+5));
				Vector2D newdir = new Vector2D(0,10);
				if (dir.angle(new Vector2D(0,-10)) < dir.angle(newdir)){
					newdir = new Vector2D(0,-10);
				}
				if (dir.angle(new Vector2D(10,0)) < dir.angle(newdir)){
					newdir = new Vector2D(10,0);
				}
				if (dir.angle(new Vector2D(-10,0)) < dir.angle(newdir)){
					newdir = new Vector2D(-10,0);
				}
				this.direction(newdir);
			}
		}
		this.processphysics(timestep);
	}
	
	private void processphysics(double timestep) {
		this.prevpos(this.position());
		this.velocity(this.velocity().add(this.acceleration().multiply(timestep)));
		this.position(this.prevpos().add(this.velocity().multiply(timestep).add(this.acceleration().multiply(0.5*Math.pow(timestep, 2)))));
		if (this.velocity().length() == 0) {
			this.direction(this.direction().rotate(rotationalmomentum*timestep));
		}
	}
	
	public double rotationalmomentum() {
		return this.rotationalmomentum;
	}
	
	public void rotationalmomentum(double rotationalmomentum) {
		this.rotationalmomentum = rotationalmomentum;
	}
}

