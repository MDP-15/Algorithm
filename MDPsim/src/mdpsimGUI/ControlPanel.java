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

public class ControlPanel extends JPanel{
	Viewer vw;
	JComboBox speedindicator;
	JComboBox explorePercent;
	JTextArea mdfinput;
	JTextArea userTime;
	JButton resetbutton;
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
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
