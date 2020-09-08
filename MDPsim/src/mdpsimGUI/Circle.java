package mdpsimGUI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class Circle extends JComponent{

	private static final long serialVersionUID = 2286405575165160373L;
	public VecInt position;
	public int diameter;
	public Color color;
	
	public Circle(VecInt position, int diameter, Color color) {
		this.position = position;
		this.diameter = diameter;
		this.color = color;
		return;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(color);
		g.drawOval(position.x(),position.y(), diameter, diameter);
		
	}
	
}
