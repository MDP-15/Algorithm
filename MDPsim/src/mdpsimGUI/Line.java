package mdpsimGUI;

import mdpsimEngine.Vector2D;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class Line{
	public VecInt start;
	public VecInt end;
	public Color color;
	
	public Line(VecInt start, VecInt end, Color color) {
		this.start = start;
		this.end = end;
		this.color = color;
	}
}
