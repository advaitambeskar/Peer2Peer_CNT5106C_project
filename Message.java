class Message {
    public String type;
    public byte [] payload;
    Message(String type, byte [] payload) {
        this.type = type;
        this.payload = payload;
    }
}