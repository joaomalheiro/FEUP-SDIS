
public class Message {
	
	private String version;
	private int senderId;
	private int fileId;
	private int chunkNo;
	private int replicationDeg;
	private String body;
	
	Message(String version, int senderId, int fileId, int chunkNo, int replicationDeg, String body){
		this.version = version;
		this.senderId = senderId;
		this.fileId = fileId;
		this.chunkNo = chunkNo;
		this.replicationDeg = replicationDeg;
		this.body = body;
	}
	
	public String createPutChunk() {
		return "PUTCHUNK" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNo + " " + this.replicationDeg + "\n" + "\n" + this.body;
	}
	
	public String createStored() {
		return "STORED" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNo + "\n" + "\n";
	}
	
	public String createGetChunk() {
		return "GETCHUNK" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNo + "\n" + "\n";
	}
	
	public String createChunk() {
		return "CHUNK" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNo + "\n" + "\n" + this.body;
	}
	
	public String createDelete() {
		return "DELETE" + " " + this.version + " " + this.senderId + " " + this.fileId + "\n" + "\n";
 	}
	
	public String createRemoved() {
		return "REMOVED" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNo + "\n" + "\n";
	}
	
	
	
	
}
