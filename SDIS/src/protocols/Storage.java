package protocols;


import peer.Peer;

import java.io.File;

public class Storage {
    private long spaceReserved;
    private long spaceOcupied;

    public Storage(long spaceReserved){
        this.spaceReserved = spaceReserved;
        updateSpaceOcupied();
    }

    public void updateSpaceOcupied(){
        File peerFolder = new File("./peerDisk/peer" + Peer.getPeerId());

        this.spaceOcupied = folderSize(peerFolder) / 1024;
    }

    private long folderSize(File directory) {
        long length = 0;
        try {
            for (File file : directory.listFiles()) {
                if (file.isFile())
                    length += file.length();
                else
                    length += folderSize(file);
            }
        }
        catch (NullPointerException e) {

        }
        return length;
    }

    public long getSpaceReserved() {
        return spaceReserved;
    }

    public void setSpaceReserved(long spaceReserved) {
        this.spaceReserved = spaceReserved;
    }

    public long getSpaceOcupied() {
        updateSpaceOcupied();
        return spaceOcupied;
    }
}
