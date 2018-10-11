import java.net.*;

public class MessageStream {
    Socket socket;
    MessageStream(Socket socket) {
        this.socket = socket;
    }
    Message next() {
        return new Message("TODO", new byte[]{});
    }
    void send(Message msg) {
        return;
    }
}