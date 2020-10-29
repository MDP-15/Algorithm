package mdpsimEngine;

public class BoundingBoxPointer {
	private double value;
	private BoundingBox2D bb;
	
	public BoundingBoxPointer(double value, BoundingBox2D bb) {
		this.value = value;
		this.bb = bb;
	}
	
	public BoundingBox2D bb() {
		return this.bb;
	}
	
	public double value() {
		return this.value;
	}
}
