package mdpsimGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Lines2D extends JPanel implements ActionListener{

//	Timer t = new Timer(16, this);
	int x1=0, y1=0, x2=0, y2=0;

	public Lines2D() {
		super();
	}

	public Lines2D(int x1, int y1, int x2, int y2) {
		super();
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawLine(x1, y1, x2, y2);
//		t.start();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if((x1>=0 && x1<400) && (y1>=0 && y1<550)
				&& (x2>=0 && x2<=400) && (y2>=0 && y2<=500)) {

			repaint();
		}

	}

}
