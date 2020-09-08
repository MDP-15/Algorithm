package mdpsimGUI;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Panel extends JPanel{

	public ArrayList<Line> lines;
	public ArrayList<Circle> circles;
	private static final long serialVersionUID = 7177602708373003430L;

	public Panel() {
		super();
		lines = new ArrayList<Line>();
		circles = new ArrayList<Circle>();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (Circle circle: circles) {
			g.drawOval(circle.position.x(),circle.position.y(), circle.diameter, circle.diameter);
		}
		for (Line line: lines) {
			g.drawLine(line.start.x(), line.start.y(), line.end.x(), line.end.y());
		}
		return;
	}
}
