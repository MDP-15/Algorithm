package mdpsimRobot;
import mdpsimEngine.Action2D;
import mdpsimEngine.Action2D.Action;
import mdpsimEngine.Object2D;
import mdpsimEngine.Vector2D;
import mdpsimGUI.TCPsocket;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Stack;

import mdpsim.MDPSIM;

public class Robot {
	public ArrayList<Sensor> sensors;
	public ArrayList<Double> sensorvalues;
	public double radius;
	public Object2D taggedobject;
	String fP = "";
	public MovementHandler mh;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	public ArrayList<RobotAction> actionqueue;
	//WAYPOINT INITILIASATION
	public static int way_x = 7;
	public static int way_y = 17;
	public LogicHandler lh;
	private Stack<String> actionstack;
	
	public Robot(ArrayList<Sensor> sensors, double radius) {
		this.sensors = sensors;
		this.radius = radius;
		this.sensorvalues = new ArrayList<>();
		this.actionqueue = new ArrayList<RobotAction>(0);
		this.mh = new MovementHandler(actionqueue);
		this.lh = new LogicHandler(15,20,1,1);
		lh.setUnexploredMap(18, 1, 20, 15, RobotDirection.RIGHT);
		//lh.parseMDF(MDPSIM.mdfString);
	}
	
	public void addSensor(String name,Vector2D position, Vector2D direction, double minrange, double maxrange) {
		Vector2D posnormalized = position.multiply((double)position.length(new Vector2D(0,0))/radius);
		Vector2D unitdirection = direction.unit();
		this.sensors.add(new Sensor(name,posnormalized,unitdirection, minrange, maxrange));
	}
	
	public void updateSensors(ArrayList<Double> values) {
		sensorvalues.clear();
		if (values.size() != sensors.size()) {
			System.out.println("sensor vector incorrectly sized!");
			return;
		}
		for (int a = 0; a < values.size(); a++) {
			double maxrange = sensors.get(a).maxrange();
			double minrange = sensors.get(a).minrange();
			double val = values.get(a);
			if (val >= minrange && val <= maxrange) {
				sensorvalues.add(val);
			} else if (val > maxrange){
				sensorvalues.add(null);
			} else {
				sensorvalues.add(10.0);
			}
		}
	}
	
	public void printSensor() {
		for (int a = 0; a < sensorvalues.size(); a++) {
			System.out.print(sensors.get(a).name+"\t ");
		}
		System.out.println();
		for (int a = 0; a < sensorvalues.size(); a++) {
			if(sensorvalues.get(a) == null) {
				System.out.print(sensorvalues.get(a)+"\t\t ");
			}
			else {}
		}
		System.out.println();
	}
	
	public ArrayList<Action2D> explore(double time){
		ArrayList<Action2D> actions = new ArrayList<Action2D>(0);
		if (mh.currentaction == null && mh.actionqueue.size() == 0 && (MDPSIM.mode == 1 || MDPSIM.mode == 2) && !MDPSIM.real) {
			lh.updatePos();
			lh.scan(sensorvalues);
			mh.actionqueue.add(lh.getNextAction(time));
		} else if (MDPSIM.real) {
			RobotAction r = lh.getNextAction(time);
			if (MDPSIM.real && r != null) {
				String s = r.toString();
				actionstack = new Stack<String>();
				actionstack.push(s);
				moveExplore(actionstack);
			}
		}
		actions.add(mh.doNext(time , sensorvalues)); // DO NOT TOUCH
		return actions;
	}

	public void startFastestPath() {
		System.out.println(way_x +" "+ way_y);
		//lh.parseMDF(MDPSIM.mdfString);
	    System.out.println("Doing fastestPath");
	    Node n = lh.computeFastestPath(lh.x_pos, lh.y_pos, way_x, way_y, lh.robotdir);
	    Node c = lh.computeFastestPath(way_x, way_y, 1, 13, n.rd);//Need to pass waypoint coordinates thru here
	    actionstack = new Stack<String>();
	    stackactions(c);
	    stackactions(n);
	    moveFP2(actionstack);	    
	  }
	
	public void stackactions(Node n) {
		boolean isnull = false;
	    while (!isnull) {
	      if (n != null) {
	        if (n.ra != null) {
	          String raction = n.ra.toString();
	          System.out.println(raction);
	          actionstack.push(raction);
	        }
	        if (n.prev != null) {
	          n = n.prev;
	        } else {
	          isnull = true;
	        }
	      }
	    }
	}
	
