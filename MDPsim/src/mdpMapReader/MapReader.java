package mdpMapReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import mdpsim.main;

public class MapReader {

	public String mapString;
	
	public MapReader() {
		
	}
	
	public void loadSampleArena(String fileName) {
		
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
			main.inputMDF(mapString);
			
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
