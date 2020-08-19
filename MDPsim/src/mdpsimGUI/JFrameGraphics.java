package mdpsimGUI;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JFrameGraphics extends JPanel{
	public void paint(Graphics g){
		g.drawLine(10, 10, 200, 300);
		return;
	}
	
	public static void GUI(String[] args){
		JFrame frame= new JFrame("MDPsim");	
		frame.getContentPane().add(new JFrameGraphics());
		frame.setSize(1024, 768);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);	
		return;
	}
	
	public static void update(String[] args){
		JFrame frame= new JFrame("MDPsim");	
		frame.getContentPane().add(new JFrameGraphics());
		frame.setSize(1024, 768);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);	
		return;
	}
	
	public static int resizeX(double x) {
		return (int)((x/200*998)+15);
	}
	
	public static int resizeY(double y) {
		return (int)((y/200*738)+15);
	}
}
