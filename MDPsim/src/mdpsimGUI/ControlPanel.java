package mdpsimGUI;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import mdpMapReader.MapReader;
import mdpsim.MDPSIM;
import mdpsimRobot.LogicHandler;
import mdpsimRobot.Robot;

public class ControlPanel extends JPanel {
	Viewer vw;
	JComboBox speedindicator;
	public JTextArea explorePercent;
	JTextArea timeCover;
	JTextArea mdfinput;
	public JTextArea userTime;
	JButton resetbutton;
	JButton flag;
	JButton setTime;
	JButton setPercent;
	
	private static final long serialVersionUID = 1L;

	public ControlPanel() {
		super();
		constructor();
	}

	public ControlPanel(LayoutManager lm, Viewer vw) {
		super(lm);
		constructor();
		this.vw = vw;
	}

	public void constructor() {

		// SELECT ROBOT SPEED
		String[] strings = { "MAX", "1x", "0.5x", "0.25x" };
		this.speedindicator = new JComboBox(strings);
		speedindicator.setBounds(5, 5, 100, 20);
		speedindicator.setSelectedIndex(1);
		speedindicator.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vw.enginespeedflag = true;
				vw.enginespeed = speedindicator.getSelectedIndex();
			}
		});
		this.add(speedindicator);

		// USER SELECTED TIME LIMIT
		this.userTime = new JTextArea("360");
		userTime.setBounds(5, 70, 300, 30);
		this.add(userTime);
		this.setTime = new JButton();
		setTime.setText("Set Time");
		setTime.setBounds(305, 70, 100, 30);
		this.add(setTime);
		setTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vw.timeflag = true;
			}
		});
		
		// SELECT % OF EXPLORATION
		this.explorePercent = new JTextArea("Enter a value between 0 and 1");
		explorePercent.setBounds(5, 105, 300, 30);
		this.add(explorePercent);
		this.setPercent = new JButton();
		setPercent.setText("Coverage");
		setPercent.setBounds(305, 105, 100, 30);
		this.add(setPercent);
		setPercent.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
				vw.coverageflag = true;
			}
		});


		// INPUT MDF STRING
		this.mdfinput = new JTextArea("Input an MDF string");
		mdfinput.setBounds(5, 35, 300, 30);
		this.add(mdfinput);
		this.resetbutton = new JButton();
		resetbutton.setText("Enter!");
		resetbutton.setBounds(305, 35, 100, 30);
		this.add(resetbutton);
		resetbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vw.custommdfresetflag = true;
				vw.mdfstring = mdfinput.getText();
			}
		});

		// FLAG BUTTON
		flag = new JButton("Scan");
		flag.setBounds(5, 140, 100, 30);
		flag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MDPSIM.mode = 1;
			       TCPsocket.sendMessage("{\"MDP15\":\"RI\",\"RI\":\"s\"}");
			}
		});
		this.add(flag);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}