package mdpsimGUI;
import java.awt.Color;
import java.awt.*;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
public class Viewer extends JFrame{
	public Panel map1;
	public Panel map2;
	public Panel sensors;
	public Graphics g;
	
	public Viewer (String title, int w, int h){
		setTitle(title);
		setSize(w, h);
		setResizable(false);
		getContentPane().setBackground(Color.darkGray);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());
		map1 = constructVirtualMap(c);
		map2 = constructRobotMap(c);
		sensors = constructSensorScreen(c);
	}
	
	private Panel constructVirtualMap(GridBagConstraints c) {
		Panel virtualmap = new Panel();
		virtualmap.setSize(339,452);
		virtualmap.setBackground(Color.lightGray);
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 339;
		c.ipady = 452;
		c.gridwidth = 1;
		c.gridheight = 1;
		this.add(virtualmap, c);
		return virtualmap;
	}
	
	private Panel constructRobotMap(GridBagConstraints c) {
		Panel virtualmap = new Panel();
		virtualmap.setSize(339,452);
		virtualmap.setBackground(Color.lightGray);
		c.insets = new Insets(2,2,2,2);
		c.gridx = 1;
		c.gridy = 0;
		c.ipadx = 339;
		c.ipady = 452;
		c.gridwidth = 1;
		c.gridheight = 1;
		this.add(virtualmap, c);	
		return virtualmap;
	}
	
	private Panel constructSensorScreen(GridBagConstraints c) {
		Panel virtualmap = new Panel();
		virtualmap.setSize(339,452);
		virtualmap.setBackground(Color.darkGray);
		c.insets = new Insets(2,2,2,2);
		c.gridx = 2;
		c.gridy = 0;
		c.ipadx = 339;
		c.ipady = 452;
		c.gridwidth = 1;
		c.gridheight = 1;
		this.add(virtualmap, c);
		int x = virtualmap.getWidth();
		System.out.println(x);
		return virtualmap;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
}
