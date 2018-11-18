import java.util.*;
import java.util.concurrent.atomic.*;

public class peerProcess {
    static int id;
    static Config config = new Config();
    static Logger logger;
    static HashMap<Integer, Peer> peers = new HashMap<Integer, Peer>();
    static Server server = null;
    static AtomicBoolean done = new AtomicBoolean(false);
    static final int pieces = 100;  // TODO: this is only a stub
    static FileManager filemanager;

    public static void checkIfDone() {
        peerProcess.logger.logDebug("peerProcess: checkIfDone");
        for (Peer peer : peers.values()) {
            if (!peer.done.get()) {
                peerProcess.logger.logDebug("peerProcess: Peer " + peer.id + " not done");
                done.set(false);
                return;
            }
        }
        peerProcess.logger.logDebug("peerProcess: done");
        done.set(true);
    }

    public static void main(String[] args) throws Exception {
        id = Integer.parseInt(args[0]);
        logger = new Logger(id);
        filemanager = new FileManager(config.getFileName());

        // initialize peers
        for (Peer peer : config.getPeers()) {
            if (peer.id != id)
                peers.put(peer.id, peer);
        }

        // start server
        server = new Server();
        server.start();

        // connect to peers
        for (Peer peer : peers.values())
            if (peer.id < id)
                Client.connectTo(peer.id);

        // main loop
        while (!done.get()) {
            checkIfDone();
            Thread.sleep(1000);
        }
    }
}