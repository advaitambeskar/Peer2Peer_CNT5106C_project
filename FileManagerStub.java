import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileManagerStub {
    static final int sleep = 100;
    FileManagerStub(String name) {
        return;
    }
    public byte [] getPiece(int index) throws Exception {
        Thread.sleep(sleep);
        return null;
    }
    public void setPiece(int index, byte [] payload) throws Exception {
        Thread.sleep(sleep);
    }
}