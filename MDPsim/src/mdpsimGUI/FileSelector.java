package mdpsimGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileSelector {
	
	final JFrame frame = new JFrame();
	final JPanel mainPanel = new JPanel();
	
	JButton openFile = new JButton("Open File...");
	JLabel messageLabel = new JLabel();
	
	private final JFileChooser openFileChooser;
	private BufferedReader originalFile;
	
	public FileSelector() {
		openFileFrame();
		
		openFileChooser = new JFileChooser();
		openFileChooser.setCurrentDirectory(new File("C:\\Users\\esthe\\OneDrive\\Documents\\GitHub"
				+ "\\Algorithm\\MDPsim\\src\\mdpMapReader"));
		openFileChooser.setFileFilter(new FileNameExtensionFilter("Txt files","txt"));
		
	}
	
	public void openFileFrame() {
		
		mainPanel.add(openFile);
		mainPanel.add(messageLabel);
		
		openFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFileActionPerformed(e);
				
			}
		});
		
		frame.setTitle("File Explorer");
		frame.setSize(100,100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.setProperty("file_bkg", "#e4f2f2");
		frame.setVisible(true);
		frame.add(mainPanel);
		
	}
	
	private void openFileActionPerformed(ActionEvent e) {
		int returnValue = openFileChooser.showOpenDialog(this);
		
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			try {
				originalFile.readLine(openFileChooser.getSelectedFile());
				messageLabel.setText("File Successfully loaded");
			}catch (IOException ioe) {
				messageLabel.setText("Failed to load the file!");
			}
		}
		else {
			messageLabel.setText("No File Chosen!");
		}
		
	}

}
