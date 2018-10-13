public class PeerThread extends Thread {
    Peer peer;
    PeerThread(Peer peer) {
        this.peer = peer;
    }
    public void run() {
        try {
            while (!peerProcess.done.get()) {
                for (int i = 0; i < peerProcess.pieces; i++) {
                    peer.bitfield[i] = true;
                    peerProcess.bitfield[i].set(true);
                }
                Thread.sleep(1000);
                peer.checkDone();
            }
            peerProcess.logger.logDebug("PeerThread: exit normally");
        } catch (Exception e) {
            peerProcess.logger.logDebug("Exception raised in PeerThread: " + e);
        }
    }
}