public class Config {
    public int getPort() {
        return peerProcess.id;
    }
    public Peer[] getPeers() {
        return new Peer[] {
            new Peer(11001, "localhost", 11001),
            new Peer(11002, "localhost", 11002),
            new Peer(11003, "localhost", 11003),
        };
    }
}