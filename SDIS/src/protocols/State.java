package protocols;

import peer.Peer;

import java.io.File;

public class State implements Runnable {

    @Override
    public void run() {

        System.out.println("Space Reserved:" + Peer.getStorage().getSpaceReserved());
        System.out.println("Space Occupied by chunks:" + Peer.getStorage().getSpaceOcupied());

        File peerFolder = new File("./peerDisk/peer" + Peer.getPeerId() + "/backup");

        for(int i = 0; i < peerFolder.listFiles().length; i++) {
            System.out.println("File " + i);
            System.out.println("Absolute Filepath : " + peerFolder.listFiles()[i].getAbsolutePath());
            System.out.println("Backup service id of the file : " + peerFolder.listFiles()[i].getName());

            for(int j = 0; j < peerFolder.listFiles()[i].listFiles().length; j++) {
                System.out.println("    ChunkID: " + peerFolder.listFiles()[i].listFiles()[j].getName());
                System.out.println("    Chunk size: " + peerFolder.listFiles()[i].listFiles()[j].length());
                System.out.println("    Perceived Rep Degree: " + Peer.getMC().getRepDegreeStorage().getRepDegree(peerFolder.listFiles()[i].listFiles()[j].getName()));
            }
        }

    }
}
