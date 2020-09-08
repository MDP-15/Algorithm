package mdpsimGUI;

import java.awt.Color;
import java.awt.Graphics;

public class Circle{

	public VecInt position;
	public int diameter;
	public Color color;
	
	public Circle(VecInt position, int diameter, Color color) {
		this.position = position;
		this.diameter = diameter;
		this.color = color;
		return;
	}
}
