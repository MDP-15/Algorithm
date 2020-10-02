package mdpsimGUI;

import java.awt.Graphics;
import java.awt.LayoutManager;

<<<<<<< Updated upstream
import javax.swing.JPanel;

public class ControlPanel extends JPanel{

=======
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import mdpMapReader.MapReader;
import mdpsim.MDPSIM;
import mdpsimRobot.LogicHandler;

public class ControlPanel extends JPanel{
	Viewer vw;
	LogicHandler logic;
	JComboBox speedindicator;
	JComboBox explorePercent;
	JTextArea mdfinput;
	JTextArea userTime;
	JLabel coor_label;
	JTextArea x_cor,y_cor;
	JButton resetbutton;
	JButton setTime;
	JButton setCoor;
	
>>>>>>> Stashed changes
	private static final long serialVersionUID = 1L;
	public ControlPanel () {
		super();
		constructor();
	}
	
	public ControlPanel(LayoutManager lm) {
		super(lm);
		constructor();
	}
	
	public void constructor() {
<<<<<<< Updated upstream
	
=======
		
		//SELECT ROBOT SPEED
		String[] strings = {"MAX","1x","0.5x","0.25x"};
		this.speedindicator = new JComboBox(strings);
		speedindicator.setBounds(5,5,100,20);
		speedindicator.setSelectedIndex(1);
		speedindicator.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			vw.enginespeedflag = true;
			vw.enginespeed = speedindicator.getSelectedIndex();
			}
		});
		this.add(speedindicator);
		
		//SELECT % OF EXPLORATION
		String[] explore = {"100%","75%","50%"};
		this.explorePercent = new JComboBox(explore);
		explorePercent.setBounds(110, 5, 100, 20);
		explorePercent.setSelectedIndex(1);
		explorePercent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			//ADD ACTION
			}
		});
		this.add(explorePercent);
		

		//USER SELECTED TIME LIMIT
		this.userTime = new JTextArea("Input Time Limit in seconds");
		userTime.setBounds(5,70,300,30);
		this.add(userTime);
		this.setTime = new JButton();
		setTime.setText("Set Time");
		setTime.setBounds(305,70, 100,30);
		this.add(setTime);
		setTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			//ADD ACTION
			}
		});
		
		//INPUT MDF STRING
		this.mdfinput = new JTextArea("Input an MDF string");
		mdfinput.setBounds(5,35,300,30);
		this.add(mdfinput);
		this.resetbutton = new JButton();
		resetbutton.setText("Enter!");
		resetbutton.setBounds(305,35,100,30);
		this.add(resetbutton);
		resetbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			vw.custommdfresetflag = true;
			vw.mdfstring = mdfinput.getText();
			}
		});
		
		//INPUT STARTING COORDINATES
		this.coor_label = new JLabel("=== Starting Coordinates ===");
		coor_label.setBounds(500, 10, 200, 20);
		this.add(coor_label);
		
		this.x_cor = new JTextArea("X: 1-13");
		x_cor.setBounds(500,35,200,30);
		this.add(x_cor);
		
		this.y_cor = new JTextArea("Y: 1-18");
		y_cor.setBounds(500,70,200,30);
		this.add(y_cor);
		
		this.setCoor = new JButton();
		setCoor.setText("Go!");
		setCoor.setBounds(710,35,70,60);
		this.add(setCoor);
		setCoor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			//ADD ACTION
				logic.x_coor = Integer.parseInt(x_cor.getText());
				logic.y_coor = Integer.parseInt(y_cor.getText());
				//MDPSIM.robot_x = (Integer.parseInt(x_cor.getText()) * 10 + 5);
				//MDPSIM.robot_y = (Integer.parseInt(y_cor.getText()) * 10 + 5);
			}
		});
		
>>>>>>> Stashed changes
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
