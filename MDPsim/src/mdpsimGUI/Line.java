package mdpsimGUI;

import mdpsimEngine.Vector2D;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class Line extends JComponent{
	public VecInt start;
	public VecInt end;
	public Color color;
	
	public Line(VecInt start, VecInt end, Color color) {
		this.start = start;
		this.end = end;
		this.color = color;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(color);
		g.drawLine(start.x(), start.y(), end.x(), end.y());
	}
}
