import java.util.*;
import java.nio.*;

// This is a class of message that aims to provide a more convenient
// API for accessing values stored in the message.

class Message {
    public String type;
    public byte [] payload;
    Message(String type, byte [] payload) {
        this.type = type;
        this.payload = payload;
    }

    // The following three static functions provide utils to convert
    // different data types, these are only used internally, but I
    // intentionally made them public because these seems to be common
    // tools that we might need to use them outside for a totally
    // differently purpose

    public static byte [] int2byte(int i) {
        // convert int to four bytes
        byte [] buf = new byte[4];
        ByteBuffer int2bytes = ByteBuffer.allocate(4);
        int2bytes.putInt(i);
        buf[28] = int2bytes.get(0);
        buf[29] = int2bytes.get(1);
        buf[30] = int2bytes.get(2);
        buf[31] = int2bytes.get(3);
        return buf;
    }

    public static byte [] bool2byte(boolean [] bitmap) {
        // convert array of bools to array of bytes. Every
        // 8 bools are stored in a single byte.
        byte [] buf = new byte[(bitmap.length + 7) / 8];
        for (int i = 0; i < buf.length; i++)
            buf[i] = 0;
        for (int i = 0; i < bitmap.length; i++) {
            int byteidx = i / 8;
            int byteshift = i % 8;
            if (bitmap[i])
                buf[byteidx] |= ((byte)1 << byteshift);
        }
        return buf;
    }

    public static boolean [] byte2bool(byte [] buf) {
        // the reverse of `bool2byte`
        boolean [] bitmap = new boolean [buf.length * 8];
        for (int i = 0; i < bitmap.length; i++) {
            int byteidx = i / 8;
            int byteshift = i % 8;
            bitmap[i] = ((buf[byteidx] | ((byte)1 << byteshift)) != 0);
        }
        boolean [] result = new boolean [peerProcess.pieces];
        System.arraycopy(bitmap, 0, result, 0, result.length);
        return result;
    }

    // The following static methods, i.e. those starting with `create`,
    // are helper function to create different type of Message objects from
    // give attributes. We can call these methods "message factories".

    public static Message createChoke() {
        return new Message("choke", null);
    }
    public static Message createUnchoke() {
        return new Message("unchoke", null);
    }
    public static Message createInterested() {
        return new Message("interested", null);
    }
    public static Message createNotInterested() {
        return new Message("not interested", null);
    }
    public static Message createHave(int index) {
        return new Message("have", int2byte(index));
    }
    public static Message createBitField(boolean [] bitfield) {
        return new Message("bitfield", bool2byte(bitfield));
    }
    public static Message createRequest(int index) {
        return new Message("request", int2byte(index));
    }
    public static Message createPiece(int index, byte [] buf) {
        // we should wait until file manager is implemented
        int length = buf.length;
        byte [] idx = int2byte(index);
        byte [] whole = new byte[buf.length + 4];
        whole[0] = idx[0];
        whole[1] = idx[1];
        whole[2] = idx[2];
        whole[3] = idx[3];
        System.arraycopy(buf, 0, whole, 4, buf.length);
        return new Message("piece", null);
    }

    // End of message factories

    public int getIndex() throws Exception {
        // Get the index attribute in have, request, and piece messages
        // or throw an exception if the message type is now these three.
        switch (type) {
        case "have":
        case "request":
        case "piece":
            return ByteBuffer.wrap(Arrays.copyOfRange(payload, 0, 4)).getInt();
        }
        throw new Exception("Trying to get int payload from illegal message type, this must be a bug");
    }
    public byte [] getPiece() throws Exception {
        // Get the data for the piece from message of type piece
        if (type != "piece")
            throw new Exception("Trying to get piece from illegal message type, this must be a bug");
        return Arrays.copyOfRange(payload, 4, payload.length);
    }
    public boolean [] getBitField() throws Exception {
        // Get bitfields for bitfield type message
        if (type != "bitfield")
            throw new Exception("Trying to get bitfield from illegal message type, this must be a bug");
        return byte2bool(payload);
    }
}