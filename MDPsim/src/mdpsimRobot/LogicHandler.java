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
		this.mapmemory = new ArrayList<ArrayList<Integer>>();
		for(int a = 0; a < x_size; a++) {
			mapmemory.add(new ArrayList<Integer>(y_size));
		}
	}
	public void MapMemory(ArrayList<ArrayList<Integer>> mapmemory) {
		this.mapmemory = mapmemory;
	}
	
	public ArrayList<ArrayList<Integer>>MapMemory() {
		return this.mapmemory;
	}

	private String parseFormatToMap(String b) {
		int length = x_size*y_size - b.length();
		String s = "";
		for (int a = 0; a < length; a++) {
			s = s.concat("0");
		}
		return s.concat(b);
	}
	
	public void parseMDF(String s) {
		s = parseFormatToMap(s);
		System.out.println(s);
		ArrayList<ArrayList<Integer>> mem = new ArrayList<ArrayList<Integer>>();
		for (int a = 0 ; a < y_size; a++) {
			ArrayList<Integer> ar = new ArrayList<Integer>();
			for (int b = 0; b < x_size; b++) {
				if (s.charAt((a*x_size)+b) == '1') {
					ar.add(1);
				} else {
					ar.add(0);
				}
			}
			mem.add(ar);
		}
		this.mapmemory = mem;
		printmemory();
	}
	
	public void printmemory() {
		for (int a = 0 ; a < y_size; a++) {
			System.out.println("Row "+ a);
			for (int b = 0; b < x_size; b++) {
				System.out.print(mapmemory.get(a).get(b));
			}
			System.out.println();
		}
	}
	public void computeFastestPath(int start_x, int start_y, int dest_x, int dest_y) {
		
	}
	
	public void exploreNextStep() {
		
	}
}
