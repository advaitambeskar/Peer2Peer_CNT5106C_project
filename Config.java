package test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class cfg_reader {
	
	public static String[][] start_read(String filepath){
		Properties prop=new Properties();
		String textLine="";
		String str="";
		
		int i;
		int j=0;
		int m=0;
		if (filepath=="Common.cfg"){
			m=1;
		} else if(filepath=="PeerInfo.cfg"){
			m=3;
		}
		String[][] all_data=new String[6][m];
		
		try {
			BufferedReader file_here = new BufferedReader (new FileReader(filepath));
			while ((textLine=file_here.readLine())!=null){
				
				String[] number=textLine.split(" ");
			 
				all_data[j]=number;
							
				for (i=0;i<=m;i++){
					all_data[j][i]=number[i];
				    //System.out.println(number[i]);
				}
			    j++;
			    
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		} 

		return all_data;

	}
	
	public int getNumberOfPreferredNeighbors(){
		String[][] it_here=start_read("Common.cfg");
		return Integer.parseInt(it_here[0][1]);
	}
	
	public int getUnchokingInterval(){
		String[][] it_here=start_read("Common.cfg");
		return Integer.parseInt(it_here[1][1]);
	}
	
	public int getOptimisticUnchokingInterval(){
		String[][] it_here=start_read("Common.cfg");
		return Integer.parseInt(it_here[2][1]);
		
	}
	
	public String getFileName(){
		String[][] it_here=start_read("Common.cfg");
	    return it_here[3][1];
	}
	
	public int getFileSize(){
		String[][] it_here=start_read("Common.cfg");
		return Integer.parseInt(it_here[4][1]);
	}
	
	public int getPieceSize(){
		String[][] it_here=start_read("Common.cfg");
		return Integer.parseInt(it_here[5][1]);
	}
	
	public int getPort(){
		String[][] it_here=start_read("PeerInfo.cfg");
        for(int k=0;k<)
        
		
		return 0;
	}
	
	public String getHostName(){
		return "";
	}
	
    
    public Peer[] getPeers() {
        return new Peer[] {
            new Peer(1001,"localhost", 1001),
            new Peer(1002,"localhost", 1002),
        };
    }
}
