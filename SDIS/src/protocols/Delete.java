package protocols;

import java.io.File;
import java.io.IOException;

import files.FileHandler;
import messages.Message;
import peer.Peer;


public class Delete implements Runnable {

    private File file;

    public Delete(String fileName) {
        this.file = new File("./testFiles/" + fileName);

    }

    @Override
    public void run() {
        //System.out.println(file.getName() + " DELETE" + file.lastModified());
        Message message = new Message("1.0", Integer.parseInt(Peer.getPeerId()),file.getName() + file.lastModified(),0,0,null);
        message.createDelete();
    }

}