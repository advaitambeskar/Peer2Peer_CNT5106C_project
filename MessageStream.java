import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.*;
import java.nio.*;

// `MessageStream` wraps `DataInputStream` and `DataOutputStream` to provide API for
// getting a the next `Message` object in a blocking manner, as well sending a `Message`
// object by serializing it using the format specified in the document.
// The only API provided by this class is `next` and `send`, where `next` blockingly get
// a `Message` object from the stream, and `send` send a `Message` object to the stream.

public class MessageStream {
    Peer peer;
    Socket connection;
    DataInputStream input;
    DataOutputStream output;
    ReentrantLock mutex = new ReentrantLock(true);  // To make sure that only one thread are sending messages.
    MessageStream(Peer peer, Socket connection, DataInputStream input, DataOutputStream output) {
        this.connection = connection;
        this.peer = peer;
        this.input = input;
        this.output = output;
    }
    private static String getMessageTypeString(byte type) throws Exception {
        // Convert the message type from byte to string. The mapping is given in the document.
        // this is pravite, and will only used internally in this class.
        switch (type) {
        case 0:
            return "choke";
        case 1:
            return "unchoke";
        case 2:
            return "interested";
        case 3:
            return "not interested";
        case 4:
            return "have";
        case 5:
            return "bitfield";
        case 6:
            return "request";
        case 7:
            return "piece";
        }
        throw new Exception("is this possible? type = " + type);
    }

    public static String buf2hex(byte []buf) {
        String log = "";
        for(byte b:buf) {
            log += String.format("%02X ", b);
        }
        return log;
    }

    Message next() throws Exception {
        // Get the next document blockingly.
        // Messages have format [length][type][payload], here in this function,
        // we construct `Message` objects through from bytes in this format.
        // peerProcess.logger.logDebug("waiting for next message");
        // get message length
        byte [] lengthbuf = new byte[4];
        int bytes_read = input.read(lengthbuf);
        if(bytes_read < 4) {  // connection closed.
            connection.close();
            connection = null;
            return new Message("terminate", null);  // special message to terminate thread.
        }
        int length = Message.pack(lengthbuf[0], lengthbuf[1], lengthbuf[2], lengthbuf[3]);
        String length_in_bytes = "[" + buf2hex(lengthbuf) + "]";
        // read message type
        byte [] typebuf = new byte[1];
        input.read(typebuf);
        peerProcess.logger.logDebugPeer(peer.id, "recv msg length = " + length + length_in_bytes + ", type = " + typebuf[0]);
        String type = getMessageTypeString(typebuf[0]);
        // read the rest of the message
        length--;
        byte [] buf = new byte[length];
        int pos = 0;
        while(pos < length)
            pos += input.read(buf, pos, length - pos);
        // create message
        return new Message(type, buf);
    }

    private void sendWithTypePayload(byte type, byte [] payload) throws Exception {
        // Messages have format [length][type][payload], here in this function,
        // the type and payload is given, length is automatically computed, and
        // the final bytes are computed and sent throught output stream.
        // this is pravite, and will only used internally in this class.
        // This is public, and designed to be used externally
        if (payload == null)
            payload = new byte[0];
        int length = 1 + payload.length;
        byte [] buf = new byte[payload.length + 5];
        byte [] lengthbuf = Message.int2byte(length);
        peerProcess.logger.logDebugPeer(peer.id, "send msg length = " + length + ", type = " + type);
        buf[0] = lengthbuf[0];
        buf[1] = lengthbuf[1];
        buf[2] = lengthbuf[2];
        buf[3] = lengthbuf[3];
        buf[4] = type;
        System.arraycopy(payload, 0, buf, 5, payload.length);
        if (peer != null)
            peerProcess.logger.logSendRawMsg(peer.id, buf);
        mutex.lock();
        output.write(buf, 0, buf.length);
        mutex.unlock();
    }

    void send(Message msg) throws Exception {
        // Given `Message` object, convert it to bytes and send it using output stream
        // This is public, and designed to be used externally
        switch (msg.type) {
        case "choke":
            sendWithTypePayload((byte)0, null);
            return;
        case "unchoke":
            sendWithTypePayload((byte)1, null);
            return;
        case "interested":
            sendWithTypePayload((byte)2, null);
            return;
        case "not interested":
            sendWithTypePayload((byte)3, null);
            return;
        case "have":
            sendWithTypePayload((byte)4, msg.payload);
            return;
        case "bitfield":
            sendWithTypePayload((byte)5, msg.payload);
            return;
        case "request":
            sendWithTypePayload((byte)6, msg.payload);
            return;
        case "piece":
            sendWithTypePayload((byte)7, msg.payload);
            return;
        }
        return;
    }
}