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

        while((length =stream.read(data)) > 0) {

            data = Arrays.copyOf(data,length);
            Message msg = new Message("1.0", Integer.parseInt(Peer.getPeerId()), 4, i, 6, data);
            msg.createPutChunk();
            i++;

            /*
            try {
                Thread.sleep((long)1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */
        }
    }

    public static void mergeFile(){

    }
}
