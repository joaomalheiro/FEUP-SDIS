package channel;

import messages.MessageController;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import protocols.Chunk;

import java.io.IOException;
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

    private HashMap<String,HashSet<Integer>> repDegree = new HashMap<>();

    public MultiCast(String address, String port) throws IOException {
        this.multCast_address = InetAddress.getByName(address);
        this.multCast_port = Integer.parseInt(port);

        multCast_socket = new MulticastSocket(multCast_port);
        multCast_socket.setTimeToLive(1);
        multCast_socket.joinGroup(multCast_address);
    }

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

    public void sendMsg(byte[] msg){

        DatagramPacket packet = new DatagramPacket(Arrays.copyOf(msg, msg.length), msg.length, multCast_address, multCast_port);
        try {
            multCast_socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveChunkReplication(String fileId,int chunkNumber,int peerId){
        String key = "";

        key = "fileId" + fileId + "chkn" + chunkNumber;
        System.out.println("Storing" + key + "in" + peerId);
        if (!repDegree.containsKey(key)){
            repDegree.put(key, new HashSet<>());
        }
        repDegree.get(key).add(peerId);

    }
    public int getRepDegree(String key){
        return repDegree.get(key).size();
    }

}