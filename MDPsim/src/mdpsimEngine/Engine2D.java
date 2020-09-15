package mdpsimEngine;
import java.util.ArrayList;
import java.util.HashMap;
/* 	MDP-specific 2D physics engine.
*	Broad-phase collision detection using sort and sweep AABB.
*/
public class Engine2D{
	private double time;		//current time since start
	private double timestep;	//discrete time step for simulation (please set as small as feasible)
	private long timestepselapsed;
	private ArrayList<Object2D> staticobjects;
	private ArrayList<Object2D> movingobjects;
	private BoundingBoxList bbx_static;
	private BoundingBoxList bby_static;
	
	//Private methods
	
	// adds objects to static or moving list depending on indicator in Object2D class.
	private void parseStaticity(ArrayList<Object2D> objects) {
		boolean verbose = false;
		int movingobjectsno = 0;
		int staticobjectsno = 0;
		for (int a = 0; a < objects.size(); a++) {
			if (objects.get(a).isstatic() == true) {
				this.staticobjects.add(objects.get(a));
				staticobjectsno += 1;
			} else {
				this.movingobjects.add(objects.get(a));
				movingobjectsno += 1;
			}
		}
		if (verbose) {
			System.out.println(staticobjectsno +" static (non-moving) object(s) recognized.");
			System.out.println(movingobjectsno +" moving object(s) recognized.");
		}
		return;
	}
	
	// generate bounding boxes automatically for an array of different objects.
	public void parseBoundingBoxes(ArrayList<Object2D> objects) {
		for (int a = 0; a < objects.size(); a++) {
			Object2D obj = objects.get(a);
			if (obj.isstatic()) {
				BoundingBox2D bb = new BoundingBox2D(obj);
				ArrayList<BoundingBoxPointer> bbpointersx = bb.bbpointersx();
				ArrayList<BoundingBoxPointer> bbpointersy = bb.bbpointersy();
				for(int b = 0; b < bbpointersx.size(); b++) {
					this.bbx_static.insert(bbpointersx.get(b));
					}
				for(int b = 0; b < bbpointersy.size(); b++) {
					this.bby_static.insert(bbpointersy.get(b));
					}
			}
		}
		return;
	}
	
	// returns an ArrayList of possible colliding static objects given an object.
	public ArrayList<Object2D> isBroadCollide(Object2D object) {
		boolean verbose = false;
		BoundingBox2D objbb = new BoundingBox2D(object);
		ArrayList<BoundingBoxPointer> objxbbp = objbb.bbpointersx();
		ArrayList<BoundingBoxPointer> objybbp = objbb.bbpointersy();
		ArrayList<Object2D> collisionobjects = new ArrayList<Object2D>(0);
		ArrayList<Object2D> xcollide = this.bbx_static.between(objxbbp.get(0), objxbbp.get(1));
		ArrayList<Object2D> ycollide = this.bby_static.between(objybbp.get(0), objybbp.get(1));
		for (int a = 0 ; a < xcollide.size(); a++) {
			if (!collisionobjects.contains(xcollide.get(a))) {
				collisionobjects.add(xcollide.get(a));
			}
		}
		for (int a = 0 ; a < ycollide.size(); a++) {
			if (!collisionobjects.contains(ycollide.get(a))) {
				collisionobjects.add(ycollide.get(a));
			}
		}
		if (verbose) {
			System.out.println(collisionobjects.size() + " objects detected in broad-phase collision detection.");
		}
		return collisionobjects;
	}
	
	// assume moving object is only Circle2D else damn, only consider Circle2D -> Line2D collision
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
	public void narrowCollideCircleLine (Object2D circle, Object2D line) {
		Vector2D circlepos = circle.position();
		double radius = ((Circle2D)circle.object()).radius();
		Vector2D linestart = ((Line2D)line.object()).start();
		Vector2D lineend = ((Line2D)line.object()).end();
		if (pointLineClosestOrigin(linestart.subtract(circlepos),lineend.subtract(circlepos)).length() <= radius) {
			circle.position(circle.prevpos());
			circle.velocity(new Vector2D(0,0));
			circle.acceleration(new Vector2D(0,0));
		}
		return;
	}
	
