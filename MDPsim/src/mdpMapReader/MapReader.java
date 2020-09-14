package mdpMapReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import mdpsim.main;
import mdpsimGUI.Viewer;

public class MapReader {

	public String mapString;
	
	public MapReader() {
		
	}
	
	public void loadSampleArena(String fileName, Viewer vw) {
		
		try {
			
			InputStream in = new FileInputStream("src/mdpMapReader/"+fileName+".txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder out = new StringBuilder();
			String textLine;
			
			while((textLine=reader.readLine()) != null){
				out.append(textLine+"\n");
			}
			
			reader.close();
			
			mapString = out.toString().replace("\n","");
			main.inputMDF(mapString, vw);
			
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
