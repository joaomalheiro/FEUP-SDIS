package protocols;

import java.io.Serializable;

public class Chunk implements Serializable {

    public static final int SIZE_MAX = 64000;

    private byte[] data;
    private int fileId;
    private int chunkNumber;
    private int replicationDeg;


    public Chunk(int fileId, int chunkNumber, int replicationDeg, byte[] data) {
        this.data = data;
        this.fileId = fileId;
        this.chunkNumber = chunkNumber;
        this.replicationDeg = replicationDeg;
    }


    public byte[] getData() {
        return data;
    }

    public int getFileId() {
        return fileId;
    }


    public int getChunkNumber() {
        return chunkNumber;
    }

    public int getReplicationDeg() {
        return replicationDeg;
    }
}
