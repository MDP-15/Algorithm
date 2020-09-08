package mdpsimGUI;
import java.awt.Color;
import java.awt.*;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
public class Viewer extends JFrame{
	public ArrayList<Line> enginelines;
	public Circle virtualrobot1;
	public JPanel map1;
	public JPanel map2;
	public JPanel sensors;
	public Graphics g;
	
	public Viewer (String title, int w, int h){
		setTitle(title);
		setSize(w, h);
		setResizable(false);
		getContentPane().setBackground(Color.darkGray);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagConstraints c = new GridBagConstraints();
		enginelines = new ArrayList<Line>();
		setLayout(new GridBagLayout());
		map1 = constructVirtualMap(c);
		map2 = constructRobotMap(c);
		sensors = constructSensorScreen(c);
	}
	
	private JPanel constructVirtualMap(GridBagConstraints c) {
		JPanel virtualmap = new JPanel();
		Circle circle = new Circle(new VecInt(15,15),25);
		virtualmap.setBackground(Color.lightGray);
		virtualmap.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.darkGray));
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 339;
		c.ipady = 452;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(virtualmap, c);
		return virtualmap;
	}
	
	private JPanel constructRobotMap(GridBagConstraints c) {
		JPanel virtualmap = new JPanel();
		virtualmap.setBackground(Color.lightGray);
		virtualmap.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.darkGray));
		c.gridx = 1;
		c.gridy = 0;
		c.ipadx = 339;
		c.ipady = 452;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(virtualmap, c);
		return virtualmap;
	}
	
	private JPanel constructSensorScreen(GridBagConstraints c) {
		JPanel virtualmap = new JPanel();
		virtualmap.setBackground(Color.darkGray);
		virtualmap.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.darkGray));
		c.gridx = 2;
		c.gridy = 0;
		c.ipadx = 339;
		c.ipady = 452;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(virtualmap, c);
		return virtualmap;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawOval(15,15,25,25);
		}
}