	public void moveFP(Stack<String> actionstack) {
		while (!actionstack.isEmpty()) {
		      switch (actionstack.peek()) {
		      case "F1":
		        actionqueue.add(RobotAction.F1);
		        break;
		      case "F2":
		        actionqueue.add(RobotAction.F2);
		        break;
		      case "F3":
		        actionqueue.add(RobotAction.F3);
		        break;
		      case "TL":
		        actionqueue.add(RobotAction.TL);
		        break;
		      case "TR":
		        actionqueue.add(RobotAction.TR);
		        break;
		      }
		      actionstack.pop();
		    }
	}
	
	public void moveFP2(Stack<String> actionstack) {
		while (!actionstack.isEmpty()) {
		      switch (actionstack.peek()) {
		      case "F1":
		        actionqueue.add(RobotAction.F1);
		        //TCPsocket.sendMessage("F1s");
		        fP = fP + "F1";
		        break;
		      case "F2":
		        actionqueue.add(RobotAction.F2);
		        //TCPsocket.sendMessage("F2s");
		        fP = fP + "F2";
		        break;
		      case "F3":
		        actionqueue.add(RobotAction.F3);
		        //TCPsocket.sendMessage("F3s");
		        fP = fP + "F3";
		        break;
		      case "TL":
		        actionqueue.add(RobotAction.TL);
		        //TCPsocket.sendMessage("Ls");
		        fP = fP + "L";
		        break;
		      case "TR":
		        actionqueue.add(RobotAction.TR);
		        //TCPsocket.sendMessage("Rs");
		        fP = fP + "R";
		        break;
		      }
		      actionstack.pop();
		    }
		TCPsocket.sendMessage("{\"MDP15\":\"FP\",\"FP\":\""+fP+"\"}");
	}
	
	public void moveExplore(Stack<String> actionstack) {
		while (!actionstack.isEmpty()) {
		      switch (actionstack.peek()) {
		      case "F1":
		    	  	lh.prevaction = RobotAction.F1;
		    	  	actionqueue.add(RobotAction.F1);
		    	  	System.out.println("F1 "+lh.robotdir);
		    	  	TCPsocket.sendMessage("{\"MDP15\":\"RI\",\"RI\":\"F1s\"}");
		    	  	break;
		      case "F2":
		    	  	lh.prevaction = RobotAction.F2;
		    	  	actionqueue.add(RobotAction.F2);
		    	  	System.out.println("F2 "+lh.robotdir);
		    	  	TCPsocket.sendMessage("{\"MDP15\":\"RI\",\"RI\":\"F2s\"}");
		    	  	break;
		      case "F3":
		    	  	lh.prevaction = RobotAction.F3;
		    	  	actionqueue.add(RobotAction.F3);
		    	  	System.out.println("F3 "+lh.robotdir);
		    	  	TCPsocket.sendMessage("{\"MDP15\":\"RI\",\"RI\":\"F3s\"}");
		    	  	break;
		      case "TL":
		    	  	lh.prevaction = RobotAction.TL;
		    	  	actionqueue.add(RobotAction.TL);
		    	  	System.out.println("TL "+lh.robotdir);
		    	  	TCPsocket.sendMessage("{\"MDP15\":\"RI\",\"RI\":\"Ls\"}");
		    	  	break;
		      case "TR":
		    	  	lh.prevaction = RobotAction.TR;
		    	  	actionqueue.add(RobotAction.TR);
		    	  	System.out.println("TR");
		    	  	TCPsocket.sendMessage("{\"MDP15\":\"RI\",\"RI\":\"Rs\"}");
		    	  	break;
		      case "RCA":
		    	  	actionqueue.add(RobotAction.RCA);
		    	  	System.out.println("RCA "+lh.robotdir);
		    	  	TCPsocket.sendMessage("{\"MDP15\":\"RI\",\"RI\":\"As\"}");
		    	  	break;
		      case "RCH":
		    	  	actionqueue.add(RobotAction.RCH);
		    	  	System.out.println("RCH "+lh.robotdir);
		    	  	TCPsocket.sendMessage("{\"MDP15\":\"RI\",\"RI\":\"Hs\"}");
		    	  	break;
		      }
		      actionstack.pop();
		}
	}
}
