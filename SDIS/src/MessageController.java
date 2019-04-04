import java.io.*;
import java.net.DatagramPacket;

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
		System.out.println(header[0]);
		System.out.println(header[1]);
		System.out.println(header[2]);
	
		//If message comes from the same peer
		if(header[2].equals(Peer.getPeerId())) {
			System.out.println("Message from myself");
			return;
		}
		
		switch(type) {
		case "PUTCHUNK":
			handlePutchunk();
			break;
		}
		
	}

	private void handlePutchunk() {
		int fileId = Integer.parseInt(header[3]);
		System.out.print(fileId);
		int chunkNumber = Integer.parseInt(header[4]);
		System.out.print(chunkNumber);
		int replicationDeg = Integer.parseInt(header[5]);
		System.out.print(replicationDeg);

		Chunk chunk = new Chunk(fileId,chunkNumber,replicationDeg, packet.getData());

		String fileIdDir = "peerDisk/peer" + Peer.getPeerId() + "/backup/" + "fileId" + fileId ;
		new File("./" + fileIdDir).mkdirs();

		saveChunk(chunk, fileId);

		try {
			Chunk test = loadChunk(fileId, 5);
			System.out.print(test.getFileId());
			System.out.print(test.getChunkNumber());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static void saveChunk(Chunk chunk, int fileId){
		try {
			FileOutputStream fileOut = new FileOutputStream("./peerDisk/peer" + Peer.getPeerId() + "/backup/fileId" + fileId  + "/chk" + chunk.getChunkNumber() );
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(chunk);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public static Chunk loadChunk(int fileId, int chunkNumber) throws IOException, ClassNotFoundException {
		Chunk chunk = null;
		FileInputStream fileIn = new FileInputStream("./peerDisk/peer" + Peer.getPeerId() + "/backup/fileId" + fileId  + "/chk" + chunkNumber);
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
