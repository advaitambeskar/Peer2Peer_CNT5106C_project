import java.net.*;
import java.io.*;

public class Client {

    public static void connectTo(int peerid) {
        try {
            Peer peer = peerProcess.peers.get(peerid);
            Socket socket = new Socket(peer.host, peer.port);
            new Handshake(socket).run();
            peerProcess.logger.connectionToPeer(peerid);
        } catch (Exception e) {
            peerProcess.logger.logDebug("Exception raised at client when trying to establish connection to servers: " + e);
        }
    }
}