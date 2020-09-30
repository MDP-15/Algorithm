package mdpsim;
import mdpsimEngine.Line2D;

import mdpsimGUI.*;
import mdpsimEngine.*;
import mdpsimEngine.Action2D.Action;

import java.awt.Color;
import java.lang.String;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Timer;

import mdpsimRobot.*;

public class MDPSIM {
	public static boolean pause;
	public static boolean done = false;
	public static Viewer vw;
	public static String mdfString = null;
	public static double timeres = 1;
	public static Vector2D north = new Vector2D(0,10);
	public static int simspeed = 1;
	public static double reftime;
	private static Clock t;
	private static long t0;
	
 static ArrayList<Action2D> actionqueue;
	public static void main(String[] args) throws InterruptedException{
		mdfString = parseFormatToMap("000000000000000000000000000000000000010000000000000000000000000000000000000000000000001110111111000000000000000000000000000000000000000000000000010000000000000000000000001110000000000000000000000000000000000000010000000000000000000000001110000000000000000000000001000000000000000000000000000000000000"); 
		vw = new Viewer("MDP Simulator", 1024, 768); //First Panel
		pause = false;
		actionqueue = new ArrayList<Action2D>(0);
		while (true) {
			inputMDF();
		}
	}
	//Problem: Removing of panel and Updating of panel 
	//Cannot see the map
	
	//Removed vw cause not initialized in MapReader
	public static void inputMDF() throws InterruptedException{
		//System.out.println("MDF STRING: "+mdfString);
		t = Clock.systemDefaultZone();
		t0 = t.millis();
		String s = parseFormatToMap(mdfString);   
		ArrayList<Object2D> objects = generateMap(s);
		Engine2D phyeng = new Engine2D(objects, (double)timeres/1000);
		Robot r = initializeRobot();
		vw.setVisible(true);
		updateEngine2DPanel(r,phyeng, vw.map1);	
		updateRobot2DPanel(phyeng, r, vw.map2);
		while(MovementHandler.moving == false) { //can put true
				try {
					while((phyeng.time()-reftime)*1000 >= ((double)(t.millis()-t0)/simspeed)) {
						NOP();
					}
				} catch (Exception e) {}
				if (actionqueue.size() == 0) {
					phyeng.next(null);
				} else {
					phyeng.next(actionqueue.remove(0));
				}
				updateEngine2DPanel(r, phyeng, vw.map1);
				updateRobot2DPanel(phyeng, r, vw.map2);
				sensorUpdate(phyeng, vw.sensors, r);
				actionqueue.addAll(r.getNextAction(phyeng.time()));
				r.robotExploration();
				// flag handler functions
				if (vw.engineresetflag == true) {
					vw.engineresetflag = false;
					break;
				}
				if (vw.enginespeedflag == true) {
					vw.enginespeedflag = false;
					reftime = phyeng.time();
					t0 = t.millis();
					simspeed = vw.enginespeed;

				}
				if (vw.custommdfresetflag == true) {
					vw.custommdfresetflag = false;
					mdfString = parseFormatToMap(vw.mdfstring);
					break;
				}
		}
	}
	
	private static void NOP(){
		assert true;
	}
	
	private static boolean flagHandler() {
		if (vw.engineresetflag == true) {
			vw.engineresetflag = false;
			return true;
		}
		if (vw.enginespeedflag == true) {
			vw.enginespeedflag = false;
			simspeed = vw.enginespeed;

		}
		if (vw.custommdfresetflag == true) {
			vw.custommdfresetflag = false;
			mdfString = parseFormatToMap(vw.mdfstring);
			return true;
		}
		return false;
	}
	//initialize virtual robot object;
	public static Robot initializeRobot() {
		Robot robot = new Robot(new ArrayList<Sensor>(), 10);
		robot.addSensor("RIGHT_FRONT(O)",new Vector2D(0,9), new Vector2D(-10,0),10, 30); //0
		robot.addSensor("RIGHT_BACK(1)",new Vector2D(0,-9), new Vector2D(-10,0),10, 30); //1
		robot.addSensor("FRONT_RIGHT(2)",new Vector2D(-7,7), new Vector2D(0,10), 10, 30); //2
		robot.addSensor("FRONT_MIDDLE(3)",new Vector2D(0,9), new Vector2D(0,10), 10, 30); //3
		robot.addSensor("FRONT_LEFT(4)",new Vector2D(7,7), new Vector2D(0,10), 10, 30); //4
		robot.addSensor("LEFT_LONG(5)",new Vector2D(0,9), new Vector2D(10,0), 20, 50);//5
		return robot;
	}
	
