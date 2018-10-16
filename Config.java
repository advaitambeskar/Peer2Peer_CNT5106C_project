import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class Config {
	/*
	 This function at read the information from Common.cfg and PeerInfo.cfg
	 */
	public static String[][] start_read(String filepath) throws IOException{
		/*
		 This method realize the read the parameters from the file
		 and then return a array which includes the information in the file
		 */
		Properties prop=new Properties();
		String textLine="";
		String str="";
		
		int i;
		int j=0;
		int m=0;
		if (filepath=="Common.cfg"){
			m=1;
		} else if(filepath=="PeerInfo.cfg"){
			m=0;
		}
		
		BufferedReader file_here1 =new BufferedReader(new FileReader(filepath));
		int n=0;
		while ((textLine=file_here1.readLine())!=null){
			n++;
		}
		String[][] all_data=new String[n][m];
		
		try {
			BufferedReader file_here = new BufferedReader (new FileReader(filepath));
			
			while ((textLine=file_here.readLine())!=null){
				
				String[] number=textLine.split("\\s+");
			 
				all_data[j]=number;
							
				for (i=0;i<=m;i++){
					all_data[j][i]=number[i];
					
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
	
	//now read the parameters
	
	public int getNumberOfPreferredNeighbors() throws IOException{
		/*
		 return the number of preferred neighbors
		 */
		String[][] it_here=start_read("Common.cfg");
		int final_result=0;
		for (int k=0;k<it_here.length;k++){
			if (it_here[k][0]!=null){
				if (it_here[k][0].equals("NumberOfPreferredNeighbors")){
					final_result=Integer.parseInt(it_here[k][1]);
				}
			}
		}
		return final_result;
	}
	
	public int getUnchokingInterval() throws IOException{
		/*
		 return the number of unchoking intervals
		 */
		String[][] it_here=start_read("Common.cfg");
		int final_result=0;
		for (int k=0;k<it_here.length;k++){
			if (it_here[k][0]!=null){
				if (it_here[k][0].equals("UnchokingInterval")){
					final_result=Integer.parseInt(it_here[k][1]);
				}
			}
		}
		return final_result;
	}
	
	public int getOptimisticUnchokingInterval() throws IOException{
		/*
		 *return the optimistic unchoking interval
		 */
		String[][] it_here=start_read("Common.cfg");
		int final_result=0;
		for (int k=0;k<it_here.length;k++){
			if (it_here[k][0]!=null){
				if (it_here[k][0].equals("OptimisticUnchokingInterval")){
					final_result=Integer.parseInt(it_here[k][1]);
				}
			}
		}
		return final_result;
		
	}
	
	public String getFileName() throws IOException{
		/*
		 * return the file name
		 */
		String[][] it_here=start_read("Common.cfg");
		String final_result="";
		for (int k=0;k<it_here.length;k++){
			if (it_here[k][0]!=null){
				if (it_here[k][0].equals("FileName")){
					final_result=it_here[k][1];
				}
			}
		}
		return final_result;
	}
	
	public int getFileSize() throws IOException{
		/*
		 * return the file size
		 */
		String[][] it_here=start_read("Common.cfg");
		int final_result=0;
		for (int k=0;k<it_here.length;k++){
			if (it_here[k][0]!=null){
				if (it_here[k][0].equals("FileSize")){
					final_result=Integer.parseInt(it_here[k][1]);
				}
			}
		}
		return final_result;
	}
	
	public int getPieceSize() throws IOException{
		/*
		 * return the piece size
		 */
		String[][] it_here=start_read("Common.cfg");
		int final_result=0;
		for (int k=0;k<it_here.length;k++){
			if (it_here[k][0]!=null){
				if (it_here[k][0].equals("PieceSize")){
					final_result=Integer.parseInt(it_here[k][1]);
				}
			}
		}
		return final_result;
	}
	
	//now read the user profile
	public int getPort() throws IOException{
		/*
		 * return the port number
		 */
		String[][] it_here=start_read("PeerInfo.cfg");
		int k=0;
		int final_result=0;
		for (k=0;k<it_here.length;k++){
			
			if (it_here[k][2]!=null){
				
			if (peerProcess.id==Integer.parseInt(it_here[k][2])){
				final_result=Integer.parseInt(it_here[k][2]);
			} 
			}
			
		}
		return final_result;
		
	}
	
	public String getHostName() throws IOException{
		/*
		 * return the host name
		 */
		String[][] it_here=start_read("PeerInfo.cfg");
		int k=0;
		String final_result="";
		for (k=0;k<it_here.length;k++){
			if (peerProcess.id==Integer.parseInt(it_here[k][2])){
				final_result=it_here[k][1];
			} 
			
		}

		return final_result;
	}
	
    public Peer [] getPeers() throws IOException {
    	/*
    	 * return a array of peers
    	 */
	    String[][] it_here=start_read("PeerInfo.cfg");
	    Peer[] peer=new Peer[it_here.length];
	    for (int i=0;i<it_here.length;i++){
		    peer[i]=new Peer(Integer.parseInt(it_here[i][0]),it_here[i][1],Integer.parseInt(it_here[i][2]));
	    }
	    //TODO: WILL ALSO ADD boolean in the peer fourth argument
	          
	    return peer;  

    }
	//the following are for the test part.
	public static void main(String[] args) throws IOException{
		String[][] it_here=start_read("PeerInfo.cfg");
		System.out.println(it_here[5][1]);
		
		Config config=new Config();
		
				
	}

}
