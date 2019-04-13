package files;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import channel.MultiCast;
import messages.Message;
import messages.MessageController;
import peer.Peer;
import protocols.Chunk;

public class ResponseHandler implements Runnable{

    private long wait_time = 1;
    private int tries = 0;
    private int stored = 0;
    private int repDegree;
    private Message msg;


    private String key;


    public ResponseHandler(int repDegree, String key,Message msg) {
        this.repDegree = repDegree;
        this.key = key;
        this.msg = msg;
    }

    @Override
    public void run() {
        stored = Peer.getMC().getRepDegreeStorage().getRepDegree(key);
        do {
            //System.out.println(repDegree + "rep" + stored + "stored");
            try {
                TimeUnit.SECONDS.sleep(wait_time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            stored = Peer.getMC().getRepDegreeStorage().getRepDegree(key);
            if(stored == repDegree){
                return;
            }
            wait_time *=2;

            msg.createPutChunk();
            tries++;

        }while(stored<repDegree || tries !=5);

    }



}