import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileManager {
    String filename;
    static Config config=new Config();
    private RandomAccessFile file;
	static int fileSize;
	static int pierceSize;
	static int total_piece_size;

    FileManager(String filename) throws Exception {
		this.filename = filename;
		
		fileSize=100;
		pierceSize=2;
		/*
		 * make changes on file size and pierce size here
		 */

		//fileSize=config.getFileSize();
		//pierceSize=config.getPieceSize();
		total_piece_size=divdier(fileSize,pierceSize);
    }

    static int divdier(int x, int y){
		int final_result=0;
		if (x%y==0){
			final_result=x/y;
		}else{
			final_result=x/y+1;
		}
    	return final_result;
    	
    }
    
    static int calculate_length(int index){
    	int pierce_length=pierceSize;
    	if (index==total_piece_size){
    		pierce_length=fileSize-(index-1)*pierceSize;
    	}else{
    		pierce_length=pierceSize;
    	}
    	return pierce_length;
    }
    //return the data for that pierce
    
    //index here means the number of the data pierce.  MIGHT REDUCE 1!!!!!!! 
    public byte [] getPiece(int index) throws IOException {
    	
    	int fileSize=100;
    	int pierceSize=2;
    	
    	//int fileSize=config.getFileSize();
    	//int pierceSize=config.getPieceSize();
    	
    	int total_piece_size=divdier(fileSize,pierceSize); 
    	
    	int pierce_length=calculate_length(index);
    	
    	byte[] pierce_needed=new byte[pierce_length];
    	
    	byte[] requested_data=new byte[pierce_length];
    	
    	file=new RandomAccessFile(filename,"rw");
    	long place= (long) (index-1)*pierceSize;
    	file.seek(place);
    	file.read(pierce_needed);
    	
    	System.arraycopy(pierce_needed, 0, requested_data, 0,pierce_needed.length);
    	
        return requested_data;
    }
    
    //will be called when the peer received such a pierce to write the data into disk
    /*
     * functions include: 1) create a file named peerid+
     *  2) create a txt under the peerid file
     *  3) write the data into the txt file based on the index number
     */
    public void setPiece(int index, byte [] payload) throws IOException {
    	String place1="/Users/qibing/Desktop/peer_"+peerProcess.id;
    	File newfile=new File(place1);
    	if (!newfile.exists()){
    		newfile.mkdirs();
    	}
    	String filename1="/Users/qibing/Desktop/peer_"+peerProcess.id+"/"+filename;
    
    		
    	RandomAccessFile file1=new RandomAccessFile(filename1,"rw");
    	int pierce_length=calculate_length(index);
 
    	
    	long place=(long)((index-1)*pierce_length);
    	file1.seek(place);
    	
    	file1.write(payload);
    	
        return;
    }
	
    public static void main(String[] args) throws Exception{
    	/*
    	 * Testing is based on change the filesize to 100, change the pierce to 2 in the Common.cfg. 
    	 */
    	 FileManager filemanager=new FileManager("1.txt");
    	 for (int i=50;i>0;i--){
    		 
    	 
    	 int random_index=i;
    	// System.out.println(random_index);
    	 
    	 byte[] m=filemanager.getPiece(random_index);
    	 int gewei=random_index%10;
    	 int first;
    	 int second;
    	 /*
    	  * Transfer to ASCII code here
    	  */
    	 if (gewei<6&&gewei>0){
    		 first=(gewei-1)*2+48;
    		 second=(gewei-1)*2+49;
    		 
    	 }else if (gewei<10&&gewei>5) {
    		 first=(gewei-6)*2+48;
    		 second=(gewei-6)*2+49;
    	 }else {
    		 first=8+48;
    		 second=9+48;
    	 }
    	
    	 
    	 if (m[0]==first && m[1]==second){
    		 System.out.println("Reading result is correct");
    		 
    	 }else{
    		 
    		 System.out.println("Reading result is incorrect");
    		 System.exit(1);
    	 }
    	 
    	 filemanager.setPiece(random_index, m);

    	 }
    	 
    	 String filename1="/Users/qibing/Desktop/peer_"+peerProcess.id+"/"+"1.txt";
    	 FileManager filemanager2=new FileManager(filename1);
    	 for (int j=1;j<=50;j++){
    	    byte[] b1=filemanager2.getPiece(j);
    	    byte[] b2=filemanager.getPiece(j);
    	    if (b1[0]==b2[0]&&b1[1]==b2[1]){
    	    	System.out.println("Match");
    	    }else{
    	    	System.out.println("Does not match");
    	    	System.exit(1);
    	    }
    	 }
    	 
    	//System.out.println(divdier(10000232,32768));
    }
}
