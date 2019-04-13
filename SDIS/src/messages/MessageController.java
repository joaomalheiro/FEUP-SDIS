package messages;

import messages.Message;
import peer.Peer;
import protocols.Chunk;

import java.io.*;
import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MessageController implements Runnable {
	private DatagramPacket packet;
	private String[] header;
	
	public MessageController(DatagramPacket packet) {
		this.packet = packet;
	}

	@Override
	public void run() {
		header = parsePacketHeader();

		String type = header[0];
	
		//If message comes from the same peer
		if(header[2].equals(Peer.getPeerId())) {
			System.out.println("Message from me");
			return;
		}
		
		switch(type) {
			case "PUTCHUNK":
			    if(Peer.getStorage().allowChunk(packet.getData())){
                    handlePutChunk();
                } else {
			        System.out.println("Not enough space");
                }

				break;
			case "STORED":
				handleStored();
				break;
			case "GETCHUNK":
				handleGetChunk();
				break;
			case "CHUNK":
				System.out.println("CHUNK");
				handleChunk();
				break;
            case "DELETE":
                handleDelete();
                break;
            case "REMOVED":
                handleRemoved();
			default:
				break;
		}
		
	}

    private void handleRemoved() {
        String fileId = header[3];
        int chunkNumber = Integer.parseInt(header[4]);
	    System.out.println(header[0] + " " + header[1] + " " + header[2] + " " + header[3]);
        File f = new File("./peerDisk/peer" + Peer.getPeerId() + "/backup/" + fileId + "/chk" + chunkNumber);
        if(f.exists() && !f.isDirectory()) {
            Chunk chunk = null;
            try {
                chunk = loadChunk(fileId, chunkNumber);
            } catch (IOException ignored) {

            } catch (ClassNotFoundException ignored) {

            }
           Peer.getMC().getRepDegreeStorage().removeChunkReplication(fileId,chunkNumber,Integer.parseInt(header[2]),chunk.getData());
        }


    }

    private void handleDelete() {

        String fileId = header[3];

        File directory = new File("./peerDisk/peer" + Peer.getPeerId() + "/backup/" + fileId);
		System.out.println(header[0] + " " + header[1] + " " + header[2] + " " + header[3]);
        if(!directory.exists()){

            System.out.println("Directory does not exist.");

        }else{
            delete(directory);
        }
    }

    private void delete(File file) {
        if(file.isDirectory()){

            //directory is empty, then delete it
            if(file.list().length==0){

                file.delete();
                System.out.println("Directory is deleted : "
                        + file.getAbsolutePath());

            }else{

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    file.delete();
                    System.out.println("Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }

        }else{
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

	private void handleStored() {

		String version = header[1];
		int senderId = Integer.parseInt(header[2]);
		String fileId = header[3];
		int chunkNumber = Integer.parseInt(header[4]);
        System.out.println("STORED " + version + " " + senderId + " " + fileId + " " + chunkNumber);
		Peer.getMC().getRepDegreeStorage().saveChunkReplication(fileId, chunkNumber, senderId);
	}

	private void handlePutChunk() {
		String fileId = header[3];
		int chunkNumber = Integer.parseInt(header[4]);
		int replicationDeg = Integer.parseInt(header[5]);
        System.out.println("PUTCHUNK " + fileId + " " + chunkNumber + " " + replicationDeg);


		Chunk chunk = new Chunk(fileId,chunkNumber,replicationDeg, getDataFromPacket());

		String fileIdDir = "peerDisk/peer" + Peer.getPeerId() + "/backup/" + fileId;
		new File("./" + fileIdDir).mkdirs();

		saveChunk(chunk, fileId);

		Chunk newChunk = null;
		try {
			newChunk = loadChunk(fileId,  chunkNumber);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		String s = new String(newChunk.getData());
		//System.out.println(s);

		Peer.getMC().getRepDegreeStorage().saveChunkReplication(fileId, chunkNumber, Integer.parseInt(Peer.getPeerId()));
        Peer.getMC().getRepDegreeStorage().setDesiredRepDegree(fileId,replicationDeg);
		sendStored(fileId, chunkNumber, replicationDeg);
	}

	private byte[] getDataFromPacket() {
		
		int headerLength = 0;
		
		for(int i = 0; i < packet.getData().length; i++){

            if((packet.getData()[i] == (char)0xD) &&(packet.getData()[i+1] == (char)0xA)&&(packet.getData()[i] == (char)0xD)&&(packet.getData()[i+1] == (char)0xA)){
				break;
			}
			headerLength++;
		}

		//System.out.println(new String(packet.getData()));
		//System.out.println(headerLength);
		return Arrays.copyOfRange(packet.getData(), headerLength + 4, packet.getLength());
		
	}

	private void handleGetChunk(){
		String fileId = header[3];
		int chunkNumber = Integer.parseInt(header[4]);
		System.out.println(header[0] + " " + fileId + " " + chunkNumber);

		try {
            Chunk chunk = loadChunk(fileId, chunkNumber);
			sendChunk(chunk);
		} catch (IOException e) {
			System.out.println("Do not own that chunk");
		} catch (ClassNotFoundException e) {
			System.out.println("Do not own that chunk");
		}

	}

	private void handleChunk() {
		String fileId = header[3];
		int chunkNumber = Integer.parseInt(header[4]);
		byte[] data = getDataFromPacket();

		//System.out.println(fileId);
		//System.out.println(chunkNumber);
		//System.out.println("Received Chunk with data :");
		//System.out.println(new String(data));
		//System.out.println(data.length);

		Chunk chunk = new Chunk(fileId,chunkNumber, 0, data);
		Peer.getMDR().insertChunk(chunk, fileId);

	}

	public static void sendChunk(Chunk chunk){
		Message responseMsg = new Message("1.0", Integer.parseInt(Peer.getPeerId()), chunk.getFileId(), chunk.getChunkNumber(), 0,chunk.getData());
		byte[] response = responseMsg.createChunk();
		//System.out.println(chunk.getData().length);
		try {
			Thread.sleep((long)(Math.random() * 400));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Peer.getMDR().sendMsg(response);
	}

	private void sendStored(String fileId, int chunkNumber, int replicationDeg){

		Message responseMsg = new Message("1.0", Integer.parseInt(Peer.getPeerId()), fileId, chunkNumber, replicationDeg, null);
		byte[] response = responseMsg.createStored();

		try {
			Thread.sleep((long)(Math.random() * 400));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long wait_time = (long) (Math.random() * (400 - 1)) + 1;
		try {
			TimeUnit.MILLISECONDS.sleep(wait_time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Peer.getMC().sendMsg(response);

	}

	private static void saveChunk(Chunk chunk, String fileId){
		try {
			FileOutputStream fileOut = new FileOutputStream("./peerDisk/peer" + Peer.getPeerId() + "/backup/" + fileId  + "/chk" + chunk.getChunkNumber() );
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(chunk);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public static Chunk loadChunk(String fileId, int chunkNumber) throws IOException, ClassNotFoundException {

		Chunk chunk = null;
		FileInputStream fileIn = new FileInputStream("./peerDisk/peer" + Peer.getPeerId() + "/backup/" + fileId  + "/chk" + chunkNumber);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		chunk = (Chunk) in.readObject();
		in.close();
		fileIn.close();

		return chunk;
	}

	private String[] parsePacketHeader() {
		
		byte[] buffer;
		buffer = packet.getData();
		String headerString =new String(buffer, 0, packet.getLength());
		
		return headerString.split(" ");
	}
	
	
	

}
