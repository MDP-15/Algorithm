package mdpsimGUI;

import java.io.IOException;

import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.*;

import mdpsim.MDPSIM;
import mdpsimRobot.Robot;
import mdpsimRobot.RobotAction;
import mdpsimRobot.RobotDirection;

public class TCPsocket {
	public static Socket socket = null;
	public static InputStream din = null;
	public static PrintStream dout = null;
	public static String[] bufferableCommand = new String[] { "Image" };
	public static boolean on=false;
	
	public static void tcpSocket() {
		try {
			socket = new Socket("192.168.15.1", 9000);
			//socket = new Socket("127.0.0.1", 9000);
			System.out.println("Connected to " + socket.getInetAddress() + ":" + Integer.toString(socket.getPort()));
			din = socket.getInputStream();
			dout = new PrintStream(socket.getOutputStream());
			
			Runnable r1 = new Runnable() {
		         public void run() {
		        	try {
						while(din.read()>=0) {	 
							receiveMessage();	
						 }
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("nah3");
						//e.printStackTrace();
					}
		         }
		     };
		     new Thread(r1).start();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("nah");
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("nah2");
		}
		
		Runnable r = new Runnable() {
	         public void run() {
	        	 try {
	        		 receiveMessage();
	        	 } catch (Exception e) {
	        		 return;
	        	 }
	        	 while(socket != null) {
	        		 receiveMessage();
	        	 }
	         }
	     };
	     new Thread(r).start();
	}
	public static void tryConnect() throws IOException {
   	 try {
		 receiveMessage();
		 return;
	 } catch (Exception e) {
   	 	socket.close();
   	 	socket = new Socket("192.168.15.1",9000);
		System.out.println("Connected to " + socket.getInetAddress() + ":" + Integer.toString(socket.getPort()));
		din = socket.getInputStream();
		dout = new PrintStream(socket.getOutputStream());
		Runnable r = new Runnable() {
	         public void run() {
	        	 try {
	        		 receiveMessage();
	        	 } catch (Exception e) {
	        		 return;
	        	 }
	        	 while(socket != null) {
	        		 receiveMessage();
	        	 }
	         }
	     };
	     new Thread(r).start();
	 }
	}
	
	public static void sendNextAction(ArrayList<RobotAction> r) {

	}
	
	public static void sendMessage(String message) {
		try {
			dout.write(message.getBytes());
			dout.flush();
		} catch (IOException IOEx) {
			System.out.println("IOException in ConnectionSocket sendMessage Function");
		}
	}
	
	// Get message from buffer
	public static String receiveMessage() {
		byte[] byteData = new byte[4096];
		try {
			int size = 0;
			while (din.available() == 0) {
				try {

				} catch (Exception e) {
					System.out.println("Error in receive message");
				}
			}
			
			din.read(byteData);
			// This is to get rid of junk bytes
			while (size < 4096) {
				if (byteData[size] == 0) {
					break;
				}
				size++;
			}
			String message = new String(byteData, 0, size, "UTF-8");
			if (message.charAt(0) != '{') {
				message = "{" + message;
			}
			System.out.println(message);
			JSONObject jobj;
			try {
				jobj = new JSONObject(message);
				message = jobj.getString("MDP15");
				switch (message) {
				case "RP":
					int robotx = jobj.getInt("X");
					int roboty = jobj.getInt("Y");
					String dir = jobj.getString("O");
					RobotDirection rdir;
					if (dir == "RIGHT") {
						rdir = RobotDirection.RIGHT;
					}
					if (dir == "UP") {
						rdir = RobotDirection.UP;
					}
					if (dir == "LEFT") {
						rdir = RobotDirection.LEFT;
					}
					if (dir == "DOWN") {
						rdir = RobotDirection.DOWN;
					}
					// Set Robot Starting Position for exploration
					break;
				case "SE": // call the start exploration
					System.out.println("Doing Stuff");
					break;
				case "SF":
					MDPSIM.robot.startFastestPath();
					break;
				case "RI":
					String instruction = jobj.getString("RI");
					sendMessage(instruction);
					// Send instruction to RPI
					break;
				case "W":
					Robot.way_x = jobj.getInt("X");
					Robot.way_y = jobj.getInt("Y");
					System.out.println(Robot.way_x +" "+ Robot.way_y);
					break;
				case "SENSORS":
					String ss = jobj.getString("SENSORS");
					String[] ss1 = ss.split(";");
					ArrayList<Double> ar = new ArrayList<Double>();
					for (int a = 0; a < ss1.length; a++) {
						ar.add(Double.parseDouble(ss1[a]));
					}
					if (ar.size() > 0) {
						MDPSIM.realsensors = ar;
						MDPSIM.sensorflag = true;
					}
					break;
				default:
					break;
				}
			} catch (JSONException e) {
				System.out.println("not yet recieve msg");
			}
		} catch (IOException IOEx) {
			System.out.println("IOException in ConnectionSocket receiveMessage Function");
		}
		return "";
	}
}
