package mdpsimRobot;

import mdpsimEngine.Action2D;
import mdpsimEngine.Action2D.Action;
import mdpsimEngine.Object2D;
import mdpsimEngine.Vector2D;
import java.util.ArrayList;

public class Robot {
	public ArrayList<Sensor> sensors;
	public ArrayList<Double> sensorvalues;
	public double radius;
	public Object2D taggedobject;
<<<<<<< Updated upstream
	
=======
	public MovementHandler mh;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	public ArrayList<RobotAction> actionqueue;
	public LogicHandler lh;
	public static boolean oneMovement = false;

>>>>>>> Stashed changes
	public Robot(ArrayList<Sensor> sensors, double radius) {
		this.sensors = sensors;
		this.radius = radius;
		this.sensorvalues = new ArrayList<>();
<<<<<<< Updated upstream
=======
		this.actionqueue = new ArrayList<RobotAction>(0);
<<<<<<< Updated upstream
<<<<<<< Updated upstream
		this.actionqueue.add(RobotAction.TL);
		this.actionqueue.add(RobotAction.TL);
		this.actionqueue.add(RobotAction.TR);
		this.actionqueue.add(RobotAction.TR);
=======
//		actionqueue.add(RobotAction.TL);
=======
//		actionqueue.add(RobotAction.TL); //STARTING ORIENTATION OF THE ROBOT
>>>>>>> Stashed changes
//		actionqueue.add(RobotAction.F1);
//		actionqueue.add(RobotAction.F1);
//		actionqueue.add(RobotAction.F1);
//		actionqueue.add(RobotAction.F1);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.TR);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.F2);
//		actionqueue.add(RobotAction.TL);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.F2);
//		actionqueue.add(RobotAction.TL);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.TR);
//		actionqueue.add(RobotAction.TR);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.F1);
//		actionqueue.add(RobotAction.TL);
//		actionqueue.add(RobotAction.F2);
//		actionqueue.add(RobotAction.TR);
//		actionqueue.add(RobotAction.F3);
//		actionqueue.add(RobotAction.TR);
//		actionqueue.add(RobotAction.TR);
//		actionqueue.add(RobotAction.TR);
//		actionqueue.add(RobotAction.TL);
//		actionqueue.add(RobotAction.TL);
>>>>>>> Stashed changes
		this.mh = new MovementHandler(actionqueue);
<<<<<<< Updated upstream
>>>>>>> Stashed changes
	}
	
	public void addSensor(Vector2D position, Vector2D direction, double minrange, double maxrange) {
		Vector2D posnormalized = position.multiply((double)position.length(new Vector2D(0,0))/radius);
		Vector2D unitdirection = direction.unit();
		this.sensors.add(new Sensor(posnormalized,unitdirection, minrange, maxrange));
=======
		this.lh = new LogicHandler(15, 20, 1, 1);
		lh.parseMDF("0000000100000002000000002222211111000000000111122220000000010000010");
		// lh.parseMDF(MDPSIM.mdfString);
	}

	public void addSensor(String name, Vector2D position, Vector2D direction, double minrange, double maxrange) {
		Vector2D posnormalized = position.multiply((double) position.length(new Vector2D(0, 0)) / radius);
		Vector2D unitdirection = direction.unit();
		this.sensors.add(new Sensor(name, posnormalized, unitdirection, minrange, maxrange));
>>>>>>> Stashed changes
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
			if (val <= maxrange) {
				sensorvalues.add(val);
			} else {
				sensorvalues.add(null);
			}
		}
	}

	public void printSensor() {
		for (int a = 0; a < sensorvalues.size(); a++) {
<<<<<<< Updated upstream
			System.out.print(sensorvalues.get(a)+" ");
=======
			System.out.print(sensors.get(a).name + "\t ");
		}
		System.out.println();
		for (int a = 0; a < sensorvalues.size(); a++) {
			if (sensorvalues.get(a) == null) {
				System.out.print(sensorvalues.get(a) + "\t\t ");
			} else {
				System.out.print(df2.format(sensorvalues.get(a)) + "\t\t ");
			}

>>>>>>> Stashed changes
		}
		System.out.println();
	}

	// robot logic
