package files;

import messages.Message;
import peer.Peer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class FileHandler {

    public static void splitFile(File file) throws IOException{

        byte[] data = new byte[1000 * 64];

        FileInputStream stream = new FileInputStream(file);
        int i = 0;
        int length;

        while((length = stream.read(data)) > 0) {

            Message msg = new Message("1.0", Integer.parseInt(Peer.getPeerId()), file.getName() + file.lastModified(), i, 6, Arrays.copyOf(data,length));
            msg.createPutChunk();
            i++;
        }
    }

    public static void mergeFile(){

    }
}
