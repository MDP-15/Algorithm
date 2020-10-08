package mdpsimRobot;
import java.util.ArrayList;
//0 for nothing, 1 for something;

import mdpsim.MDPSIM;
import mdpsimEngine.Action2D;

public class LogicHandler {
	public int x_size;
	public int y_size;
	public int x_pos;
	public int y_pos;
	public RobotDirection robotdir;
	public ArrayList<ArrayList<Integer>> mapmemory;
	public ArrayList<RobotAction> queue;
	public RobotAction prevaction;
	public int recal;
	public double timelimit;
	
	public LogicHandler(int x_size, int y_size, int x_pos, int y_pos) {
		this.x_size = x_size;
		this.y_size = y_size;
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		this.recal = 0;
		this.timelimit = 0;
		this.robotdir = null;
		this.prevaction = null;
		this.queue = new ArrayList<RobotAction>();
		this.mapmemory = new ArrayList<ArrayList<Integer>>();
		for(int a = 0; a < x_size; a++) {
			mapmemory.add(new ArrayList<Integer>(y_size));
		}
	}
	
	public void printPos() {
		System.out.println("X: "+x_pos+" Y:"+y_pos+ " RD:"+robotdir);
	}
	public void updatePosition(int x, int y) {
		this.x_pos = x;
		this.y_pos = y;
		return;
	}
	
	public void initPos(int x, int y) {
		setMapMemory(x+1,y+1,0);
		setMapMemory(x,y+1,0);
		setMapMemory(x-1,y+1,0);
		setMapMemory(x+1,y,0);
		setMapMemory(x,y,0);
		setMapMemory(x-1,y,0);
		setMapMemory(x+1,y-1,0);
		setMapMemory(x,y-1,0);
		setMapMemory(x-1,y-1,0);

	}
	
	public boolean isCalibrate(Node n) {
		int x = n.x;
		int y = n.y;
		if (n.rd == RobotDirection.UP) {
			if ((	mapmemory.get(x-1).get(y+2) == 1
				&&	mapmemory.get(x).get(y+2) == 1
				&& 	mapmemory.get(x+1).get(y+2) == 1) || 
				(	isException(x-1,y+2)
				&&	isException(x,y+2)
				&&	isException(x+1, y+2))) {
				return true;
			}
		} else if (n.rd == RobotDirection.DOWN) {
			if ((	mapmemory.get(x-1).get(y-2) == 1
					&&	mapmemory.get(x).get(y-2) == 1
					&& 	mapmemory.get(x+1).get(y-2) == 1) || 
					(	isException(x-1,y-2)
					&&	isException(x,y-2)
					&&	isException(x+1, y-2))) {
					return true;
				}
		} else if (n.rd == RobotDirection.LEFT) {
			if ((	mapmemory.get(x-2).get(y-1) == 1
					&&	mapmemory.get(x-2).get(y) == 1
					&& 	mapmemory.get(x-2).get(y+1) == 1) || 
					(	isException(x-2,y-1)
					&&	isException(x-2,y)
					&&	isException(x-2, y+1))) {
					return true;
				}
		} else if (n.rd == RobotDirection.RIGHT) {
			if ((	mapmemory.get(x+2).get(y-1) == 1
					&&	mapmemory.get(x+2).get(y) == 1
					&& 	mapmemory.get(x+2).get(y+1) == 1) || 
					(	isException(x+2,y-1)
					&&	isException(x+2,y)
					&&	isException(x+2, y+1))) {
					return true;
				}
		}
	}
	
