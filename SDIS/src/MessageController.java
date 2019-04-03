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
			handlePutchunk();
			break;
		}
		
	}

	private void handlePutchunk() {
		System.out.println(new String(packet.getData()));
		
	}

	private String[] parsePacketHeader() {
		
		byte[] buffer = new byte[1024]; 
		buffer = packet.getData();
		String headerString =new String(buffer, 0, packet.getLength());
		
		return headerString.split(" ");
	}
	
	
	

}
