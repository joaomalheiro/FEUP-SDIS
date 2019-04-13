package protocols;

import messages.Message;
import peer.Peer;

import java.io.*;
import java.util.Arrays;
import java.util.Hashtable;

public class Restore implements Runnable {
    private String fileName;

    public Restore(String fileName){
        this.fileName = fileName;
    }

    @Override
    public void run() {

        System.out.println("Hey");
        Hashtable chunks = new Hashtable<Integer,Chunk>();

        File file = new File("./testFiles/" + fileName);
        int nChunks = (int)file.length() / 64000 + 1;

        Peer.getMDR().insertFileId(fileName);

        for (int i = 0 ; i < nChunks; i++){

            Message msg = new Message("1.0", Integer.parseInt(Peer.getPeerId()), file.getName() + file.lastModified(), i, 0 , null);
            msg.createGetChunk();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //save chunk from MC to this class Hashtable

            Chunk chunk = Peer.getMDR().getChunksFromFile(fileName).get(i);

            if(chunk != null) {
                break;
            } else return;
        }

        byte[] data = mergeIntoFile(chunks);

        createFile(data);
    }

    private void createFile(byte[] data) {

        String restoreDirNam = "peerDisk/peer" + Peer.getPeerId() + "/restored/";
        File restoredFolder = new File("./" + restoreDirNam);

        try {
            OutputStream streamToFile = new FileOutputStream(restoredFolder.getName() + fileName);
            streamToFile.write(data);
            streamToFile.close();
        } catch (IOException e) {
            System.out.println("Restore Error : Could not write byte[] into restored file");
        }
    }

    private byte[] mergeIntoFile(Hashtable<Integer,Chunk> chunks) {

        byte[] data = new byte[0];
        ByteArrayOutputStream outputMessageStream = new ByteArrayOutputStream();

        for (int i = 0; i < chunks.size(); i++){

            try {
                outputMessageStream.write(Arrays.copyOf(data, data.length));
                outputMessageStream.write(Arrays.copyOf(chunks.get(i).getData() ,chunks.get(i).getData().length));
                } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return data;
    }

}
