package mdpsimGUI;

import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mdpMapReader.MapReader;
import mdpsim.MDPSIM;

public class MapSelector extends JPanel{
	
	public MapSelector() {
		super();
	
	}

	public MapSelector(LayoutManager lm) {
		super(lm);
		
	}
	
	public void MapSelector(String string) {
		
		Viewer view = new Viewer();
		view.enginePanel.remove(view.map1);
		MDPSIM.inputMDF(string);
		
		
	}

}
