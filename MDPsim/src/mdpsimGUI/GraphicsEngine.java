package mdpsimGUI;
import javax.swing.*;
import mdpsimEngine.*;
import java.awt.*;
import java.util.ArrayList;
import java.lang.Math;

public class GraphicsEngine {
	
	public static void paint(Graphics g) {
		g.drawLine(10, 10, 1024, 1024);
	}
	
	public JFrame newFrame() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0,0,1024,768);
		frame.setVisible(true);
		frame.setResizable(false);
		return frame;
	}
	
	public int scaleX(double x) {
		return (int) Math.round((x/200*994)+15);
	}
	
	public int scaleY(double x) {
		return (int) Math.round((x/200*738)+15);
	}
}
