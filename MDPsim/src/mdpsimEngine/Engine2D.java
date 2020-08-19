package mdpsimEngine;
import java.util.ArrayList;
//MDP-specific line-based 2d physics engine

public class Engine2D {
	private double time;
	private double timestep;
	private ArrayList<Object2D> staticobjects;
	private ArrayList<Object2D> movingobjects;
	
	//Private methods
	private void parseObjects(ArrayList<Object2D> objects) {
		System.out.println(objects.size());
		for (int a = 0; a < objects.size(); a++) {
			if (objects.get(a).isstatic() == true) {
				this.staticobjects.add(objects.get(a));
			} else {
				this.movingobjects.add(objects.get(a));
			}
		}
	}
	
	private ArrayList<Object2D> getCollisionBoundingBoxes(ArrayList<Object2D> movingobjects) {
		ArrayList<Object2D> boundingboxes = new ArrayList<Object2D>();
		for (int a = 0; a < movingobjects.size(); a++) {
			Vector2D position = movingobjects.get(a).position();
			Vector2D unitnormal = movingobjects.get(a).velocity().rotate(90).unit();
			double radius = ((Circle2D) movingobjects.get(a).object()).radius();
			Vector2D OA = position.add(unitnormal.multiply(radius));
			Vector2D OB = position.add(unitnormal.multiply(-radius));
		}
	}
	
	private void next(int timesteps) {
		
	}
	
	//Public methods
	public Engine2D(ArrayList<Object2D> objects, double timestep) {
		this.time = 0;
		this.timestep = timestep;
		this.staticobjects = new ArrayList<Object2D>();
		this.movingobjects = new ArrayList<Object2D>();
		this.parseObjects(objects);
	}
	
	public ArrayList<Object2D >staticObjects(){
		return staticobjects;
	}
	
	public ArrayList<Object2D >movingObjects(){
		return movingobjects;
	}
}
