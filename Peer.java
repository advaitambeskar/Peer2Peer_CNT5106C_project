import java.net.*;

public class Peer {
    public int id;
    public String host;
    public int port;

    Peer(int id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
    }

    public Socket socket = null;
}