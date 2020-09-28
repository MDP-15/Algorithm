package mdpsim;
import mdpsimEngine.Line2D;

import mdpsimGUI.*;
import mdpsimEngine.*;
import mdpsimEngine.Action2D.Action;

import java.awt.Color;
import java.lang.String;
import java.util.ArrayList;
import mdpsimRobot.*;

public class MDPSIM {
	public static boolean pause;
	public static boolean done = false;
	public static Viewer vw;
	public static String mdfString = null;
	
 static ArrayList<Action2D> actionqueue;
	public static void main(String[] args) throws InterruptedException{
		mdfString = parseFormatToMap(""); 
		vw = new Viewer("MDP Simulator", 1024, 768); //First Panel
		pause = false;
		actionqueue = new ArrayList<Action2D>(0);
		while (true) {
			inputMDF(mdfString);
		}
	}
	//Problem: Removing of panel and Updating of panel 
	//Cannot see the map
	
	//Removed vw cause not initialized in MapReader
	public static void inputMDF(String mdfString) throws InterruptedException{
		//System.out.println("MDF STRING: "+mdfString);
		String s = parseFormatToMap(mdfString);   
		ArrayList<Object2D> objects = generateMap(s);
		Engine2D phyeng = new Engine2D(objects, 0.008);
		Robot r = initializeRobot();
		vw.setVisible(true);
		updateAll(r,phyeng, vw.map1);	
		actionqueue.add(new Action2D(Action.ACCELERATE,20));
		while(true) {
				Thread.sleep(8);
				if (actionqueue.size() == 0) {
					phyeng.next(null);
				} else {
					phyeng.next(actionqueue.remove(0));
				}
				updateAll(r, phyeng, vw.map1);
				sensorUpdate(phyeng, vw.sensors);
				r.printSensor();
				actionqueue.addAll(r.policyUpdate());
				if (vw.flag == true) {
					vw.flag = false;
					break;
				}
		}
	}
	
	//initialize virtual robot object;
	public static Robot initializeRobot() {
		Robot robot = new Robot(new ArrayList<Sensor>(), 10);
		robot.addSensor(new Vector2D(7,-7), new Vector2D(10,-10), 10, 80);
		robot.addSensor(new Vector2D(-7,-7), new Vector2D(-10,-10), 10, 80);
		robot.addSensor(new Vector2D(0,-10), new Vector2D(0,-10), 10, 80);
		return robot;
	}
	
	public static ArrayList<Line> rayTrace(Robot r,Engine2D phyeng, Engine2DPanel panel) {
		double mult = (float) panel.getWidth()/(float) 150;
		ArrayList<Line> lines = new ArrayList<Line>(0);
		ArrayList<Double> sensoroutput = new ArrayList<Double>();
		Vector2D origin = phyeng.movingObjects().get(0).position();
		double angle = phyeng.movingObjects().get(0).direction().angle(new Vector2D(0,10));
		if (phyeng.movingObjects().get(0).getClass() == Vehicle2D.class) {
				for (Sensor s: r.sensors) {
					Vector2D sensororigin = origin.add(s.position().rotate(angle));
					Vector2D sensordirection = s.direction().rotate(angle);
					Vector2D vec = phyeng.rayTraceVec(sensororigin, sensordirection);
					if (vec != null) {
						sensoroutput.add(sensororigin.length(vec));
						lines.add(new Line(new VecInt(sensororigin.multiply(mult), true), new VecInt(vec.multiply(mult),true), Color.red,2));
					} else {
						sensoroutput.add(Double.POSITIVE_INFINITY);
					}
				}
		}
		r.updateSensors(sensoroutput);
		return lines;
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
		Vector2D bottomright = new Vector2D(x + 5, y + 5);
		Line2D top = new Line2D(topleft, topright);
		Line2D left = new Line2D(topleft, bottomleft);
		Line2D right = new Line2D(topright, bottomright);
		Line2D bottom = new Line2D(bottomleft, bottomright);
		Line2D diagleftright = new Line2D(topleft, bottomright);
		Line2D diagrightleft = new Line2D(topright, bottomleft);
		Object2D topobj = new Object2D(top, top.midpoint(), new Vector2D(0,0), new Vector2D(0,0),true);
		Object2D leftobj = new Object2D(left, left.midpoint(), new Vector2D(0,0), new Vector2D(0,0), true);
		Object2D rightobj = new Object2D(right, right.midpoint(), new Vector2D(0,0), new Vector2D(0,0), true);
		Object2D bottomobj = new Object2D(bottom, bottom.midpoint(), new Vector2D(0,0), new Vector2D(0,0), true);
		Object2D diagleftrightobj = new Object2D(diagleftright, diagleftright.midpoint(), new Vector2D(0,0), new Vector2D(0,0), true);
		Object2D diagrightleftobj = new Object2D(diagrightleft, diagrightleft.midpoint(), new Vector2D(0,0), new Vector2D(0,0), true);
		ArrayList<Object2D> list= new ArrayList<Object2D>();
		list.add(topobj);
		list.add(leftobj);
		list.add(rightobj);
		list.add(bottomobj);
		list.add(diagleftrightobj);
		list.add(diagrightleftobj);
		return list;
	}
	
