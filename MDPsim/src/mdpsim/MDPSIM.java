package mdpsim;
import mdpsimEngine.Line2D;

import mdpsimGUI.*;
import mdpsimEngine.*;
import mdpsimEngine.Action2D.Action;

import java.awt.Color;
import java.io.IOException;
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
	public static Robot robot;
	public static int mode; // 1:explore 2: return to base after explore 3: fastest path
	public static double coverage;
	public static int timelimit;
	public static boolean real=false;;
	public static ArrayList<Double> realsensors;
	public static boolean sensorflag;
	public static boolean nextmessage;
	
 static ArrayList<Action2D> actionqueue;
	public static void main(String[] args) throws InterruptedException, IOException{
		coverage = 1.0;
		sensorflag = false;
		realsensors = new ArrayList<Double>();
		mode = 0;
		real = true;
		mdfString = parseFormatToMap(""); 
		vw = new Viewer("MDP Simulator", 1024, 768); //First Panel
		pause = false;
		actionqueue = new ArrayList<Action2D>(0);
		while (true) {
			inputMDF(real);
		}
	}
	//Problem: Removing of panel and Updating of panel 
	//Cannot see the map
	
	//Removed vw cause not initialized in MapReader
	public static void inputMDF(boolean real) throws InterruptedException, IOException{
		//System.out.println("MDF STRING: "+mdfString);
		if (!real) {
			t = Clock.systemDefaultZone();
			timelimit = 0;
			t0 = t.millis();
			String s = parseFormatToMap(mdfString);   
			ArrayList<Object2D> objects = generateMap(s);
			Engine2D phyeng = new Engine2D(objects, (double)timeres/1000);
			Robot r = initializeRobot();
			vw.setVisible(true);
			updateEngine2DPanel(r,phyeng, vw.map1);	
			updateFakeRobot2DPanel(phyeng, r, vw.map2);
			while(!real) { //can put true
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
					updateFakeRobot2DPanel(phyeng, r, vw.map2);
					sensorUpdate(phyeng, vw.sensors, r);
					//vw.sensors.updateMDF(convertMDFHex());
					if (mode == 1 || mode == 2 || mode == 3) {
						actionqueue.addAll(r.explore(phyeng.time()));
					}
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
						vw.enginespeedflag = true;
						vw.custommdfresetflag = false;
						mdfString = parseFormatToMap(vw.mdfstring);
						break;
					}
					if (vw.fpflag == true) {
						vw.fpflag = false;
						break;
					}
					if (vw.coverageflag == true) {
						vw.coverageflag = false;
						coverage = Double.parseDouble(vw.cp.explorePercent.getText());
						mode = 1;
					}
					if (vw.timeflag == true) {
						vw.timeflag = false;
						r.lh.timelimit = phyeng.time()+Double.parseDouble(vw.cp.userTime.getText());
						mode = 1;
					}
			}
		} else {
			TCPsocket.tcpSocket();
			Robot r = initializeRobot();
			r.lh.setRobotDirection(RobotDirection.RIGHT);
			r.lh.updatePosition(18, 1);
			r.lh.initPos(18, 1);
			r.lh.timelimit = Double.POSITIVE_INFINITY;
			updateRealRobot2DPanel(r, vw.map2);
			vw.setVisible(true);
			while (real) {	
				System.out.print("");
				if(sensorflag == true) {
						r.lh.updatePos();
						r.lh.scan(realsensors);
						String mdf = r.lh.reverseMdf();
						TCPsocket.sendMessage("{\"MDP15\":\"MDF\",\"MDF\":\""+mdf+"\"}");
						updateRealRobot2DPanel(r, vw.map2);
						r.sensorvalues = realsensors;
						r.explore(0.0);
					updateRealRobot2DPanel(r, vw.map2);
					sensorflag = false;
				}
			}
		}
	}
	
	private static void NOP(){
		assert true;
	}
	
	//initialize virtual robot object;
	public static Robot initializeRobot() {
		robot = new Robot(new ArrayList<Sensor>(), 10);
		robot.addSensor("RIGHT_FRONT(O)",new Vector2D(0,9), new Vector2D(-10,0),10, 30); //0
		robot.addSensor("RIGHT_BACK(1)",new Vector2D(0,-9), new Vector2D(-10,0),10, 30); //1
		robot.addSensor("FRONT_RIGHT(2)",new Vector2D(-7,7), new Vector2D(0,10), 10, 30); //2
		robot.addSensor("FRONT_MIDDLE(3)",new Vector2D(0,9), new Vector2D(0,10), 10, 30); //3
		robot.addSensor("FRONT_LEFT(4)",new Vector2D(7,7), new Vector2D(0,10), 10, 30); //4
		robot.addSensor("LEFT_LONG(5)",new Vector2D(0,9), new Vector2D(10,0), 20, 55);//5
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
		sc.update(time, timesteps, r.sensors, r.sensorvalues,r.mh.currentaction,r.mh.actionqueue);
	}
	
	
	public static void updateFakeRobot2DPanel(Engine2D phyeng, Robot r, Robot2DPanel rpanel) {
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
	
	public static void updateRealRobot2DPanel(Robot r, Robot2DPanel rpanel) {
		rpanel.setMap(r.lh.mapmemory);
		ArrayList<Circle> circles = new ArrayList<Circle>();
		double mult = (float) rpanel.getWidth()/ (float) 10;
		int x = r.lh.x_pos;
		int y = r.lh.y_pos;
		int dia = 20;
		//int rad = (int) Math.floor(dia/2);
		VecInt vec = new VecInt(((int)(x*mult)), ((int)(y*mult)));
		circles.add(new Circle(vec, dia, Color.BLACK,true));
		rpanel.circles = circles;
		rpanel.repaint();
	}
	
	private static String convertMDFHex() {
		//String mdf = robot.lh.reverseMdf();
		String mdf = robot.lh.getMdf();
		mdf = convertMDF3(mdf);
        int decimal;
        String hexStr = "";
        String mdffour = "";
        String mdfString = "";

        for(int i = 0; i < mdf.length(); i = i + 4){
            mdffour = "";
            hexStr = "";
            for(int k = 0; k < 4 ; k++){
                mdffour = mdffour + mdf.charAt(i+k);
            }
            decimal = Integer.parseInt(mdffour,2);
            hexStr = Integer.toString(decimal,16);
            mdfString = mdfString + hexStr;
        }
        return mdfString;
    }
	
	private static String convertMDF1(String mdf) {
        String MDF1 = mdf;
        MDF1 = MDF1.replace("1", "1");
        MDF1 = MDF1.replace("0", "1");
        MDF1 = MDF1.replace("2", "0");
        MDF1 = "11" + MDF1 + "11";
        //MDF1 = convertMDFHex(MDF1);
        return MDF1;
    }
	
	private static String convertMDF2(String mdf) {
        String MDF2 = mdf;
        MDF2 = MDF2.replace("2","");
        while(MDF2.length()%8 != 0) {
            MDF2 = MDF2 +"0";
        }
        return MDF2;

    }
	
    private static String convertMDF3(String mdf) {
        String MDF3 = mdf;
        MDF3 = MDF3.replace("2","0");
        
        return MDF3;
    }
	
	public static String convertMDF(String s) {
		String ret = "";
		s = s.replaceAll("2", "0");
		for (int a = 74; a >= 0; a--) {
			String sub = s.substring(a*4,(a+1)*4);
			switch(sub) {
				case "0000":
					ret = ret.concat("0");
					break;
				case "0001":
					ret = ret.concat("1");
					break;
				case "0010":
					ret = ret.concat("2");
					break;
				case "0011":
					ret = ret.concat("3");
					break;
				case "0100":
					ret = ret.concat("4");
					break;
				case "0101":
					ret = ret.concat("5");
					break;
				case "0110":
					ret = ret.concat("6");
					break;
				case "0111":
					ret = ret.concat("7");
					break;
				case "1000":
					ret = ret.concat("8");
					break;
				case "1001":
					ret = ret.concat("9");
					break;
				case "1010":
					ret = ret.concat("A");
					break;
				case "1011":
					ret = ret.concat("B");
					break;
				case "1100":
					ret = ret.concat("C");
					break;
				case "1101":
					ret = ret.concat("D");
					break;
				case "1110":
					ret = ret.concat("E");
					break;
				case "1111":
					ret = ret.concat("F");
					break;
			}	
		}
		return ret;
	}
	public static void updateRealEngine2DPanel(Robot r, Engine2DPanel rpanel) {
		rpanel.lines = new ArrayList<Line>();
		rpanel.circles = new ArrayList<Circle>();
		rpanel.repaint();
	}
}
