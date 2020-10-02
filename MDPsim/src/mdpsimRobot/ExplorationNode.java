package mdpsimRobot;

public class ExplorationNode extends Node{
	public double informationgain;
	
	public ExplorationNode (int x, int y, Node prev, RobotAction ra, RobotDirection rd, double score, double gain) {
		super(x,y,prev,ra,rd,score);
		constructor(gain);
	}
	
	public ExplorationNode (Node n, double gain) {
		super(n.x,n.y,n.prev,n.ra,n.rd,n.score);
		constructor(gain);
	}
	
	public void constructor(double informationgain) {
		this.informationgain = informationgain;
	}
	
	@Override
	public void print() {
		ExplorationNode n = this;
		while (n != null) {
			System.out.print("Information Gain: " + informationgain+ " ");
			super.print();
			n = (ExplorationNode) super.prev;
		}
	}
}
