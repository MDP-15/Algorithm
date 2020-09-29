package mdpsimRobot;
import java.util.ArrayList;

public class LogicHandler {
	public int x_size;
	public int y_size;
	public int x_pos;
	public int y_pos;
	public ArrayList<ArrayList<Integer>> mapmemory;
	
	public LogicHandler(int x_size, int y_size, int x_pos, int y_pos) {
		this.x_size = x_size;
		this.y_size = y_size;
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		this.mapmemory = new ArrayList<ArrayList<Integer>>(y_size);
		for(int a = 0; a < y_size; a++) {
			mapmemory.add(new ArrayList<Integer>(x_size));
		}
	}
	public void MapMemory(ArrayList<ArrayList<Integer>> mapmemory) {
		this.mapmemory = mapmemory;
	}
	
	public ArrayList<ArrayList<Integer>>MapMemory() {
		return this.mapmemory;
	}

	public void computeFastestPath(int start_x, int start_y, int dest_x, int dest_y) {
		
	}
	
	public void exploreNextStep() {
		
	}
}
