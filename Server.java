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
        peerProcess.peers.get(peerid).msgstream = new MessageStream(connection);
    }
}

class Server extends Thread {
    public void run() {
        try {
            ServerSocket listener = new ServerSocket(peerProcess.config.getPort());
            while (!peerProcess.done.get())
                new HandshakeThread(listener.accept()).start();
        } catch (Exception e) {}
    }
}