package mdpsimEngine;
import java.util.ArrayList;
/* 	MDP-specific 2D physics engine.
*	Broad-phase collision detection using AABB.
*/
public class Engine2D {
	private double time;
	private double timestep;
	private ArrayList<Object2D> staticobjects;
	private ArrayList<Object2D> movingobjects;
	private BoundingBoxList bbx_static;
	private BoundingBoxList bby_static;
	
	//Private methods
	private void parseStaticity(ArrayList<Object2D> objects) {
		for (int a = 0; a < objects.size(); a++) {
			if (objects.get(a).isstatic() == true) {
				this.staticobjects.add(objects.get(a));
			} else {
				this.movingobjects.add(objects.get(a));
			}
		}
	}
	
	private void parseBoundingBoxes(ArrayList<Object2D> objects) {
		for (int a = 0; a < objects.size(); a++) {
			Object2D obj = objects.get(a);
			if (obj.type() == Line2D.class) {
				Line2D line = ((Line2D) obj.object());
				BoundingBox2D bb = new BoundingBox2D(line.start(), line.end(), obj);
				ArrayList<BoundingBoxPointer> bbpointersx = bb.bbpointersx();
				ArrayList<BoundingBoxPointer> bbpointersy = bb.bbpointersy();
				for(int a = 0; a < bbpointersx.size(); a++) {
					bbx.insert(bbpointersx.get(a));
				}
			} else {
				Circle2D circle = ((Circle2D) obj.object());
				double radius = circle.radius();
				Vector2D npos = new Vector2D(-radius,-radius);
				Vector2D ppos = new Vector2D(radius, radius);
				BoundingBox2D bb = new BoundingBox2D(obj.position().add(npos),obj.position().add(ppos),obj);
				ArrayList<BoundingBoxPointer> bbpointersx = bb.bbpointersx();
				ArrayList<BoundingBoxPointer> bbpointersy = bb.bbpointersy();
			}
		}
	}

	//Public methods
	public Engine2D(ArrayList<Object2D> objects, double timestep) {
		this.time = 0;
		this.timestep = timestep;
		this.staticobjects = new ArrayList<Object2D>();
		this.movingobjects = new ArrayList<Object2D>();
		this.parseStaticity(objects);
	}
	
	public ArrayList<Object2D >staticObjects(){
		return staticobjects;
	}
	
	public ArrayList<Object2D >movingObjects(){
		return movingobjects;
	}
}
