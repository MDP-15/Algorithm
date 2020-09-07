package mdpsimEngine;
import java.util.ArrayList;

public class BoundingBox2D {
	private Vector2D p1;
	private Vector2D p2;
	private Object2D object;
	
	public BoundingBox2D(Vector2D p1, Vector2D p2, Object2D object) {
		double x1 = p1.x();
		double x2 = p2.x();
		double y1 = p1.y();
		double y2 = p2.y();
		if (x1 > x2) {
			double temp = x1;
			x1 = x2;
			x2 = temp;
		}
		if (y1 > y2) {
			double temp = y1;
			y1 = y2;
			y2 = temp;
		}
		this.p1 = new Vector2D(x1, y1);
		this.p2 = new Vector2D(x2, y2);
		this.object = object;
	}
	
	public BoundingBox2D(Object2D obj) {
		if (obj.type() == Line2D.class) {
			Line2D line = ((Line2D) obj.object());
			this.p1 = line.start();
			this.p2 = line.end();
			this.object = obj;
			}
		else {
			Circle2D circle = ((Circle2D) obj.object());
			double radius = circle.radius();
			Vector2D npos = new Vector2D(-radius,-radius);
			Vector2D ppos = new Vector2D(radius, radius);
			this.p1 = obj.position().add(npos);
			this.p2 = obj.position().add(ppos);
			this.object = obj;
		}
	}
	public boolean collide(BoundingBox2D bb2) {
		double x1 = bb2.p1().x();
		double x2 = bb2.p2().x();
		double y1 = bb2.p1().y();
		double y2 = bb2.p2().y();
		if (this.inside(bb2.p1()) || this.inside(bb2.p2) || this.inside(new Vector2D(x1,y2)) || this.inside(new Vector2D(x2, y1))) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean inside(Vector2D vec) {
		if (this.p1.x() <= vec.x() && vec.x() <= this.p2.x() && this.p1.y() <= vec.y() && vec.y() <= this.p2.y()) {
			return true;
		} else {
			return false;
		}
	}
	public Vector2D p1() {
		return this.p1;
	}
	
	public Vector2D p2() {
		return this.p2;
	}
	
	public void p1 (Vector2D p1) {
		this.p1 = p1;
	}
	
	public void p2 (Vector2D p2) {
		this.p2 = p2;
	}
	
	public double min_x() {
		return p1.x();
	}
	
	public double min_y() {
		return p1.y();
	}
	
	public double max_x() {
		return p2.x();
	}
	
	public double max_y() {
		return p2.y();
	}
	public ArrayList<BoundingBoxPointer> bbpointersx(){
		ArrayList<BoundingBoxPointer> bbpointers = new ArrayList<BoundingBoxPointer>(0);
		BoundingBoxPointer bb1 = new BoundingBoxPointer(this.p1.x(),this);
		BoundingBoxPointer bb2 = new BoundingBoxPointer(this.p2.x(),this);
		bbpointers.add(bb1);
		bbpointers.add(bb2);
		return bbpointers;
	}
	
	public ArrayList<BoundingBoxPointer> bbpointersy(){
		ArrayList<BoundingBoxPointer> bbpointers = new ArrayList<BoundingBoxPointer>(0);
		BoundingBoxPointer bb1 = new BoundingBoxPointer(this.p1.y(),this);
		BoundingBoxPointer bb2 = new BoundingBoxPointer(this.p2.y(),this);
		bbpointers.add(bb1);
		bbpointers.add(bb2);
		return bbpointers;
	}
	
	public Object2D object() {
		return this.object;
	}
}
