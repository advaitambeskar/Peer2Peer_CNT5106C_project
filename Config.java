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

	public int getPort() {
		return peerProcess.id;
	}

    public Peer [] getPeers() {
        return new Peer [] {
            new Peer(11001, "localhost", 11001),
            new Peer(11002, "localhost", 11002),
            new Peer(11003, "localhost", 11003),
        };
    }

    public int getFileSize() {
        return 1000000;
    }

    public int getPieceSize() {
        return 100;
    }
}
