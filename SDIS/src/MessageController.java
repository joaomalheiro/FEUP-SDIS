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
	
		//If message comes from the same peer
		if(header[2].equals(Peer.getPeerId())) {
			System.out.println("Message from myself");
			return;
		}
		
		switch(type) {
			case "PUTCHUNK":
				System.out.println("PUTCHUNK");
				handlePutChunk();
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
			default:
				break;
		}
		
	}

	private void handleStored() {
		String version = header[1];
		int senderId = Integer.parseInt(header[2]);
		int fileId = Integer.parseInt(header[3]);
		int chunkNumber = Integer.parseInt(header[4]);

		System.out.println("STORED " + version + " " + senderId + " " + fileId + " " + chunkNumber);

	}

	private void handlePutChunk() {
		int fileId = Integer.parseInt(header[3]);
		int chunkNumber = Integer.parseInt(header[4]);
		int replicationDeg = Integer.parseInt(header[5]);

		Chunk chunk = new Chunk(fileId,chunkNumber,replicationDeg, packet.getData());

		String fileIdDir = "peerDisk/peer" + Peer.getPeerId() + "/backup/" + "fileId" + fileId ;
		new File("./" + fileIdDir).mkdirs();

		saveChunk(chunk, fileId);

		sendStored(fileId, chunkNumber, replicationDeg);
	}

	private void handleGetChunk(){
		int fileId = Integer.parseInt(header[3]);
		int chunkNumber = Integer.parseInt(header[4]);

		try {
            Chunk chunk = loadChunk(fileId, chunkNumber);
			sendChunk(chunk);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void handleChunk() {
		int fileId = Integer.parseInt(header[3]);
		int chunkNumber = Integer.parseInt(header[4]);
		byte[] data = header[5].getBytes();

		System.out.println(fileId);
		System.out.println(chunkNumber);
		System.out.println(data);

	}

	private void sendChunk(Chunk chunk){
		Message responseMsg = new Message("1.0", Integer.parseInt(Peer.getPeerId()), chunk.getFileId(), chunk.getChunkNumber(), chunk.getData());
		byte[] response = responseMsg.createChunk();

		try {
			System.out.println("Beginning time");
			Thread.sleep((long)(Math.random() * 400));
			System.out.println("End time");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Peer.getMDR().sendMsg(response);
	}

	private void sendStored(int fileId, int chunkNumber, int replicationDeg){

		Message responseMsg = new Message("1.0", Integer.parseInt(Peer.getPeerId()), fileId, chunkNumber, replicationDeg);
		byte[] response = responseMsg.createStored();

		try {
			System.out.println("Beginning time");
			Thread.sleep((long)(Math.random() * 400));
			System.out.println("End time");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Peer.getMC().sendMsg(response);

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
