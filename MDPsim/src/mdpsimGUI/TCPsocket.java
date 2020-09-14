package mdpsimGUI;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPsocket {
	public static Socket socket = null; 
    public static InputStream  din = null; 
    public static PrintStream dout = null;
    public static String[] bufferableCommand = new String[] {"Image"};
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	    try {
			socket = new Socket("192.168.15.1", 9000);
			System.out.println("Connected to " + socket.getInetAddress() + ":" + Integer.toString(socket.getPort()));
			din  = socket.getInputStream(); 
			dout = new PrintStream(socket.getOutputStream()); 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	    
	    while(true) {
	    	sendMessage("N|A|B|C|D");
		    String receivedString = receiveMessage();
			System.out.println(receivedString);
	    }
	}
	
	 public static void sendMessage(String message) {
	    	try {
	    		dout.write(message.getBytes());
	    		dout.flush();
	    		
	    	}
	    	catch (IOException IOEx) {
	    		System.out.println("IOException in ConnectionSocket sendMessage Function");
	    	}
	    }
	    
	    // Get message from buffer
	    public static String receiveMessage() {

	    	byte[] byteData = new byte[512];
	    	try {
	    		int size = 0;
	    		while (din.available() == 0) {
	    			try {
	    				
	    			}
	    			catch(Exception e) {
	    				System.out.println("Error in receive message");
	    			}
	    		}
	    		din.read(byteData);
	    		
	    		// This is to get rid of junk bytes
	    		while (size < 512) {
	    			if (byteData[size] == 0) {
	    				break;
	    			}
	    			// size is the array character index position and byteData[size] is the ASCII code
	    			System.out.println(size + " :" + byteData[size]);
	    			size++;
	    		}
	    		String message = new String(byteData, 0, size, "UTF-8");

	    		return message;
	    	}
	    	catch (IOException IOEx) {
	    		System.out.println("IOException in ConnectionSocket receiveMessage Function");
	    	}
	    	return "Error";
	    }
}
