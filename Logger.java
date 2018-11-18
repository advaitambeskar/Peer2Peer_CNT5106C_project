import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.text.*;

/*
    
*/

public class Logger{
    int id;
    Logger(int id) {
        this.id = id;
    }
    
    /*
        LoggerFileSave log_save = new LoggerFileSave();
    */
    String fileStatus;
    String log;

    /*
        Extra function to log timeout status
     */

    public static String timeFinder(){
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' hh:mm:ss");

        String time = ft.format(date);
        return time;
    }

    public static boolean statusTimeout(boolean timeout){
        /*
             Function Description
            ----------------------
            Name            statusTimeout
            Return type     boolean
            Description     prints when the status timeout occurs

             Data Variables
            ----------------
            Datatype        VariableName            Information
            boolean         timeout                 stores the boolean value of connection status

        */
        String log = "";
        if(timeout){
            log = "Sorry. The connection timed out.";
            System.out.println(log);
            return (!timeout);
        }
        else{
            return (!timeout);
        }
    }

    public void logDebug(String s) {
        System.out.println(s);
    }


    /*
        TCP Connection Log  
     */

    public boolean  connectionToPeer(int destinationID) throws Exception{
        int sourceID = id;
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
        log = time + ": Peer " + Integer.toString(sourceID) + " makes connection to Peer " + Integer.toString(destinationID);
        System.out.println(log);
        LoggerFileSave log_save = new LoggerFileSave();
        log_save.fileOpenWriteClose(sourceID, log);
        return true;
    }

    public boolean  connectionFromPeer(int sourceID) throws Exception{
        int destinationID = id;
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
        log = time + ": Peer " + Integer.toString(destinationID) + " is connected from Peer " + Integer.toString(sourceID);
        System.out.println(log);

        LoggerFileSave log_save = new LoggerFileSave();
        log_save.fileOpenWriteClose(destinationID, log);
        return true;
    }


    /*
        change of preferred neigbours
     */
    
    public boolean  preferredNeighborLog(int sourceID, int [] preferredList) throws Exception{
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
        String time = timeFinder();
        String preferredListString = "";
        for(int i = 0; i < preferredList.length; i++){
            preferredListString = preferredListString  + Integer.toString(i) + ", ";
        }
        int length_preferredListString = preferredListString.length();
        preferredListString = preferredListString.substring(0, length_preferredListString-2);

        log = time + ": Peer " +  Integer.toString(sourceID) + " has the preferred neighbors " + preferredListString;

        System.out.println(log);

        LoggerFileSave log_save = new LoggerFileSave();
        log_save.fileOpenWriteClose(sourceID, log);
        return true;
    }


    /*
        change of optimistically unchoked neighbor
     */
    
    public boolean optimisticallyUnchokedLog(int sourceID, int unchokedID){
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
        String time = timeFinder();

        log = time +": Peer " + Integer.toString(sourceID) + " has the optimistically unchoked neighbor " + Integer.toString(unchokedID);
        System.out.println();

        LoggerFileSave log_save = new LoggerFileSave();
        log_save.fileOpenWriteClose(sourceID, log);
        return true;
    }

    /*
        unchoking
     */

    public boolean unchokedLog(int sourceID, int unchokedID){
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
        String time = timeFinder();

        log = time + ": Peer "+ sourceID + " is unchoked by  Peer " + unchokedID;
        System.out.println(log);

        LoggerFileSave log_save = new LoggerFileSave();
        log_save.fileOpenWriteClose(sourceID, log);
        return true;
    }


    /*
     *  choking
     */

    public boolean chokedLog(int sourceID, int chokedID){
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
        String time = timeFinder();

        log = time + ": Peer "+ sourceID + " is choked by  Peer " + chokedID;
        System.out.println(log);

        LoggerFileSave log_save = new LoggerFileSave();
        log_save.fileOpenWriteClose(sourceID, log);
        return true; 
    }


    /*
        receiving messages
     */

    public boolean messageHaveLog(int sourceID, int destinationID, int pieceIndex){
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
        String time = timeFinder();
        log = time + ": Peer " + destinationID + " received the 'have' message from Peer "+ sourceID + "for the piece " + pieceIndex + ".";
        System.out.println(log);

        LoggerFileSave log_save = new LoggerFileSave();
        log_save.fileOpenWriteClose(sourceID, log);
        return true;
    }

    public boolean messageInterestedLog(int sourceID, int destinationID){
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
        String time = timeFinder();

        log = time + ": Peer " + destinationID + " received the 'interested' message from Peer "+ sourceID + ".";
        System.out.println(log);

        LoggerFileSave log_save = new LoggerFileSave();
        log_save.fileOpenWriteClose(sourceID, log);
        return true;
    }
    
    public boolean messageNotInterestedLog(int sourceID, int destinationID){
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

        String time = timeFinder();

        log = time + ": Peer " + destinationID + " received the 'not interested' message from Peer "+ sourceID + ".";
        System.out.println(log);

        LoggerFileSave log_save = new LoggerFileSave();
        log_save.fileOpenWriteClose(sourceID, log);
        return true;
    }


    /*
        Piece Download
     */
    public boolean pieceDownload(int sourceID, int destinationID, int pieceIndex, int collectedPiece){
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

        String time = timeFinder();

        log = time + ": Peer " + destinationID + " has downloaded piece " + pieceIndex + " from Peer " + sourceID + ". Now the number of pieces it has is "+ collectedPiece;
        System.out.println(log);

        LoggerFileSave log_save = new LoggerFileSave();
        log_save.fileOpenWriteClose(sourceID, log);
        return true;
    }
    
    /*
        File Download
     */
    public boolean fileDownload(int sourceID){
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

        String time = timeFinder();
        
        log = time + ": Peer " + sourceID + " has downloaded the complete file.";

        System.out.println(log);

        LoggerFileSave log_save = new LoggerFileSave();
        log_save.fileOpenWriteClose(sourceID, log);
        return true;
    }

}
