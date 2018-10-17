import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.text.*;

public class LoggerFileSaver{

    LoggerFileSaver(int peerNumber){
        filePath = "./log/"+ Integer.toString(this.peerNumber);
        System.out.println(filePath);
    }

    public static void fileWrite(String log){
        
    }
}