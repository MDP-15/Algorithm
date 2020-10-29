package mdpsimGUI;

import java.awt.Color;
import java.awt.Graphics;

public class Circle{

	public VecInt position;
	public int diameter;
	public Color color;
	public boolean fill;
	
	public Circle(VecInt position, int diameter, Color color, boolean fill) {
		this.position = position;
		this.diameter = diameter;
		this.color = color;
		this.fill = fill;
		return;
	}
}
