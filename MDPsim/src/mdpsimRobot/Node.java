package mdpsimRobot;

import java.util.ArrayList;

public class Node {
	public int x;
	public int y;
	public Node prev;
	public RobotAction ra;
	public RobotDirection rd;
	public double score;
	
	public Node (int x, int y, Node prev, RobotAction ra, RobotDirection rd, double score) {
		this.x =x;
		this.y =y;
		this.prev = prev;
		this.ra = ra;
		this.rd = rd;
		this.score = score;
	}
	
	public ArrayList<Node>neighbours(ArrayList<ArrayList<Integer>> mapmemory){
		ArrayList<Node> n = new ArrayList<Node>();
		if (rd == RobotDirection.UP) {
			//TR
			Node tr = new Node(x,y,this,RobotAction.TR,rd.TR(),score+1);
			if (tr.isValid(mapmemory)) { 
				n.add(tr);
			}
			//TL
			Node tl =new Node(x,y,this,RobotAction.TL,rd.TL(),score+1);
			if (tl.isValid(mapmemory)) {
				n.add(tl);
			}
			//F1
			Node f1 = new Node(x-1,y,this,RobotAction.F1,rd.F1(),score+1);
			if (f1.isValid(mapmemory)) {
				n.add(f1);
			}
			//F2
			Node f2 = new Node(x-2,y,this,RobotAction.F2,rd.F2(),score+1);
			if (f2.isValid(mapmemory)) {
				n.add(f2);
			} 
			//F3
			Node f3 = new Node(x-3,y,this,RobotAction.F3,rd.F3(),score+1);
			if (f3.isValid(mapmemory)) {
				n.add(f3);
			} 
		} else if (rd == RobotDirection.DOWN) {
			//TR
			Node tr = new Node(x,y,this,RobotAction.TR,rd.TR(),score+1);
			if (tr.isValid(mapmemory)) { 
				n.add(tr);
			}
			//TL
			Node tl =new Node(x,y,this,RobotAction.TL,rd.TL(),score+1);
			if (tl.isValid(mapmemory)) {
				n.add(tl);
			}
			//F1
			Node f1 = new Node(x+1,y,this,RobotAction.F1,rd.F1(),score+1);
			if (f1.isValid(mapmemory)) {
				n.add(f1);
			}
			//F2
			Node f2 = new Node(x+2,y,this,RobotAction.F2,rd.F2(),score+1);
			if (f2.isValid(mapmemory)) {
				n.add(f2);
			} 
			//F3
			Node f3 = new Node(x+3,y,this,RobotAction.F3,rd.F3(),score+1);
			if (f3.isValid(mapmemory)) {
				n.add(f3);
			} 
		} else if (rd == RobotDirection.LEFT) {
			//TR
			Node tr = new Node(x,y,this,RobotAction.TR,rd.TR(),score+1);
			if (tr.isValid(mapmemory)) { 
				n.add(tr);
			}
			//TL
			Node tl =new Node(x,y,this,RobotAction.TL,rd.TL(),score+1);
			if (tl.isValid(mapmemory)) {
				n.add(tl);
			}
			//F1
			Node f1 = new Node(x,y-1,this,RobotAction.F1,rd.F1(),score+1);
			if (f1.isValid(mapmemory)) {
				n.add(f1);
			}
			//F2
			Node f2 = new Node(x,y-2,this,RobotAction.F2,rd.F2(),score+1);
			if (f2.isValid(mapmemory)) {
				n.add(f2);
			} 
			//F3
			Node f3 = new Node(x,y-3,this,RobotAction.F3,rd.F3(),score+1);
			if (f3.isValid(mapmemory)) {
				n.add(f3);
			} 
		} else if (rd == RobotDirection.RIGHT) {
			//TR
			Node tr = new Node(x,y,this,RobotAction.TR,rd.TR(),score+1);
			if (tr.isValid(mapmemory)) { 
				n.add(tr);
			}
			//TL
			Node tl =new Node(x,y,this,RobotAction.TL,rd.TL(),score+1);
			if (tl.isValid(mapmemory)) {
				n.add(tl);
			}
			//F1
			Node f1 = new Node(x,y+1,this,RobotAction.F1,rd.F1(),score+1);
			if (f1.isValid(mapmemory)) {
				n.add(f1);
			}
			//F2
			Node f2 = new Node(x,y+2,this,RobotAction.F2,rd.F2(),score+1);
			if (f2.isValid(mapmemory)) {
				n.add(f2);
			} 
			//F3
			Node f3 = new Node(x,y+3,this,RobotAction.F3,rd.F3(),score+1);
			if (f3.isValid(mapmemory)) {
				n.add(f3);
			} 
		}
		return n;
	}
	
	public boolean isValid(ArrayList<ArrayList<Integer>> mapmemory) {
		int mapx = 0;
		int mapy = 0;
		try {
			mapx = mapmemory.size();
		} catch (Exception e) {}
		try {
			mapy = mapmemory.get(0).size();
		} catch (Exception e) {}
		if (x < 0 || x > mapx || y < 0 || y >mapy) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean is(Node n) {
		if (x == n.x && y == n.y) {
			return true;
		}
		return false;
	}
	
	public void print() {
		System.out.println("X: " + x+ "\tY: "+y+"\tRA: "+ra+"\tRD: "+rd+" Score: "+score);
	}
	
	public void printParents() {
		Node n = this;
		boolean isnull = false;
		while (!isnull) {
			n.print();
			if (n.prev != null) {
				n = n.prev;
			} else {
				isnull = true;
			}
		}
	}
}
