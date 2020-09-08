package mdpsimGUI;

import mdpsimEngine.Vector2D;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class Line extends JComponent{
	public Vector2D start;
	public Vector2D end;
	public Color color;
	
	public Line(Vector2D start, Vector2D end, Color color) {
		this.start = start;
		this.end = end;
		this.color = color;
	}
	
	public void draw() {
		Graphics g = new Graphics();
	}
}
