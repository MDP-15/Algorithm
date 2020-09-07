package mdpsimGUI;

import sun.tools.jps.Jps;

import java.awt.Color;
import java.awt.Component;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.sound.sampled.Line;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;

public class JFrameGraphics extends JFrame {
	
	Map map = new Map();
	Circles2D circle = new Circles2D();
	Lines2D line = new Lines2D();
	
	
	JFrame f = new JFrame();
	//JPanel panel_map = new JPanel();
	//JPanel panel_circle = new JPanel();
	//JPanel panel_line = new JPanel();
	
	private JPanel mapPanel, circlePanel, linePanel;
	//To overlay 2 JPanels 
	//JLayeredPane laypa = new JLayeredPane();
	
	public JFrameGraphics() {
		
		/*f.getContentPane().add(laypa,BorderLayout.CENTER);
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
		laypa.add(panel_line);*/
		/*
		//panel_map.add(map);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3, 1));
		mainPanel.add(panel_map,SwingConstants.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);*/



		setTitle("Map Descriptor");
		mapPanel = new JPanel(); // main panel
		mapPanel.setLayout(new OverlayLayout(mapPanel));
		mapPanel.setBackground(Color.white);
		mapPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		mapPanel.add(map);

//	      circlePanel = new JPanel();
//	      circlePanel.add(circle);
//	      linePanel = new JPanel();
//	      linePanel.add(line);

		for (int i = 1; i < 15; i++) {
			Lines2D line = new Lines2D(Map.SPACING + i * 30, Map.SPACING, Map.SPACING + i * 30,  3 + 600);
			mapPanel.add(line);
		}
		for (int i = 1; i < 20; i++) {
			Lines2D line = new Lines2D(Map.SPACING, Map.SPACING + i * 30, 3 + 450,  Map.SPACING + i * 30);
			mapPanel.add(line);
		}


		mapPanel.add(circle);

		add(mapPanel);
		setContentPane(mapPanel);
		this.setSize(470, 640);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}}
