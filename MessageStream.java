import java.net.*;

public class MessageStream {
    Socket socket;
    MessageStream(Socket socket) {
        this.socket = socket;
    }
}