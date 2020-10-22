package mdpsimRobot;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
//0 for nothing, 1 for something;

import mdpsim.MDPSIM;
import mdpsimEngine.Action2D;
import mdpsimEngine.Engine2D;
import mdpsimGUI.TCPsocket;

public class LogicHandler {
	public int x_size;
	public int y_size;
	public int x_pos;
	public int y_pos;
	public RobotDirection robotdir;
	public ArrayList<ArrayList<Integer>> mapmemory;
	public ArrayList<ArrayList<ArrayList<Double>>> confidencematrix;
	public ArrayList<RobotAction> queue;
	public RobotAction prevaction;
	public RobotAction rwaction;
	public int recal;
	public double timelimit;
	private boolean rwcycle;
	private boolean infogain;
	private int calibratecounterh;
	private int hlimit;
	private ArrayList<Node> travelhistory;
	
	public LogicHandler(int x_size, int y_size, int x_pos, int y_pos) {
		this.rwcycle = false;
		this.infogain = false;
		this.rwaction = null;
		this.calibratecounterh = 0;
		this.hlimit = 6;
		this.x_size = x_size;
		this.y_size = y_size;
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		this.recal = 0;
		this.timelimit = Double.POSITIVE_INFINITY;
		this.robotdir = null;
		this.prevaction = null;
		this.queue = new ArrayList<RobotAction>();
		this.mapmemory = new ArrayList<ArrayList<Integer>>();
		this.travelhistory = new ArrayList<Node>();
		this.confidencematrix = new ArrayList<ArrayList<ArrayList<Double>>>();
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
		updateRobotPosCF(x,y);
		return;
	}
	
