package mdpsimGUI;
import java.awt.Color;
import java.awt.*;
import javax.swing.JFrame;

public class Viewer extends JFrame{
	public Viewer (String title, int w, int h){
		setTitle(title);
		setSize(w, h);
		setResizable(false);
		this.getContentPane().setBackground(Color.darkGray);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void draw() {
		
	}
}
