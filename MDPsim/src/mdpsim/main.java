package mdpsim;
import mdpsimEngine.Line2D;
import mdpsimGUI.*;
import mdpsimEngine.*;
import java.lang.String;
import java.util.ArrayList;

import javax.swing.JFrame;

public class main {
	public static void main(String[] args) {
		String s = parseFormatToMap(0);
		System.out.println(s);
		ArrayList<Object2D> objects = generateMap(s);
		Engine2D phyeng = new Engine2D(objects, 0.1);
		JFrameGraphics frame = new JFrameGraphics();
	}

	private static String parseFormatToMap(int b) {
		return String.format("%300s", Integer.toBinaryString(b)).replace(" ", "0");
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
		Object2D bottomborder = new Object2D(left, bottom.midpoint(), new Vector2D(0,0), true);
		objectmap.add(topborder);
		objectmap.add(leftborder);
		objectmap.add(rightborder);
		objectmap.add(bottomborder);
		return objectmap;
	}
}
