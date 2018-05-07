package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
	File file;
	
		
	public CsvReader(File file) {
		this.file = file;
	}


	public String[][] parse(){
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String st;
			List<String[]> lines= new  ArrayList<String[]>();
			while ((st = br.readLine()) != null) {
				lines.add(st.split(","));
			}
			String[][] out= new String[lines.size()][] ;
			for(int i=0;i< lines.size() ; i++) {
				out[i] = lines.get(i);
			}
			br.close();
			return out;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  null;
	}

	
	

}
