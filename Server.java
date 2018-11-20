import java.net.*;

// Server just listen to the port and wait for client to connect.
// After accepting connections from client, it will do handshake,
// and then set the sockets of peers in `peerProcess.peers`. No
// other parts of protocols are implemented here.

class Server extends Thread {
    boolean done() {
        for(Peer p:peerProcess.peers.values()) {
            if(p.msgstream == null || p.thread==null)
                return false;
        }
        return true;
    }

    public void run() {
        try {
            ServerSocket listener = new ServerSocket(peerProcess.config.getPort());
            listener.setSoTimeout(100);
            while (!done()) {
                try {
                    int peerid = new Handshake(listener.accept()).run();
                    peerProcess.logger.connectionFromPeer(peerid);
                } catch (SocketTimeoutException e) {}
            }
            peerProcess.logger.logDebug("Everybody is connected, closing server.");
            listener.close();
        } catch (Exception e) {
            peerProcess.logger.logDebug("Exception raised in server thread: " + e);
        }
    }
}