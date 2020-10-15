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
		return;
	}
	
	public double manhattanDist(int x1, int y1, int x2, int y2) {
		return Math.abs(x1-x2)+Math.abs(y1-y2);
	}
	
	public ArrayList<Node>neighbours(int dx, int dy, ArrayList<ArrayList<Integer>> mapmemory){
		boolean en = true;
		double trscore = 1;
		double tlscore = 1;
		double f1score = 2;
		double f2score = 3;
		double f3score = 4;;
		ArrayList<Node> n = new ArrayList<Node>();
		if (rd == RobotDirection.UP) {
			//TR
			Node tr = new Node(x,y,this,RobotAction.TR,rd.TR(),score+trscore+scorer(x,y,dx,dy,en));
			if (tr.isValid(mapmemory)) { 
				n.add(tr);
			}
			//TL
			Node tl =new Node(x,y,this,RobotAction.TL,rd.TL(),score+tlscore+scorer(x,y,dx,dy,en));
			if (tl.isValid(mapmemory)) {
				n.add(tl);
			}
			//F1
			Node f1 = new Node(x-1,y,this,RobotAction.F1,rd.F1(),score+f1score+scorer(x,y,dx,dy,en));
			if (f1.isValid(mapmemory)) {
				n.add(f1);
			}
			//F2
			Node f2 = new Node(x-2,y,this,RobotAction.F2,rd.F2(),score+f2score+scorer(x,y,dx,dy,en));
			if (f2.isValid(mapmemory)) {
				n.add(f2);
			} 
			//F3
			Node f3 = new Node(x-3,y,this,RobotAction.F3,rd.F3(),score+f3score+scorer(x,y,dx,dy,en));
			if (f3.isValid(mapmemory)) {
				n.add(f3);
			} 
		} else if (rd == RobotDirection.DOWN) {
			//TR
			Node tr = new Node(x,y,this,RobotAction.TR,rd.TR(),score+trscore+scorer(x,y,dx,dy,en));
			if (tr.isValid(mapmemory)) { 
				n.add(tr);
			}
			//TL
			Node tl =new Node(x,y,this,RobotAction.TL,rd.TL(),score+tlscore+scorer(x,y,dx,dy,en));
			if (tl.isValid(mapmemory)) {
				n.add(tl);
			}
			//F1
			Node f1 = new Node(x+1,y,this,RobotAction.F1,rd.F1(),score+f1score+scorer(x,y,dx,dy,en));
			if (f1.isValid(mapmemory)) {
				n.add(f1);
			}
			//F2
			Node f2 = new Node(x+2,y,this,RobotAction.F2,rd.F2(),score+f2score+scorer(x,y,dx,dy,en));
			if (f2.isValid(mapmemory)) {
				n.add(f2);
			} 
			//F3
			Node f3 = new Node(x+3,y,this,RobotAction.F3,rd.F3(),score+f3score+scorer(x,y,dx,dy,en));
			if (f3.isValid(mapmemory)) {
				n.add(f3);
			} 
		} else if (rd == RobotDirection.LEFT) {
			//TR
			Node tr = new Node(x,y,this,RobotAction.TR,rd.TR(),score+trscore+scorer(x,y,dx,dy,en));
			if (tr.isValid(mapmemory)) { 
				n.add(tr);
			}
			//TL
			Node tl =new Node(x,y,this,RobotAction.TL,rd.TL(),score+tlscore+scorer(x,y,dx,dy,en));
			if (tl.isValid(mapmemory)) {
				n.add(tl);
			}
			//F1
			Node f1 = new Node(x,y-1,this,RobotAction.F1,rd.F1(),score+f1score+scorer(x,y,dx,dy,en));
			if (f1.isValid(mapmemory)) {
				n.add(f1);
			}
			//F2
			Node f2 = new Node(x,y-2,this,RobotAction.F2,rd.F2(),score+f2score+scorer(x,y,dx,dy,en));
			if (f2.isValid(mapmemory)) {
				n.add(f2);
			} 
			//F3
			Node f3 = new Node(x,y-3,this,RobotAction.F3,rd.F3(),score+f3score+scorer(x,y,dx,dy,en));
			if (f3.isValid(mapmemory)) {
				n.add(f3);
			} 
		} else if (rd == RobotDirection.RIGHT) {
			//TR
			Node tr = new Node(x,y,this,RobotAction.TR,rd.TR(),score+trscore+scorer(x,y,dx,dy,en));
			if (tr.isValid(mapmemory)) { 
				n.add(tr);
			}
			//TL
			Node tl =new Node(x,y,this,RobotAction.TL,rd.TL(),score+tlscore+scorer(x,y,dx,dy,en));
			if (tl.isValid(mapmemory)) {
				n.add(tl);
			}
			//F1
			Node f1 = new Node(x,y+1,this,RobotAction.F1,rd.F1(),score+f1score+scorer(x,y,dx,dy,en));
			if (f1.isValid(mapmemory)) {
				n.add(f1);
			}
			//F2
			Node f2 = new Node(x,y+2,this,RobotAction.F2,rd.F2(),score+f2score  +scorer(x,y,dx,dy,en));
			if (f2.isValid(mapmemory)) {
				n.add(f2);
			} 
			//F3
			Node f3 = new Node(x,y+3,this,RobotAction.F3,rd.F3(),score+f3score+scorer(x,y,dx,dy,en));
			if (f3.isValid(mapmemory)) {
				n.add(f3);
			} 
		}
		return n;
	}

	public double scorer(int x, int y, int dx, int dy, boolean enable) {
		if(enable) {
			return manhattanDist(x,y,dx,dy);
		} else {
			return wallBonus(x,y);
		}
	}
	
	public boolean isValid(ArrayList<ArrayList<Integer>> mapmemory) {
		int mapx = 0;
		int mapy = 0;
		boolean verbose = false;
		if (verbose) {
			System.out.println(x+" "+y);
			System.out.print(mapValid(x-1	,y+1	,mapmemory));
			System.out.print(mapValid(x-1	,y		,mapmemory));
			System.out.println(mapValid(x-1	,y-1	,mapmemory));
			System.out.print(mapValid(x		,y+1	,mapmemory));
			System.out.print(mapValid(x		,y		,mapmemory));
			System.out.println(mapValid(x	,y-1	,mapmemory));
			System.out.print(mapValid(x+1	,y+1	,mapmemory));
			System.out.print(mapValid(x+1	,y		,mapmemory));
			System.out.println(mapValid(x+1	,y-1	,mapmemory));
			System.out.println();
		}
		if (	mapValid(x+1	,y+1	,mapmemory)
			&&	mapValid(x		,y+1	,mapmemory)
			&&	mapValid(x-1	,y+1	,mapmemory)
			&&	mapValid(x+1	,y		,mapmemory)
			&&	mapValid(x		,y		,mapmemory)
			&&	mapValid(x-1	,y		,mapmemory)
			&&	mapValid(x+1	,y-1	,mapmemory)
			&&	mapValid(x		,y-1	,mapmemory)
			&&	mapValid(x-1	,y-1	,mapmemory)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean mapValid(int xpos, int ypos, ArrayList<ArrayList<Integer>> mapmemory) {
		boolean verbose = false;
		if (verbose) {
			for (int a = 0; a < mapmemory.size(); a++) {
				for (int b = 0; b < mapmemory.get(0).size(); b++) {
					System.out.print(mapmemory.get(a).get(b));
				}
				System.out.println();
			}
			System.out.println();
		}
		try { 
			int a = mapmemory.get(xpos).get(ypos);
			if (a != 0) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean is(Node n) {
		if (x == n.x && y == n.y && rd == n.rd) {
			return true;
		}
		return false;
	}
	
	public boolean isCell(Node n) {
		if (x == n.x && y == n.y) {
			return true;
		}
		return false;
	}
	
	public void print() {
		System.out.println("X: " + x+ "\tY: "+y+"\tRA: "+ra+"\tRD: "+rd+" Score: "+score);
		System.out.println();
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
	
	public double wallBonus(int x, int y) {
		double xn = (double)Math.min(Math.abs(20-x), Math.abs(0-x));
		double yn = (double)Math.min(Math.abs(15-y), Math.abs(0-y));
		return Math.min(xn, yn);

	}
}
