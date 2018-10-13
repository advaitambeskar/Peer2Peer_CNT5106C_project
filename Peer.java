import java.net.*;
import java.util.*;
import java.util.concurrent.atomic.*;

public class Peer {
    public int id;
    public String host;
    public int port;

    Peer(int id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
        bitfield = new boolean [peerProcess.pieces];
        Arrays.fill(bitfield, false);
    }

    MessageStream msgstream = null;
    AtomicBoolean selected = new AtomicBoolean(false);
    AtomicBoolean done = new AtomicBoolean(false);
    PeerThread thread = null;
    boolean [] bitfield;
    public void checkDone() {
        for (boolean b : bitfield) {
            if (!b) {
                done.set(false);
                return;
            }
        }
        done.set(true);
    }
}