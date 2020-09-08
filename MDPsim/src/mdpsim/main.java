package mdpsim;
import mdpsimEngine.Line2D;
import mdpsimGUI.*;
import mdpsimEngine.*;

import java.awt.Color;
import java.lang.String;
import java.util.ArrayList;

public class main {
	public static void main(String[] args){
		String s = parseFormatToMap("011");
		ArrayList<Object2D> objects = generateMap(s);
		Engine2D phyeng = new Engine2D(objects, 0.016);
		Viewer vw = new Viewer("MDP Simulator", 1024, 768);
		vw.setVisible(true);
		updateAll(phyeng, vw.map1);
	}

	private static String parseFormatToMap(String b) {
		int length = 300 - b.length();
		String s = "";
		for (int a = 0; a < length; a++) {
			s = s.concat("0");
		}
		return s.concat(b);
	}
	private static ArrayList<Object2D> generateXYFromBits(String s) {
		ArrayList<Object2D> objects = new ArrayList<Object2D>();
		for (int x = 0; x < 20; x++) {
			for (int y = 0; y < 15; y++) {
				if (s.charAt((x*15)+y) == '1'){
					objects.addAll(generateLinesAsSquare((x*10)+5,(y*10)+5));
				}
			}
		}
		return objects;
	}
	
	private static ArrayList<Object2D> generateLinesAsSquare(double x, double y) {
		Vector2D topleft = new Vector2D(x - 5, y - 5);
		Vector2D topright = new Vector2D(x - 5, y + 5);
		Vector2D bottomleft = new Vector2D(x + 5, y - 5);
		Vector2D bottomright = new Vector2D(x +5, y + 5);
		Line2D top = new Line2D(topleft, topright);
		Line2D left = new Line2D(topleft, bottomleft);
		Line2D right = new Line2D(topright, bottomright);
		Line2D bottom = new Line2D(bottomleft, bottomright);
		Object2D topobj = new Object2D(top, top.midpoint(), new Vector2D(0,0), true);
		Object2D leftobj = new Object2D(left, left.midpoint(), new Vector2D(0,0), true);
		Object2D rightobj = new Object2D(right, right.midpoint(), new Vector2D(0,0), true);
		Object2D bottomobj = new Object2D(bottom, bottom.midpoint(), new Vector2D(0,0), true);
		ArrayList<Object2D> list= new ArrayList<Object2D>();
		list.add(topobj);
		list.add(leftobj);
		list.add(rightobj);
		list.add(bottomobj);
		return list;
	}
	
	private static ArrayList<Object2D> generateMap(String map) {
		ArrayList<Object2D> objectmap = new ArrayList<Object2D>();
		Line2D top = new Line2D(new Vector2D(0,0), new Vector2D(200,0));
		Object2D topborder = new Object2D(top, top.midpoint(), new Vector2D(0,0), true);
		Line2D left = new Line2D(new Vector2D(0,0), new Vector2D(0,150));
		Object2D leftborder = new Object2D(left, left.midpoint(), new Vector2D(0,0), true);
		Line2D right = new Line2D(new Vector2D(200,0), new Vector2D(200,150));
		Object2D rightborder = new Object2D(right, right.midpoint(), new Vector2D(0,0), true);
		Line2D bottom = new Line2D(new Vector2D(0,150), new Vector2D(200,150));
		Object2D bottomborder = new Object2D(bottom, bottom.midpoint(), new Vector2D(0,0), true);
		objectmap.add(topborder);
		objectmap.add(leftborder);
		objectmap.add(rightborder);
		objectmap.add(bottomborder);
		Circle2D robot = new Circle2D(12.5);
		Object2D robotobject = new Object2D(robot, new Vector2D(15, 15), new Vector2D(10, 10), false);
		objectmap.addAll(generateXYFromBits(map));
		objectmap.add(robotobject);
		return objectmap;
	}
	
	private static void updateAll(Engine2D phyeng, Panel panel) {
		ArrayList<Line> lines = new ArrayList<Line>();
		double mult = 339/150;
		System.out.println(panel.getHeight());
		ArrayList<Circle> circles = new ArrayList<Circle>();
		for (Object2D obj : phyeng.staticObjects()) {
			Line2D ln = (Line2D) obj.object();
			VecInt start = new VecInt(ln.start().multiply(mult),true);
			VecInt end = new VecInt(ln.end().multiply(mult),true);
			Line drawedline = new Line(start,end,Color.green);
			lines.add(drawedline);
		}
		for (Object2D obj : phyeng.movingObjects()) {
			Circle2D circle = (Circle2D) obj.object();
			VecInt pos = new VecInt(obj.position().add(new Vector2D(-circle.radius(),-circle.radius())).multiply(mult),true);
			int diameter = (int)Math.round(2*circle.radius()*mult);
			circles.add(new Circle(pos,diameter, Color.black));
		}
		panel.lines = lines;
		panel.circles = circles;
		panel.repaint();
	}
	
	private static void update(Engine2D phyeng, Panel panel) {
		ArrayList<Circle> circles = new ArrayList<Circle>();
		double mult = 339/150;
		for (Object2D obj : phyeng.movingObjects()) {
			Circle2D circle = (Circle2D) obj.object();
			VecInt pos = new VecInt(obj.position().add(new Vector2D(-circle.radius(),-circle.radius())).multiply(mult),true);
			int diameter = (int)Math.round(2*circle.radius()*mult);
			circles.add(new Circle(pos,diameter, Color.black));
		}
		panel.circles = circles;
		panel.repaint();
	}
}
