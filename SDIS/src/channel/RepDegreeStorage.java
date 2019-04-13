package channel;

import files.ResponseHandler;
import peer.Peer;
import protocols.Chunk;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class RepDegreeStorage implements Serializable{
    private HashMap<String,HashSet<Integer>> repDegree = new HashMap<>();
    private HashMap<String,Integer> desiredRepDegree = new HashMap<>();


    public void saveChunkReplication(String fileId,int chunkNumber,int peerId){
        String key = "";

        key = "fileId" + fileId + "chkn" + chunkNumber;
        System.out.println("Storing" + key + "in" + peerId);
        if (!repDegree.containsKey(key)){
            repDegree.put(key, new HashSet<>());
        }
        repDegree.get(key).add(peerId);
        this.saveRepDegreeStorage();

    }

    public void removeChunkReplication(String fileId,int chunkNumber,int peerId){
        String key = "fileId" + fileId + "chkn" + chunkNumber;
        if (repDegree.containsKey(key)){
            repDegree.get(key).remove(peerId);
            if(getRepDegree(key) < getDesiredRepDegree(fileId)){
                ResponseHandler resp = new ResponseHandler(getDesiredRepDegree(fileId), key);
                new Thread(resp).start();
            }
        }
        this.saveRepDegreeStorage();

    }
    public int getRepDegree(String key){
        return repDegree.get(key).size();
    }

    public int getDesiredRepDegree(String key){
        return desiredRepDegree.get(key);
    }

    public void setDesiredRepDegree(String key, int replicationDegree){
        desiredRepDegree.put(key,new Integer(replicationDegree));
        this.saveRepDegreeStorage();
    }

    public void saveRepDegreeStorage(){
        try {
            FileOutputStream fileOut = new FileOutputStream("./storage" + Peer.getPeerId());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
