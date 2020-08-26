package mdpsimGUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import javax.swing.*;

import javax.swing.JPanel;

public class Circle2D extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Timer t = new Timer(16, this);
	double x=0, y=0;
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Ellipse2D circle = new Ellipse2D.Double(x,y,30,30);
		g2.fill(circle);
		t.start();
	}
	
	//Can only check if this works after engine is finished
	@Override
	public void actionPerformed(ActionEvent e) {
		if((x>=0 && x<400) && (y>=0 && y<550))
		{
			//need to recall the paint method cause the value changes 
			repaint();
		}
		
	}

}
