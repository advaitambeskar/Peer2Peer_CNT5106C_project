import java.net.*;
import java.io.*;
import java.nio.*;
import java.util.*;

// Server just listen to the port and wait for client to connect.
// After accepting connections from client, it will do handshake,
// and then set the sockets of peers in `peerProcess.peers`. No
// other parts of protocols are implemented here.

class HandshakeThread extends Thread {
    public Socket connection;
    HandshakeThread(Socket connection) {
        this.connection = connection;
    }
    public void run() {
        try {
            DataInputStream input = new DataInputStream(connection.getInputStream());
            DataOutputStream output =  new DataOutputStream(connection.getOutputStream());

            byte [] buf = new byte[32];
            input.read(buf);
            String header = new String(Arrays.copyOfRange(buf, 0, 19));
            if (header != "P2PFILESHARINGPROJ") {
                peerProcess.logger.logDebug("Bad handshake header!");
                return;
            }
            int peerid = ByteBuffer.wrap(Arrays.copyOfRange(buf, 28, 32)).getInt();
            peerProcess.logger.logConnectedFrom(peerid);

            Peer peer = peerProcess.peers.get(peerid);
            peer.msgstream = new MessageStream(input, output);
            peer.thread = new PeerThread(peer);
            peer.thread.start();
        } catch (Exception e) {
            peerProcess.logger.logDebug("Exception raised during handshake!");
        }
    }
}

class Server extends Thread {
    public void run() {
        try {
            ServerSocket listener = new ServerSocket(peerProcess.config.getPort());
            while (!peerProcess.done.get())
                new HandshakeThread(listener.accept()).start();
            listener.close();
        } catch (Exception e) {
            peerProcess.logger.logDebug("Exception raised in server thread!");
        }
    }
}