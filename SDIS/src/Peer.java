import java.io.IOException;

public class Peer {

    private String peerId;
    private String peerAcessPoint;
    private static  MultiCast MC;

    public static void main(String args[]) throws IOException {

        MC = new MultiCast("230.0.0.0","4446");


    }

    private static void initPeer(String args[]){

    }

}