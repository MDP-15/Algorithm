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
	JComboBox explorePercent;
	JTextArea mdfinput;
	JTextArea userTime;
	JButton resetbutton;
	JButton flag;
	JButton setTime;

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

		// SELECT % OF EXPLORATION
		String[] explore = { "100%", "75%", "50%" };
		this.explorePercent = new JComboBox(explore);
		explorePercent.setBounds(110, 5, 100, 20);
		explorePercent.setSelectedIndex(1);
		explorePercent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ADD ACTION
			}
		});
		this.add(explorePercent);

		// USER SELECTED TIME LIMIT
		this.userTime = new JTextArea("Input Time Limit in seconds");
		userTime.setBounds(5, 70, 300, 30);
		this.add(userTime);
		this.setTime = new JButton();
		setTime.setText("Set Time");
		setTime.setBounds(305, 70, 100, 30);
		this.add(setTime);
		setTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ADD ACTION
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
		flag = new JButton("Explore");
		flag.setBounds(5, 120, 100, 30);
		flag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (MDPSIM.mode == 1) {
					MDPSIM.mode = 0;
				} else if (MDPSIM.mode == 0) {
					MDPSIM.mode = 1;
				}
			}
		});
		this.add(flag);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