	//uses (x1,y1) + s(x2-x1,y2-y1) = (0,k) to solve for point closest to line
	public Vector2D pointLineClosestOrigin (Vector2D linestart, Vector2D lineend) {
		Vector2D svec = lineend.subtract(linestart);
		double s = -((double)linestart.x()/(double)svec.x());
		double t = -((double)linestart.y()/(double)svec.y());
		double m = (double)svec.y()/(double)svec.x();
		if (Math.abs(s) <=1 || Math.abs(t) <= 1) {
			if (m == Double.POSITIVE_INFINITY || m == Double.NEGATIVE_INFINITY){
				return new Vector2D(linestart.x(),0);
			} else if (m == 0 ) {
				return new Vector2D(0, linestart.y());
			} else {
				double b = linestart.y() + s*svec.y();
				return new Vector2D((double)-(m*b)/(Math.pow(m, 2)+1), (double)b/(Math.pow(m, 2)+1));
			}
		} else {
			return new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
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
	
	public Vector2D rayTraceVec(Vector2D origin, Vector2D direction) {
		if (direction.x() == 0 && direction.y() == 0) {
			return null;
		} else {
			Vector2D svec = direction.multiply((double)200/direction.length(new Vector2D(0,0)));
			Line2D line = new Line2D(origin, origin.add(svec));
			Object2D lineobj = new Object2D(line, line.start(), new Vector2D(0,0), new Vector2D(0,0), true);
			ArrayList<Object2D> broadcollide = isBroadCollide(lineobj);
			double s = Double.POSITIVE_INFINITY;
			Vector2D vec = null;
			for (Object2D collide : broadcollide) {
				double test_s = lineIntersectDistance(line.start(), line.end(),((Line2D)collide.object()).start(), (((Line2D) collide.object()).end()));
				if (test_s > 0 && test_s < 1 && test_s < s) {
					s = test_s;
					vec = lineIntersect(line.start(), line.end(),((Line2D)collide.object()).start(), (((Line2D) collide.object()).end()));

				}
			}
			return vec;
		}
	}
		
	public double lineIntersectDistance(Vector2D line1_start, Vector2D line1_end, Vector2D line2_start, Vector2D line2_end) {
		double s1_x = line1_end.x() - line1_start.x();
		double s1_y = line1_end.y() - line1_start.y();
		double s2_x = line2_end.x() - line2_start.x();
		double s2_y = line2_end.y() - line2_start.y();
		
		double s = (((-s1_y * (line1_start.x() - line2_start.x())) + (s1_x * (line1_start.y() - line2_start.y()))))/((-s2_x * s1_y) + (s1_x * s2_y));
		double t = ((( s2_x * (line1_start.y() - line2_start.y())) - (s2_y * (line1_start.x() - line2_start.x()))))/((-s2_x * s1_y) + (s1_x * s2_y));
		
		if ( s>= 0 && s <= 1 && t >= 0 && t <= 1) {
			return t;
		}
		
		return -1;
	}
	
	public Object2D closestCollide(Object2D object, ArrayList<Object2D> staticobjectarray) {
		boolean verbose = false;
		double distance = Double.POSITIVE_INFINITY;
		double newdistance = Double.POSITIVE_INFINITY;
		Object2D objectreturn = (Object2D) null;
		for (int a  = 0; a < staticobjectarray.size(); a++) {
			Vector2D pos = object.position();
			Vector2D linestart = ((Line2D)staticobjectarray.get(a).object()).start().subtract(pos);
			Vector2D lineend = ((Line2D)staticobjectarray.get(a).object()).end().subtract(pos);
			newdistance = pointLineClosestOrigin(linestart,lineend).length(new Vector2D(0,0));
			if (newdistance < distance) {
				distance = newdistance;
				objectreturn = staticobjectarray.get(a);
			}
		}
		if (distance < 0) {
			return null;
		} else {
			if (verbose && distance < Double.POSITIVE_INFINITY) {
				System.out.println("Closest object is at " + distance +" cm. Object is "+objectreturn.type());
				System.out.println("Number of objects considered: "+ staticobjectarray.size());
				if (objectreturn.type() == Line2D.class) {
					System.out.println("Start X: "+((Line2D)objectreturn.object()).start().x() + " Start Y: " +((Line2D)objectreturn.object()).start.y());
					System.out.println("End X: "+((Line2D)objectreturn.object()).end().x() + " End Y: " +((Line2D)objectreturn.object()).end.y());

				}
			}
			return objectreturn;
		}
	}
	
	//simulates next time step and updates positions of objects, and does collision resolution.
	// velocity formula = v = u + at;
	// displacement formula = s = ut + 0.5*at^2;
	public void next(Action2D action) {
		for (int a  = 0 ; a < movingobjects.size(); a++) {
			Object2D obj = movingobjects.get(a);
			obj.update(timestep, action);
		}
		for(int b = 0 ; b < movingobjects.size(); b++) {
			ArrayList<Object2D> broadcollide = isBroadCollide(movingobjects.get(b));
			Object2D collided = closestCollide(movingobjects.get(b),broadcollide);
			if (collided != null) {
				narrowCollide(movingobjects.get(b),collided);
			}
		}
		this.time += this.timestep;
		this.timestepselapsed += 1;
	}
	
	//Public methods
	public Engine2D(ArrayList<Object2D> objects, double timestep) {
		boolean verbose = false;
		this.timestep = timestep;
		this.staticobjects = new ArrayList<Object2D>();
		this.movingobjects = new ArrayList<Object2D>();
		this.bbx_static = new BoundingBoxList();
		this.bby_static = new BoundingBoxList();
		this.parseStaticity(objects);
		this.parseBoundingBoxes(objects);
		if (verbose) {
			bbx_static.printall();
			bby_static.printall();
		}
	}
	
	public ArrayList<Object2D >staticObjects(){
		return staticobjects;
	}
	
	public ArrayList<Object2D >movingObjects(){
		return movingobjects;
	}
	
	public void printMoving() {
		System.out.println("Time :" + time);
		for (int a = 0; a < movingobjects.size(); a++) {
			System.out.println("Moving Object :" + movingobjects.get(a).type() + " X :" + movingobjects.get(a).position().x() + " Y :" + movingobjects.get(a).position().y());
		}
	}
	public double time() {
		return this.time;
	}
	
	public long timestepselapsed() {
		return this.timestepselapsed;
	}
}
