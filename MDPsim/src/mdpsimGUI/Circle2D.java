package mdpsimGUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import javax.swing.*;

import javax.swing.JPanel;

public class Circle2D extends JPanel implements ActionListener{

	Timer t = new Timer(5, this);
	double x, y, new_x, new_y;
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Ellipse2D circle = new Ellipse2D.Double(x,y,30,30);
		g2.fill(circle);
		t.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		while(x>0 && x<400) {
			x = new_x;
			y = new_y;
		}
		
		//need to recall the paint method cause the value changes 
		repaint();
		
	}

	
}
