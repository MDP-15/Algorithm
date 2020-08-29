package mdpsimGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class JFrameGraphics extends JFrame {
	
	Map map = new Map();
	Circle2D circle = new Circle2D();
	Line2D line = new Line2D();
	
	JFrame f = new JFrame();
	JPanel panel_map = new JPanel();
	JPanel panel_circle = new JPanel();
	JPanel panel_line = new JPanel();
	//To overlay 2 JPanels 
	JLayeredPane laypa = new JLayeredPane();
	
	public JFrameGraphics() {
		
		f.getContentPane().add(laypa,BorderLayout.CENTER);
		this.setTitle("Map Descriptor");
		this.setSize(470, 640);
		panel_map.add(map);
		panel_circle.add(circle);
		panel_line.add(line);
		//just to embed the circle and line on the map:- white bkg goes away
		panel_circle.setOpaque(false);
		panel_line.setOpaque(false);
		
		laypa.add(panel_map);
		laypa.add(panel_circle);
		laypa.add(panel_line);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		
	}
