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

public class ControlPanel extends JPanel{
	Viewer vw;
	JComboBox speedindicator;
	JComboBox explorePercent;
	JTextArea mdfinput;
	JTextArea userTime;
	JButton resetbutton;
	JButton flag;
	JButton setTime;
	
	private static final long serialVersionUID = 1L;
	public ControlPanel () {
		super();
		constructor();
	}
	
	public ControlPanel(LayoutManager lm, Viewer vw) {
		super(lm);
		constructor();
		this.vw = vw;
	}
	
	public void constructor() {
		
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
		this.mdfinput = new JTextArea();
		mdfinput.setBounds(5,25,300,20);
		this.add(mdfinput);
		this.resetbutton = new JButton();
		resetbutton.setText("Reset!");
		resetbutton.setBounds(305,25,100,20);
		this.add(resetbutton);
		resetbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			vw.custommdfresetflag = true;
			vw.mdfstring = mdfinput.getText();
			}
		});
		//FLAG BUTTON
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
