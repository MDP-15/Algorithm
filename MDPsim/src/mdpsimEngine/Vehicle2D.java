package mdpsimEngine;

public class Vehicle2D extends Object2D{
	private boolean reverse;
	
	public Vehicle2D(Circle2D circle, Vector2D position, Vector2D velocity, Vector2D acceleration, boolean isstatic, boolean reverse) {
		super(circle, position, velocity, acceleration, isstatic);
		this.reverse = reverse;
	}
	
	public Vehicle2D (Circle2D circle, Vector2D position, Vector2D velocity, Vector2D acceleration, Vector2D direction, boolean isstatic, boolean reverse) {
		super(circle, position, velocity, acceleration, direction, isstatic);
		this.reverse = reverse;
	}
	
	@Override
	public void update(double timestep) {
		this.prevpos(this.position());
		this.velocity(this.velocity().add(this.acceleration().multiply(timestep)));
		if (reverse) {
			if(this.velocity().length(new Vector2D(0,0)) != 0) {
				this.direction(this.velocity().multiply(-1));
			}
		} else {
			if(this.velocity().length(new Vector2D(0,0)) != 0) {
				this.direction(this.velocity());
			}
		}
		this.position(this.prevpos().add(this.velocity().multiply(timestep).add(this.acceleration().multiply(0.5*Math.pow(timestep, 2)))));
	}
}
