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
	
	public ArrayList<BoundingBoxPointer> bbpointersx(){
		ArrayList<BoundingBoxPointer> bbpointers = new ArrayList<BoundingBoxPointer>();
		bbpointers.add(new BoundingBoxPointer(p1.x(),this));
		bbpointers.add(new BoundingBoxPointer(p2.x(),this));
		return bbpointers;
	}
	
	public ArrayList<BoundingBoxPointer> bbpointersy(){
		ArrayList<BoundingBoxPointer> bbpointers = new ArrayList<BoundingBoxPointer>();
		bbpointers.add(new BoundingBoxPointer(p1.y(),this));
		bbpointers.add(new BoundingBoxPointer(p2.y(),this));
		return bbpointers;
	}
	
	public Object2D object() {
		return this.object;
	}
}
