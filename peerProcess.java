import java.util.*;
import java.util.concurrent.atomic.*;

public class peerProcess {
    static int id;
    static Config config = new Config();
    static Logger logger = new Logger();
    static HashMap<Integer, Peer> peers = new HashMap<Integer, Peer>();
    static Server server = null;
    static AtomicBoolean done = new AtomicBoolean(false);

    public static void main(String[] args) throws Exception {
        id = Integer.parseInt(args[0]);
        logger.logDebug("this is for debugging only");

        // initialize peers
        for (Peer peer : config.getPeers()) {
            peers.put(peer.id, peer);
        }

        // start server
        server = new Server();
        server.start();

        // connect to peers
        for (Peer peer : peers.values())
            if (peer.id < id)
                Client.connectTo(peer.id);
    }
}