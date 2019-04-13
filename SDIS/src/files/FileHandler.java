package files;

import messages.Message;
import peer.Peer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class FileHandler {

    public static void splitFile(File file, int repDegree) throws IOException{

        byte[] data = new byte[1000 * 64];

        FileInputStream stream = new FileInputStream(file);
        int i = 0;
        int length;

        while((length = stream.read(data)) > 0) {

            Message msg = new Message("1.0", Integer.parseInt(Peer.getPeerId()),  file.getName() + file.lastModified(), i, repDegree, Arrays.copyOf(data,length));
            String[] header = msg.createPutChunk();
            String key = "fileId" +  Message.encrypt(file.getName() + file.lastModified()) + "chkn" + header[4];
            System.out.println("Sending " + key);
            Peer.getMC().getRepDegreeStorage().setDesiredRepDegree(Message.encrypt(file.getName() + file.lastModified()),repDegree);
            ResponseHandler resp = new ResponseHandler(Integer.parseInt(header[5]), key);
            new Thread(resp).start();

            i++;
        }
    }

}