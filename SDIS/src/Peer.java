import java.io.IOException;
import java.util.Scanner;

public class Peer {

    private static int serverId;
    private String peerId;
    private static String peerAcessPoint;
    private static String protocolVersion;
    private static  MultiCast MC;

    public static void main(String args[]) throws IOException {


        initAtributes(args);

            while(true) {
                System.out.println("Write a msg");
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();
                MC.sendMsg(msg.getBytes());
            }


    }

    private static void initAtributes(String[] args) throws IOException {
        protocolVersion = args[0];
        serverId = Integer.parseInt(args[1]);
        peerAcessPoint = args[2];
        MC = new MultiCast(args[3],args[4]);

        new Thread(MC).start();


    }

    private static void initPeer(String args[]){

    }

}