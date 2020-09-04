package mdpsimGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;

public class Lines2D extends JPanel implements ActionListener{

	Timer t = new Timer(16, this);
	int x1=0, y1=0, x2=0, y2=0;
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawLine(x1, y1, x2, y2);
		t.start();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if((x1>=0 && x1<400) && (y1>=0 && y1<550) 
				&& (x2>=0 && x2<=400) && (y2>=0 && y2<=500)) {
			
			repaint();
		}
		
	}

}
