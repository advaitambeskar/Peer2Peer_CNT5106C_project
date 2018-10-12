import java.net.*;
import java.io.*;

public class MessageStream {
    DataInputStream input;
    DataOutputStream output;
    MessageStream(DataInputStream input, DataOutputStream output) {
        this.input = input;
        this.output = output;
    }
    Message next() {
        return new Message("TODO", new byte[]{});
    }
    void send(Message msg) {
        return;
    }
}