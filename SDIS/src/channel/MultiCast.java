package channel;

import messages.MessageController;
import peer.Peer;
import protocols.Chunk;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class MultiCast implements Runnable{
    public MulticastSocket multCast_socket;
    public InetAddress multCast_address;
    public int multCast_port;

    private RepDegreeStorage repDegreeStorage = new RepDegreeStorage();
    private HashMap<String,HashSet<Integer>> repDegree = new HashMap<>();
    private HashMap<String,HashMap<Integer,Chunk>> restoredChunks = new HashMap<>();

    /**
     * Constructor for class MultiCast, class that is used to create the 3 channels used in the service
     * @param address
     * @param port
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public MultiCast(String address, String port) throws IOException, ClassNotFoundException {
        this.multCast_address = InetAddress.getByName(address);
        this.multCast_port = Integer.parseInt(port);

        multCast_socket = new MulticastSocket(multCast_port);
        multCast_socket.setTimeToLive(1);
        multCast_socket.joinGroup(multCast_address);
        try {
        FileInputStream fileIn = new FileInputStream("./storage" + Peer.getPeerId());
        ObjectInputStream in = new ObjectInputStream(fileIn);
        this.repDegreeStorage = (RepDegreeStorage) in.readObject();
        in.close();
        fileIn.close();
        } catch (IOException ignored) {

        }
    }

    /**
     * Runnable method that waits for the datagram packet to be received and then creates a MessageController
     */
    public void run() {
        byte[] buf;
        boolean end = false;
        while (!end) {
            try {
                buf = new byte[65000];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                multCast_socket.receive(packet);
                MessageController msgControl = new MessageController(packet);
                new Thread(msgControl).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        multCast_socket.close();
    }

    /**
     * Sends a byte[] msg through the channel
     * @param msg
     */
    public void sendMsg(byte[] msg){

        DatagramPacket packet = new DatagramPacket(Arrays.copyOf(msg, msg.length), msg.length, multCast_address, multCast_port);
        try {
            multCast_socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter for class attribute repDegreeStorage
     * @return RepDegreeStorage
     */
    public RepDegreeStorage getRepDegreeStorage() {
        return repDegreeStorage;
    }

    /**
     * Getter from the HashMap<Integer,Chunk> based on the key fileId passed as parameter
     * @param fileId
     * @return HashMap<Integer,Chunk>
     */
    public HashMap<Integer,Chunk> getChunksFromFile(String fileId){
        if(restoredChunks.containsKey(fileId)) {
            return restoredChunks.get(fileId);
        } else return null;
    }

    /**
     * Inserts the a new fileId key so that the restore process for that file can be started
     * @param fileId
     */
    public void insertFileId(String fileId){
        restoredChunks.put(fileId, new HashMap<>());
    }

    /**
     * Inserts the chunk passed as parameter to the restoredChunks from the fileId passed also as parameter
     * @param chunk
     * @param fileId
     */
    public void insertChunk(Chunk chunk, String fileId){
        if(restoredChunks.containsKey(fileId)) {
            restoredChunks.get(fileId).put(chunk.getChunkNumber(), chunk);
        }

    }

}