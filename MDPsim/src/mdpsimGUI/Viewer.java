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

import java.util.ArrayList;
public class Viewer extends JFrame{
	public Engine2DPanel map1;
	public Robot2DPanel map2;
	public SensorScreen sensors;
	public Graphics g;
	public ControlPanel cp;
	public JMenuBar menu;
	public String mapBits;
	MapReader newMap = new MapReader();
	
	
	public Viewer (String title, int w, int h){
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
		cp = constructControlPanel(c);
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
	
	private ControlPanel constructControlPanel(GridBagConstraints c) {
		ControlPanel controlpanel = new ControlPanel(null);
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
		JMenuItem SampleArena = new JMenuItem("Sample Arenas");
		file.add(SampleArena);
		
		JMenuItem tcpSocket = new JMenuItem("Socket Connection");
		file.add(tcpSocket);
		
		JMenuItem exit = new JMenuItem("Exit");
		file.add(exit);
		
		JMenu settings = new JMenu("Settings");
		menu.add(settings);
		JMenuItem view = new JMenuItem("View");
		settings.add(view);
		
		//FILE -> Sample Arenas
		class loadMapAction implements ActionListener{
			public void actionPerformed(ActionEvent e) {
			
				JFrame frame = new JFrame();
				frame.setSize(300,300);
				frame.setVisible(true);
				//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				JPanel samplePanel = new JPanel();
				frame.add(samplePanel);
				
				JButton sample1 = new JButton("Arena 1");
				samplePanel.add(sample1);
				
				JButton sample2 = new JButton("Arena 2");
				samplePanel.add(sample2);
				
				JButton sample3 = new JButton("Arena 3");
				samplePanel.add(sample3);
				
				JButton sample4 = new JButton("Arena 4");
				samplePanel.add(sample4);
				
				
				//ARENA 1
				sample1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						String mapInput = JOptionPane.showInputDialog("Load map?","Arena_1");
						
						MapReader newMap = new MapReader();
						remove(map1); //This is correct (maybe) need to re-initialize map1
						newMap.loadSampleArena(mapInput);
						
					}
				});
				
				//ARENA 2
				sample2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						String mapInput = JOptionPane.showInputDialog("Load map?","Arena_2");
						
						MapReader newMap = new MapReader();
						newMap.loadSampleArena(mapInput);
						
					}
				});
				
				//ARENA 3
				sample3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						String mapInput = JOptionPane.showInputDialog("Load map?","Arena_3");
						
						MapReader newMap = new MapReader();
						newMap.loadSampleArena(mapInput);
					}
				});
				
				//ARENA 4
				sample4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						String mapInput = JOptionPane.showInputDialog("Load map?","Arena_4");
						
						MapReader newMap = new MapReader();
						newMap.loadSampleArena(mapInput);
					}
				});
			}
		}
		SampleArena.addActionListener(new loadMapAction());
		
		
		class socketBtn implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				TCPsocket.tcpSocket();
			}
		}
			tcpSocket.addActionListener(new socketBtn());
    }
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
	}
	
}
