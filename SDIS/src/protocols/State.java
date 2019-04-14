package protocols;

import peer.Peer;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class State {


    public String run() {

        String state = "";
        state = "Space Reserved: " + Peer.getStorage().getSpaceReserved() + "\n" + "Space occupied by chunks: " + Peer.getStorage().getSpaceOcupied() + "\n";
        File peerFolder = new File("./peerDisk/peer" + Peer.getPeerId() + "/backup");

        for(int i = 0; i < peerFolder.listFiles().length; i++) {
            state = state + "File number: " + i + "\n";
            state = state + "Absolute Filepath : " + peerFolder.listFiles()[i].getAbsolutePath() + "\n";
            state = state + "Backup service id of the file : " + peerFolder.listFiles()[i].getName() + "\n";

            for(int j = 0; j < peerFolder.listFiles()[i].listFiles().length; j++) {
                state = state + "   ChunkID: " + peerFolder.listFiles()[i].listFiles()[j].getName() + "\n";
                state = state + "   Chunk size: " + peerFolder.listFiles()[i].listFiles()[j].length() + "\n";
                state = state + "   Perceived Rep Degree: " + Peer.getMC().getRepDegreeStorage().getRepDegree(peerFolder.listFiles()[i].listFiles()[j].getName()) + "\n";
            }
        }

        state += "Chunks that Peer " + Peer.getPeerId() + " has initiated backup protocol: ";
        Iterator it = Peer.getStorage().getHashtable().entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            state += "   ChunkID: " + pair.getKey() + "\n" + "   RepDegree: " + pair.getValue() + "\n";
        }

        return state;
    }
}