	public void initPos(int x, int y) {
		directSetMapMemory(x+1,y+1,0);
		directSetMapMemory(x,y+1,0);
		directSetMapMemory(x-1,y+1,0);
		directSetMapMemory(x+1,y,0);
		directSetMapMemory(x,y,0);
		directSetMapMemory(x-1,y,0);
		directSetMapMemory(x+1,y-1,0);
		directSetMapMemory(x,y-1,0);
		directSetMapMemory(x-1,y-1,0);
		initCFMatrixFromMapMemory();
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
		updateRobotPosCF(x_pos,y_pos);
		if (prevaction != RobotAction.RCA && prevaction != RobotAction.RCH) {
			prevaction = null;
		}
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
	
	public RobotAction getNextAction(double time) throws IOException {
		//EXPLORATION
		//FIND NEXT NODEs
		if (queue.size() == 0) {
			Node n = null;
			if (MDPSIM.mode == 1) {
				if (coverage() >= MDPSIM.coverage) {
					MDPSIM.mode = 2;
				}
				if(time >= timelimit) {
					MDPSIM.mode = 4;
				}
				if (calibratecounterh >= hlimit) {
					n = findNextCalibrateNode();
					if (n == null) {
						n = findNext();
					}
				} else {
					n = findNext();
				}
			}
			if (MDPSIM.mode == 2) {
				n = returnToBase();
				if (MDPSIM.real) {
					//TCPsocket.sendMessage(generateImageCoords());
				}
				//printMapMemory();
			}
			if (n != null) {
				queue = trailAction(n);
			} else if (n == null && MDPSIM.real){
				//TCPsocket.sendMessage("{\"MDP15\":\"RI\",\"RI\":\"E\"}");
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
				if (ra != RobotAction.RCA || ra != RobotAction.RCH) {
					calibratecounterh += 1;
				}
			}
			return ra;
		}
		return null;
	}

	public boolean canCalibrate(Node n) {
		if (n.rd == RobotDirection.UP) {
			if (	((!isException(x_pos+1,y_pos+2) && mapmemory.get(x_pos+1).get(y_pos+2) == 1)
				&&	(!isException(x_pos,y_pos+2) && mapmemory.get(x_pos).get(y_pos+2) == 1)
				&& 	(!isException(x_pos-1,y_pos+2) && mapmemory.get(x_pos-1).get(y_pos+2) == 1)) || 
					((isException(x_pos+1,y_pos+2)
				&&	isException(x_pos,y_pos+2)
				&& isException(x_pos-1,y_pos+2))))
				{
				return true;
			}
		} else if (n.rd == RobotDirection.DOWN){
			if (	((!isException(x_pos+1,y_pos-2) && mapmemory.get(x_pos+1).get(y_pos-2) == 1)
					&&	(!isException(x_pos,y_pos-2) && mapmemory.get(x_pos).get(y_pos-2) == 1)
					&& 	(!isException(x_pos-1,y_pos-2) && mapmemory.get(x_pos-1).get(y_pos-2) == 1)) || 
						((isException(x_pos+1,y_pos-2)
					&&	isException(x_pos,y_pos-2)
					&& isException(x_pos-1,y_pos-2))))
					{
					return true;
				}
		} else if (n.rd == RobotDirection.RIGHT){
			if (	((!isException(x_pos+2,y_pos+1) && mapmemory.get(x_pos+2).get(y_pos+1) == 1)
					&&	(!isException(x_pos+2,y_pos) && mapmemory.get(x_pos+2).get(y_pos) == 1)
					&& 	(!isException(x_pos+2,y_pos-1) && mapmemory.get(x_pos+2).get(y_pos-1) == 1)) || 
						((isException(x_pos+2,y_pos+1)
					&&	isException(x_pos+2,y_pos)
					&& isException(x_pos+2,y_pos-1))))
					{
					return true;
				}
		} else if (n.rd == RobotDirection.LEFT){
			if (	((!isException(x_pos-2,y_pos+1) && mapmemory.get(x_pos-2).get(y_pos+1) == 1)
					&&	(!isException(x_pos-2,y_pos) && mapmemory.get(x_pos-2).get(y_pos) == 1)
					&& 	(!isException(x_pos-2,y_pos-1) && mapmemory.get(x_pos-2).get(y_pos-1) == 1)) || 
						((isException(x_pos-2,y_pos+1)
					&&	isException(x_pos-2,y_pos)
					&& isException(x_pos-2,y_pos-1))))
					{
					return true;
				}
		}
		return false;
	}
	
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
					mdf = mdf.concat("0");
				} else if (mem.get(a).get(b) == 1) {
					mdf = mdf.concat("1");
				}  else if (mem.get(a).get(b) == 2) {
					mdf = mdf.concat("2");
				}
			}
		}
		String fin = "";
		if (mdf.length() > 0) {
			for (int a = mdf.length()-1; a >= 0; a--) {
				fin = fin.concat(Character.toString(mdf.charAt(a)));
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

	public Node findNextCalibrateNode() {
		Node n = new Node(x_pos,y_pos,null,null,robotdir,0.0);
		if (!canCalibrate(n)) {
			n = null;
		}
		Node now = n;
		if (n == null) {
			calibratecounterh -= 2;
			return n;
		}
		while (now.prev != null) {
			now = now.prev;
		}
		if (n.score <= 1) {
			if (calibratecounterh >= hlimit) {
				now.ra = RobotAction.RCH;
				calibratecounterh = 0;
			}
		} else {
			n = null;
			calibratecounterh -= 2;
		}
		return n;
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
		Node end = new Node(1,13,null,null,null,0.0);
		Node now = new Node(x_pos,y_pos,null,null,null,0.0);
		Node start = new Node(18,1,null,null,null,0.0);
		if (infogain) {
			ArrayList<Node> pathable = dijkstraSearch(x_pos,y_pos,robotdir);
			ArrayList<ExplorationNode> enodes = new ArrayList<ExplorationNode>(0);
			for (int a = 0; a < pathable.size(); a++) {
				if (pathable.get(a).score != 0) {
					double info = informationGained(pathable.get(a).x,pathable.get(a).y,pathable.get(a).rd);
					if (info == 0) {
						continue;
					}
					ExplorationNode e = new ExplorationNode(pathable.get(a), Math.pow((double)info/pathable.get(a).score, 2));
					enodes.add(e);
				}
			}
			enodes = exSort(enodes);
			if (enodes.size() == 0) {
				MDPSIM.mode = 2;
				return null;
			} else {
				return enodes.get(0);
			}
		}
		if (now.isCell(end)) {
			rwcycle = true;
		}
		if (!(now.isCell(start) && rwcycle)) {
			return rwHug();
		}
		if ((now.isCell(start) && rwcycle)) {
			infogain = true;
			return findNext();
		}
		return null;
	}
	
	
	public Node rwHug() {
		Node n = intRWHug();
		if (!(n == null)) {
			n.print();
			this.travelhistory.add(n);
		}
		if (isCycle(n)) {
			System.out.print("called");
			return FPtoWall();
		}
		return n;
	}
	
	public Node intRWHug() {
		Node now = new Node(x_pos,y_pos,null,null,null,0.0);
		if (robotdir == RobotDirection.RIGHT) {
			if 	(((	(!isException(x_pos+2,y_pos+1) && mapmemory.get(x_pos+2).get(y_pos+1) != 0) 
				|| 	(!isException(x_pos+2,y_pos) && mapmemory.get(x_pos+2).get(y_pos) != 0) 
				|| 	(!isException(x_pos+2,y_pos-1) && mapmemory.get(x_pos+2).get(y_pos-1) != 0) )||
				(	(isException(x_pos+2,y_pos+1))
				|| 	(isException(x_pos+2,y_pos))
				|| 	(isException(x_pos+2,y_pos-1))	))
				&&(	(!isException(x_pos-1,y_pos+2) && mapmemory.get(x_pos-1).get(y_pos+2) == 0)
				&&	(!isException(x_pos,y_pos+2) && mapmemory.get(x_pos).get(y_pos+2) == 0)
				&& 	(!isException(x_pos+1,y_pos+2) && mapmemory.get(x_pos+1).get(y_pos+2) == 0)))	{
				rwaction = RobotAction.F1;
				return new Node(x_pos,y_pos+1,now,RobotAction.F1,RobotDirection.RIGHT,0.0);
			} else if (((!isException(x_pos+2,y_pos+1) && mapmemory.get(x_pos+2).get(y_pos+1) == 0) 
					&&	(!isException(x_pos+2,y_pos) && mapmemory.get(x_pos+2).get(y_pos) == 0) 
					&& 	(!isException(x_pos+2,y_pos-1) && mapmemory.get(x_pos+2).get(y_pos-1) == 0) )){
				if (rwaction == RobotAction.TR) {
					rwaction = RobotAction.F1;
					return new Node(x_pos,y_pos+1,now,RobotAction.F1,RobotDirection.RIGHT,0.0);
				}
				rwaction = RobotAction.TR;
				return new Node(x_pos,y_pos,now,RobotAction.TR,RobotDirection.DOWN,0.0);
			} else {
				rwaction = RobotAction.TL;
				return new Node(x_pos,y_pos,now,RobotAction.TL,RobotDirection.UP,0.0);
			}
		} else if (robotdir == RobotDirection.UP) {
			if 	(((	(!isException(x_pos+1,y_pos+2) && mapmemory.get(x_pos+1).get(y_pos+2) != 0) 
					|| 	(!isException(x_pos,y_pos+2) && mapmemory.get(x_pos).get(y_pos+2) != 0) 
					|| 	(!isException(x_pos-1,y_pos+2) && mapmemory.get(x_pos-1).get(y_pos+2) != 0) )||
					(	(isException(x_pos+1,y_pos+2))
					|| 	(isException(x_pos,y_pos+2))
					|| 	(isException(x_pos-1,y_pos+2))	))
					&&(	(!isException(x_pos-2,y_pos-1) && mapmemory.get(x_pos-2).get(y_pos-1) == 0)
					&&	(!isException(x_pos-2,y_pos) && mapmemory.get(x_pos-2).get(y_pos) == 0)
					&& 	(!isException(x_pos-2,y_pos+1) && mapmemory.get(x_pos-2).get(y_pos+1) == 0)))	{	
					rwaction = RobotAction.F1;
					return new Node(x_pos-1,y_pos,now,RobotAction.F1,RobotDirection.UP,0.0);
				} else if ((	(!isException(x_pos+1,y_pos+2) && mapmemory.get(x_pos+1).get(y_pos+2) == 0) 
						&& 	(!isException(x_pos,y_pos+2) && mapmemory.get(x_pos).get(y_pos+2) == 0) 
						&&	(!isException(x_pos-1,y_pos+2) && mapmemory.get(x_pos-1).get(y_pos+2) == 0) )){
					if (rwaction == RobotAction.TR) {
						rwaction = RobotAction.F1;
						return new Node(x_pos-1,y_pos,now,RobotAction.F1,RobotDirection.UP,0.0);
					}
					rwaction = RobotAction.TR;
					return new Node(x_pos,y_pos,now,RobotAction.TR,RobotDirection.RIGHT,0.0);
				} else {
					rwaction = RobotAction.TL;
					return new Node(x_pos,y_pos,now,RobotAction.TL,RobotDirection.LEFT,0.0);
				}
		}  else if (robotdir == RobotDirection.LEFT) {
			if 	(((	(!isException(x_pos-2,y_pos+1) && mapmemory.get(x_pos-2).get(y_pos+1) != 0) 
					|| 	(!isException(x_pos-2,y_pos) && mapmemory.get(x_pos-2).get(y_pos) != 0) 
					|| 	(!isException(x_pos-2,y_pos-1) && mapmemory.get(x_pos-2).get(y_pos-1) != 0) )||
					(	(isException(x_pos-2,y_pos+1))
					|| 	(isException(x_pos-2,y_pos))
					|| 	(isException(x_pos-2,y_pos-1))	))
					&&(	(!isException(x_pos-1,y_pos-2) && mapmemory.get(x_pos-1).get(y_pos-2) == 0)
					&&	(!isException(x_pos,y_pos-2) && mapmemory.get(x_pos).get(y_pos-2) == 0)
					&& 	(!isException(x_pos+1,y_pos-2) && mapmemory.get(x_pos+1).get(y_pos-2) == 0)))	{	
					rwaction = RobotAction.F1;
					return new Node(x_pos,y_pos-1,now,RobotAction.F1,RobotDirection.LEFT,0.0);
				} else if ((	(!isException(x_pos-2,y_pos+1) && mapmemory.get(x_pos-2).get(y_pos+1) == 0) 
						&& 	(!isException(x_pos-2,y_pos) && mapmemory.get(x_pos-2).get(y_pos) == 0) 
						&& 	(!isException(x_pos-2,y_pos-1) && mapmemory.get(x_pos-2).get(y_pos-1) == 0) )){
					if (rwaction == RobotAction.TR) {
						rwaction = RobotAction.F1;
						return new Node(x_pos,y_pos-1,now,RobotAction.F1,RobotDirection.LEFT,0.0);
					}
					rwaction = RobotAction.TR;
					return new Node(x_pos,y_pos,now,RobotAction.TR,RobotDirection.UP,0.0);
				} else {
					rwaction = RobotAction.TL;
					return new Node(x_pos,y_pos,now,RobotAction.TL,RobotDirection.DOWN,0.0);
				}
			} else if (robotdir == RobotDirection.DOWN) {
				if 	(((	(!isException(x_pos+1,y_pos-2) && mapmemory.get(x_pos+1).get(y_pos-2) != 0) 
						|| 	(!isException(x_pos,y_pos-2) && mapmemory.get(x_pos).get(y_pos-2) != 0) 
						|| 	(!isException(x_pos-1,y_pos-2) && mapmemory.get(x_pos-1).get(y_pos-2) != 0) )||
						(	(isException(x_pos+1,y_pos-2))
						|| 	(isException(x_pos,y_pos-2))
						|| 	(isException(x_pos-1,y_pos-2))	))
						&&(	(!isException(x_pos+2,y_pos-1) && mapmemory.get(x_pos+2).get(y_pos-1) == 0)
						&&	(!isException(x_pos+2,y_pos) && mapmemory.get(x_pos+2).get(y_pos) == 0)
						&& 	(!isException(x_pos+2,y_pos+1) && mapmemory.get(x_pos+2).get(y_pos+1) == 0)))	{	
						rwaction = RobotAction.F1;
						return new Node(x_pos+1,y_pos,now,RobotAction.F1,RobotDirection.DOWN,0.0);
					} else if ((	(!isException(x_pos+1,y_pos-2) && mapmemory.get(x_pos+1).get(y_pos-2) == 0) 
							&& 	(!isException(x_pos,y_pos-2) && mapmemory.get(x_pos).get(y_pos-2) == 0) 
							&& 	(!isException(x_pos-1,y_pos-2) && mapmemory.get(x_pos-1).get(y_pos-2) == 0) )){
						if (rwaction == RobotAction.TR) {
							rwaction = RobotAction.F1;
							return new Node(x_pos+1,y_pos,now,RobotAction.F1,RobotDirection.DOWN,0.0);
						}
						rwaction = RobotAction.TR;
						return new Node(x_pos,y_pos,now,RobotAction.TR,RobotDirection.LEFT,0.0);
					} else {
						rwaction = RobotAction.TL;
						return new Node(x_pos,y_pos,now,RobotAction.TL,RobotDirection.RIGHT,0.0);
					}
			}
		return null; 
	}
	
	public Node intRWHug(int x, int y, RobotDirection rd) {
		Node now = new Node(x,y,null,null,rd,0.0);
		if (rd == RobotDirection.RIGHT) {
			if 	(((	(!isException(x+2,y+1) && mapmemory.get(x+2).get(y+1) != 0) 
				|| 	(!isException(x+2,y) && mapmemory.get(x+2).get(y) != 0) 
				|| 	(!isException(x+2,y-1) && mapmemory.get(x+2).get(y-1) != 0) )||
				(	(isException(x+2,y+1))
				|| 	(isException(x+2,y))
				|| 	(isException(x+2,y-1))	))
				&&(	(!isException(x-1,y+2) && mapmemory.get(x-1).get(y+2) == 0)
				&&	(!isException(x,y+2) && mapmemory.get(x).get(y+2) == 0)
				&& 	(!isException(x+1,y+2) && mapmemory.get(x+1).get(y+2) == 0)))	{
				rwaction = RobotAction.F1;
				return new Node(x,y+1,now,RobotAction.F1,RobotDirection.RIGHT,0.0);
			} else if (((!isException(x+2,y+1) && mapmemory.get(x+2).get(y+1) == 0) 
					&&	(!isException(x+2,y) && mapmemory.get(x+2).get(y) == 0) 
					&& 	(!isException(x+2,y-1) && mapmemory.get(x+2).get(y-1) == 0) )){
				if (rwaction == RobotAction.TR) {
					rwaction = RobotAction.F1;
					return new Node(x,y+1,now,RobotAction.F1,RobotDirection.RIGHT,0.0);
				}
				rwaction = RobotAction.TR;
				return new Node(x,y,now,RobotAction.TR,RobotDirection.DOWN,0.0);
			} else {
				rwaction = RobotAction.TL;
				return new Node(x,y,now,RobotAction.TL,RobotDirection.UP,0.0);
			}
		} else if (rd == RobotDirection.UP) {
			if 	(((	(!isException(x+1,y+2) && mapmemory.get(x+1).get(y+2) != 0) 
					|| 	(!isException(x,y+2) && mapmemory.get(x).get(y+2) != 0) 
					|| 	(!isException(x-1,y+2) && mapmemory.get(x-1).get(y+2) != 0) )||
					(	(isException(x+1,y+2))
					|| 	(isException(x,y+2))
					|| 	(isException(x-1,y+2))	))
					&&(	(!isException(x-2,y-1) && mapmemory.get(x-2).get(y-1) == 0)
					&&	(!isException(x-2,y) && mapmemory.get(x-2).get(y) == 0)
					&& 	(!isException(x-2,y+1) && mapmemory.get(x-2).get(y+1) == 0)))	{	
					rwaction = RobotAction.F1;
					return new Node(x-1,y,now,RobotAction.F1,RobotDirection.UP,0.0);
				} else if ((	(!isException(x+1,y+2) && mapmemory.get(x+1).get(y+2) == 0) 
						&& 	(!isException(x,y+2) && mapmemory.get(x).get(y+2) == 0) 
						&&	(!isException(x-1,y+2) && mapmemory.get(x-1).get(y+2) == 0) )){
					if (rwaction == RobotAction.TR) {
						rwaction = RobotAction.F1;
						return new Node(x-1,y,now,RobotAction.F1,RobotDirection.UP,0.0);
					}
					rwaction = RobotAction.TR;
					return new Node(x,y,now,RobotAction.TR,RobotDirection.RIGHT,0.0);
				} else {
					rwaction = RobotAction.TL;
					return new Node(x,y,now,RobotAction.TL,RobotDirection.LEFT,0.0);
				}
		}  else if (rd == RobotDirection.LEFT) {
			if 	(((	(!isException(x-2,y+1) && mapmemory.get(x-2).get(y+1) != 0) 
					|| 	(!isException(x-2,y) && mapmemory.get(x-2).get(y) != 0) 
					|| 	(!isException(x-2,y-1) && mapmemory.get(x-2).get(y-1) != 0) )||
					(	(isException(x-2,y+1))
					|| 	(isException(x-2,y))
					|| 	(isException(x-2,y-1))	))
					&&(	(!isException(x-1,y-2) && mapmemory.get(x-1).get(y-2) == 0)
					&&	(!isException(x,y-2) && mapmemory.get(x).get(y-2) == 0)
					&& 	(!isException(x+1,y-2) && mapmemory.get(x+1).get(y-2) == 0)))	{	
					rwaction = RobotAction.F1;
					return new Node(x,y-1,now,RobotAction.F1,RobotDirection.LEFT,0.0);
				} else if ((	(!isException(x-2,y+1) && mapmemory.get(x-2).get(y+1) == 0) 
						&& 	(!isException(x-2,y) && mapmemory.get(x-2).get(y) == 0) 
						&& 	(!isException(x-2,y-1) && mapmemory.get(x-2).get(y-1) == 0) )){
					if (rwaction == RobotAction.TR) {
						rwaction = RobotAction.F1;
						return new Node(x,y-1,now,RobotAction.F1,RobotDirection.LEFT,0.0);
					}
					rwaction = RobotAction.TR;
					return new Node(x,y,now,RobotAction.TR,RobotDirection.UP,0.0);
				} else {
					rwaction = RobotAction.TL;
					return new Node(x,y,now,RobotAction.TL,RobotDirection.DOWN,0.0);
				}
			} else if (rd == RobotDirection.DOWN) {
				if 	(((	(!isException(x+1,y-2) && mapmemory.get(x+1).get(y-2) != 0) 
						|| 	(!isException(x,y-2) && mapmemory.get(x).get(y-2) != 0) 
						|| 	(!isException(x-1,y-2) && mapmemory.get(x-1).get(y-2) != 0) )||
						(	(isException(x+1,y-2))
						|| 	(isException(x,y-2))
						|| 	(isException(x-1,y-2))	))
						&&(	(!isException(x+2,y-1) && mapmemory.get(x+2).get(y-1) == 0)
						&&	(!isException(x+2,y) && mapmemory.get(x+2).get(y) == 0)
						&& 	(!isException(x+2,y+1) && mapmemory.get(x_pos+2).get(y+1) == 0)))	{	
						rwaction = RobotAction.F1;
						return new Node(x+1,y,now,RobotAction.F1,RobotDirection.DOWN,0.0);
					} else if ((	(!isException(x+1,y-2) && mapmemory.get(x+1).get(y-2) == 0) 
							&& 	(!isException(x,y-2) && mapmemory.get(x).get(y-2) == 0) 
							&& 	(!isException(x-1,y-2) && mapmemory.get(x-1).get(y-2) == 0) )){
						if (rwaction == RobotAction.TR) {
							rwaction = RobotAction.F1;
							return new Node(x+1,y,now,RobotAction.F1,RobotDirection.DOWN,0.0);
						}
						rwaction = RobotAction.TR;
						return new Node(x,y,now,RobotAction.TR,RobotDirection.LEFT,0.0);
					} else {
						rwaction = RobotAction.TL;
						return new Node(x,y,now,RobotAction.TL,RobotDirection.RIGHT,0.0);
					}
			}
		return null; 
	}
	
	public boolean isCycle(Node n) {
		for (int a = travelhistory.size()-2; a > 0; a--) {
			try {
				if (n.is(travelhistory.get(a))) {
					return true;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	
	public Node FPtoWall() {
		Node n = travelhistory.get(travelhistory.size()-1);
		travelhistory.remove(travelhistory.size()-1);
		while (!travelhistory.get(travelhistory.size()-1).is(n)) {
			travelhistory.remove(travelhistory.size()-1);
		}
		travelhistory.remove(travelhistory.size()-1);
		while (!isRightWall(travelhistory.get(travelhistory.size()-1))) {
			travelhistory.remove(travelhistory.size()-1);
		}
		Node dest = travelhistory.get(travelhistory.size()-1);
		while (isAvailRightObstacle(dest)) {
			dest = intRWHug(dest.x,dest.y,dest.rd);
		}
		return computeFastestPath(x_pos, y_pos, dest.x,dest.y,robotdir,dest.rd);
	}
	
	public boolean isRightWall(Node n) {
		if (n.rd == RobotDirection.UP) {
			if (	(isValid(n.x+1,n.y+2) && mapmemory.get(n.x+1).get(n.y+2) == 1) 	||
					(isValid(n.x,n.y+2) && mapmemory.get(n.x).get(n.y+2) == 1) 	||
					(isValid(n.x-1,n.y+2) && mapmemory.get(n.x-1).get(n.y+2) == 1) 	||
					!isValid(n.x-1,n.y+2) 	||
					!isValid(n.x-1,n.y+2)	||
					!isValid(n.x-1,n.y+2)) {
				return true;
			}
		} else if (n.rd == RobotDirection.DOWN) {
			if (	(isValid(n.x+1,n.y-2) && mapmemory.get(n.x+1).get(n.y-2) == 1) 	||
					(isValid(n.x,n.y-2) && mapmemory.get(n.x).get(n.y-2) == 1) 	||
					(isValid(n.x-1,n.y-2) && mapmemory.get(n.x-1).get(n.y-2) == 1) 	||
					!isValid(n.x-1,n.y-2) 	||
					!isValid(n.x-1,n.y-2)	||
					!isValid(n.x-1,n.y-2)) {
				return true;
			}
		} else if (n.rd == RobotDirection.LEFT) {
			if (	(isValid(n.x-2,n.y+1) && mapmemory.get(n.x-2).get(n.y+1) == 1) 	||
					(isValid(n.x-2,n.y) && mapmemory.get(n.x-2).get(n.y) == 1) 	||
					(isValid(n.x-2,n.y-1) && mapmemory.get(n.x-2).get(n.y-1) == 1) 	||
					!isValid(n.x-2,n.y+1) 	||
					!isValid(n.x-2,n.y)	||
					!isValid(n.x-2,n.y-1)) {
				return true;
			}
		} else if (n.rd == RobotDirection.RIGHT) {
			if (	(isValid(n.x+2,n.y+1) && mapmemory.get(n.x+2).get(n.y+1) == 1) 	||
					(isValid(n.x+2,n.y) && mapmemory.get(n.x+2).get(n.y) == 1) 	||
					(isValid(n.x+2,n.y-1) && mapmemory.get(n.x+2).get(n.y-1) == 1) 	||
					!isValid(n.x+2,n.y+1) 	||
					!isValid(n.x+2,n.y)	||
					!isValid(n.x+2,n.y-1)) {
				return true;
			}
		}
		return false;
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
		if (ar.size() == 0) {
			return ar;
		}
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
	public boolean isInformationGained(int x, int y, RobotDirection rd){
		if (informationGained(x,y,rd) != 0) {
			return true;
		} else {
			return false;
		}
	}
	//FIND AMOUNT OF INFORMATION GAINED IF SCAN AT GIVEN SQUARE;
	//SQUARE MUST BE VALID ROBOT POSITION;
	public int informationGained(int x, int y, RobotDirection rd) {
		int information = 0;
		int frontmidbox = 2;
		int frontrightbox = 2;
		int frontleftbox = 2;
		int leftlongbox = 3;
		int rightfrontbox = 2;
		int rightbackbox = 2;
		//CALCULATE POTENTIALLY EXPLORED BLOCKS FROM VALUE;
		if (rd == RobotDirection.RIGHT) {
			for (int a = 1; a <= frontmidbox; a++) {
				if (isValid(x, y+1+a)) {
					int b = mapmemory.get(x).get(y+1+a);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
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
					} else {
						break;
					}
				} else {
					break;
				}
			}
		} else if (rd == RobotDirection.LEFT) {
			for (int a = 1; a <= frontmidbox; a++) {
				if (isValid(x, y-1-a) ){
					int b = mapmemory.get(x).get(y-1-a);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
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
					} else {
						break;
					}
				} else {
					break;
				}
			}
		} else if (rd == RobotDirection.UP) {
			for (int a = 0; a <= frontmidbox; a++) {
				if (isValid(x-1-a, y) ){
					int b = mapmemory.get(x-1-a).get(y);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
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
					} else {
						break;
					}
				} else {
					break;
				}
			}
		} else if (rd == RobotDirection.DOWN) {
			for (int a = 1; a <= frontmidbox; a++) {
				if (isValid(x+1+a, y) ){
					int b = mapmemory.get(x+1+a).get(y);
					if (b == 2) {
						information += 1;
					} else if (b == 0){
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
		int fmbmax = 2;
		int frbmax = 2;
		int flbmax = 2;
		int llbmax = 3;
		int rfbmax = 2;
		int rbbmax = 2;
		int frontmidbox = 2;
		int frontrightbox = 2;
		int frontleftbox = 2;
		int leftlongbox = 3;
		int rightfrontbox = 2;
		int rightbackbox = 2;
		//FROM SENSOR VALUE DETERMINE RANGE OF NEXT BLOCK
		//DEFINE INTEGERS AS LENGTH PROTRUDING FROM ROBOT 3X3 BODY
		//HANDLE FRONT MIDDLE;
		if (MDPSIM.real) {
			if (front_middle <= 15) { //11
				frontmidbox = 0;
			} else if (front_middle <= 22){ //21 //24
				frontmidbox = 1;
			}// else if (front_middle <= 31){
				//frontmidbox = 2;
			//}
			//HANDLE FRONT RIGHT;
			if (front_right <= 15) { //12
				frontrightbox = 0;
			} else if (front_right <= 24){ //20
				frontrightbox = 1;
			} //else if (front_right <= 30){
				//frontrightbox = 2;
			//}
			//HANDLE FRONT LEFT;
			if (front_left <= 15) { //11
				frontleftbox = 0;
			} else if (front_left <= 25){ //21
				frontleftbox = 1; //CHECK(changed from 2 -> 1)
			}// else if (front_left <= 31){
				//frontleftbox = 2;
			//}
			//HANDLE LEFT LONG
			if (left_long <= 0) {
				leftlongbox = 3;
			}else if (left_long <= 22) { //20
				
				leftlongbox = 0;
			} else if (left_long <= 27) { //24
				leftlongbox = 1;
			}  else if (left_long < 33) { //32 // reduce this range
				leftlongbox = 2;
			}
//			} else if(left_long <= 41) {
//				leftlongbox = 3;
//			} 
			//HANDLE RIGHT_FRONT
			if (right_front <= 19) { //18
				rightfrontbox = 0;
			} else if (right_front <= 32) { //28
				rightfrontbox = 1;
			} 
			//HANDLE RIGHT_BACK
			if (right_back <= 20) { //18
				rightbackbox = 0;
			} else if (right_back < 28) {
				rightbackbox = 1;
			}
		} else {
			if (front_middle <= 12.5) {
				frontmidbox = 0;
			} else if (front_middle <= 22.5) {
				frontmidbox = 1;
			} else if (front_middle <= 32.5) {
				frontmidbox = 2;
			} 
			//HANDLE FRONT RIGHT;
			if (front_right <= 12.5) {
				frontrightbox = 0;
			} else if (front_right <= 22.5) {
				frontrightbox = 1;
			} else if (front_right <= 32.5) {
				frontrightbox = 2;
			}
			//HANDLE FRONT LEFT;
			if (front_left <= 12.5) {
				frontleftbox = 0;
			} else if (front_left <= 22.5) {
				frontleftbox = 1;
			} else if (front_left <= 32.5) {
				frontleftbox = 2;
			}
			//HANDLE LEFT LONG
			if (left_long <= 22.5) {
				leftlongbox = 0;
			} else if (left_long <= 32.5) {
				leftlongbox = 1;
			} else if (left_long <= 42.5) {
				leftlongbox = 2;
			}
			//HANDLE RIGHT_FRONT
			if (right_front <= 22.5) {
				rightfrontbox = 0;
			} else if (right_front <= 32.5) {
				rightfrontbox = 1;
			}
			//HANDLE RIGHT_BACK
			if (right_back <= 22.5) {
				rightbackbox = 0;
			} else if (right_back <= 32.5) {
				rightbackbox = 1;
			}
		}
		if (verbose) {
			System.out.println("RF: "+ rightfrontbox+ "/"+rfbmax+" RB: "+ rightbackbox +"/"+rbbmax+" FR: "+frontrightbox+"/"+ frbmax+" FM: "+ frontmidbox +"/"+fmbmax+" FL: "+ frontleftbox +"/"+flbmax+" LL: "+leftlongbox+"/"+llbmax);
		}
		//FROM KNOWN BLOCK RANGES AND ROBOT DIRECTION UPDATE MAP
		if (robotdir == RobotDirection.RIGHT) {
			for (int a = 0; a <= frontmidbox; a++) {
				readConfidenceMatrix(x_pos, y_pos+1+a, 0,a);
			}
			for (int a = 0; a <= frontrightbox; a++) {
				readConfidenceMatrix(x_pos+1, y_pos+1+a,0,a);
			}
			for (int a = 0; a <= frontleftbox; a++) {
				readConfidenceMatrix(x_pos-1, y_pos+1+a,0,a);
			}
			for (int a = 0; a <= rightfrontbox; a++) {
				readConfidenceMatrix(x_pos+1+a, y_pos+1,0,a);
			}
			for (int a = 0; a <= rightbackbox; a++) {
				readConfidenceMatrix(x_pos+1+a, y_pos-1,0,a);
			}
			for (int a = 0; a <= leftlongbox; a++) {
				readConfidenceMatrix(x_pos-1-a, y_pos+1,0,a);
			}
			
			if (frontmidbox < fmbmax) {
				if (isValid(x_pos,y_pos+1+frontmidbox+1)) {
					readConfidenceMatrix(x_pos,y_pos+1+frontmidbox+1,1,frontmidbox);
				}
			}
			if (frontrightbox < frbmax) {
				if (isValid(x_pos+1,y_pos+1+frontrightbox+1)) {
					readConfidenceMatrix(x_pos+1,y_pos+1+frontrightbox+1,1,frontrightbox);
				}
			}
			if (frontleftbox < flbmax) {
				if (isValid(x_pos-1,y_pos+1+frontleftbox+1)) {
					readConfidenceMatrix(x_pos-1,y_pos+1+frontleftbox+1,1,frontleftbox);
				}
			}
			if (rightfrontbox < rfbmax) {
				if (isValid(x_pos+1+rightfrontbox+1, y_pos+1)) {
					readConfidenceMatrix(x_pos+1+rightfrontbox+1, y_pos+1,1,rightfrontbox);
				}
			}
			if (rightbackbox < rbbmax) {
				if (isValid(x_pos+1+rightbackbox+1, y_pos-1)) {
					readConfidenceMatrix(x_pos+1+rightbackbox+1, y_pos-1,1,rightbackbox);
				}
			}
			if (leftlongbox < llbmax) {
				if (isValid(x_pos-1-leftlongbox-1, y_pos+1)) {
					readConfidenceMatrix(x_pos-1-leftlongbox-1, y_pos+1,1,leftlongbox);
				}
			}
		} else if (robotdir == RobotDirection.LEFT) {
			for (int a = 0; a <= frontmidbox; a++) {
				readConfidenceMatrix(x_pos, y_pos-1-a, 0,a);
			}
			for (int a = 0; a <= frontrightbox; a++) {
				readConfidenceMatrix(x_pos-1, y_pos-1-a,0,a);
			}
			for (int a = 0; a <= frontleftbox; a++) {
				readConfidenceMatrix(x_pos+1, y_pos-1-a,0,a);
			}
			for (int a = 0; a <= rightfrontbox; a++) {
				readConfidenceMatrix(x_pos-1-a, y_pos-1,0,a);
			}
			for (int a = 0; a <= rightbackbox; a++) {
				readConfidenceMatrix(x_pos-1-a, y_pos+1,0,a);
			}
			for (int a = 0; a <= leftlongbox; a++) {
				readConfidenceMatrix(x_pos+1+a, y_pos-1,0,a);
			}
			if (frontmidbox < fmbmax) {
				readConfidenceMatrix(x_pos,y_pos-1-frontmidbox-1,1,frontmidbox);
			}
			if (frontrightbox < frbmax) {
				readConfidenceMatrix(x_pos-1,y_pos-1-frontrightbox-1,1,frontrightbox);
			}
			if (frontleftbox < flbmax) {
				readConfidenceMatrix(x_pos+1,y_pos-1-frontleftbox-1,1,frontleftbox);
			}
			if (rightfrontbox < rfbmax) {
				readConfidenceMatrix(x_pos-1-rightfrontbox-1, y_pos-1,1,rightfrontbox);
			}
			if (rightbackbox < rbbmax) {
				readConfidenceMatrix(x_pos-1-rightbackbox-1, y_pos+1,1,rightbackbox);
			}
			if (leftlongbox < llbmax) {
				readConfidenceMatrix(x_pos+1+leftlongbox+1, y_pos-1,1,leftlongbox);
			}
		} else if (robotdir == RobotDirection.UP) {
			for (int a = 0; a <= frontmidbox; a++) {
				readConfidenceMatrix(x_pos-1-a, y_pos,0, a);
			}
			for (int a = 0; a <= frontrightbox; a++) {
				readConfidenceMatrix(x_pos-1-a, y_pos+1,0,a);
			}
			for (int a = 0; a <= frontleftbox; a++) {
				readConfidenceMatrix(x_pos-1-a, y_pos-1, 0,a);
			}
			for (int a = 0; a <= rightfrontbox; a++) {
				readConfidenceMatrix(x_pos-1, y_pos+1+a, 0,a);
			}
			for (int a = 0; a <= rightbackbox; a++) {
				readConfidenceMatrix(x_pos+1, y_pos+1+a,0,a);
			}
			for (int a = 0; a <= leftlongbox; a++) {
				readConfidenceMatrix(x_pos-1, y_pos-1-a,0,a);
			}
			if (frontmidbox < fmbmax) {
				readConfidenceMatrix(x_pos-1-frontmidbox-1,y_pos,1,frontmidbox);
			}
			if (frontrightbox < frbmax) {
				readConfidenceMatrix(x_pos-1-frontrightbox-1,y_pos+1,1,frontrightbox);
			}
			if (frontleftbox < flbmax) {
				readConfidenceMatrix(x_pos-1-frontleftbox-1,y_pos-1,1,frontleftbox);
			}
			if (rightfrontbox < rfbmax) {
				readConfidenceMatrix(x_pos-1, y_pos+1+rightfrontbox+1,1,rightfrontbox);
			}
			if (rightbackbox < rbbmax) {
				readConfidenceMatrix(x_pos+1, y_pos+1+rightbackbox+1,1,rightbackbox);
			}
			if (leftlongbox < llbmax) {
				readConfidenceMatrix(x_pos-1, y_pos-1-leftlongbox-1,1,leftlongbox);
			}
		} else if (robotdir == RobotDirection.DOWN) {
			for (int a = 0; a <= frontmidbox; a++) {
				readConfidenceMatrix(x_pos+1+a, y_pos, 0,a);
			}
			for (int a = 0; a <= frontrightbox; a++) {
				readConfidenceMatrix(x_pos+1+a, y_pos-1, 0,a);
			}
			for (int a = 0; a <= frontleftbox; a++) {
				readConfidenceMatrix(x_pos+1+a, y_pos+1, 0,a);
			}
			for (int a = 0; a <= rightfrontbox; a++) {
				readConfidenceMatrix(x_pos+1, y_pos-1-a, 0,a);
			}
			for (int a = 0; a <= rightbackbox; a++) {
				readConfidenceMatrix(x_pos-1, y_pos-1-a,0, a);
			}
			for (int a = 0; a <= leftlongbox; a++) {
				readConfidenceMatrix(x_pos+1, y_pos+1+a,0,a);
			}
			if (frontmidbox < fmbmax) {
				readConfidenceMatrix(x_pos+1+frontmidbox+1,y_pos,1,frontmidbox);
			}
			if (frontrightbox < frbmax) {
				readConfidenceMatrix(x_pos+1+frontrightbox+1,y_pos-1,1,frontrightbox);
			}
			if (frontleftbox < flbmax) {
				readConfidenceMatrix(x_pos+1+frontleftbox+1,y_pos+1,1,frontleftbox);
			}
			if (rightfrontbox < rfbmax) {
				readConfidenceMatrix(x_pos+1, y_pos-1-rightfrontbox-1,1,rightfrontbox);
			}
			if (rightbackbox < rbbmax) {
				readConfidenceMatrix(x_pos-1, y_pos-1-rightbackbox-1,1,rightbackbox);
			}
			if (leftlongbox < llbmax) {
				readConfidenceMatrix(x_pos+1, y_pos+1+leftlongbox+1,1,leftlongbox);
			}
		}
		setMapMemoryFromCFMatrix();
	}
	
	// MODIFIED COMPUTEFASTESTPATH USING RD FOR INFORMATION GAIN PATHING;
	public ArrayList<Node> dijkstraSearch(int start_x, int start_y, RobotDirection rd) {
		Node start = new Node(start_x, start_y, null, null, rd, 0.0);
		//construct searched list;
		ArrayList<Node> done = new ArrayList<Node>();
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
	
	public Node computeFastestPath(int start_x, int start_y, int dest_x, int dest_y, RobotDirection rd, RobotDirection erd) {
		Node start = new Node(start_x, start_y, null, null, rd, 0.0);
		Node end = new Node(dest_x, dest_y,null,null,erd,Double.POSITIVE_INFINITY);
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
	
	public void directSetMapMemory(int x, int y, int value) {
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
	
	public void setMapMemoryFromCFMatrix() {
		for (int a = 0; a < y_size; a++) {
			for (int b = 0; b < x_size; b++) {
				double zero = confidencematrix.get(0).get(a).get(b);
				double one = confidencematrix.get(1).get(a).get(b);
				double two = confidencematrix.get(2).get(a).get(b);
				if (zero >= one && zero >= two) {
					directSetMapMemory(a,b,0);
				} else if (one >= zero && one >= two) {
					directSetMapMemory(a,b,1);
				}else if (two >= zero && two >= one) {
					directSetMapMemory(a,b,2);
				}
			}
		}
	}
	
	public void initCFMatrixFromMapMemory() {
		ArrayList<ArrayList<ArrayList<Double>>> ar = new ArrayList<ArrayList<ArrayList<Double>>>();
		for (int a = 0; a < 3; a++) {
			ArrayList<ArrayList<Double>> art = new ArrayList<ArrayList<Double>>();
			for (int b = 0; b < y_size; b++) {
				ArrayList<Double> arv = new ArrayList<Double>();
				for (int c = 0; c < x_size; c++) {
					arv.add(0.0);
				}
				art.add(arv);
			}
			ar.add(art);
		}
		this.confidencematrix = ar;
		for (int a = 0; a < mapmemory.size(); a++) {
			for (int b = 0; b < mapmemory.get(0).size(); b++) {
				int memvalue = mapmemory.get(a).get(b);
				switch(memvalue) {
				case 0:
					setHardCFMatrix(0,a,b);
					break;
				case 1:
					setHardCFMatrix(1,a,b);
					break;
				case 2:
					setHardCFMatrix(2,a,b);
					break;
				}
			}
		}
	}
	
	public void setHardCFMatrix(int value, int x, int y) {
		switch(value) {
			case 0:
				confidencematrix.get(0).get(x).set(y, 1.0);
				confidencematrix.get(1).get(x).set(y, 0.0);
				confidencematrix.get(2).get(x).set(y, 0.0);
				break;
			case 1:
				confidencematrix.get(0).get(x).set(y, 0.0);
				confidencematrix.get(1).get(x).set(y, 1.0);
				confidencematrix.get(2).get(x).set(y, 0.0);
				break;
			case 2:
				confidencematrix.get(0).get(x).set(y, 0.0);
				confidencematrix.get(1).get(x).set(y, 0.0);
				confidencematrix.get(2).get(x).set(y, 1.0);
				break;
		}
	}
	
	public void addCFMatrix(int value, int x, int y, double add) {
		switch(value) {
			case 0:
				confidencematrix.get(0).get(x).set(y, confidencematrix.get(0).get(x).get(y)+ add);
				break;
			case 1:
				confidencematrix.get(1).get(x).set(y, confidencematrix.get(1).get(x).get(y)+ add);
				break;
			case 2:
				confidencematrix.get(2).get(x).set(y, confidencematrix.get(2).get(x).get(y)+ add);
				break;
		}
	}
	
	public void printCFMatrix() {
		for (int a = 0; a < 3; a++) {
			System.out.println(a+"-confidence matrix");
			for (int b = 0; b < y_size; b++) {
				for (int c = 0;c < x_size; c++) {
					System.out.print(confidencematrix.get(a).get(b).get(c)+ " ");
				}
				System.out.println();
			}
		}
	}
	
	public void updateRobotPosCF(int x, int y) {
		if (isValid(x,y)) {
			addCFMatrix(0,x,y,1000);
		}
		if (isValid(x+1,y)) {
			addCFMatrix(0,x+1,y,1000);
		}
		if (isValid(x-1,y)) {
			addCFMatrix(0,x-1,y,1000);
		}
		if (isValid(x,y+1)) {
			addCFMatrix(0,x,y+1,1000);
		}
		if (isValid(x+1,y+1)) {
			addCFMatrix(0,x+1,y+1,1000);
		}
		if (isValid(x-1,y+1)) {
			addCFMatrix(0,x-1,y+1,1000);
		}
		if (isValid(x,y-1)) {
			addCFMatrix(0,x,y-1,1000);
		}
		if (isValid(x+1,y-1)) {
			addCFMatrix(0,x+1,y-1,1000);
		}
		if (isValid(x-1,y-1)) {
			addCFMatrix(0,x-1,y-1,1000);
		}
	}
	
	public void readConfidenceMatrix(int x, int y, int value, int distance) {
		double val;
		switch(distance) {
			case 0:
				val = 20;
				break;
			case 1:
				val = 10;
				break;
			case 2:
				val = 5;
				break;
			case 3:
				val = 3;
				break;
			default:
				val = 0;
				break;
		}
		if (isValid(x,y)) {
			addCFMatrix(value,x,y,val);
		}
	}
	
	public ArrayList<Double> normalize(ArrayList<Double> ar) { //normalizes array to (0,1);
		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		for (int a = 0; a < ar.size(); a++) {
			if (ar.get(a) < min) {
				min = ar.get(a);
			}
			if (ar.get(a) > max) {
				max = ar.get(a);
			}
		}
		double difference = max - min;
		for (int a = 0; a < ar.size(); a++) {
			ar.set(a, (double)(ar.get(a)-min)/difference);
		}
		double total = 0;
		for (int a = 0; a < ar.size(); a++) {
			total += ar.get(a);
		}
		for (int a = 0; a < ar.size(); a++) {
			ar.set(a, (double)(ar.get(a)/total));
		}
		return ar;
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
		printCFMatrix();
	}
	
	
	public String reverseMdf() {
		//get x get y
		String row = "";
		String nMDF = "";
		
		for(int i = 0; i < mapmemory.size() ;i++) {
			row = "";
			for(int k = 0; k < mapmemory.get(0).size(); k++) {
				row = row + mapmemory.get(i).get(k);
			}
			nMDF = nMDF + row;
		}
		return nMDF;
	}
	
	public String getMdf() {
		//get x get y
		String row = "";
		String nMDF = "";
		
		for(int i = mapmemory.size(); i > 0 ;i--) {
			row = "";
			for(int k = 0; k < mapmemory.get(0).size(); k++) {
				row = row + mapmemory.get(i-1).get(k);
			}
			nMDF = nMDF + row;
		}
		return nMDF;
	}
	
	public double wallHugBonus(int x, int y) {
		int xbonus = Math.min(Math.abs(x-0), Math.abs(x-x_size));
		int ybonus = Math.min(Math.abs(y-0), Math.abs(y-y_size));
		return Math.min((20-xbonus),(15-ybonus));
	}
	
	public boolean isAvailRightObstacle(Node n) {
		int x = n.x;
		int y = n.y;
		if (n.rd == RobotDirection.UP) {
			if ((	(!isException(x-2,y+2) && mapmemory.get(x-2).get(y+2) == 1)
				||	(!isException(x-1,y+2) && mapmemory.get(x-1).get(y+2) == 1)
				||	(!isException(x,y+2) && mapmemory.get(x).get(y+2) == 1)
				||	(!isException(x+1,y+2) && mapmemory.get(x+1).get(y+2) == 1)
				|| 	(!isException(x+2,y+2) && mapmemory.get(x+2).get(y+2) == 1)) || 
				(	isException(x-2,y+2)
				||	isException(x-1,y+2)
				||	isException(x,y+2)
				||	isException(x+1, y+2)
				||	isException(x+2, y+2))) {
				return true;
			}
		} else if (n.rd == RobotDirection.DOWN) {
			if ((		(!isException(x-2,y-2) && mapmemory.get(x-2).get(y-2) == 1)
					||	(!isException(x-1,y-2) && mapmemory.get(x-1).get(y-2) == 1)
					||	(!isException(x,y-2) && mapmemory.get(x).get(y-2) == 1)
					|| 	(!isException(x+1,y-2) && mapmemory.get(x+1).get(y-2) == 1)
					|| 	(!isException(x+2,y-2) && mapmemory.get(x+2).get(y-2) == 1)) || 
					(	isException(x-2,y-2)
					||	isException(x-1,y-2)
					||	isException(x,y-2)
					||	isException(x+1, y-2)
					||	isException(x+2, y-2))) {
					return true;
				}
		} else if (n.rd == RobotDirection.LEFT) {
			if ((		(!isException(x-2,y-2) && mapmemory.get(x-2).get(y-2) == 1)
					||	(!isException(x-2,y-1) && mapmemory.get(x-2).get(y-1) == 1)
					||	(!isException(x-2,y) && mapmemory.get(x-2).get(y) == 1)
					||	(!isException(x-2,y+1) && mapmemory.get(x-2).get(y+1) == 1)
					|| 	(!isException(x-2,y+2) && mapmemory.get(x-2).get(y+2) == 1)) || 
					(	isException(x-2,y-2)
					||	isException(x-2,y-1)
					||	isException(x-2,y)
					||	isException(x-2, y+1)
					||	isException(x-2, y+2))) {
					return true;
				}
		} else if (n.rd == RobotDirection.RIGHT) {
			if ((		(!isException(x+2,y-2) && mapmemory.get(x+2).get(y-2) == 1)
					||	(!isException(x+2,y-1) && mapmemory.get(x+2).get(y-1) == 1)
					||	(!isException(x+2,y) && mapmemory.get(x+2).get(y) == 1)
					|| 	(!isException(x+2,y+1) && mapmemory.get(x+2).get(y+1) == 1)
					|| 	(!isException(x+2,y+2) && mapmemory.get(x+2).get(y+2) == 1)) || 
					(	isException(x+2,y-2)
					||	isException(x+2,y-1)
					||	isException(x+2,y)
					||	isException(x+2, y+1)
					||	isException(x+2, y+2))) {
					return true;
				}
		}
		return false;
	}
	
	public boolean isException(int x, int y) {
		try {
			mapmemory.get(x).get(y);
			return false;
		} catch (Exception e) {
			return true;
		}
	}
	
	public int wallBonus(Node n) {
		int nx = Math.min(Math.abs(20-n.x), Math.abs(0-n.x));
		int ny = Math.min(Math.abs(15-n.y), Math.abs(0-n.y));
		return Math.min(nx, ny);
	}
	
	//{"MDP15":"IR","Images":"..."}
	//{"ID":1,"X":1,"Y":1},{"ID":1,"X":1,"Y":1},{"ID":1,"X":1,"Y":1}
	//{"MDP15":"IR","Images":"{ID:12,X:3,Y:10"}{"ID":13,"X":3,"Y":10"}{ID:8,X:3,Y:10"}{"ID":14,"X":3,"Y":10"}{ID:10,X:3,Y:10"}{"ID":11,"X":3,"Y":10"}{ID:4,X:3,Y:10"}"\"}"
	public String generateImageCoords() throws IOException {
		BufferedReader csvReader = new BufferedReader(new FileReader("C:/Users/Ether/Desktop/Y3S1/MDP Image/Image-Recognition/predictions.csv"));
		//BufferedReader csvReader = new BufferedReader(new FileReader("C:/Users/liger/OneDrive/Documents/GitHub/Image-Recognition/predictions.csv"));
		String row;
		ArrayList<String> ar = new ArrayList<String>(0);
		row = csvReader.readLine();
		while(row != null) {
			ar.add(row);
			row = csvReader.readLine();
		}
		csvReader.close();
		for (int a = 0; a < ar.size(); a++) {
			System.out.println(ar.get(a));
		}
		String s_start = "{\"MDP15\":\"IR\",\"Images\":\"";
		String s_end = "\"}";
		for (int a = 1 ; a < ar.size(); a++) {
			String[] array = ar.get(a).split(",");
			s_start = s_start.concat("(ID:"+array[3]+",X:"+array[1]+",Y:"+array[0]+")"); //Changed array pos to account for wrong dimensions
			if (a < (ar.size()-1)) {
				s_start = s_start.concat(";");
			}
		}
		String s = s_start.concat(s_end);
		return s;
	}
}