<<<<<<< Updated upstream
	public ArrayList<Action2D> policyUpdate(){
		ArrayList<Action2D> actions = new ArrayList<Action2D>(0);
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
		if (sensorvalues.get(1) == null) {
			actions.add(new Action2D(Action.ACCELERATE, 10));
		} else if (sensorvalues.get(1) < 10) {
			actions.add(new Action2D(Action.ACCELERATE, 40));
		} else if (sensorvalues.get(1) > 10) {
			actions.add(new Action2D(Action.ACCELERATE, -40));
=======
		if (sensorvalues != null) {
<<<<<<< Updated upstream
			if(sensorvalues.get(0) <= 15) {
				mh.addAction(RobotAction.TL);
			}
			else if(sensorvalues.get(5) <= 15) {
				mh.addAction(RobotAction.TR);
			}
>>>>>>> Stashed changes
=======
		
		
		if (sensorvalues != null) {
			mh.addAction(RobotAction.TL);
		}	
		if(sensorvalues.get(3) != null && sensorvalues.get(3) > 30) {
			mh.addAction(RobotAction.TL);
		}
		
		if(sensorvalues.get(3) != null && sensorvalues.get(3) < 15) {
			
			//if front sensors >30 move forward 
			//if front sensors <15 TR
			mh.addAction(RobotAction.TR);
		}
		if(sensorvalues.get(0) != null) {
			//System.out.println("INSIDE GET 0");
			if(sensorvalues.get(0) <= 15) {
				System.out.println("TURN LEFT");
				mh.addAction(RobotAction.TL);
			}
		}
		
		if(sensorvalues.get(5) != null) {
			//System.out.println("INSIDE GET 5");
			if(sensorvalues.get(5) <= 15) {
				System.out.println("TURN RIGHT");
				mh.addAction(RobotAction.TR);
			}
>>>>>>> Stashed changes
		}
		actions.add(mh.doNext(time , sensorvalues));
=======
=======
	public ArrayList<Action2D> getNextAction(double time) {
		ArrayList<Action2D> actions = new ArrayList<Action2D>(0);

//		if (sensorvalues != null) {
>>>>>>> Stashed changes
//			mh.addAction(RobotAction.TL);
//		}
//		
//		if(sensorvalues.get(3) != null && sensorvalues.get(3) > 30) {
//			mh.addAction(RobotAction.TL);
//		}
//		
//		if(sensorvalues.get(3) != null && sensorvalues.get(3) < 15) {
//			mh.addAction(RobotAction.TR);
//		}
//		if(sensorvalues.get(0) != null) {
//			//System.out.println("INSIDE GET 0");
//			if(sensorvalues.get(0) <= 15) {
//				System.out.println("TURN LEFT");
//				mh.addAction(RobotAction.TL);
//			}
//		}
//		
//		if(sensorvalues.get(5) != null) {
//			//System.out.println("INSIDE GET 5");
//			if(sensorvalues.get(5) <= 15) {
//				System.out.println("TURN RIGHT");
//				mh.addAction(RobotAction.TR);
//			}
<<<<<<< Updated upstream
	}
=======
		
		//null == obstacle beyond 30cm 
		

=======
//		}
//		}

		actions.add(mh.doNext(time, sensorvalues)); // DO NOT TOUCH
>>>>>>> Stashed changes

>>>>>>> Stashed changes
=======
		
		if(!mh.moving) {
			   if(sensorvalues != null) {
			    if(sensorvalues.get(3) == null) {
			    	actionqueue.add(RobotAction.TL);
			    }
			    else if(sensorvalues.get(3) != null || sensorvalues.get(3) <= 19) {
			     System.out.println("There is an obstacle");
			     actionqueue.add(RobotAction.TR);
			     mh.moving = true;
			    }
			 }
		 }
>>>>>>> Stashed changes
		actions.add(mh.doNext(time , sensorvalues)); // DO NOT TOUCH
>>>>>>> Stashed changes
		return actions;
	}
<<<<<<< Updated upstream
<<<<<<< Updated upstream

=======
	
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
	public void robotExploration() {
		if(!mh.moving) {
			if(sensorvalues.get(3) == null) {
				if(!oneMovement) {
					oneMovement = true;
					actionqueue.add(RobotAction.F1);
				}
			}
			else {
				if(!oneMovement) {
					oneMovement = true;
					actionqueue.add(RobotAction.TL);
				}
				
			}
		}
	}
>>>>>>> Stashed changes
=======

	public void robotExploration() {
		if (!mh.moving) {
			if (sensorvalues.get(2) == null && sensorvalues.get(3) == null && sensorvalues.get(4) == null) {
				if (!oneMovement) {
					oneMovement = true;
					actionqueue.add(RobotAction.F1);
				}
			} else if (sensorvalues.get(4) == null && sensorvalues.get(3) == null && sensorvalues.get(2) > 19) {
				if (!oneMovement) {
					oneMovement = true;
					actionqueue.add(RobotAction.F1);
				}

			} else if (sensorvalues.get(2) == null && sensorvalues.get(4) == null && sensorvalues.get(3) > 19) {
				if (!oneMovement) {
					oneMovement = true;
					actionqueue.add(RobotAction.F1);
				}

			} else if (sensorvalues.get(2) == null && sensorvalues.get(3) == null && sensorvalues.get(4) > 19) {
				if (!oneMovement) {
					oneMovement = true;
					actionqueue.add(RobotAction.F1);
				}

			} else if (sensorvalues.get(2) == null && sensorvalues.get(3) == null && sensorvalues.get(4) < 19) {
				if (!oneMovement) {
					oneMovement = true;
					actionqueue.add(RobotAction.TL);
				}

			}else if((sensorvalues.get(1) == null && sensorvalues.get(0) == null) && (sensorvalues.get(2) <19 && sensorvalues.get(3) <19 && sensorvalues.get(4) < 19 )) {
				if (!oneMovement) {
					oneMovement = true;
					actionqueue.add(RobotAction.TR);
				}
			}
		}
	}

//	public void robotExploration() {
//		if(!mh.moving) {
//			if(sensorvalues.get(2) == null && sensorvalues.get(3) == null && sensorvalues.get(4) == null) {
//				if(!oneMovement) {
//					oneMovement = true;
//					actionqueue.add(RobotAction.F1);
//				}
//			}
//			else if (sensorvalues.get(2)==null && sensorvalues.get(3)==null&& sensorvalues.get(4)<19) {
//				if(!oneMovement) {
//					oneMovement = true;
//					actionqueue.add(RobotAction.TL);
//				}
//			}
//		}
//	}
>>>>>>> Stashed changes
}