	public boolean isException(int x, int y) {
		try {
			int a = mapmemory.get(x).get(y);
			return false;
		} catch (Exception e) {
			return true;
		}
	}
	public void updatePos(){
		if (robotdir == RobotDirection.UP) {
			if (prevaction == RobotAction.F1) {
				this.x_pos = x_pos - 1;
			} else if (prevaction == RobotAction.F2) {
				this.x_pos = x_pos - 2;
			} else if (prevaction == RobotAction.F3) {
				this.x_pos = x_pos - 3;
			} else if (prevaction == RobotAction.TR) {
				this.robotdir = RobotDirection.RIGHT;
			} else if (prevaction == RobotAction.TL) {
				this.robotdir = RobotDirection.LEFT;
			}
		} else if (robotdir == RobotDirection.DOWN) {
			if (prevaction == RobotAction.F1) {
				this.x_pos = x_pos + 1;
			} else if (prevaction == RobotAction.F2) {
				this.x_pos = x_pos + 2;
			} else if (prevaction == RobotAction.F3) {
				this.x_pos = x_pos + 3;
			} else if (prevaction == RobotAction.TR) {
				this.robotdir = RobotDirection.LEFT;
			} else if (prevaction == RobotAction.TL) {
				this.robotdir = RobotDirection.RIGHT;
			}
		} else if (robotdir == RobotDirection.LEFT) {
			if (prevaction == RobotAction.F1) {
				this.y_pos = y_pos - 1;
			} else if (prevaction == RobotAction.F2) {
				this.y_pos = y_pos - 2;
			} else if (prevaction == RobotAction.F3) {
				this.y_pos = y_pos - 3;
			} else if (prevaction == RobotAction.TR) {
				this.robotdir = RobotDirection.UP;
			} else if (prevaction == RobotAction.TL) {
				this.robotdir = RobotDirection.DOWN;
			}
		} else if (robotdir == RobotDirection.RIGHT) {
			if (prevaction == RobotAction.F1) {
				this.y_pos = y_pos + 1;
			} else if (prevaction == RobotAction.F2) {
				this.y_pos = y_pos + 2;
			} else if (prevaction == RobotAction.F3) {
				this.y_pos = y_pos + 3;
			} else if (prevaction == RobotAction.TR) {
				this.robotdir = RobotDirection.DOWN;
			} else if (prevaction == RobotAction.TL) {
				this.robotdir = RobotDirection.UP;
			}
		}
		prevaction = null;
	}
	
	public Node returnToBase(){
		Node n = computeFastestPath(x_pos,y_pos,18,1,robotdir);
		if (n.isCell(new Node (x_pos,y_pos,null,null,null,0.0))) {
			MDPSIM.mode = 0;
		}
		return n;
	}
	
	
	public double coverage() {
		int x_size = mapmemory.size();
		int y_size = 0;
		int covered = 0;
		try {
			y_size = mapmemory.get(0).size();
		} catch (Exception e) {
			return 0.0;
		}
		int maxsize = x_size * y_size;
		for (int a = 0; a < x_size; a++) {
			for (int b = 0; b < y_size; b++) {
				if (mapmemory.get(a).get(b) != 2) {
					covered += 1;
				}
			}
		}
		return (double)covered/maxsize;
	}
	
	public RobotAction getNextAction(double time) {
		//EXPLORATION
		//FIND NEXT NODE
		if (queue.size() == 0) {
			Node n = null;
			if (MDPSIM.mode == 1) {
				if (coverage() >= MDPSIM.coverage || time >= timelimit) {
					MDPSIM.mode = 2;
					return null;
				}
				n = findNext();
			}
			if (MDPSIM.mode == 2 && !MDPSIM.real) {
				n = returnToBase();
				//printMapMemory();
			} else if (MDPSIM.mode == 2 && MDPSIM.real) {
				n = returnToBase();
			}
			if (n != null) {
				queue = trailAction(n);
			}
		} else {
			RobotAction ra = queue.remove(queue.size()-1);
			if (ra != null) {
				prevaction = ra;
			}
			return ra;
		}
		if (MDPSIM.real && queue.size() > 0) {
			RobotAction ra = queue.remove(queue.size()-1);
			if (ra != null) {
				prevaction = ra;
			}
			return ra;
		}
		return null;
	}

	/*
	public boolean canCalibrate() {
		if (robotdir = RobotDirection.UP) {
			
		}
	}
	*/
	public String updateMDFStringFromRobotMemory() {
		ArrayList<ArrayList<Integer>> mem = mapmemory;
		String mdf = "";
		int x_size = mem.size();
		int y_size = 0;
		try {
			y_size = mem.get(0).size();
		} catch (Exception e){
			return null;
		}
		for (int a = 0; a < x_size; a++) {
			for(int b = 0; b < y_size; b++) {
				if (mem.get(a).get(b) == 0) {
					mdf.concat("0");
				} else if (mem.get(a).get(b) == 1) {
					mdf.concat("1");
				}  else if (mem.get(a).get(b) == 2) {
					mdf.concat("2");
				}
			}
		}
		String fin = "";
		if (mdf.length() > 0) {
			for (int a = mdf.length()-1; a == 0; a--) {
				fin.concat(Character.toString(mdf.charAt(a)));
			}

		}
		return fin;
	}
	
