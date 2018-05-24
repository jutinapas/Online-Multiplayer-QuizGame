//นายจุตินภัส คลังเจริญกุล 5810400973

package clientProgram;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ClientProgram
{

	private String name;
	private String roomName;
	private InetSocketAddress address;
	private Channel channel;

	public ClientProgram(String name, String roomName) {
		this.name = name;
		this.roomName = roomName;
		address = new InetSocketAddress("localhost", 2404);
	}

	public void start() throws SocketException {
		channel = new Channel();
		channel.bind();
		channel.start(); // Start Receive
	}

	public void sendToServer(String msg) throws IOException {
		channel.sendToServer(address, msg);
	}

	public void stop() throws IOException {
		channel.stop();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public void setController(ControllerLoginPage controllerLoginPage) {
		channel.setController(controllerLoginPage);
	}

	public void setController(ControllerQuizPage controllerQuizPage) {
		channel.setController(controllerQuizPage);
	}


}
