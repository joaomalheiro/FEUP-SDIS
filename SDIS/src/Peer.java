import java.io.IOException;
import java.util.Scanner;

public class Peer {

    private static int serverId;
    private static String peerId;
    private static String peerAcessPoint;
    private static String protocolVersion;
    private static  MultiCast MC;
    private static Storage storage;

    public static void main(String args[]) throws IOException {


        initAtributes(args);

        //    while(true) {
            	Message teste = new Message("1.0", 2, 4, 5, 6, "DURIOLA");
            	String msg = teste.createPutChunk();
                MC.sendMsg(msg.getBytes());
          //  }


    }

    private static void initAtributes(String[] args) throws IOException {
        protocolVersion = args[0];
        serverId = Integer.parseInt(args[1]);
        peerId = args[2];
        MC = new MultiCast(args[3],args[4]);

        new Thread(MC).start();


    }

    private static void initPeer(String args[]){

    }

	public static String getPeerId() {
		return peerId;
	}


}