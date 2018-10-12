public class PeerThread extends Thread {
    Peer peer;
    PeerThread(Peer peer) {
        this.peer = peer;
    }
    public void run() {
        try {
            while (!peerProcess.done.get()) {
                peer.done.set(true);
                Thread.sleep(1000);
            }
            peerProcess.logger.logDebug("PeerThread: exit normally");
        } catch (Exception e) {
            peerProcess.logger.logDebug("Exception raised in PeerThread: " + e);
        }
    }
}