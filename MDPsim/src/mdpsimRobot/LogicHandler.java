package mdpsimRobot;
import java.util.ArrayList;
//0 for nothing, 1 for something;

import mdpsimEngine.Action2D;

public class LogicHandler {
	public int x_size;
	public int y_size;
	public int x_pos;
	public int y_pos;
	public Node heem;
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

	private String parseFormatToMap(String b) {
		int length = x_size*y_size - b.length();
		String s = "";
		for (int a = 0; a < length; a++) {
			s = s.concat("0");
		}
		return s.concat(b);
	}
	
	public void printhehe() {
		for(int i = 0; i < mapmemory.size(); i++) {
			mapmemory.get(i).toString();
		}
	}
	
	public void parseMDF(String s) {
		s = parseFormatToMap(s);
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
		printMapMemory();
		//Node n = computeFastestPath(1,1,1,13,RobotDirection.RIGHT);
		//heem = computeFastestPath(18,1,1,13,RobotDirection.RIGHT);
		//heem.printParents();
	}
	
	public Node computeFastestPath(int start_x, int start_y, int dest_x, int dest_y, RobotDirection rd) {
		Node start = new Node(start_x, start_y, null, null, rd, 0);
		Node end = new Node(dest_x, dest_y,null,null,null,Double.POSITIVE_INFINITY);
		//construct searched list;
		ArrayList<Node> done = new ArrayList<Node>();
		//construct list of neighbours to search
		ArrayList<Node>search = new ArrayList<Node>();
		search.add(start);
		while(search.size() > 0) {
			Node n = search.remove(0);
			done.add(n);
			if (n.isCell(end)) {
				return n;
			}
			ArrayList<Node> neighbours = n.neighbours(mapmemory);
			for (Node nb : neighbours) {
				if (!in(done, nb)) {
					search = insert(search, nb);
				}
			}
		}
		return null;
	}
	
	public void findAndReplace(ArrayList<ArrayList<Node>>map, Node n) {
		Node nod = map.get(n.x).get(n.y);
		if (nod.score > n.score) {
			ArrayList<Node> arlist = map.get(n.x);
			arlist.set(n.y, n);
			map.set(n.x, arlist);
		}	
	}
	
	public boolean in(ArrayList<Node> list, Node n) {
		for (int a = 0 ; a < list.size(); a++) {
			if (n.is(list.get(a))) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Node> insert(ArrayList<Node> ar, Node n){
		if (ar.size() == 0) {
			ar.add(n);
			return ar;
		} else {
			for (int a = 0; a < ar.size(); a++) {
				if (ar.get(a).score > n.score) {
					ar.add(a,n);
					return ar;
				}
			}
		}
		ar.add(n);
		return ar;
	}
	
	public void printMapMemory() {
		int x_size = mapmemory.size();
		int y_size = 0;
		try {
			y_size = mapmemory.get(0).size();
		} catch (Exception e) {}
		for (int a = 0; a < x_size; a++) {
			for (int b = 0; b < y_size; b++) {
				System.out.print(mapmemory.get(a).get(b));
			}
			System.out.println();
		}
		System.out.println();
	}
	
}
