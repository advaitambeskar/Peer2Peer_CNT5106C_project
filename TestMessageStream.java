import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.*;
import java.nio.*;

public class TestMessageStream {
    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(12345);
        Socket socket1 = new Socket("localhost", 12345);
        Socket socket2 = listener.accept();
        DataInputStream input1 = new DataInputStream(socket1.getInputStream());
        DataOutputStream output1 =  new DataOutputStream(socket1.getOutputStream());
        MessageStream msgstream1 =  new MessageStream(null, socket1, input1, output1);
        DataInputStream input2 = new DataInputStream(socket2.getInputStream());
        DataOutputStream output2 =  new DataOutputStream(socket2.getOutputStream());
        // socket1 is for raw data, socket2 is for message

        // test send choke message
        msgstream1.send(Message.createChoke());
        byte [] buf = new byte[5];
        int length = input2.read(buf);
        if (
            length < 5 ||
            buf[0] != 0 ||
            buf[1] != 0 ||
            buf[2] != 0 ||
            buf[3] != 1 ||
            buf[4] != 0
        ) {
            System.out.println("error on sending choke message");
            System.exit(1);
        }
        System.out.println("Sending choke test pass");

        // test receive choke message
        buf = new byte[] { 0, 0, 0, 1, 0};
        output2.write(buf);
        Message msg = msgstream1.next();
        if (
            msg.type != "choke" ||
            msg.payload.length != 0
        ) {
            System.out.println("error on receiving choke message");
            System.exit(1);
        }
        System.out.println("Receiving choke test pass");
    }
}