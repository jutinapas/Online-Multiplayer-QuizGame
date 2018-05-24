//นายจุตินภัส คลังเจริญกุล 5810400973

package clientProgram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Channel implements Runnable
{
	private DatagramSocket socket;
	private boolean running;
	private ControllerLoginPage controllerLoginPage;
	private ControllerQuizPage controllerQuizPage;


	public void bind()
	{
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			stop();
		}
	}
	
	public void start()
	{
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void stop()
	{
		running = false;
		socket.close();
	}

	@Override
	public void run()
	{
		byte[] buffer = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
		running = true;
		while(running)
		{
			try
			{
				socket.receive(packet);
				String msg = new String(buffer, 0, packet.getLength());

				System.out.println("SERVER >> " + msg);

				//CREATE AND JOIN ROOM
				if (msg.equals("100CREATE") || msg.equals("403CREATED") || msg.equals("402FULL") || msg.equals("101JOINED") || msg.equals("401NOJOIN")) {
					controllerLoginPage.setStatus(msg);
				}

				//START GAME
				else if (msg.equals("200START")) {
					controllerLoginPage.loadQuizPage();
				}

				//WON
				else if (msg.equals("300WON")) {
					controllerQuizPage.setWinnerLabel();
				}

				//SHOW WINNER
				else if (msg.split(" ")[0].equals("301WINNER")) {
					controllerQuizPage.setWinnerLabel(msg.split(" ")[1], msg.split(" ")[2]);
				}

				//RESTART GAME
				else if (msg.equals("201RESTART")) {
					controllerQuizPage.restartGame();
				}

				else if (msg.split(" ")[0].equals("102QUIT")) {
					controllerQuizPage.setStatus(msg.split(" ")[1]);
				}

			} 
			catch (IOException e)
			{
				break;
			}
		}
	}

	public void sendToServer(InetSocketAddress address, String msg) throws IOException
	{
		byte[] buffer = msg.getBytes();
		
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		packet.setSocketAddress(address);
		
		socket.send(packet);
	}

	public void setController(ControllerLoginPage controllerLoginPage) {
		this.controllerLoginPage = controllerLoginPage;
	}

	public void setController(ControllerQuizPage controllerQuizPage) {
		this.controllerQuizPage = controllerQuizPage;
	}

}
