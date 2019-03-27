import java.io.IOException;

public class Peer {

    private static int serverId;
    private String peerId;
    private static String peerAcessPoint;
    private static String protocolVersion;
    private static  MultiCast MC;

    public static void main(String args[]) throws IOException {

        initAtributes(args);
        MC = new MultiCast(args[0],args[1]);
        if(args.length == 2) {
            MC.run();
        }
        if(args.length == 3) {
            if("sender".equals(args[2])){
                MC.sendMsg("end".getBytes());
            }
        }


    }

    private static void initAtributes(String[] args) {
        protocolVersion = args[0];
        serverId = Integer.parseInt(args[1]);
        peerAcessPoint = args[2];
    }

    private static void initPeer(String args[]){

    }

}