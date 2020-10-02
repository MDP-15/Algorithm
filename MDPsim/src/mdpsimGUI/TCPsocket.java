package mdpsimGUI;

import java.io.IOException;

import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.*;

import mdpsim.MDPSIM;
import mdpsimRobot.Robot;
import mdpsimRobot.RobotDirection;

public class TCPsocket {
	public static Socket socket = null;
	public static InputStream din = null;
	public static PrintStream dout = null;
	public static String[] bufferableCommand = new String[] { "Image" };

	public static void tcpSocket() {
		try {
			socket = new Socket("192.168.15.1", 9000);
			//socket = new Socket("127.0.0.1", 9000);
			System.out.println("Connected to " + socket.getInetAddress() + ":" + Integer.toString(socket.getPort()));
			din = socket.getInputStream();
			dout = new PrintStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Runnable r = new Runnable() {
	         public void run() {
	        	 while(true) {
	        		 receiveMessage();
	        	 }
	         }
	     };
	     new Thread(r).start();

		/*while (true) {
			// sendMessage("dsfa");
			String receivedString = receiveMessage();
			System.out.println(receivedString);
		}*/
	}
	/*
	 * public static void tcpSocket2() { try { //socket = new Socket("192.168.15.1",
	 * 9000); socket = new Socket("127.0.0.1", 9000);
	 * System.out.println("Connected to " + socket.getInetAddress() + ":" +
	 * Integer.toString(socket.getPort())); din = socket.getInputStream(); dout =
	 * new PrintStream(socket.getOutputStream()); } catch (UnknownHostException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); } catch (IOException
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * 
	 * }
	 */

	public static void sendMessage(String message) {
		System.out.println("wtf");
		try {
			dout.write(message.getBytes());
			dout.flush();

		} catch (IOException IOEx) {
			System.out.println("IOException in ConnectionSocket sendMessage Function");
		}
	}

	// Get message from buffer
	public static String receiveMessage() {

		byte[] byteData = new byte[1024];
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
			while (size < 1024) {
				if (byteData[size] == 0) {
					break;
				}
				// size is the array character index position and byteData[size] is the ASCII
				// code
				// System.out.println(size + " :" + byteData[size]);
				size++;
			}
			String message = new String(byteData, 0, size, "UTF-8");
			JSONObject jobj;
			try {
				jobj = new JSONObject(message);
				message = jobj.getString("A15");
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
				default:
					break;

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//return message;
		} catch (IOException IOEx) {
			System.out.println("IOException in ConnectionSocket receiveMessage Function");
		}
		return "Error";
	}
}
