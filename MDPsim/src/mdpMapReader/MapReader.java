package mdpMapReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;

import mdpsim.MDPSIM;
import mdpsimGUI.Viewer;

public class MapReader {

	public String mapString;
	public Viewer vw; //Not initialized
	
	public MapReader() {
		
	}
	
	public void loadSampleArena(String fileName, Viewer vw) {
		
		try {
			
			InputStream in = new FileInputStream("src/mdpMapReader/"+fileName+".txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder out = new StringBuilder();
			String textLine;
			
			while((textLine=reader.readLine()) != null){
				out.append(textLine);
			}
			
			reader.close();
			//System.out.println("Debugging "+out);
			mapString = out.toString().replace("\n","");
			String newMapString = out.toString();
			String newMDF = convertMDF(newMapString);
<<<<<<< HEAD
			System.out.println("Debugging "+newMapString);
			MDPSIM.mdfString = newMapString;
			vw.flag = true;
			vw.add(vw.map1); //Maybe correct
			MDPSIM.inputMDF(newMapString);
			MDPSIM.mdfString = newMDF;
			vw.flag = true;
=======
			//System.out.println("Debugging "+newMDF);
			MDPSIM.mdfString = newMDF;
			vw.engineresetflag = true;
>>>>>>> 10/7/2020
			
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
<<<<<<< HEAD
	
=======
>>>>>>> 10/7/2020
	public static String convertMDF(String mdfHex) {
		String mapdesriptor = mdfHex;
        String curr;
        String mdf = "",mdfbin;
        for(int j = 0; j < 75; j++){
            curr = String.valueOf(mapdesriptor.charAt(j));
            mdfbin = new BigInteger(curr,16).toString(2);
            if(mdfbin.length() == 1)
                mdfbin = "000" + mdfbin;
            if(mdfbin.length() == 2)
                mdfbin = "00" + mdfbin;
            if(mdfbin.length() == 3)
                mdfbin = "0" + mdfbin;
            mdf = mdf + mdfbin;
        }
<<<<<<< HEAD
		return mdf;
	}
	
	
	
	
=======
        System.out.println(mdf);
		return mdf;
	}
>>>>>>> 10/7/2020
}
