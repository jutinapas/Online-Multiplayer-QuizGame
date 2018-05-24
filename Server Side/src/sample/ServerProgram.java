//นายจุตินภัส คลังเจริญกุล 5810400973

package sample;

import java.io.IOException;

public class ServerProgram
{

	private int sourcePort;
	private Channel channel;

	public ServerProgram(int sourcePort) {
		this.sourcePort = sourcePort;
	}

	public void start() throws IOException {
		channel = new Channel();
		channel.bind(sourcePort);
		channel.start(); // Start Receive
	}

	public void stop() throws IOException {
		channel.stop();
	}

}
