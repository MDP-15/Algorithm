package mdpsimGUI;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.io.FileReader;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mdpMapReader.MapReader;
import mdpsim.MDPSIM;
import mdpsimRobot.LogicHandler;
import mdpsimRobot.RobotDirection;

import java.util.ArrayList;
public class Viewer extends JFrame{
	public Engine2DPanel map1;
	public Robot2DPanel map2;
	public SensorScreen sensors;
	public Graphics g;
	public ControlPanel cp;
	public JMenuBar menu;
	public String mapBits;
	public boolean engineresetflag;
	public boolean enginespeedflag;
	public boolean custommdfresetflag;
	public String mdfstring;
	public int enginespeed;
	MapReader newMap = new MapReader();
	
	
	
	public Viewer (String title, int w, int h){
		this.engineresetflag = false;
		this.enginespeedflag = false;
		setTitle(title);
		setSize(w, h);
		setResizable(false);
		System.setProperty("background", "#f2f4f7");
		getContentPane().setBackground(Color.getColor("background"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());
		map1 = constructVirtualMap(c);
		map2 = constructRobotMap(c);
		sensors = constructSensorScreen(c);
		cp = constructControlPanel(c, this);
		menuBar();
	}
	
	//VIRTUAL MAP
	private Engine2DPanel constructVirtualMap(GridBagConstraints c) {
		Engine2DPanel virtualmap = new Engine2DPanel(null);
		virtualmap.setSize(331,441);
		virtualmap.setBackground(Color.darkGray);
		c.insets = new Insets(1,1,1,1);
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 331;
		c.ipady = 441;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		this.add(virtualmap, c);
		return virtualmap;
	}
	
	//ROBOT MAP
	private Robot2DPanel constructRobotMap(GridBagConstraints c) {
		Robot2DPanel virtualmap = new Robot2DPanel(null);
		virtualmap.setSize(331,441);
		virtualmap.setBackground(Color.darkGray);
		c.insets = new Insets(1,1,1,1);
		c.gridx = 1;
		c.gridy = 0;
		c.ipadx = 331;
		c.ipady = 441;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		this.add(virtualmap, c);	
		return virtualmap;
	}
	
	//SENSOR 
	private SensorScreen constructSensorScreen(GridBagConstraints c) {
		SensorScreen virtualmap = new SensorScreen(null);
		virtualmap.setSize(331,441);
		virtualmap.setBackground(Color.darkGray);
		c.insets = new Insets(1,1,1,1);
		c.gridx = 2;
		c.gridy = 0;
		c.ipadx = 331;
		c.ipady = 441;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		this.add(virtualmap, c);
		return virtualmap;
	}
	
	private ControlPanel constructControlPanel(GridBagConstraints c, Viewer vw) {
		ControlPanel controlpanel = new ControlPanel(null, vw);
		controlpanel.setSize(1000,250);
		c.insets = new Insets(1,1,1,1);
		c.gridx = 0;
		c.gridy = 1;
		c.ipadx = 996;
		c.ipady = 250;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		this.add(controlpanel,c);
		return controlpanel;
	}
	
	//MENU BAR
	private void menuBar() {
		JMenuBar menu = new JMenuBar();
		this.setJMenuBar(menu);
		
		JMenu file = new JMenu("File");
		menu.add(file);
		
		JMenu arenas = new JMenu("SampleArena");
		menu.add(arenas);
		JMenuItem sampleA1 = new JMenuItem("Sample Arena 1");
		JMenuItem sampleA2 = new JMenuItem("Sample Arena 2");
		JMenuItem sampleA3 = new JMenuItem("Sample Arena 3");
		JMenuItem sampleA4 = new JMenuItem("Sample Arena 4");
		
		arenas.add(sampleA1);
		arenas.add(sampleA2);
		arenas.add(sampleA3);
		arenas.add(sampleA4);
		
		JMenu instructions = new JMenu("Instructions");
		menu.add(instructions);
		
		JMenuItem exStart = new JMenuItem("Explore Map");
		instructions.add(exStart);
		JMenuItem fpStart = new JMenuItem("Fastest Path");
		instructions.add(fpStart);
		
		
		
		
		
		JMenuItem tcpSocket = new JMenuItem("Socket Connection");
		file.add(tcpSocket);
		
		
		class socketBtn implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				TCPsocket.tcpSocket();
			}
		}
			tcpSocket.addActionListener(new socketBtn());
			
			
			exStart.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					MDPSIM.mode = 1;
					
				}
			});
			
			fpStart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					System.out.println("Button Pressed");
					MDPSIM.robot.lh.parseMDF(MDPSIM.mdfString);
					MDPSIM.robot.startFastestPath();
					
				}
			});
			
			//SampleArenas
			//Arena 1
			sampleA1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					System.out.println("BUTTON PRESSED");
					String mapInput = "Arena_1";
					
					MapReader newMap = new MapReader();
					//remove(map1); //This is correct (maybe) need to re-initialize map1
					newMap.loadSampleArena(mapInput,Viewer.this);
					//repaint();
					System.out.println("Arena 1 clicked");
					
				}
			});
			
			//Arena 2
			sampleA2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String mapInput = "Arena_2";
					
					MapReader newMap = new MapReader();
					newMap.loadSampleArena(mapInput, Viewer.this);
					System.out.println("Arena 2 clicked");
					
				}
			});
			
			//Arena 3
			sampleA3.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String mapInput = "Arena_3";
					
					MapReader newMap = new MapReader();
					newMap.loadSampleArena(mapInput, Viewer.this);
					System.out.println("Arena 3 clicked");
					
				}
			});
			
			//Arena 4
			sampleA4.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String mapInput = "Arena_4";
					
					MapReader newMap = new MapReader();
					newMap.loadSampleArena(mapInput, Viewer.this);
					System.out.println("Arena 4 clicked");
					
				}
			});
			
			
    }
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
	}
	
}
