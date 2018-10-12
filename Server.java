import java.net.*;

// Server just listen to the port and wait for client to connect.
// After accepting connections from client, it will do handshake,
// and then set the sockets of peers in `peerProcess.peers`. No
// other parts of protocols are implemented here.

class Server extends Thread {
    public void run() {
        try {
            ServerSocket listener = new ServerSocket(peerProcess.config.getPort());
            listener.setSoTimeout(1000);
            while (!peerProcess.done.get()) {
                try {
                    new HandshakeThread(listener.accept()).start();
                    peerProcess.logger.logDebug("Server: new connection accepted");
                } catch (SocketTimeoutException e) {
                    peerProcess.logger.logDebug("Server: timeout");
                }
            }
            listener.close();
            peerProcess.logger.logDebug("Server: exit");
        } catch (Exception e) {
            peerProcess.logger.logDebug("Exception raised in server thread: " + e);
        }
    }
}