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
		Vector2D line1start = moving.prevpos();
		Vector2D line1end = moving.position();
		Vector2D line2start = ((Line2D)stationary.object()).start();
		Vector2D line2end = ((Line2D)stationary.object()).end();
		Vector2D vec = lineIntersect(line1start, line1end, line2start, line2end);
		if (vec != null) {
			moving.position(vec);
			moving.velocity(moving.velocity().multiply(-1));
		}
	}
	
	public Vector2D lineIntersect(Vector2D line1_start, Vector2D line1_end, Vector2D line2_start, Vector2D line2_end) {
		double s1_x = line1_end.x() - line1_start.x();
		double s1_y = line1_end.y() - line1_start.y();
		double s2_x = line2_end.x() - line2_start.x();
		double s2_y = line2_end.y() - line2_start.y();
		
		double s = (((-s1_y * (line1_start.x() - line2_start.x())) + (s1_x * (line1_start.y() - line2_start.y()))))/((-s2_x * s1_y) + (s1_x * s2_y));
		double t = ((( s2_x * (line1_start.y() - line2_start.y())) - (s2_y * (line1_start.x() - line2_start.x()))))/((-s2_x * s1_y) + (s1_x * s2_y));
		
		double i_x = -1;
		double i_y = -1;
		if ( s>= 0 && s <= 1 && t >= 0 && t <= 1) {
			i_x = line1_start.x() + (t * s1_x);
			i_y = line1_start.y() + (t * s1_y);
			return new Vector2D (i_x, i_y);
		}
		
		return (Vector2D) null;
	}
	
	public double lineIntersectDistance(Vector2D line1_start, Vector2D line1_end, Vector2D line2_start, Vector2D line2_end) {
		double s1_x = line1_end.x() - line1_start.x();
		double s1_y = line1_end.y() - line1_start.y();
		double s2_x = line2_end.x() - line2_start.x();
		double s2_y = line2_end.y() - line2_start.y();
		
		double s = (((-s1_y * (line1_start.x() - line2_start.x())) + (s1_x * (line1_start.y() - line2_start.y()))))/((-s2_x * s1_y) + (s1_x * s2_y));
		double t = ((( s2_x * (line1_start.y() - line2_start.y())) - (s2_y * (line1_start.x() - line2_start.x()))))/((-s2_x * s1_y) + (s1_x * s2_y));
		
		double i_x = -1;
		double i_y = -1;
		if ( s>= 0 && s <= 1 && t >= 0 && t <= 1) {
			return t; 
		}
		
		return -1;
	}
	
	
	public Object2D closestCollide(Object2D object, ArrayList<Object2D> staticobjectarray) {
		double distance = -1;
		double newdistance = -1;
		Object2D objectreturn = (Object2D) null;
		for (int a  = 0; a < staticobjectarray.size(); a++) {
			Line2D line = (Line2D) (staticobjectarray.get(a).object());
			newdistance = lineIntersectDistance(object.prevpos(), object.position(), line.start(), line.end);
			if (newdistance > distance) {
				distance = newdistance;
				objectreturn = staticobjectarray.get(a);
			}
		}
		if (distance < 0) {
			return null;
		} else {
			return objectreturn;
		}
	}
	
	//simulates next time step and updates positions of objects, and does collision resolution.
	public void next() {
		for (int a  = 0 ; a < movingobjects.size(); a++) {
			Object2D obj = movingobjects.get(a);
			obj.prevpos(obj.position());
			obj.position(obj.prevpos().add(obj.velocity().multiply(this.timestep)));
		}
		for(int b = 0 ; b < movingobjects.size(); b++) {
			ArrayList<Object2D> broadcollide = isBroadCollide(movingobjects.get(b));
			for(int a  = 0; a < broadcollide.size(); a++) {
				narrowCollide(movingobjects.get(b),broadcollide.get(a));
			}
		}
		this.time += this.timestep;
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
