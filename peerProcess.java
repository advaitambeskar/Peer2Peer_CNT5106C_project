import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.*;

public class peerProcess {
    static int id;
    static Config config = new Config();
    static Logger logger;
    static HashMap<Integer, Peer> peers = new HashMap<Integer, Peer>();
    static Server server = null;
    static FileManager filemanager;
    static int pieces;
    static boolean [] bitfield;
    static ReentrantLock bitfield_mutex = new ReentrantLock(true);

    public static boolean done() {
        // check if peers are done
        for (Peer peer : peers.values()) {
            if (!peer.done.get()) {
                return false;
            }
        }
        // check if have all bitfields
        boolean result = true;
        bitfield_mutex.lock();
        for (boolean i : bitfield) {
            if(!i) {
                result = false;
                break;
            }
        }
        bitfield_mutex.unlock();
        return result;
    }

    public static void reselect_topk() throws Exception {
        int k = config.getNumberOfPreferredNeighbors();
        // selected top k
        int [] topk_counts = new int[k];
        Peer [] topk_peers = new Peer[k];
        for(int i=0;i<k;i++) { topk_counts[i] = 0; }
        List<Peer> all_interested_peers = new ArrayList<Peer>();
        for(Peer p : peers.values()) {
            if (p.interested_by.get())
                all_interested_peers.add(p);
        }
        Collections.shuffle(all_interested_peers);
        for(Peer p : all_interested_peers) {
            int selected_index = -1;
            int this_count = p.recv_count.get();
            for(int i=0;i<k;i++) {
                if(topk_peers[i] == null) {
                    selected_index = i;
                }
            }
            if(selected_index < 0) {
                int min = 999999999;
                for(int i=0;i<k;i++) {
                    if(topk_counts[i] < min) {
                        min = topk_counts[i];
                        selected_index = i;
                    }
                }
                if(this_count <= min) {
                    selected_index = -1;
                }
            }
            if(selected_index >= 0) {
                topk_counts[selected_index] = this_count;
                topk_peers[selected_index] = p;
            }
        }
        for(Peer p : topk_peers) {
            if(p != null) {
                p.newly_selected_topk = true;
            }
        }
        logger.preferredNeighborLog(topk_peers);
        for(Peer p : peers.values()) {
            if (p.newly_selected_topk) {
                p.newly_selected_topk = false;
                if(!p.top_k.get()) {
                    p.top_k.set(true);
                    if(p.msgstream != null) {  // in case handshake is not finished yet
                        p.msgstream.send(Message.createUnchoke());
                    }
                }
            } else {
                if(p.top_k.get() && !p.optim_selected.get()) {
                    p.top_k.set(false);
                    if(p.msgstream != null) {  // in case handshake is not finished yet
                        p.msgstream.send(Message.createChoke());
                    }
                }
            }
        }
    }

    public static void reselect_optim() throws Exception {
        // randomly select a peer and unchoke it
        List<Peer> valuesList = new ArrayList<Peer>();
        for(Peer p : peers.values()) {
            if (p.interested_by.get()) {
                valuesList.add(p);
            }
        }
        if(valuesList.size() > 0) {
            int selected = ThreadLocalRandom.current().nextInt(valuesList.size());
            Peer selectedPeer = valuesList.get(selected);
            logger.optimisticallyUnchokedLog(selectedPeer);
            for(Peer p : peers.values()) {
                if (p == selectedPeer && p.optim_selected.get()) {
                    break;
                } else if(p == selectedPeer && !p.optim_selected.get()) {
                    selectedPeer.optim_selected.set(true);
                    if(selectedPeer.msgstream != null) {  // in case handshake is not finished yet
                        selectedPeer.msgstream.send(Message.createUnchoke());
                    }
                } else if(p != selectedPeer && p.optim_selected.get() && !p.top_k.get()) {
                    p.optim_selected.set(false);
                    p.msgstream.send(Message.createChoke());
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        id = Integer.parseInt(args[0]);
        logger = new Logger();
        int filesize = config.getFileSize();
        int piecesize = config.getPieceSize();
        pieces = (filesize + piecesize - 1) / piecesize;
        bitfield = new boolean[pieces];

        // Check if I have the whole file, set bitfield
        boolean havefile = config.getIHaveFile();
        bitfield_mutex.lock();
        for(int i=0;i<pieces;i++) {
            bitfield[i] = havefile;
        }
        bitfield_mutex.unlock();
        if(havefile) logger.fileDownload();
        filemanager = new FileManager("peer_" + id + "/" + config.getFileName());

        // initialize peers
        for (Peer peer : config.getPeers()) {
            if (peer.id != id)
                peers.put(peer.id, peer);
        }

        // start server
        server = new Server();
        server.start();

        // connect to peers
        for (Peer peer : peers.values())
            if (peer.id < id)
                Client.connectTo(peer.id);

        // sleep until everybody is up
        server.join();
        logger.logDebug("Everyone is connected, continue");

        // send bitfield
        if (havefile) {
            for (Peer peer : peers.values())
                peer.sendMyBitField();
        }

        // main loop
        int sleep_ms = 100;
        int unchoking_count = config.getUnchokingInterval() * 1000 / sleep_ms;
        int optim_select_count = config.getOptimisticUnchokingInterval() * 1000 / sleep_ms;
        int tick = 0;
        while (!done()) {
            Thread.sleep(sleep_ms);
            if(tick % unchoking_count == 0) {
                reselect_topk();
            }
            if(tick % optim_select_count == 0) {
                reselect_optim();
            }
            tick++;
        }
        logger.logDebug("everybody is done, exiting");
        for(Peer p : peers.values()) {
            if(p.msgstream.connection != null)
                p.msgstream.connection.close();
        }
        System.exit(0);
    }
}
