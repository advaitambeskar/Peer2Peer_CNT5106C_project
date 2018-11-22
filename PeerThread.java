import java.io.*;

public class PeerThread extends Thread {
    Peer peer;
    PeerThread(Peer peer) {
        this.peer = peer;
    }
    public void run() {
        try {
            while (true) {
                Message msg = peer.msgstream.next();
                switch (msg.type) {
                case "choke":
                    peer.choked_by.set(true);
                    peerProcess.logger.chokedLog(peer.id);
                    break;
                case "unchoke":
                    peer.choked_by.set(false);
                    peerProcess.logger.unchokedLog(peer.id);
                    if(peer.interested_in()) {
                        peer.requestNewPiece();
                    }
                    break;
                case "interested":
                    peer.interested_by.set(true);
                    peerProcess.logger.messageInterestedLog(peer.id);
                    break;
                case "not interested":
                    peer.interested_by.set(false);
                    peerProcess.logger.messageNotInterestedLog(peer.id);
                    break;
                case "have":
                    peer.bitfield_mutex.lock();
                    peer.bitfield[msg.getIndex()] = true;
                    peer.bitfield_mutex.unlock();
                    peer.sendInterestedOrNotInterested();
                    peerProcess.logger.messageHaveLog(peer.id, msg.getIndex());
                    break;
                case "bitfield":
                    peer.bitfield_mutex.lock();
                    peer.bitfield = msg.getBitField();
                    peerProcess.logger.logBitField(peer.id, msg.getBitField());
                    peer.bitfield_mutex.unlock();
                    peer.sendInterestedOrNotInterested();
                    break;
                case "request":
                    if(peer.top_k.get() || peer.optim_selected.get()) {
                        int pieceid = msg.getIndex();
                        peerProcess.logger.logRequest(pieceid, peer.id);
                        peer.sendPiece(pieceid);
                    }
                    break;
                case "piece":
                    {
                        int pieceid = msg.getIndex();
                        // set my bitfield
                        peerProcess.bitfield_mutex.lock();
                        peerProcess.bitfield[pieceid] = true;
                        peerProcess.bitfield_mutex.unlock();
                        // log this event
                        peerProcess.logger.pieceDownload(peer.id, pieceid);
                        // send have to everyone
                        for(Peer p : peerProcess.peers.values()) {
                            if (p.msgstream == null) continue;
                            p.msgstream.send(Message.createHave(pieceid));
                        }
                        // increase counter
                        peer.recv_count.addAndGet(1);
                        // write this peice to file
                        peerProcess.filemanager.setPiece(pieceid, msg.getPiece());
                        // request another piece
                        if(!peer.choked_by.get() && peer.interested_in()) {
                            peer.requestNewPiece();
                        }
                    }
                    break;
                case "terminate":
                    System.exit(0);
                    break;
                }
                peer.checkDone();
            }
        } catch (Exception e) {
            StringWriter outError = new StringWriter();
            e.printStackTrace(new PrintWriter(outError));
            peerProcess.logger.logDebugPeer(peer.id, "Exception raised in PeerThread: " + outError.toString());
        }
    }
}