	public static ArrayList<Line> rayTrace(Robot r,Engine2D phyeng, Engine2DPanel panel) {
		double mult = (float) panel.getWidth()/(float) 150;
		ArrayList<Line> lines = new ArrayList<Line>(0);
		ArrayList<Double> sensoroutput = new ArrayList<Double>();
		Vector2D origin = phyeng.movingObjects().get(0).position();
		double angle = phyeng.movingObjects().get(0).direction().angle(north);
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
		Vector2D topleft = new Vector2D(y - 5, x - 5);
		Vector2D topright = new Vector2D(y - 5, x + 5);
		Vector2D bottomleft = new Vector2D(y + 5, x - 5);
		Vector2D bottomright = new Vector2D(y + 5, x + 5);
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
		Line2D top = new Line2D(new Vector2D(0,0), new Vector2D(0,200));
		Object2D topborder = new Object2D(top, top.midpoint(), new Vector2D(0,0), new Vector2D(0,0),true);
		Line2D left = new Line2D(new Vector2D(0,0), new Vector2D(150,0));
		Object2D leftborder = new Object2D(left, left.midpoint(), new Vector2D(0,0), new Vector2D(0,0), true);
		Line2D right = new Line2D(new Vector2D(0,200), new Vector2D(150,200));
		Object2D rightborder = new Object2D(right, right.midpoint(), new Vector2D(0,0), new Vector2D(0,0), true);
		Line2D bottom = new Line2D(new Vector2D(150,0), new Vector2D(150,200));
		Object2D bottomborder = new Object2D(bottom, bottom.midpoint(), new Vector2D(0,0), new Vector2D(0,0), true);
		objectmap.add(topborder);
		objectmap.add(leftborder);
		objectmap.add(rightborder);
		objectmap.add(bottomborder);
		Circle2D robot = new Circle2D(10);
		Vehicle2D robotobject = new Vehicle2D(robot, new Vector2D(15, 185), new Vector2D(0,0), new Vector2D(0,0), new Vector2D(-10,0));
		objectmap.addAll(generateXYFromBits(map));
		objectmap.add(robotobject);
		return objectmap;
	}
	
	private static void updateEngine2DPanel(Robot r, Engine2D phyeng, Engine2DPanel panel) {
		ArrayList<Line> lines = new ArrayList<Line>();
		double mult = (float) panel.getWidth()/ (float) 150;
		ArrayList<Circle> circles = new ArrayList<Circle>();
		for (Object2D obj : phyeng.staticObjects()) {
			Line drawedline;
			Line2D ln = (Line2D) obj.object();
			VecInt start = new VecInt(ln.start().multiply(mult),true);
			VecInt end = new VecInt(ln.end().multiply(mult),true);
			if (ln.flag == true) {
				drawedline = new Line(start,end,Color.green,3);
			} else {
				drawedline = new Line(start,end,Color.white,3);
			}
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
	
	private static void sensorUpdate(Engine2D phyeng, SensorScreen sc, Robot r) {
		double time = phyeng.time();
		long timesteps = phyeng.timestepselapsed();
		sc.update(time, timesteps, r.sensors, r.sensorvalues);
	}
	
	
	public static void updateRobot2DPanel(Engine2D phyeng, Robot r, Robot2DPanel rpanel) {
		rpanel.setMap(r.lh.mapmemory);
		ArrayList<Circle> circles = new ArrayList<Circle>();
		double mult = (float) rpanel.getWidth()/ (float) 150;

		for (Object2D obj : phyeng.movingObjects()) {
			Circle2D circle = (Circle2D) obj.object();
			VecInt pos = new VecInt(obj.position().add(new Vector2D(-circle.radius(),-circle.radius())).multiply(mult),true);
			int diameter = (int)Math.round(2*circle.radius()*mult);
			circles.add(new Circle(pos,diameter, Color.BLACK, true));
			circles.add(new Circle(pos,diameter, Color.WHITE, false));
		}
		rpanel.circles = circles;
		rpanel.repaint();
	}
	
}