	public ArrayList<RobotAction> trailAction(Node n){
		ArrayList<RobotAction> ar = new ArrayList<RobotAction>();
		while (n.ra != null) {
			ar.add(n.ra);
			if (n.prev == null) {
				break;
			}
			n = n.prev;
		}
		return ar;
	}
	public void setRobotDirection(RobotDirection rd) {
		this.robotdir = rd;
	}

	private String parseFormatToMap(String b) {
		int length = x_size*y_size - b.length();
		String s = "";
		for (int a = 0; a < length; a++) {
			s = s.concat("0");
		}
		return s.concat(b);
	}
	
	public void setUnexploredMap(int start_x, int start_y, int x_siz, int y_siz, RobotDirection rd) {
		robotdir = rd;
		x_pos = start_x;
		y_pos = start_y;
		ArrayList<ArrayList<Integer>> mem = new ArrayList<ArrayList<Integer>>();
		//set all to unexplored
		for (int a = 0; a < x_siz; a++) {
			ArrayList<Integer>yarray = new ArrayList<Integer>();
			for (int b = 0; b < y_siz; b++) {
				yarray.add(2);
			}
			mem.add(yarray);
		}
		this.mapmemory = mem;
		initPos(start_x,start_y);
		
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
//		Node n = computeFastestPath(18,1,1,13,RobotDirection.DOWN);
//		n.printParents();
	}
	
	public void parseMDF() {
		String s = updateMDFStringFromRobotMemory();
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
//		Node n = computeFastestPath(18,1,1,13,RobotDirection.DOWN);
//		n.printParents();
	}
	
	//EXPLORATION
	//CONSTRUCT BOUNDARY, THEN FIND TARGET NODE THAT WILL GIVE HIGHEST EXPLORATION REWARD PER SCORE;
	public Node findNext() {
		ArrayList<Node> pathable = dijkstraSearch(x_pos, y_pos, robotdir);
		ArrayList<ExplorationNode> first = new ArrayList<ExplorationNode>(0);
		for (int a = 0; a < pathable.size(); a++) {
			if (pathable.get(a).score != 0) {
				double info = informationGained(pathable.get(a).x, pathable.get(a).y,pathable.get(a).rd);
				if (info == 0) {
					continue;
				}
				ExplorationNode ex = new ExplorationNode(pathable.get(a), Math.pow((double)info/pathable.get(a).score,2));
				first.add(ex);
			}
		}
		first = exSort(first);
		if (first.size() == 0) {
			MDPSIM.mode = 2;
			return null;
		}
		ExplorationNode e = first.get(0);
		Node n = new Node(e.x,e.y,e.prev,e.ra,e.rd,e.score);
		return n;
	}
	
	//INSERT INTO SORTED LIST
	public ArrayList<ExplorationNode> exInsert(ArrayList<ExplorationNode> ar, ExplorationNode en){
		if (ar.size() == 0) {
			ar.add(en);
			return ar;
		}
		int left = 0;
		int right = ar.size() - 1;
		int middle = 0;
		while (left <= right) {
			middle = Math.floorDiv((left + right),2);
			int cmp = Double.compare(en.informationgain, ar.get(middle).informationgain);
			if (cmp > 0) {
				left = middle + 1;
			} else if (cmp < 0) {
				right = middle - 1;
			} else {
				ar.add(middle, en);
				return ar;
			}
		}
		ar.add(en);
		return ar;
	}
	
	public ArrayList<ExplorationNode> exSort(ArrayList<ExplorationNode> ar){
		boolean sorted = false;
		while (!sorted) {
			sorted = true;
			for (int a = 1; a < ar.size(); a++) {
				if (ar.get(a-1).informationgain < ar.get(a).informationgain) {
					ExplorationNode temp = ar.get(a);
					ar.set(a, ar.get(a-1));
					ar.set(a-1, temp);
					sorted = false;
				}
			}
		}
		return ar;
	}
	
