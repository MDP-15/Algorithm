package mdpsimGUI;

import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class SensorScreen extends Panel{
	JTextArea timeelapsed;
	JLabel timeelapsedlabel;
	JTextArea noelapsedtimesteps;
	JLabel noelapsedtimestepslabel;
	
	public SensorScreen() {
		super();
		constructor();
		
	}
	
	public SensorScreen(LayoutManager lm) {
		super(lm);
		constructor();
	}
	
	public void constructor() {
		this.timeelapsed = new JTextArea();
		timeelapsed.setBounds(105,5,200,20);
		this.add(timeelapsed);
		this.timeelapsedlabel = new JLabel("Time Elapsed");
		timeelapsedlabel.setBounds(5,5,100,20);
		this.add(timeelapsedlabel);
		this.noelapsedtimesteps = new JTextArea();
		noelapsedtimesteps.setBounds(105, 25, 200, 20);
		this.add(noelapsedtimesteps);
		this.noelapsedtimestepslabel = new JLabel("Time Steps");
		this.noelapsedtimestepslabel.setBounds(5,25,100,20);
		this.add(noelapsedtimestepslabel);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6839293665463710847L;
	
	public void update (double time, int timesteps) {
		timeelapsed.setText(Double.toString(time));
		noelapsedtimesteps.setText(Integer.toString(timesteps));
		return;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		return;
	}
	
}
