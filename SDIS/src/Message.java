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

	Message(String version, int senderId, int fileId, int chunkNumber, int replicationDeg, byte[] body){
		this.version = version;
		this.senderId = senderId;
		this.fileId = fileId;
		this.chunkNumber = chunkNumber;
		this.replicationDeg = replicationDeg;
		this.body = body;
	}

	Message(String version, int senderId, int fileId, int chunkNumber, int replicationDeg){
		this.version = version;
		this.senderId = senderId;
		this.fileId = fileId;
		this.chunkNumber = chunkNumber;
		this.replicationDeg = replicationDeg;
	}

	Message(String version, int senderId, int fileId, int chunkNumber, byte[] body){
		this.version = version;
		this.senderId = senderId;
		this.fileId = fileId;
		this.chunkNumber = chunkNumber;
		this.body = body;
	}
	
	public byte[] createPutChunk() {
		String header = "PUTCHUNK" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNumber + " " + this.replicationDeg + messageEnd;
		ByteArrayOutputStream outputMessageStream = new ByteArrayOutputStream();

		try {
			outputMessageStream.write(header.getBytes());
			outputMessageStream.write(this.body);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return outputMessageStream.toByteArray();
	}
	
	public byte[] createStored() {

		String header = "STORED" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNumber + messageEnd;

		return header.getBytes();
	}
	
	public byte[] createGetChunk() {

		String header = "GETCHUNK" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNumber + messageEnd;

		return header.getBytes();
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
	
	public byte[] createDelete() {

		String header = "DELETE" + " " + this.version + " " + this.senderId + " " + this.fileId + messageEnd;

		return header.getBytes();
 	}
	
	public byte[] createRemoved() {

		String header = "REMOVED" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNumber + messageEnd;

		return header.getBytes();
	}
	
	
	
	
}
