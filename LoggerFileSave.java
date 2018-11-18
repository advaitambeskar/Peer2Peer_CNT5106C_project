import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.text.*;

public class LoggerFileSave{
    

    public void fileOpenWriteClose(int peerNumber, String log){
        try{
            FileWriter file = null;
            String path = "./log/" + Integer.toString(peerNumber) + ".log";
            boolean append_to_file = true;
            file = new FileWriter(path, append_to_file);
            PrintWriter print = new PrintWriter(file);
            System.out.println(path);
            print.println(log);
            print.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
        //System.out.println(filePath);
    }
}