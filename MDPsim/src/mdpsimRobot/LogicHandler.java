package mdpsimRobot;
import java.util.ArrayList;
//0 for nothing, 1 for something;

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
	public boolean donext = false;
	
	public LogicHandler(int x_size, int y_size, int x_pos, int y_pos) {
		this.x_size = x_size;
		this.y_size = y_size;
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		this.robotdir = null;
		this.prevaction = null;
		this.queue = new ArrayList<RobotAction>();
		this.donext = true;
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
	
	public RobotAction getNextAction(int dowhat) {
		//EXPLORATION
		printPos();
		//FIND NEXT NODE
		if (queue.size() == 0) {
			donext = true;
		} else {
			donext = false;
		}
		if (donext) {
			Node n = findNext();
			queue = trailAction(n);
		} else {
			RobotAction ra = queue.remove(queue.size()-1);
			prevaction = ra;
			return ra;
		}
		return null;
	}
	
	public ArrayList<RobotAction> trailAction(Node n){
		ArrayList<RobotAction> ar = new ArrayList<RobotAction>();
		while (n.ra != null) {
			ar.add(n.ra);
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
		Node n = computeFastestPath(18,1,1,13,RobotDirection.DOWN);
		n.printParents();
	}
	
	//EXPLORATION
	//CONSTRUCT BOUNDARY, THEN FIND TARGET NODE THAT WILL GIVE HIGHEST EXPLORATION REWARD PER SCORE;
	public Node findNext() {
		boolean verbose = true;
		printMapMemory();
		ArrayList<Node> possiblenodes = validNodes();
		ArrayList<ExplorationNode> exnodes = new ArrayList<ExplorationNode>();
		for (Node n: possiblenodes) {
			int info = informationGained(n.x, n.y, n.rd);
			Node nod = computeFastestPathRD(x_pos,y_pos,n.x,n.y,robotdir,n.rd);
			System.out.println("X: "+nod.x+" Y:"+nod.y+" RD:"+nod.rd+" INF:"+info);
			if (nod.score != 0) {
				double inforate = (double)info/nod.score;
				ExplorationNode en = new ExplorationNode(nod,inforate);
				exnodes = exInsert(exnodes, en);
			}
		}
		if (verbose) {
			System.out.println("Exnodes size : " + exnodes.size());
			for (int a = 0; a < exnodes.size()-1; a++) {
				System.out.println("#"+ a+ ": "+exnodes.get(a).informationgain);
			}
			System.out.println();
		}
		if (!(exnodes.size() == 0)){
			return exnodes.get(0);
		}
		return null;
	}
	
	//INSERT INTO SORTED LIST
	public ArrayList<ExplorationNode> exInsert(ArrayList<ExplorationNode> ar, ExplorationNode en){
		if (ar.size() == 0) {
			ar.add(en);
		}
		int left = 0;
		int right = ar.size() - 1;
		int middle = 0;
		while (left <= right) {
			middle = (left + right)/2;
			int cmp = Double.compare(en.informationgain, ar.get(middle).informationgain);
			if (cmp > 0) {
				left = middle + 1;
			} else if (cmp < 0) {
				right = middle - 1;
			} else {
				break;
			}
		}
		ar.add(middle, en);
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
		int frontmidbox = 3;
		int frontrightbox = 3;
		int frontleftbox = 3;
		int leftlongbox = 5;
		int rightfrontbox = 2;
		int rightbackbox = 2;
		//CALCULATE POTENTIALLY EXPLORED BLOCKS FROM VALUE;
		if (robotdir == RobotDirection.RIGHT) {
			for (int a = 1; a <= frontmidbox; a++) {
				if (isValidExplored(x, y+1+a)) {
					information += 1;
				}  else {
					break;
				}
			}
			for (int a = 1; a <= frontrightbox; a++) {
				if (isValidExplored(x+1, y+1+a)) {
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= frontleftbox; a++) {
				if (isValidExplored(x-1, y+1+a) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightfrontbox; a++) {
				if (isValidExplored(x+1+a, y+1) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightbackbox; a++) {
				if (isValidExplored(x+1+a, y-1) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= leftlongbox; a++) {
				if (isValidExplored(x-1-a, y+1) ){
					information += 1;
				} else {
					break;
				}
			}
		} else if (robotdir == RobotDirection.LEFT) {
			for (int a = 1; a <= frontmidbox; a++) {
				if (isValidExplored(x, y-1-a) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= frontrightbox; a++) {
				if (isValidExplored(x-1, y-1-a) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= frontleftbox; a++) {
				if (isValidExplored(x+1, y-1-a) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightfrontbox; a++) {
				if (isValidExplored(x-1-a, y-1) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightbackbox; a++) {
				if (isValidExplored(x-1-a, y+1) ){
					information += 1;
				} else {
					break;
				}	
			}
			for (int a = 1; a <= leftlongbox; a++) {
				if (isValidExplored(x+1+a, y-1) ){
					information += 1;
				} else {
					break;
				}
			}
		} else if (robotdir == RobotDirection.UP) {
			for (int a = 0; a <= frontmidbox; a++) {
				if (isValidExplored(x-1-a, y) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= frontrightbox; a++) {
				if (isValidExplored(x-1-a, y+1) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= frontleftbox; a++) {
				if (isValidExplored(x-1-a, y-1) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightfrontbox; a++) {
				if (isValidExplored(x-1, y+1+a) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightbackbox; a++) {
				if (isValidExplored(x+1, y+1+a) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= leftlongbox; a++) {
				if (isValidExplored(x-1, y-1-a) ){
					information += 1;
				} else {
					break;
				}
			}
		} else if (robotdir == RobotDirection.DOWN) {
			for (int a = 1; a <= frontmidbox; a++) {
				if (isValidExplored(x+1+a, y) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= frontrightbox; a++) {
				if (isValidExplored(x+1+a, y-1) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= frontleftbox; a++) {
				if (isValidExplored(x+1+a, y+1) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightfrontbox; a++) {
				if (isValidExplored(x+1, y-1-a) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= rightbackbox; a++) {
				if (isValidExplored(x-1, y-1-a) ){
					information += 1;
				} else {
					break;
				}
			}
			for (int a = 1; a <= leftlongbox; a++) {
				if (isValidExplored(x+1, y+1+a) ){
					information += 1;
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
		boolean verbose = false;
		int fmbmax = 3;
		int frbmax = 3;
		int flbmax = 3;
		int llbmax = 5;
		int rfbmax = 2;
		int rbbmax = 2;
		int frontmidbox = 3;
		int frontrightbox = 3;
		int frontleftbox = 3;
		int leftlongbox = 5;
		int rightfrontbox = 2;
		int rightbackbox = 2;
		//FROM SENSOR VALUE DETERMINE RANGE OF NEXT BLOCK
		//DEFINE INTEGERS AS LENGTH PROTRUDING FROM ROBOT 3X3 BODY
		//HANDLE FRONT MIDDLE;
		if (front_middle <= 10) {
			frontmidbox = 0;
		} else if (front_middle < 20) {
			frontmidbox = 1;
		} else if (front_middle < 30) {
			frontmidbox = 2;
		} else if (front_middle < 40) {
			frontmidbox = 3;
		}
		//HANDLE FRONT RIGHT;
		if (front_right <= 10) {
			frontrightbox = 0;
		} else if (front_right < 20) {
			frontrightbox = 1;
		} else if (front_right < 30) {
			frontrightbox = 2;
		} else if (front_right < 40) {
			frontrightbox = 3;
		}
		//HANDLE FRONT LEFT;
		if (front_left <= 10) {
			frontleftbox = 0;
		} else if (front_left < 20) {
			frontleftbox = 1;
		} else if (front_left < 30) {
			frontleftbox = 2;
		} else if (front_left < 40) {
			frontleftbox = 3;
		}
		//HANDLE LEFT LONG
		if (left_long <= 20) {
			leftlongbox = 0;
		} else if (left_long < 30) {
			leftlongbox = 1;
		} else if (left_long < 40) {
			leftlongbox = 2;
		} else if (left_long < 50) {
			leftlongbox = 3;
		} else if (left_long < 60) {
			leftlongbox = 4;
		}
		//HANDLE RIGHT_FRONT
		if (right_front <= 20) {
			rightfrontbox = 0;
		} else if (right_front < 30) {
			rightfrontbox = 1;
		} else if (right_front < 40) {
			rightfrontbox = 2;
		}
		//HANDLE RIGHT_BACK
		if (right_back <= 20) {
			rightbackbox = 0;
		} else if (right_back < 30) {
			rightbackbox = 1;
		} else if (right_back < 40) {
			rightbackbox = 2;
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
				setMapMemory(x_pos-1-frontrightbox-1,y_pos-1,1);
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
				setMapMemory(x_pos+1+frontrightbox+1,y_pos+1,1);
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
	public Node computeFastestPathRD(int start_x, int start_y, int dest_x, int dest_y, RobotDirection rd, RobotDirection endrd) {
		Node start = new Node(start_x, start_y, null, null, rd, 0);
		Node end = new Node(dest_x, dest_y,null,null,endrd,Double.POSITIVE_INFINITY);
		//construct searched list;
		ArrayList<Node> done = new ArrayList<Node>();
		//construct list of neighbours to search
		ArrayList<Node>search = new ArrayList<Node>();
		search.add(start);
		while(search.size() > 0) {
			Node n = search.remove(0);
			done.add(n);
			if (n.is(end)) {
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
	
	// A* USING MANHATTAN DISTANCE
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
	
	//CHANGED TO BINARY FOR SPEED
	public ArrayList<Node> insert(ArrayList<Node> ar, Node n){
		if (ar.size() == 0) {
			ar.add(n);
			return ar;
		} else {
			int end = ar.size()-1;
			int front = 0;
			int middle = (front+end)/2;
			while (front <= end) {
				middle = (front+end) / 2;
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
	
	public boolean isValidExplored(int x, int y) {	
		if (isValid(x,y)) {;
			if (mapmemory.get(x).get(y) == 2) {
				return true;
			} else {
				return false;
			}
		}
		return false;
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
		if (x < 0 || x > x_size || y < 0 || y > y_size) {
			return;
		} else {
			ArrayList<Integer> yarray = mem.get(x);
			yarray.set(y, value);
			mem.set(x, yarray);
			this.mapmemory = mem;
			return;
		}
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
