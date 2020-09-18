package mdpMapReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import mdpsim.MDPSIM;
import mdpsimGUI.Viewer;

public class MapReader {

	public String mapString;
	public Viewer vw; //Not initialized
	
	public MapReader() {
		
	}
	
	public void loadSampleArena(String fileName) {
		
		try {
			
			InputStream in = new FileInputStream("src/mdpMapReader/"+fileName+".txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder out = new StringBuilder();
			String textLine;
			
			while((textLine=reader.readLine()) != null){
				out.append(textLine);
			}
			
			reader.close();
			System.out.println("Debugging "+out);
			mapString = out.toString().replace("\n","");
			String newMapString = out.toString();
			System.out.println("Debugging "+newMapString);
			//MDPSIM.inputMDF(newMapString,vw); //vw is null cause not initialized
			vw.add(vw.map1); //Maybe correct
			MDPSIM.inputMDF(newMapString);
			
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
