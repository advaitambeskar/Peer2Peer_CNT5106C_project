import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.text.*;
import java.nio.file.*;

/*
    
*/

public class Logger{
    boolean debug = true;

    /*
        LoggerFileSave log_save = new LoggerFileSave();
    */
    String fileStatus;
    PrintWriter print;

    Logger() throws Exception {
        FileWriter file = null;
        String path = "./log/log_peer_" + Integer.toString(peerProcess.id) + ".log";
        boolean append_to_file = true;
        try {
            File f = new File("./log");
            f.mkdirs();
            Files.createFile(Paths.get(path));
        } catch (FileAlreadyExistsException ex) { }
        file = new FileWriter(path, append_to_file);
        print = new PrintWriter(file);
    }

    /*
        Extra function to log timeout status
     */

    public static String timeFinder(){
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss.SSS");

        String time = ft.format(date);
        return time;
    }

    public void logDebug(String s) {
        if (debug) {
            String time = timeFinder();
            String log = time + ": [" + peerProcess.id + "] " + s;
            System.out.println(log);
            print.println(log); print.flush();
        }
    }


    /*
        TCP Connection Log  
     */

    public boolean  connectionToPeer(int destinationID) throws Exception{
        int sourceID = peerProcess.id;
        /*
             Function Description
            ----------------------
            Name            connectionToPeer
            Return type     boolean
            Description     prints the log when connection is made

             Data Variables
            ----------------
            Datatype        VariableName        Information
            int             sourceID            provides peer ID of the Host
            int             destinationID       provides peer ID of the destination
            String          time                provides time of connection, in String
            
        */
        String time = timeFinder();
        String log = time + ": Peer " + Integer.toString(sourceID) + " makes connection to Peer " + Integer.toString(destinationID);
        System.out.println(log);
        
        print.println(log); print.flush();
        return true;
    }

    public boolean  connectionFromPeer(int sourceID) throws Exception{
        int destinationID = peerProcess.id;
        /*
             Function Description
            ----------------------
            Name            messageLogger
            Return type     boolean
            Description     prints the log of the connection status

             Data Variables
            ----------------
            Datatype        VariableName        Information
            int             sourceID            provides peer ID of the Host
            int             destinationID       provides peer ID of the destination
            String          time                provides time of connection, in String
            
        */
        String time = timeFinder();
        String log = time + ": Peer " + Integer.toString(destinationID) + " is connected from Peer " + Integer.toString(sourceID);
        System.out.println(log);

        print.println(log); print.flush();
        return true;
    }


    /*
        change of preferred neigbours
     */
    
    public boolean  preferredNeighborLog(Peer [] preferredList) throws Exception{
        /*
             Function Description
            ----------------------
            Name            messageLogger
            Return type     boolean
            Description     prints the log of the connection status

             Data Variables
            ----------------
            Datatype        VariableName        Information
            boolean         connectionStatus    boolean provides you with connection status
            String          sourceID            provides peer ID of the Host
            String          destinationID       provides peer ID of the destination
            String          time                provides time of connection, in String
            
        */
        int sourceID = peerProcess.id;
        String time = timeFinder();
        String preferredListString = "";
        for(int i = 0; i < preferredList.length; i++){
            Peer p = preferredList[i];
            if (p != null)
                preferredListString = preferredListString  + Integer.toString(p.id) + ", ";
        }
        int length_preferredListString = preferredListString.length();
        if (preferredListString.length() > 0)
            preferredListString = preferredListString.substring(0, length_preferredListString-2);

        String log = time + ": Peer " +  Integer.toString(sourceID) + " has the preferred neighbors " + preferredListString;

        System.out.println(log);

        
        print.println(log); print.flush();
        return true;
    }


    /*
        change of optimistically unchoked neighbor
     */
    
