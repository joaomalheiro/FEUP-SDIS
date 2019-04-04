import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Message {
	
	private String version;
	private int senderId;
	private int fileId;
	private int chunkNo;
	private int replicationDeg;
	private byte[] body;
	
	Message(String version, int senderId, int fileId, int chunkNo, int replicationDeg, byte[] body){
		this.version = version;
		this.senderId = senderId;
		this.fileId = fileId;
		this.chunkNo = chunkNo;
		this.replicationDeg = replicationDeg;
		this.body = body;
	}
	
	public byte[] createPutChunk() {
		String header = "PUTCHUNK" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNo + " " + this.replicationDeg + "\n" + "\n";
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

		String header = "STORED" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNo + "\n" + "\n";

		return header.getBytes();
	}
	
	public byte[] createGetChunk() {

		String header = "GETCHUNK" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNo + "\n" + "\n";

		return header.getBytes();
	}
	
	public byte[] createChunk() {

		String header = "CHUNK" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNo + "\n" + "\n";
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

		String header = "DELETE" + " " + this.version + " " + this.senderId + " " + this.fileId + "\n" + "\n";

		return header.getBytes();
 	}
	
	public byte[] createRemoved() {

		String header = "REMOVED" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNo + "\n" + "\n";

		return header.getBytes();
	}
	
	
	
	
}
