import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MultiCast extends Thread{
    public MulticastSocket multCast_socket;
    public InetAddress multCast_address;
    public int multCast_port;

    MultiCast(String address, String port) throws IOException {
        this.multCast_address = InetAddress.getByName(address);
        this.multCast_port = Integer.parseInt(port);

        multCast_socket = new MulticastSocket(multCast_port);
        multCast_socket.setTimeToLive(1);
        multCast_socket.joinGroup(multCast_address);
    }

    public void run() {
        byte[] buf = new byte[256];
        boolean end = false;
        while (!end) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                multCast_socket.receive(packet);
                String received = new String(
                        packet.getData(), 0, packet.getLength());

                System.out.println(received);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        multCast_socket.close();
    }

    public void sendMsg(byte[] msg){

        DatagramPacket packet = new DatagramPacket(msg, msg.length, multCast_address, multCast_port);

        try {
            multCast_socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
