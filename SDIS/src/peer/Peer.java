package peer;

import channel.MultiCast;
import files.FileHandler;
import messages.Message;
import protocols.Backup;
import protocols.Storage;

import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Peer  implements RMIStub {

    private static int serverId;
    private static String peerId;
    private static String peerAcessPoint;
    private static String protocolVersion;
    private static MultiCast MC;
    private static MultiCast MDB;
    private static MultiCast MDR;
    private static Storage storage;

    //1 2 1 230.0.0.0 4446 225.0.0.0 5000 229.0.0.0 4450

    public static void main(String args[]) throws IOException {


        initAtributes(args);

        createDir();

        RMIStub stub = null;

        //RUN IN TERMINAL rmiregistry &
        try {
            Peer peer = new Peer();
            stub = (RMIStub) UnicastRemoteObject.exportObject(peer, 0);

            Registry reg = LocateRegistry.getRegistry();
            reg.rebind(Peer.getPeerId(), stub);

            System.out.println("Peer connected");
        } catch (Exception e) {
            e.printStackTrace();
        }

            while(true) {
                Scanner myObj = new Scanner(System.in);
                System.out.println("PUTCHUNK(1) OR GETCHUNK(2)");

                int option = myObj.nextInt();

                if(option == 1) {
                    FileHandler.splitFile(new File("./testFiles/test.txt"));
                } else if (option == 2) {
                    Message teste = new Message("1.0", Integer.parseInt(peerId), "4", 6, 6, null);
                    teste.createGetChunk();
                }   else {
                    Message teste = new Message("1.0", Integer.parseInt(peerId), "4", 6, 6, null);
                    teste.createDelete();

                }
            }
    }

    private static void initAtributes(String[] args) throws IOException {
        protocolVersion = args[0];
        serverId = Integer.parseInt(args[1]);
        peerId = args[2];
        MC = new MultiCast(args[3],args[4]);
        MDB = new MultiCast(args[5],args[6]);
        MDR = new MultiCast(args[7],args[8]);

        new Thread(MC).start();
        new Thread(MDB).start();
        new Thread(MDR).start();

        storage = new Storage(1000);
    }

    private static void createDir(){

        String backupDirName = "peerDisk/peer" + peerId + "/backup";
        String restoreDirNam = "peerDisk/peer" + peerId + "/restored";
        new File("./" + backupDirName).mkdirs();
        new File("./" + restoreDirNam).mkdirs();

    }

    private static void initPeer(String args[]){

    }

    public static MultiCast getMC() { return MC;}

	public static String getPeerId() {
		return peerId;
	}


    public static MultiCast getMDR() {
        return MDR;
    }

    public static MultiCast getMDB() {
        return MDB;
    }

    public static Storage getStorage() {
        return storage;
    }

    @Override
    public void backupProtocol(String file, int replicationDeg) {
        Backup backup = new Backup(file, replicationDeg);
        backup.run();
    }

    @Override
    public void restoreProtocol(String file) {

    }

    @Override
    public void deleteProtocol(String file) {

    }
}