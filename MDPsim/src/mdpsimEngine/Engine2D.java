package mdpsimEngine;
import java.util.ArrayList;
import java.util.HashMap;
/* 	MDP-specific 2D physics engine.
*	Broad-phase collision detection using sort and sweep AABB.
*/
public class Engine2D {
	private double time;		//current time since start
	private double timestep;	//discrete time step for simulation (please set as small as feasible)
	private ArrayList<Object2D> staticobjects;
	private ArrayList<Object2D> movingobjects;
	private BoundingBoxList bbx_static;
	private BoundingBoxList bby_static;
	
	//Private methods
	
	// adds objects to static or moving list depending on indicator in Object2D class.
	private void parseStaticity(ArrayList<Object2D> objects) {
		for (int a = 0; a < objects.size(); a++) {
			if (objects.get(a).isstatic() == true) {
				this.staticobjects.add(objects.get(a));
			} else {
				this.movingobjects.add(objects.get(a));
			}
		}
	}
	
	// generate bounding boxes automatically for an array of different objects.
	public void parseBoundingBoxes(ArrayList<Object2D> objects) {
		for (int a = 0; a < objects.size(); a++) {
			Object2D obj = objects.get(a);
			BoundingBox2D bb = new BoundingBox2D(obj);
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
	
	// returns an ArrayList of possible colliding static objects given an object.
	public ArrayList<Object2D> isBroadCollide(Object2D object) {
		BoundingBox2D objbb = new BoundingBox2D(object);
		ArrayList<BoundingBoxPointer> objxbbp = objbb.bbpointersx();
		ArrayList<BoundingBoxPointer> objybbp = objbb.bbpointersy();
		ArrayList<Object2D> collisionobjects = new ArrayList<Object2D>();
		HashMap<Object2D, Object2D> hm = bbx_static.betweenHash(objxbbp.get(0), objxbbp.get(1));
		ArrayList<Object2D> ycollide = bby_static.between(objybbp.get(0), objybbp.get(0));
		for (int a = 0 ; a < ycollide.size(); a++) {
			if (hm.containsKey(ycollide.get(a))) {
				collisionobjects.add(ycollide.get(a));
			}
		}
		return collisionobjects;
	}
	
	// assume moving object is only Circle2D else damn mafan, only consider Circle2D -> Line2D collision
	@SuppressWarnings("null")
	public void narrowCollide(Object2D moving, Object2D stationary) {
		if (moving.type() == Circle2D.class) {
			if (stationary.type() == Line2D.class) {
				narrowCollideCircleLine(moving, stationary);
			}
		} else {
			return;
		}
	}
	
	// handles collisions between circle and line
	public void narrowCollideCircleLine (Object2D moving, Object2D stationary) {
		Vector2D movingposition = moving.position();
		Vector2D movingvelocity = moving.velocity();
		Vector2D stationarystart = ((Line2D)stationary.object()).start();
		Vector2D stationaryend = ((Line2D)stationary.object()).end();
		if (moving.inside(stationarystart) || moving.inside(stationaryend)) {
			//some vector reflection to handle vertex collision
		} else if (true) {
			
		}
	}
	
	
	public Object2D closestCollide(Object2D object, ArrayList<Object2D> staticobjectarray) {
		//find object that is closest to being colliding (timewise) and handles collisions using that
	}
	
	//simulates next time step and updates positions of objects, and does collision resolution.
	public void next() {
		for (int a  = 0 ; a < movingobjects.size(); a++) {
			//calls broad collide, narrow collide and impulse resolution, and returns to engine after all positions updated correctly
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
