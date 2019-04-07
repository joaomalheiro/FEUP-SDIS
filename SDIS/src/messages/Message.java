package messages;

import peer.Peer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Message {
	
	private String version;
	private int senderId;
	private int fileId;
	private int chunkNumber;
	private int replicationDeg;
	private byte[] body;

	public static String messageEnd = " " + (char) 0xD + (char) 0xA + " " + (char) 0xD + (char) 0xA;

	public Message(String version, int senderId, int fileId, int chunkNumber, int replicationDeg, byte[] body){
		this.version = version;
		this.senderId = senderId;
		this.fileId = fileId;
		this.chunkNumber = chunkNumber;
		this.replicationDeg = replicationDeg;
		this.body = body;
	}
	
	public void createPutChunk() {
		System.out.println(this.chunkNumber);
		String header = "PUTCHUNK" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNumber + " " + this.replicationDeg + messageEnd;
		ByteArrayOutputStream outputMessageStream = new ByteArrayOutputStream();

		try {
			outputMessageStream.write(header.getBytes());
			if(this.body != null)
			outputMessageStream.write(this.body);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Peer.getMDB().sendMsg(outputMessageStream.toByteArray());
	}
	
	public byte[] createStored() {

		String header = "STORED" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNumber + messageEnd;

		return header.getBytes();
	}
	
	public void createGetChunk() {

		String header = "GETCHUNK" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNumber + messageEnd;

		Peer.getMC().sendMsg(header.getBytes());
	}
	
	public byte[] createChunk() {

		String header = "CHUNK" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNumber + messageEnd;
		ByteArrayOutputStream outputMessageStream = new ByteArrayOutputStream();

		try {
			outputMessageStream.write(header.getBytes());
			outputMessageStream.write(this.body);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return outputMessageStream.toByteArray();
	}
	
	public void createDelete() {

		String header = "DELETE" + " " + this.version + " " + this.senderId + " " + this.fileId + messageEnd;

		Peer.getMC().sendMsg(header.getBytes());
 	}
	
	public void createRemoved() {

		String header = "REMOVED" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNumber + messageEnd;

		Peer.getMC().sendMsg(header.getBytes());
	}
	
	
	
	
}
