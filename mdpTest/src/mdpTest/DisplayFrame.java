package mdpTest;

import javax.swing.JFrame;

public class DisplayFrame {

	public static void main(String[] args) {
		
		JFrame mainFrame = new JFrame();
		mainFrame.setSize(700, 700);
		
		GridPanel panel = new GridPanel();
		mainFrame.add(panel);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		
	}

}
