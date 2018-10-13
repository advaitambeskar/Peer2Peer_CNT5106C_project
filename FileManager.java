public class FileManager {
    String filename;
    FileManager(String filename) {
        this.filename = filename;
    }
    public byte [] getPiece(int index) {
        return new byte[]{};
    }
    public int getPieceLength(int index) {
        return 100;
    }
    public void setPiece(int index, byte [] payload) {
        return;
    }
}
