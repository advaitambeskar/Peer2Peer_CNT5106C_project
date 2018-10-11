public class Config {
    public int getPort() {
        return 0;
    }
    public Peer[] getPeers() {
        return new Peer[] {
            new Peer(1001,"localhost", 1001),
            new Peer(1002,"localhost", 1002),
        };
    }
}