	//FIND ALL VALID NODES TO ENTER;
	public ArrayList<Node> validNodes() {
		ArrayList<Node> validnodes = new ArrayList<Node>();
		for (int a = 0; a < x_size; a++) {
			for (int b = 0; b < y_size; b++) {
				Node n = new Node(b,a,null,null,null,0.0);
				if (n.isValid(mapmemory)) {
					Node up = new Node(b,a,null,null,RobotDirection.UP,0.0);
					Node down = new Node(b,a,null,null,RobotDirection.DOWN,0.0);
					Node left = new Node(b,a,null,null,RobotDirection.LEFT,0.0);
					Node right = new Node(b,a,null,null,RobotDirection.RIGHT,0.0);
					validnodes.add(up);
					validnodes.add(down);
					validnodes.add(left);
					validnodes.add(right);
				}
			}
		}
		return validnodes;
	}
	//FIND AMOUNT OF INFORMATION GAINED IF SCAN AT GIVEN SQUARE;
	//SQUARE MUST BE VALID ROBOT POSITION;
	public int informationGained(int x, int y, RobotDirection rd) {
		int information = 0;
		int frontmidbox = 1;
		int frontrightbox = 1;
		int frontleftbox = 1;
		int leftlongbox = 1;
		int rightfrontbox = 1;
		int rightbackbox = 1;
		//CALCULATE POTENTIALLY EXPLORED BLOCKS FROM VALUE;
		if (robotdir == RobotDirection.RIGHT) {
			for (int a = 1; a <= frontmidbox; a++) {
				if (isValid(x, y+1+a)) {
					int b = mapmemory.get(x).get(y+1+a);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				}  else {
					break;
				}
			}
			for (int a = 1; a <= frontrightbox; a++) {
				if (isValid(x+1, y+1+a)) {
					int b = mapmemory.get(x+1).get(y+1+a);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}	
				} else {
					break;
				}
			}
			for (int a = 1; a <= frontleftbox; a++) {
				if (isValid(x-1, y+1+a) ){
					int b = mapmemory.get(x-1).get(y+1+a);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightfrontbox; a++) {
				if (isValid(x+1+a, y+1) ){
					int b = mapmemory.get(x+1+a).get(y+1);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightbackbox; a++) {
				if (isValid(x+1+a, y-1) ){
					int b = mapmemory.get(x+1+a).get(y-1);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= leftlongbox; a++) {
				if (isValid(x-1-a, y+1) ){
					int b = mapmemory.get(x-1-a).get(y+1);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
		} else if (robotdir == RobotDirection.LEFT) {
			for (int a = 1; a <= frontmidbox; a++) {
				if (isValid(x, y-1-a) ){
					int b = mapmemory.get(x).get(y-1-a);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= frontrightbox; a++) {
				if (isValid(x-1, y-1-a) ){
					int b = mapmemory.get(x-1).get(y-1-a);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= frontleftbox; a++) {
				if (isValid(x+1, y-1-a) ){
					int b = mapmemory.get(x+1).get(y-1-a);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightfrontbox; a++) {
				if (isValid(x-1-a, y-1) ){
					int b = mapmemory.get(x-1-a).get(y-1);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightbackbox; a++) {
				if (isValid(x-1-a, y+1) ){
					int b = mapmemory.get(x-1-a).get(y+1);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}	
			}
			for (int a = 1; a <= leftlongbox; a++) {
				if (isValid(x+1+a, y-1) ){
					int b = mapmemory.get(x+1+a).get(y-1);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
		} else if (robotdir == RobotDirection.UP) {
			for (int a = 0; a <= frontmidbox; a++) {
				if (isValid(x-1-a, y) ){
					int b = mapmemory.get(x-1-a).get(y);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= frontrightbox; a++) {
				if (isValid(x-1-a, y+1) ){
					int b = mapmemory.get(x-1-a).get(y+1);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= frontleftbox; a++) {
				if (isValid(x-1-a, y-1) ){
					int b = mapmemory.get(x-1-a).get(y-1);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightfrontbox; a++) {
				if (isValid(x-1, y+1+a) ){
					int b = mapmemory.get(x-1).get(y+1+a);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightbackbox; a++) {
				if (isValid(x+1, y+1+a) ){
					int b = mapmemory.get(x+1).get(y+1+a);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= leftlongbox; a++) {
				if (isValid(x-1, y-1-a) ){
					int b = mapmemory.get(x-1).get(y-1-a);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
		} else if (robotdir == RobotDirection.DOWN) {
			for (int a = 1; a <= frontmidbox; a++) {
				if (isValid(x+1+a, y) ){
					int b = mapmemory.get(x+1+a).get(y);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= frontrightbox; a++) {
				if (isValid(x+1+a, y-1) ){
					int b = mapmemory.get(x+1+a).get(y-1);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= frontleftbox; a++) {
				if (isValid(x+1+a, y+1) ){
					int b = mapmemory.get(x+1+a).get(y+1);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightfrontbox; a++) {
				if (isValid(x+1, y-1-a) ){
					int b = mapmemory.get(x+1).get(y-1-a);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightbackbox; a++) {
				if (isValid(x-1, y-1-a) ){
					int b = mapmemory.get(x-1).get(y-1-a);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			for (int a = 1; a <= leftlongbox; a++) {
				if (isValid(x+1, y+1+a) ){
					int b = mapmemory.get(x+1).get(y+1+a);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
						continue;
					} else {
						break;
					}
				} else {
					break;
				}
			}
		}
		return information;
	}
	
	//CONVERT SENSOR VALUE INTO USABLE DATA FOR SCANMAP;
	public void scan(ArrayList<Double> dlist) {
		boolean verbose = false;
		double rf = 1000;
		double rb = 1000;
		double fr = 1000;
		double fm = 1000;
		double fl = 1000;
		double ll = 1000;
		if (dlist.get(0) != null) {
			rf = dlist.get(0);
		}
		if (dlist.get(1) != null) {
			rb = dlist.get(1);
		}
		if (dlist.get(2) != null) {
			fr = dlist.get(2);
		}
		if (dlist.get(3) != null) {
			fm = dlist.get(3);
		}
		if (dlist.get(4) != null) {
			fl = dlist.get(4);
		}
		if (dlist.get(5) != null) {
			ll = dlist.get(5);
		}
		if (verbose) {
			System.out.println("RF: "+ rf+ " RB: "+ rb +" FR: "+fr+" FM: "+ fm +" FL: "+ fl +" LL: "+ll);
		}
		scanMap(rf,rb,fr,fm,fl,ll);
	}
	//CHECK SENSOR VALUES AND UPDATE MAP -> VALUES SHOULD BE UPDATED HERE
	public void scanMap(double right_front, double right_back, double front_right, double front_middle, double front_left, double left_long) {
		boolean verbose = true;
		int fmbmax = 1;
		int frbmax = 1;
		int flbmax = 1;
		int llbmax = 1;
		int rfbmax = 1;
		int rbbmax = 1;
		int frontmidbox = 1;
		int frontrightbox = 1;
		int frontleftbox = 1;
		int leftlongbox = 1;
		int rightfrontbox = 1;
		int rightbackbox = 1;
		//FROM SENSOR VALUE DETERMINE RANGE OF NEXT BLOCK
		//DEFINE INTEGERS AS LENGTH PROTRUDING FROM ROBOT 3X3 BODY
		//HANDLE FRONT MIDDLE;
		if (MDPSIM.real) {
			if (front_middle <= 9.5) {
				frontmidbox = 0;
			}
			//HANDLE FRONT RIGHT;
			if (front_right <= 9.5) {
				frontrightbox = 0;
			}
			//HANDLE FRONT LEFT;
			if (front_left <= 9.5) {
				frontleftbox = 0;
			}
			//HANDLE LEFT LONG
			if (left_long <= 9.5) {
				leftlongbox = 0;
			}
			//HANDLE RIGHT_FRONT
			if (right_front <= 9.5) {
				rightfrontbox = 0;
			}
			//HANDLE RIGHT_BACK
			if (right_back <= 9.5) {
				rightbackbox = 0;
			}
		} else {
			if (front_middle <= 12.5) {
				frontmidbox = 0;
			}
			//HANDLE FRONT RIGHT;
			if (front_right <= 12.5) {
				frontrightbox = 0;
			}
			//HANDLE FRONT LEFT;
			if (front_left <= 12.5) {
				frontleftbox = 0;
			}
			//HANDLE LEFT LONG
			if (left_long <= 22.5) {
				leftlongbox = 0;
			}
			//HANDLE RIGHT_FRONT
			if (right_front <= 22.5) {
				rightfrontbox = 0;
			}
			//HANDLE RIGHT_BACK
			if (right_back <= 22.5) {
				rightbackbox = 0;
			}
		}
		if (verbose) {
			System.out.println("RF: "+ rightfrontbox+ "/"+rfbmax+" RB: "+ rightbackbox +"/"+rbbmax+" FR: "+frontrightbox+"/"+ frbmax+" FM: "+ frontmidbox +"/"+fmbmax+" FL: "+ frontleftbox +"/"+flbmax+" LL: "+leftlongbox+"/"+llbmax);
		}
		//FROM KNOWN BLOCK RANGES AND ROBOT DIRECTION UPDATE MAP
		if (robotdir == RobotDirection.RIGHT) {
			for (int a = 0; a <= frontmidbox; a++) {
				setMapMemory(x_pos, y_pos+1+a, 0);
			}
			for (int a = 0; a <= frontrightbox; a++) {
				setMapMemory(x_pos+1, y_pos+1+a,0);
			}
			for (int a = 0; a <= frontleftbox; a++) {
				setMapMemory(x_pos-1, y_pos+1+a,0);
			}
			for (int a = 0; a <= rightfrontbox; a++) {
				setMapMemory(x_pos+1+a, y_pos+1,0);
			}
			for (int a = 0; a <= rightbackbox; a++) {
				setMapMemory(x_pos+1+a, y_pos-1,0);
			}
			for (int a = 0; a <= leftlongbox; a++) {
				setMapMemory(x_pos-1-a, y_pos+1,0);
			}
			
			if (frontmidbox < fmbmax) {
				if (isValid(x_pos,y_pos+1+frontmidbox+1)) {
					setMapMemory(x_pos,y_pos+1+frontmidbox+1,1);
				}
			}
			if (frontrightbox < frbmax) {
				if (isValid(x_pos+1,y_pos+1+frontrightbox+1)) {
					setMapMemory(x_pos+1,y_pos+1+frontrightbox+1,1);
				}
			}
			if (frontleftbox < flbmax) {
				if (isValid(x_pos-1,y_pos+1+frontleftbox+1)) {
					setMapMemory(x_pos-1,y_pos+1+frontleftbox+1,1);
				}
			}
			if (rightfrontbox < rfbmax) {
				if (isValid(x_pos+1+rightfrontbox+1, y_pos+1)) {
					setMapMemory(x_pos+1+rightfrontbox+1, y_pos+1,1);
				}
			}
			if (rightbackbox < rbbmax) {
				if (isValid(x_pos+1+rightbackbox+1, y_pos-1)) {
					setMapMemory(x_pos+1+rightbackbox+1, y_pos-1,1);
				}
			}
			if (leftlongbox < llbmax) {
				if (isValid(x_pos-1-leftlongbox-1, y_pos+1)) {
					setMapMemory(x_pos-1-leftlongbox-1, y_pos+1,1);
				}
			}
		} else if (robotdir == RobotDirection.LEFT) {
			for (int a = 0; a <= frontmidbox; a++) {
				setMapMemory(x_pos, y_pos-1-a, 0);
			}
			for (int a = 0; a <= frontrightbox; a++) {
				setMapMemory(x_pos-1, y_pos-1-a,0);
			}
			for (int a = 0; a <= frontleftbox; a++) {
				setMapMemory(x_pos+1, y_pos-1-a,0);
			}
			for (int a = 0; a <= rightfrontbox; a++) {
				setMapMemory(x_pos-1-a, y_pos-1,0);
			}
			for (int a = 0; a <= rightbackbox; a++) {
				setMapMemory(x_pos-1-a, y_pos+1,0);
			}
			for (int a = 0; a <= leftlongbox; a++) {
				setMapMemory(x_pos+1+a, y_pos-1,0);
			}
			if (frontmidbox < fmbmax) {
				setMapMemory(x_pos,y_pos-1-frontmidbox-1,1);
			}
			if (frontrightbox < frbmax) {
				setMapMemory(x_pos-1,y_pos-1-frontrightbox-1,1);
			}
			if (frontleftbox < flbmax) {
				setMapMemory(x_pos+1,y_pos-1-frontleftbox-1,1);
			}
			if (rightfrontbox < rfbmax) {
				setMapMemory(x_pos-1-rightfrontbox-1, y_pos-1,1);
			}
			if (rightbackbox < rbbmax) {
				setMapMemory(x_pos-1-rightbackbox-1, y_pos+1,1);
			}
			if (leftlongbox < llbmax) {
				setMapMemory(x_pos+1+leftlongbox+1, y_pos-1,1);
			}
		} else if (robotdir == RobotDirection.UP) {
			for (int a = 0; a <= frontmidbox; a++) {
				setMapMemory(x_pos-1-a, y_pos, 0);
			}
			for (int a = 0; a <= frontrightbox; a++) {
				setMapMemory(x_pos-1-a, y_pos+1,0);
			}
			for (int a = 0; a <= frontleftbox; a++) {
				setMapMemory(x_pos-1-a, y_pos-1,0);
			}
			for (int a = 0; a <= rightfrontbox; a++) {
				setMapMemory(x_pos-1, y_pos+1+a,0);
			}
			for (int a = 0; a <= rightbackbox; a++) {
				setMapMemory(x_pos+1, y_pos+1+a,0);
			}
			for (int a = 0; a <= leftlongbox; a++) {
				setMapMemory(x_pos-1, y_pos-1-a,0);
			}
			if (frontmidbox < fmbmax) {
				setMapMemory(x_pos-1-frontmidbox-1,y_pos,1);
			}
			if (frontrightbox < frbmax) {
				setMapMemory(x_pos-1-frontrightbox-1,y_pos+1,1);
			}
			if (frontleftbox < flbmax) {
				setMapMemory(x_pos-1-frontleftbox-1,y_pos-1,1);
			}
			if (rightfrontbox < rfbmax) {
				setMapMemory(x_pos-1, y_pos+1+rightfrontbox+1,1);
			}
			if (rightbackbox < rbbmax) {
				setMapMemory(x_pos+1, y_pos+1+rightbackbox+1,1);
			}
			if (leftlongbox < llbmax) {
				setMapMemory(x_pos-1, y_pos-1-leftlongbox-1,1);
			}
		} else if (robotdir == RobotDirection.DOWN) {
			for (int a = 0; a <= frontmidbox; a++) {
				setMapMemory(x_pos+1+a, y_pos, 0);
			}
			for (int a = 0; a <= frontrightbox; a++) {
				setMapMemory(x_pos+1+a, y_pos-1,0);
			}
			for (int a = 0; a <= frontleftbox; a++) {
				setMapMemory(x_pos+1+a, y_pos+1,0);
			}
			for (int a = 0; a <= rightfrontbox; a++) {
				setMapMemory(x_pos+1, y_pos-1-a,0);
			}
			for (int a = 0; a <= rightbackbox; a++) {
				setMapMemory(x_pos-1, y_pos-1-a,0);
			}
			for (int a = 0; a <= leftlongbox; a++) {
				setMapMemory(x_pos+1, y_pos+1+a,0);
			}
			if (frontmidbox < fmbmax) {
				setMapMemory(x_pos+1+frontmidbox+1,y_pos,1);
			}
			if (frontrightbox < frbmax) {
				setMapMemory(x_pos+1+frontrightbox+1,y_pos-1,1);
			}
			if (frontleftbox < flbmax) {
				setMapMemory(x_pos+1+frontleftbox+1,y_pos+1,1);
			}
			if (rightfrontbox < rfbmax) {
				setMapMemory(x_pos+1, y_pos-1-rightfrontbox-1,1);
			}
			if (rightbackbox < rbbmax) {
				setMapMemory(x_pos-1, y_pos-1-rightbackbox-1,1);
			}
			if (leftlongbox < llbmax) {
				setMapMemory(x_pos+1, y_pos+1+leftlongbox+1,1);
			}
		}
	}
	
	// MODIFIED COMPUTEFASTESTPATH USING RD FOR INFORMATION GAIN PATHING;
	public ArrayList<Node> dijkstraSearch(int start_x, int start_y, RobotDirection rd) {
		Node start = new Node(start_x, start_y, null, null, rd, 0.0);
		//construct searched list;
		ArrayList<Node> done = new ArrayList<Node>();
		//construct list of neighbours to search
		ArrayList<Node>search = new ArrayList<Node>();
		search.add(start);
		while(search.size() > 0) {
			Node n = search.remove(0);
			done.add(n);
			ArrayList<Node> neighbours = n.neighbours(start_x,start_y,mapmemory);
			for (Node nb : neighbours) {
				if (!in(done, nb) && !in(search,nb)) {
					search.add(nb);
				}
			}
		}
		return done;
	}
	
	// A* USING MANHATTAN DISTANCE
	public Node computeFastestPath(int start_x, int start_y, int dest_x, int dest_y, RobotDirection rd) {
		Node start = new Node(start_x, start_y, null, null, rd, 0.0);
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
			ArrayList<Node> neighbours = n.neighbours(dest_x,dest_y,mapmemory);
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
	
	//CANT USE BINARY UNSORTED ARRAY
	public boolean in(ArrayList<Node> list, Node n) {
		for (int a = 0 ; a < list.size(); a++) {
			if (n.is(list.get(a))) {
				return true;
			}
		}
		return false;
	}
	public boolean inScore(ArrayList<Node> list, Node n) {
		double lowest = Double.POSITIVE_INFINITY;
		for (int a = 0; a < list.size(); a++) {
			if (n.is(list.get(a))) {
				if (list.get(a).score < lowest) {
					lowest = list.get(a).score;
				}
			}
		}
		if (n.score < lowest) {
			return true;
		} else {
			return false;
		}
	}
	
	//CHANGED TO BINARY FOR SPEED
	public ArrayList<Node> insert(ArrayList<Node> ar, Node n){
		if (ar.size() == 0) {
			ar.add(n);
			return ar;
		} else {
			int end = ar.size()-1;
			int front = 0;
			int middle = Math.floorDiv((front+end),2);
			while (front <= end) {
				middle = Math.floorDiv((front+end),2);
				int cmp = Double.compare(n.score, ar.get(middle).score);
				if (cmp < 0) {
					front = middle + 1;
				} else if (cmp > 0) {
					end = middle - 1;
				} else {
					ar.add(middle,n);
					return ar;
				}
			}
		}
		ar.add(n);
		return ar;
	}
	
	public boolean isValid(int x, int y) {
		boolean verbose = false;
		if (verbose) {
			System.out.println("X SIZE: "+x_size+" Y SIZE: "+y_size);
			for(int a = 0; a < y_size; a++) {
				for (int b = 0 ; b < x_size; b++) {
					System.out.print(mapmemory.get(a).get(b));
				}
				System.out.println();
			}
			System.out.println();
		}
		try {
			int a = mapmemory.get(x).get(y);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void setMapMemory(int x, int y, int value) {
		ArrayList<ArrayList<Integer>> mem = this.mapmemory;
		int x_size = mem.size();
		int y_size = 0;
		try {
			y_size = mem.get(0).size();
		} catch (Exception e) {
		}
		//CHECK FOR VALIDITY OF XY VALUE
		if (x < 0 || x >= x_size || y < 0 || y >= y_size) {
			return;
		} else {
			ArrayList<Integer> yarray = mem.get(x);
			yarray.set(y, value);
			mem.set(x, yarray);
			this.mapmemory = mem;
			return;
		}
	}
	
	public ArrayList<Node> sort(ArrayList<Node> ar) {
		boolean sorted = false;
		while (!sorted) {
			sorted = true;
			for (int a = 1; a < ar.size(); a++) {
				if (ar.get(a-1).score < ar.get(a).score) {
					Node temp = ar.get(a);
					ar.set(a, ar.get(a-1));
					ar.set(a-1, temp);
					sorted = false;
				}
			}
		}
		return ar;
	}
	
	public ArrayList<Node> replace(ArrayList<Node> ar, Node n){
		int k = ar.size();
		for (int a = 0; a < k; a++) {
			if (n.is(ar.get(a))) {
				ar.remove(a);
				a -= 1;
				k -= 1;
			}
		}
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
	
	public String reverseMdf() {
		//get x get y
		String nMdf = "";
		String row = "";
		
		for(int i = mapmemory.size(); i > 0; i++) {
			for(int l = 0; i < mapmemory.get(0).size(); l++) {
				row = row + mapmemory.get(i).get(l);
			}
			nMdf = nMdf + row;
		}
		return nMdf;
	}
	
	
}
