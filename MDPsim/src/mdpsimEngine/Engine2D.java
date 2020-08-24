package mdpsimEngine;
import java.util.ArrayList;
import java.util.HashMap;
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
	
	public void parseBoundingBoxes(ArrayList<Object2D> objects) {
		for (int a = 0; a < objects.size(); a++) {
			Object2D obj = objects.get(a);
			if (obj.type() == Line2D.class) {
				Line2D line = ((Line2D) obj.object());
				BoundingBox2D bb = new BoundingBox2D(line.start(), line.end(), obj);
				ArrayList<BoundingBoxPointer> bbpointersx = bb.bbpointersx();
				ArrayList<BoundingBoxPointer> bbpointersy = bb.bbpointersy();
				for(int b = 0; b < bbpointersx.size(); b++) {
					if (obj.isstatic()) {
						bbx_static.insert(bbpointersx.get(b));
					}
				}
				for(int b = 0; b < bbpointersy.size(); b++) {
					if (obj.isstatic()) {
						bby_static.insert(bbpointersy.get(b));
					}
				}
			} else {
				Circle2D circle = ((Circle2D) obj.object());
				double radius = circle.radius();
				Vector2D npos = new Vector2D(-radius,-radius);
				Vector2D ppos = new Vector2D(radius, radius);
				BoundingBox2D bb = new BoundingBox2D(obj.position().add(npos),obj.position().add(ppos),obj);
				ArrayList<BoundingBoxPointer> bbpointersx = bb.bbpointersx();
				ArrayList<BoundingBoxPointer> bbpointersy = bb.bbpointersy();
				for(int b = 0; b < bbpointersx.size(); b++) {
					if (obj.isstatic()) {
						bbx_static.insert(bbpointersx.get(b));
					}
				}
				for(int b = 0; b < bbpointersy.size(); b++) {
					if (obj.isstatic()) {
						bby_static.insert(bbpointersy.get(b));
					}
				}
			}
		}
	}

	public ArrayList<Object2D> isBroadCollide(Object2D object) {
		ArrayList<Object2D> collisionobjects = new ArrayList<Object2D>();
		HashMap<Object2D, Object2D> hm = new HashMap<Object2D, Object2D>(32);
		
		return collisionobjects;
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
