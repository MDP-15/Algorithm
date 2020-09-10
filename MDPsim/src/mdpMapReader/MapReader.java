package mdpMapReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MapReader {

	public MapReader() {
		
	}
	
	public void loadSampleArena(String fileName) {
		
		try {
			
			InputStream in = new FileInputStream("src/mdpsimGUI/"+fileName+".txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder out = new StringBuilder();
			String textLine;
			
			while((textLine=reader.readLine()) != null){
				out.append(textLine+"\n");
			}
			
			reader.close();
			
			String mapString = out.toString().replace("\n","");
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
