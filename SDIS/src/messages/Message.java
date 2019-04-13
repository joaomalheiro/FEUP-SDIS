package messages;

import peer.Peer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Message {
	
	private String version;
	private int senderId;
	private String fileId;
	private int chunkNumber;
	private int replicationDeg;
	private byte[] body;

	public static String messageEnd = " " + (char) 0xD + (char) 0xA + " " + (char) 0xD + (char) 0xA;

	public Message(String version, int senderId, String fileId, int chunkNumber, int replicationDeg, byte[] body){
		this.version = version;
		this.senderId = senderId;
		this.fileId = fileId;
		this.chunkNumber = chunkNumber;
		this.replicationDeg = replicationDeg;
		this.body = body;
	}

	public static String encrypt(String text) {

		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
			return bytesToHex(encodedhash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "error in hashing";
	}

	private static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public synchronized String[] createPutChunk() {
		System.out.println(this.chunkNumber);
		String header = new String("PUTCHUNK" + " " + this.version + " " + this.senderId + " " + encrypt(this.fileId) + " " + this.chunkNumber + " " + this.replicationDeg + messageEnd);
		ByteArrayOutputStream outputMessageStream = new ByteArrayOutputStream();

		try {
			outputMessageStream.write(Arrays.copyOf(header.getBytes(), header.length()));
			if(this.body != null)
				outputMessageStream.write(Arrays.copyOf(this.body, this.body.length));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Peer.getMC().getRepDegreeStorage().setDesiredRepDegree(this.fileId,this.replicationDeg);
		Peer.getMDB().sendMsg(outputMessageStream.toByteArray());
		return header.split(" ");
	}
	
	public byte[] createStored() {

		String header = "STORED" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNumber + messageEnd;

		return header.getBytes();
	}
	
	public void createGetChunk() {

		String header = "GETCHUNK" + " " + this.version + " " + this.senderId + " " + encrypt(this.fileId) + " " + this.chunkNumber + messageEnd;

		Peer.getMC().sendMsg(header.getBytes());
	}
	
	public byte[] createChunk() {

		String header = "CHUNK" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNumber + messageEnd;
		ByteArrayOutputStream outputMessageStream = new ByteArrayOutputStream();

		System.out.println(this.body.length);

		try {
			outputMessageStream.write(Arrays.copyOf(header.getBytes(), header.length()));
			outputMessageStream.write(Arrays.copyOf(this.body, this.body.length));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return outputMessageStream.toByteArray();
	}
	
	public void createDelete() {

		String header = "DELETE" + " " + this.version + " " + this.senderId + " " + encrypt(this.fileId) + messageEnd;

		Peer.getMC().sendMsg(header.getBytes());
 	}
	
	public void createRemoved() {

		String header = "REMOVED" + " " + this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNumber + messageEnd;
		System.out.println(header);
		Peer.getMC().sendMsg(header.getBytes());
	}
	
	
	
	
}
