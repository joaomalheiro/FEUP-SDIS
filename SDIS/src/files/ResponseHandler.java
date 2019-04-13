package files;

import java.util.concurrent.TimeUnit;

import channel.MultiCast;
import peer.Peer;

public class ResponseHandler implements Runnable{

    private long wait_time = 1;
    private int tries = 0;
    private int stored = 0;
    private int repDegree;
    private String key;

    public ResponseHandler(int repDegree, String key) {
        this.repDegree = repDegree;
        this.key = key;

    }

    @Override
    public void run() {
        stored = Peer.getMC().getRepDegreeStorage().getRepDegree(key);
        do {
            System.out.println(repDegree + "rep" + stored + "stored");
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
            tries++;

        }while(stored<repDegree || tries !=5);

    }



}