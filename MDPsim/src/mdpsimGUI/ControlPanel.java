package mdpsimGUI;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import mdpMapReader.MapReader;

public class ControlPanel extends JPanel{
	Viewer vw;
	JComboBox speedindicator;
	
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
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
