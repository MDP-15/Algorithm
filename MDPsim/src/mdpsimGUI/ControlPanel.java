package mdpsimGUI;

import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class ControlPanel extends JPanel{

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
	
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
