public class PeerThread extends Thread {
    Peer peer;
    PeerThread(Peer peer) {
        this.peer = peer;
    }
    public void run() {
        while (!peerProcess.done.get()) {
            // TODO
        }
        return;
    }
}