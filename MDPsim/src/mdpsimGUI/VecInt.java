package mdpsimGUI;
import java.lang.Math;
import mdpsimEngine.Vector2D;

public class VecInt {
	private int x;
	private int y;
	
	public VecInt(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public VecInt(Vector2D vec, boolean flip) {
		this.x = (int) Math.round(vec.x());
		this.y = (int) Math.round(vec.y());
	}
	public int x() {
		return this.x;
	}
	
	public void x(int x) {
		this.x = x;
	}
	
	public int y() {
		return this.y;
	}
	
	public void y(int y) {
		this.y = y;
	}
	
	public void multiply(double mult) {
		this.x = (int)Math.round(x*mult);
		this.y = (int)Math.round(y*mult);
	}
}
