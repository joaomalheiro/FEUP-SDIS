package channel;

import files.ResponseHandler;
import messages.MessageController;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import peer.Peer;
import protocols.Chunk;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
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
        } catch (IOException e) {
            System.out.println("No Storage loaded");
        }
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

    public RepDegreeStorage getRepDegreeStorage() {
        return repDegreeStorage;
    }


}