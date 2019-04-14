package peer;

import channel.MultiCast;
import protocols.*;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Peer  implements RMIStub {

    private static String peerId;
    private static String peerAcessPoint;
    private static String protocolVersion;
    private static MultiCast MC;
    private static MultiCast MDB;
    private static MultiCast MDR;
    private static Storage storage;

    //1 2 1 230.0.0.0 4446 225.0.0.0 5000 229.0.0.0 4450

    public static void main(String args[]) throws IOException, ClassNotFoundException {

        initAtributes(args);

        createDir();

        RMIStub stub = null;

        Peer peer = new Peer();
        stub = (RMIStub) UnicastRemoteObject.exportObject(peer, 0);

        //RUN IN TERMINAL rmiregistry &
        try {
            Registry reg = LocateRegistry.getRegistry();
            reg.rebind(peerAcessPoint, stub);

            System.out.println("Peer connected through getRegistry");
        } catch (Exception e) {
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind(Peer.getPeerId(), stub);

            System.out.println("Peer connected through createRegistry");
        }

    }

    private static void initAtributes(String[] args) throws IOException, ClassNotFoundException {
        protocolVersion = args[0];
        peerId = (args[1]);
        peerAcessPoint = args[2];
        MC = new MultiCast(args[3],args[4]);
        MDB = new MultiCast(args[5],args[6]);
        MDR = new MultiCast(args[7],args[8]);

        new Thread(MC).start();
        new Thread(MDB).start();
        new Thread(MDR).start();

        MC.sendMsg(new String("JOINED " + Peer.getProtocolVersion() + " " + Peer.getPeerId()).getBytes());

        storage = new Storage(1000000);
    }

    private static void createDir(){

        String backupDirName = "peerDisk/peer" + peerId + "/backup";
        String restoreDirNam = "peerDisk/peer" + peerId + "/restored";
        new File("./" + backupDirName).mkdirs();
        new File("./" + restoreDirNam).mkdirs();

    }

    public static MultiCast getMC() { return MC;}

	public static String getPeerId() {
		return peerId;
	}

    public static String getProtocolVersion() {
        return protocolVersion;
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
        Restore restore = new Restore(file);
        restore.run();
    }

    @Override
    public void deleteProtocol(String file) {
        Delete delete = new Delete(file);
        delete.run();
    }
    @Override
    public void reclaimProtocol(int reservedSpace) {
        Reclaim reclaim = new Reclaim(reservedSpace);
        reclaim.run();
    }

    @Override
    public void stateProtocol() throws RemoteException {
        State state = new State();
        state.run();
    }
}