    public boolean optimisticallyUnchokedLog(Peer unchokedID){
        /*
             Function Description
            ----------------------
            Name            optimisticallyUnchokedLog
            Return type     boolean
            Description     prints the log of the source peers optimistically unchoked neighbor

             Data Variables
            ----------------
            Datatype     VariableName        Information
            int          sourceID            provides peer ID of the Host
            int          destinationID       provides peer ID of the destination
            String       time                provides time of connection, in String
            
        */
        int sourceID = peerProcess.id;
        String time = timeFinder();

        String log = time +": Peer " + Integer.toString(sourceID) + " has the optimistically unchoked neighbor " + Integer.toString(unchokedID.id);
        System.out.println(log);

        
        print.println(log); print.flush();
        return true;
    }

    /*
        unchoking
     */

    public boolean unchokedLog(int unchokedID){
        /*
             Function Description
            ----------------------
            Name            unchokedLog
            Return type     boolean
            Description     prints the log of the source peer unchoked by another peer

             Data Variables
            ----------------
            Datatype     VariableName        Information
            int          sourceID            provides peer ID of the Host
            int          unchokedID          provides peer ID of the destination
            String       time                provides time of connection, in String
            
        */
        int sourceID = peerProcess.id;
        String time = timeFinder();

        String log = time + ": Peer "+ sourceID + " is unchoked by Peer " + unchokedID + ".";
        System.out.println(log);

        
        print.println(log); print.flush();
        return true;
    }


    /*
     *  choking
     */

    public boolean chokedLog(int chokedID){
       /*
             Function Description
            ----------------------
            Name            chokedLog
            Return type     boolean
            Description     prints the log of the source peer choked by another peer

             Data Variables
            ----------------
            Datatype     VariableName        Information
            int          sourceID            provides peer ID of the Host
            int          chokedID            provides peer ID of the destination
            String       time                provides time of connection, in String
            
        */
        int sourceID = peerProcess.id;
        String time = timeFinder();

        String log = time + ": Peer "+ sourceID + " is choked by Peer " + chokedID;
        System.out.println(log);

        
        print.println(log); print.flush();
        return true;
    }


    /*
        receiving messages
     */

    public boolean messageHaveLog(int sourceID, int pieceIndex){
        /*
             Function Description
            ----------------------
            Name            messageHaveLog
            Return type     boolean
            Description     prints the log when destinationID sends the have message to source

             Data Variables
            ----------------
            Datatype        VariableName        Information
            int             sourceID            provide the peer ID of host
            int             destinationID       provide the peer ID of destination
            int             pieceIndex          provide index of piece destination has
            String          time                time of activity
         */
        int destinationID = peerProcess.id;
        String time = timeFinder();
        String log = time + ": Peer " + destinationID + " received the 'have' message from Peer "+ sourceID + " for the piece " + pieceIndex + ".";
        System.out.println(log);

        
        print.println(log); print.flush();
        return true;
    }

    public boolean messageInterestedLog(int sourceID){
        /*
             Function Description
            ----------------------
            Name            messageInterestedLog
            Return type     boolean
            Description     prints the log when destinationID sends the 'interested' message to source

             Data Variables
            ----------------
            Datatype        VariableName        Information
            int             sourceID            provide the peer ID of host
            int             destinationID       provide the peer ID of destination
            String          time                time of activity
         */
        int destinationID = peerProcess.id;
        String time = timeFinder();

        String log = time + ": Peer " + destinationID + " received the 'interested' message from Peer "+ sourceID + ".";
        System.out.println(log);

        
        print.println(log); print.flush();
        return true;
    }
    
    public boolean messageNotInterestedLog(int sourceID){
        /*
             Function Description
            ----------------------
            Name            messageNotInterestedLog
            Return type     boolean
            Description     prints the log when destinationID sends the 'not interested' message to source

             Data Variables
            ----------------
            Datatype        VariableName        Information
            int             sourceID            provide the peer ID of host
            int             destinationID       provide the peer ID of destination
            String          time                time of activity
         */
        int destinationID = peerProcess.id;

        String time = timeFinder();

        String log = time + ": Peer " + destinationID + " received the 'not interested' message from Peer "+ sourceID + ".";
        System.out.println(log);

        
        print.println(log); print.flush();
        return true;
    }


