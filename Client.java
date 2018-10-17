import java.net.*;
import java.io.*;

public class Client {

    public static void connectTo(int peerid) {
        try {
            Peer peer = peerProcess.peers.get(peerid);
            Socket socket = new Socket(peer.host, peer.port);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output =  new DataOutputStream(socket.getOutputStream());
            HandshakeThread.sendHandshake(output);
            HandshakeThread.consumeHandshake(input);
            peer.msgstream = new MessageStream(input, output);
            peer.thread = new PeerThread(peer);
            peer.thread.start();
            peerProcess.logger.connectionToPeer(peerid);
        } catch (Exception e) {
            peerProcess.logger.logDebug("Exception raised at client when trying to establish connection to servers: " + e);
        }
    }
}