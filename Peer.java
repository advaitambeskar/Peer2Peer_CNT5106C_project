import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.*;

public class Peer {
    public int id;
    public String host;
    public int port;
    boolean [] bitfield;
    ReentrantLock bitfield_mutex = new ReentrantLock(true);
    AtomicBoolean interested_by = new AtomicBoolean(false);  // this peer is interested in me
    AtomicBoolean choked_by = new AtomicBoolean(true);  // I am choked by this peer
    AtomicBoolean top_k = new AtomicBoolean(false);  // this peer is my top k
    AtomicBoolean optim_selected = new AtomicBoolean(false);  // I optimistically selected this peer
    AtomicInteger recv_count = new AtomicInteger(0);

    Peer(int id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
        bitfield = new boolean[peerProcess.pieces];
        for(int i=0;i<peerProcess.pieces;i++) {
            bitfield[i] = false;
        }
    }

    MessageStream msgstream = null;
    AtomicBoolean selected = new AtomicBoolean(false);
    AtomicBoolean done = new AtomicBoolean(false);
    PeerThread thread = null;
    boolean newly_selected_topk = false;
    boolean newly_selected_optim = false;

    public void checkDone() {
        boolean result = true;
        bitfield_mutex.lock();
        for(boolean i : bitfield) {
            if (!i) {
                result = false;
                break;
            }
        }
        bitfield_mutex.unlock();
        done.set(result);
    }

    public void sendMyBitField() throws Exception {
        peerProcess.bitfield_mutex.lock();
        peerProcess.logger.logSendBitField(id, peerProcess.bitfield);
        if (peerProcess.bitfield.length != peerProcess.pieces) {
            peerProcess.logger.logDebug("wrong bitfield length detected!");
        }
        msgstream.send(Message.createBitField(peerProcess.bitfield));
        peerProcess.bitfield_mutex.unlock();
    }

    public boolean interested_in() {
        bitfield_mutex.lock();
        peerProcess.bitfield_mutex.lock();
        boolean result = false;
        for(int i=0;i < peerProcess.pieces;i++) {
            if (!peerProcess.bitfield[i] && bitfield[i]) {
                result = true;
                break;
            }
        }
        bitfield_mutex.unlock();
        peerProcess.bitfield_mutex.unlock();
        return result;
    }

    public void sendInterestedOrNotInterested() throws Exception {
        // check if I am interested in this peer and send interested or not interested
        if (interested_in()) {
            msgstream.send(Message.createInterested());
        } else {
            msgstream.send(Message.createNotInterested());
        }
    }

    public void requestNewPiece() throws Exception {
        bitfield_mutex.lock();
        peerProcess.bitfield_mutex.lock();
        ArrayList<Integer> overlap = new ArrayList<Integer>();
        for(int i=0;i < peerProcess.pieces;i++) {
            if (!peerProcess.bitfield[i] && bitfield[i]) {
                overlap.add(i);
            }
        }
        if (overlap.size() > 0) {
            int index = ThreadLocalRandom.current().nextInt(overlap.size());
            index = overlap.get(index);
            peerProcess.logger.logSendRequest(index, id);
            msgstream.send(Message.createRequest(index));
        }
        bitfield_mutex.unlock();
        peerProcess.bitfield_mutex.unlock();
    }

    public void sendPiece(int pieceid) throws Exception {
        msgstream.send(Message.createPiece(pieceid, peerProcess.filemanager.getPiece(pieceid)));
        peerProcess.logger.logSendPiece(pieceid, id);
    }
}