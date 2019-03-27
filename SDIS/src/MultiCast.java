import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MultiCast {
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

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                multCast_socket.receive(packet);
                String received = new String(
                        packet.getData(), 0, packet.getLength());
                if ("end".equals(received)) {
                    System.out.println("Received");
                    break;
                }
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
