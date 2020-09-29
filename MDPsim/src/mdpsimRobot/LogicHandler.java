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
		computeFastestPath(1,1,10,10,RobotDirection.UP).printTrail();
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
	
	public Node computeFastestPath(int start_x, int start_y, int dest_x, int dest_y, RobotDirection direction) {
		Node start = new Node(start_x,start_y,null, direction,0);
		Node dest = new Node(dest_x, dest_y, null, null,Double.POSITIVE_INFINITY);
		NodeList searchspace = new NodeList();
		searchspace.merge(adjacent(start));
		while(searchspace.size() > 0) {
			searchspace.sort();
			Node n = searchspace.pop();
			if (n.isCell(dest)) {
				return n;
			}
			NodeList adj = adjacent(n);
			searchspace.merge(adj);
		}
		return null;
	}
	
	public NodeList adjacent(Node n) {
		NodeList adjacentcells= new NodeList();
		//TR
		try {
			adjacentcells.add(new Node(n.x, n.y, n,n.rd.TR(), n.score+1).validNode());
		} catch (Exception e) {
			adjacentcells.add(null);
		}
		//TL
		try {
			adjacentcells.add(new Node(n.x, n.y, n, n.rd.TL(), n.score+1).validNode());
		} catch (Exception e) {
			adjacentcells.add(null);
		}
		//F1
		try {
			adjacentcells.add(n.F(1).validNode());
		} catch (Exception e) {
			adjacentcells.add(null);
		}
		//F2
		try {
			adjacentcells.add(n.F(2).validNode());
		} catch (Exception e) {
			adjacentcells.add(null);
		}
		//F3
		try {
			adjacentcells.add(n.F(3).validNode());
		} catch (Exception e) {
			adjacentcells.add(null);
		}
		return adjacentcells;
	}
	
	class NodeList {
		ArrayList<Node> nodes;
		
		public NodeList() {
			this.nodes = new ArrayList<Node>();
			sort();
		}
		public NodeList(ArrayList<Node> nodes) {
			this.nodes = nodes;
			sort();
		}
		
		public void sort() {
			boolean sorted = false;
			while (!sorted) {
				sorted = true;
				for(int a = 1; a < nodes.size(); a++) {
					Node temp = nodes.get(a);
					if (nodes.get(a-1).score > temp.score) {
						nodes.set(a, nodes.get(a-1));
						nodes.set(a-1, temp);
						sorted = false;
					}
				}
			}
		}
		
		public void insert(Node n) {
			if (n == null) {
				return;
			}
			for (int a = 0; a < nodes.size(); a++) {
				if (nodes.get(a).score < n.score) {
					nodes.add(a, n);
					break;
				}
			}
			if (nodes.size() == 0) {
				nodes.add(n);
			}
		}
		
		public void add(Node n) {
			nodes.add(n);
		}
		
		@SuppressWarnings("null")
		public int in(Node n) {
			for (int a = 0; a < nodes.size(); a++) {
				if (nodes.get(a).is(n)) {
					return a;
				}
			}
			return -1;
		}
		
		public int size() {
			return nodes.size();
		}
		
		public Node pop() {
			Node n = nodes.remove(0);
			return n;
		}
		
		public Node pop(int a) {
			Node n = nodes.remove(a);
			return n;
		}
		
		public Node get(int a) {
			return nodes.get(a);
		}
		
		public void merge(NodeList n) {
			for (int a = 0; a < n.size(); a++) {
				Node node = n.get(a);
				int in = in(node);
				if (in != -1) {
					Node inside = get(in);
					if (inside.score > node.score) {
						pop(in);
						insert(node);
					}
				} else {
					insert(node);
				}	
			}
		}
		
		public void print() {
			for (int a = 0; a < nodes.size(); a++) {
				System.out.println("Node #"+a);
				nodes.get(a).print();
			}
			System.out.println();
		}
	}
	
	class Node{
		int x;
		int y;
		RobotDirection rd;
		Node camefrom;
		double score;
		
		public Node(int x, int y, Node camefrom, RobotDirection rd,double score) {
			this.x = x;
			this.y = y;
			this.rd = rd;
			this.camefrom = camefrom;
			this.score = score;
		}
		
		public boolean is(Node n) {
			try {
				RobotDirection r = n.rd;
			} catch (Exception e) {
				return false;
			}
			if (n.x == x && n.y == y && n.rd == rd) {
				return true;
			} else {
				return false;
			}
		}
		
		public boolean isCell(Node n) {
			if (n.x == x && n.y == y) {
				return true;
			} else {
				return false;
			}
		}
		
		public Node F(int val) {
			double sc;
			if (val <= 1) {
				sc = 1*val;
			} else if (val <= 2) {
				sc = 0.8*val;
			} else {
				sc = 0.7*val;
			}
			if (rd == RobotDirection.UP) {
				return new Node(x, y-val, this, rd, score+sc);
			} else if (rd == RobotDirection.DOWN) {
				return new Node(x, y+val, this, rd, score+sc);
			} else if (rd == RobotDirection.LEFT) {
				return new Node(x-val, y, this, rd, score+sc);
			} else if (rd == RobotDirection.RIGHT) {
				return new Node(x+val, y, this, rd, score+sc);
			} 
			return null;
		}
		
		public boolean valid() {
			if (x < 1 || x > 14 || y < 1 || y > 19) {
				return false;
			} else {
				return true;
			}
		}
		
		public Node validNode() {
			if (x < 1 || x > 14 || y < 1 || y > 19) {
				return null;
			} else {
				return this;
			}
		}
		
		public void print() {
			System.out.println("X:"+ x + "\tY:"+y+"\tDirection:"+rd+"\t\tScore:"+score);
		}
		public void printTrail() {
			Node n = this;
			boolean isnull = false;
			while (!isnull) {
				n.print();
				if(n.camefrom != null) {
					isnull = false;
					n = n.camefrom;
				} else {
					isnull = true;
				}
			}
		}
	}
}