    /*
        Piece Download
     */
    public boolean pieceDownload(int sourceID, int pieceIndex){
        /*
             Function Description
            ----------------------
            Name            pieceDownload
            Return type     boolean
            Description     prints the log when sourceID completes downloading the piece from destinationID

             Data Variables
            ----------------
            Datatype        VariableName        Information
            int             sourceID            provide the peer ID of host
            int             destinationID       provide the peer ID of destination
            int             pieceIndex          provide index of piece downloaded
            String          time                time of activity
         */
        int destinationID = peerProcess.id;

        String time = timeFinder();

        peerProcess.bitfield_mutex.lock();
        int collectedPiece = 0;
        for(boolean i : peerProcess.bitfield) {
            if(i) collectedPiece++;
        }
        peerProcess.bitfield_mutex.unlock();

        String log = time + ": Peer " + destinationID + " has downloaded piece " + pieceIndex + " from Peer " + sourceID + ". Now the number of pieces it has is "+ collectedPiece;
        System.out.println(log);

        
        print.println(log); print.flush();
        if (collectedPiece == peerProcess.pieces) {
            fileDownload();
        }
        return true;
    }
    
    /*
        File Download
     */
    public boolean fileDownload(){
        /*
             Function Description
            ----------------------
            Name            pieceDownload
            Return type     boolean
            Description     prints the log when sourceID completes downloading the file

             Data Variables
            ----------------
            Datatype        VariableName        Information
            int             sourceID            provide the peer ID of host
            String          time                time of activity
         */
        int sourceID = peerProcess.id;

        String time = timeFinder();
        
        String log = time + ": Peer " + sourceID + " has downloaded the complete file.";

        System.out.println(log);

        
        print.println(log); print.flush();
        return true;
    }

    public void logRequest(int pieceid, int from) {
        if (debug) {
            String time = timeFinder();
            String log = time + ": Peer " + peerProcess.id + " is requested piece " + pieceid + " by " + from + ".";
            System.out.println(log);
            print.println(log); print.flush();
        }
    }

    public void logBitField(int from, boolean []bitfield) {
        if (debug) {
            String time = timeFinder();
            String log = time + ": Peer " + peerProcess.id + " received bitfield message from " + from + ": ";
            for(boolean b : bitfield) {
                log += b?'1':'0';
            }
            System.out.println(log);
            print.println(log); print.flush();
        }
    }

    public void logSendBitField(int from, boolean []bitfield) {
        if (debug) {
            String time = timeFinder();
            String log = time + ": Peer " + peerProcess.id + " is sending bitfield message to " + from + ": ";
            for(boolean b : bitfield) {
                log += b?'1':'0';
            }
            System.out.println(log);
            print.println(log); print.flush();
        }
    }

    public void logSendPiece(int pieceid, int to) {
        if (debug) {
            String time = timeFinder();
            String log = time + ": Peer " + peerProcess.id + " is sending piece " + pieceid + " to " + to + ".";
            System.out.println(log);
            print.println(log); print.flush();
        }
    }

    public void logSendRequest(int pieceid, int to) {
        if (debug) {
            String time = timeFinder();
            String log = time + ": Peer " + peerProcess.id + " is sending request of " + pieceid + " to " + to + ".";
            System.out.println(log);
            print.println(log); print.flush();
        }
    }

    public void logSendRawMsg(int to, byte [] buf) {
        if (false && debug) {
            String time = timeFinder();
            String log = time + ": Peer " + peerProcess.id + " is sending raw message to " + to + ": ";
            for(byte b:buf) {
                log += String.format("%02X ", b);
            }
            System.out.println(log);
            print.println(log); print.flush();
        }
    }
}