	private static ArrayList<Object2D> generateMap(String map) {
		ArrayList<Object2D> objectmap = new ArrayList<Object2D>();
		Line2D top = new Line2D(new Vector2D(0,0), new Vector2D(200,0));
		Object2D topborder = new Object2D(top, top.midpoint(), new Vector2D(0,0), new Vector2D(0,0),true);
		Line2D left = new Line2D(new Vector2D(0,0), new Vector2D(0,150));
		Object2D leftborder = new Object2D(left, left.midpoint(), new Vector2D(0,0), new Vector2D(0,0), true);
		Line2D right = new Line2D(new Vector2D(200,0), new Vector2D(200,150));
		Object2D rightborder = new Object2D(right, right.midpoint(), new Vector2D(0,0), new Vector2D(0,0), true);
		Line2D bottom = new Line2D(new Vector2D(0,150), new Vector2D(200,150));
		Object2D bottomborder = new Object2D(bottom, bottom.midpoint(), new Vector2D(0,0), new Vector2D(0,0), true);
		objectmap.add(topborder);
		objectmap.add(leftborder);
		objectmap.add(rightborder);
		objectmap.add(bottomborder);
		Circle2D robot = new Circle2D(10);
		Vehicle2D robotobject = new Vehicle2D(robot, new Vector2D(15, 75), new Vector2D(0, 0), new Vector2D(0,0),new Vector2D(10,0),false,false,20, Math.PI/2);
		objectmap.addAll(generateXYFromBits(map));
		objectmap.add(robotobject);
		return objectmap;
	}
	
	private static void updateAll(Robot r, Engine2D phyeng, Engine2DPanel panel) {
		ArrayList<Line> lines = new ArrayList<Line>();
		double mult = (float) panel.getWidth()/ (float) 150;
		ArrayList<Circle> circles = new ArrayList<Circle>();
		for (Object2D obj : phyeng.staticObjects()) {
			Line2D ln = (Line2D) obj.object();
			VecInt start = new VecInt(ln.start().multiply(mult),true);
			VecInt end = new VecInt(ln.end().multiply(mult),true);
			Line drawedline = new Line(start,end,Color.white,3);
			lines.add(drawedline);
		}
		for (Object2D obj : phyeng.movingObjects()) {
			Circle2D circle = (Circle2D) obj.object();
			VecInt pos = new VecInt(obj.position().add(new Vector2D(-circle.radius(),-circle.radius())).multiply(mult),true);
			int diameter = (int)Math.round(2*circle.radius()*mult);
			circles.add(new Circle(pos,diameter, Color.GRAY, true));
			circles.add(new Circle(pos,diameter, Color.GREEN, false));
		}
		panel.lines = lines;
		panel.lines.addAll(rayTrace(r, phyeng,panel));
		panel.circles = circles;
		panel.repaint();
	}
	
	private static void sensorUpdate(Engine2D phyeng, SensorScreen sc) {
		double time = phyeng.time();
		long timesteps = phyeng.timestepselapsed();
		sc.update(time, timesteps);
	}
	
}
