package mdpsimGUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import mdpsim.main;

import java.util.ArrayList;
public class Viewer extends JFrame{
	public Panel map1;
	public Panel map2;
	public Panel sensors;
	public Graphics g;
	public JMenuBar menu;
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
		menuBar();
	}
	
	//VIRTUAL MAP
	private Panel constructVirtualMap(GridBagConstraints c) {
		Panel virtualmap = new Panel(null);
		virtualmap.setSize(330,440);
		virtualmap.setBackground(Color.lightGray);
		c.insets = new Insets(1,1,1,1);
		c.gridx = 1;
		c.gridy = 3;
		c.ipadx = 330;
		c.ipady = 440;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		this.add(virtualmap, c);
		return virtualmap;
	}
	
	//ROBOT MAP
	private Panel constructRobotMap(GridBagConstraints c) {
		Panel virtualmap = new Panel(null);
		virtualmap.setSize(330,440);
		virtualmap.setBackground(Color.lightGray);
		c.insets = new Insets(1,1,1,1);
		c.gridx = 2;
		c.gridy = 3;
		c.ipadx = 330;
		c.ipady = 440;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		this.add(virtualmap, c);	
		return virtualmap;
	}
	
	//SENSOR 
	private Panel constructSensorScreen(GridBagConstraints c) {
		Panel virtualmap = new Panel(null);
		virtualmap.setSize(330,440);
		virtualmap.setBackground(Color.lightGray);
		c.insets = new Insets(1,1,1,1);
		c.gridx = 3;
		c.gridy = 3;
		c.ipadx = 330;
		c.ipady = 440;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		this.add(virtualmap, c);
		return virtualmap;
	}
	
	//MENU BAR
	private void menuBar() {
		
		JMenuBar menu = new JMenuBar();
		this.setJMenuBar(menu);
		
		JMenu file = new JMenu("File");
		menu.add(file);
		JMenuItem openMDF = new JMenuItem("Open MDF...");
		file.add(openMDF);
		
		JMenuItem exit = new JMenuItem("Exit");
		file.add(exit);
		
		JMenu settings = new JMenu("Settings");
		menu.add(settings);
		JMenuItem view = new JMenuItem("View");
		settings.add(view);
		
		JMenu loadMap = new JMenu("Load Maps");
		menu.add(loadMap);
		JMenuItem SampleArena1 = new JMenuItem("Sample Arena 1");
		loadMap.add(SampleArena1);
		
		//Initialise MDF 
		class menuBarAction implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				try {
					runMain.main(null);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		openMDF.addActionListener(new menuBarAction());
		
		
		//LOAD MAPS
		class loadMapAction implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				
				
				String mapInput = JOptionPane.showInputDialog("Load map?","SampleArena1");
				
				MapReader newMap = new MapReader();
				newMap.loadSampleArena(mapInput);
				
				/*JFrame frame = new JFrame();
				frame.setSize(300,300);
				frame.setVisible(true);
				//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				JPanel samplePanel = new JPanel();
				frame.add(samplePanel);
				
				JButton sample1 = new JButton("Sample Arena 1");
				samplePanel.add(sample1);
				
				JButton sample2 = new JButton("Sample Arena 2");
				samplePanel.add(sample2);
				
				sample1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						String mapInput = JOptionPane.showInputDialog("Load map?","SampleArena1");
						
						MapReader newMap = new MapReader();
						newMap.loadSampleArena(mapInput);
					}
				});*/
				
			}
		}
		SampleArena1.addActionListener(new loadMapAction());
    }
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
}
