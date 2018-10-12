import java.net.*;
import java.util.concurrent.atomic.*;

public class Peer {
    public int id;
    public String host;
    public int port;

    Peer(int id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
    }

    MessageStream msgstream = null;
    AtomicBoolean selected = new AtomicBoolean(false);
    AtomicBoolean done = new AtomicBoolean(false);
    PeerThread thread = null;
}