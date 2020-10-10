package mdpsimGUI;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import mdpsimRobot.QueuedAction;
import mdpsimRobot.RobotAction;
import mdpsimRobot.Sensor;

public class SensorScreen extends JPanel{
	JTextArea timeelapsed;
	JTextArea MDF;
	JLabel timeelapsedlabel;
	JTextArea noelapsedtimesteps;
	JLabel noelapsedtimestepslabel;
	JTextArea action;
	JLabel actionlabel;
	ArrayList<Sensor> sensornames;
	ArrayList<Double> sensordata;
	ArrayList<JTextArea> jsensordata;
	ArrayList<JLabel> jsensornames;
	private static final long serialVersionUID = -6839293665463710847L;
	
	RobotAction currentaction;
	ArrayList<RobotAction> queuedactions;
	public SensorScreen() {
		super();
		constructSS();
		
	}
	
	public SensorScreen(LayoutManager lm) {
		super(lm);
		constructSS();
	}
	
	public void constructSS() {
		jsensordata = new ArrayList<JTextArea>(0);
		jsensornames = new ArrayList<JLabel>(0);
		this.timeelapsed = new JTextArea();
		timeelapsed.setBounds(105,5,225,20);
		timeelapsed.setBackground(Color.DARK_GRAY);
		timeelapsed.setForeground(Color.white);
		this.add(timeelapsed);
		this.timeelapsedlabel = new JLabel("Time Elapsed");
		timeelapsedlabel.setBounds(5,5,100,20);
		timeelapsedlabel.setBackground(Color.DARK_GRAY);
		timeelapsedlabel.setForeground(Color.white);
		this.add(timeelapsedlabel);
		this.noelapsedtimesteps = new JTextArea();
		noelapsedtimesteps.setBounds(105, 25, 225, 20);
		noelapsedtimesteps.setBackground(Color.DARK_GRAY);
		noelapsedtimesteps.setForeground(Color.white);
		this.add(noelapsedtimesteps);
		this.noelapsedtimestepslabel = new JLabel("Time Steps");
		noelapsedtimestepslabel.setBounds(5,25,100,20);
		noelapsedtimestepslabel.setBackground(Color.DARK_GRAY);
		noelapsedtimestepslabel.setForeground(Color.white);
		this.add(noelapsedtimestepslabel);
		for (int a = 0; a < 6; a++) {
			JTextArea data = new JTextArea("");
			data.setBounds(105, 45+a*20, 225, 20);
			data.setBackground(Color.DARK_GRAY);
			data.setForeground(Color.white);
			jsensordata.add(data);
			this.add(data);
			JLabel name = new JLabel("");
			name.setBounds(5,45+a*20,100,20);
			name.setBackground(Color.DARK_GRAY);
			name.setForeground(Color.white);
			jsensornames.add(name);
			this.add(name);
		}
		this.actionlabel = new JLabel("Current Action");
		actionlabel.setBounds(5,300,100,20);
		actionlabel.setBackground(Color.DARK_GRAY);
		actionlabel.setForeground(Color.white);
		this.add(actionlabel);
		this.action = new JTextArea();
		action.setBounds(105, 300, 225, 20);
		action.setBackground(Color.DARK_GRAY);
		action.setForeground(Color.white);
		this.add(action);
		this.MDF = new JTextArea();
		MDF.setBounds(5, 400, 300, 60);
		MDF.setLineWrap(true);
		MDF.setBackground(Color.DARK_GRAY);
		MDF.setForeground(Color.white);
		this.add(MDF);
	}
	
	public void update (double time, long timesteps, ArrayList<Sensor> sensornames, ArrayList<Double> sensordata, RobotAction currentaction, ArrayList<RobotAction> queuedactions) {
		timeelapsed.setText(Double.toString(time));
		noelapsedtimesteps.setText(Long.toString(timesteps));
		this.sensornames = sensornames;
		this.sensordata = sensordata;
		this.currentaction = currentaction;
		this.queuedactions = queuedactions;
		if (currentaction == null) {
			action.setText("null");
		} else {
			action.setText(this.currentaction.toString());
		}
		
		for (int a = 0; a < sensornames.size(); a++) {
			jsensornames.get(a).setText(sensornames.get(a).name);
			try {
				double value = sensordata.get(a);
				jsensordata.get(a).setText(Double.toString(value));
			} catch (Exception e) {
				jsensordata.get(a).setText("null");
			}
		}
		return;
	}
	
	public void updateMDF(String mdf) {
		this.MDF.setText(mdf);
		return;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		return;
	}
	
}
