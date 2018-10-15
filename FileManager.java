import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileManager {
    String filename;
    static Config config=new Config();
    private RandomAccessFile file;
	static int fileSize=config.getFileSize();
	static int pierceSize=config.getPieceSize();
	static int total_piece_size=divdier(fileSize,pierceSize); 
    

    FileManager(String filename) {
        this.filename = filename;
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
    	int fileSize=config.getFileSize();
    	int pierceSize=config.getPieceSize();
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
    public void setPiece(int index, byte [] payload) throws IOException {
    	String filename="/Users/qibing/Desktop/peer_"+peerProcess.id;
    	File newfile=new File(filename);
    	if (!newfile.exists()){
    		newfile.mkdirs();
    	}
    		
    	RandomAccessFile file1=new RandomAccessFile(filename,"rw");
    	file1.setLength(fileSize);
    	
    	int pierce_length=calculate_length(index);
    	
    	long place=(long)(index*pierceSize);
    	file1.seek(place);
    	
    	file1.write(payload);
    	
    }
    
    public static void main(String[] args){
    	//System.out.println(divdier(10000232,32768));
    }
}
