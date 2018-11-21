import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.*;

public class FileManager {
    String filename;
    private RandomAccessFile file;
	static int fileSize;
	static int pierceSize;
	static int total_piece_size;
    static ReentrantLock mutex = new ReentrantLock(true);

    FileManager(String filename) throws Exception {
		this.filename = filename;
		fileSize = peerProcess.config.getFileSize();
		pierceSize = peerProcess.config.getPieceSize();
		total_piece_size = (fileSize + pierceSize - 1) / pierceSize;

		File newfile = new File(filename);
    	if (!newfile.exists()){
    		newfile.getParentFile().mkdirs();
		}

    	file = new RandomAccessFile(filename,"rw");
    }

    static int calculate_length(int index){
    	if (fileSize % pierceSize != 0 && index == total_piece_size - 1){
    		return fileSize % pierceSize;
    	}
    	return pierceSize;
    }

    public byte [] getPiece(int index) throws Exception {
		int piece_length = calculate_length(index);
		long place = ((long)index) * pierceSize;
		byte [] buf = new byte[piece_length];
		mutex.lock();
    	file.seek(place);
		int l = file.read(buf);
		mutex.unlock();
		if (l < piece_length) {
			throw new Exception("failed to read expected length of file");
		}
        return buf;
    }

    //will be called when the peer received such a pierce to write the data into disk
    public void setPiece(int index, byte [] payload) throws Exception {
		int piece_length = calculate_length(index);
		if (payload.length < piece_length) {
			throw new Exception("failed to read expected length of file");
		}
		long place = ((long)index) * pierceSize;
    	mutex.lock();
    	file.seek(place);
    	file.write(payload);
    	mutex.unlock();
        return;
    }

    public static void main(String[] args){
    	//System.out.println(divdier(10000232,32768));
    }
}
