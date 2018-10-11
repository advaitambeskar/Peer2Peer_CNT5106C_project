import java.net.*;

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
        // TODO: implement handshake
        int peerid = 0;
        Peer peer = peerProcess.peers.get(peerid);
        peer.msgstream = new MessageStream(connection);
        peer.thread = new PeerThread(peer);
        peer.thread.start();
    }
}

class Server extends Thread {
    public void run() {
        ServerSocket listener = new ServerSocket(peerProcess.config.getPort());
        try {
            while (!peerProcess.done.get())
                new HandshakeThread(listener.accept()).start();
        } catch (Exception e) {}
        listener.close();
    }
}