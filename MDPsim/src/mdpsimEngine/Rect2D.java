package mdpsimEngine;
import java.util.ArrayList;

public class Rect2D {
	private ArrayList<Vector2D> bounds;
	
	public Rect2D(ArrayList<Vector2D> bounds) {
		this.bounds = bounds;
	}
	
	public void bounds(ArrayList<Vector2D> bounds) {
		this.bounds = bounds;
	}
	
	public ArrayList<Vector2D> bounds (){
		return this.bounds;
	}
}
