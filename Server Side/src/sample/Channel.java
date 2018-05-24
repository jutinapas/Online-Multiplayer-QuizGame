//นายจุตินภัส คลังเจริญกุล 5810400973

package sample;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Channel implements Runnable
{
	private DatagramSocket socket;
	private boolean running;
	private Map<String, Session> sessionsList = new HashMap<>();
	
	public void bind(int port) throws IOException {
		try {
			socket = new DatagramSocket(port);
			System.out.println("Server Started.");
		} catch (SocketException e) {
			stop();
		}
	}
	
	public void start()
	{
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void stop() throws IOException {
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

				InetSocketAddress address = new InetSocketAddress(packet.getAddress(), packet.getPort());

				System.out.println(packet.getPort() + " >> " + msg);

				//JOIN OR CREATE ROOM
				if (msg.split(" ")[0].equals("CONNECT")) {

					String roomName = msg.split(" ")[1];

					//JOIN SESSOIN
					if (msg.split(" ").length == 2) {
						//NO SESSION TO JOIN
						if (!sessionsList.containsKey(roomName)) {
							sendTo(address, "401NOJOIN");
							System.out.println("401NOJOIN >> " + address.getPort());
						}
						//JOINING
						else {
							if (!sessionsList.get(roomName).isFull()) {
								sessionsList.get(roomName).addClientAddress(address);
								//START GAME
								if (sessionsList.get(roomName).isFull()) {
									if (sessionsList.get(roomName).getHasPlayed()) {
										sessionsList.get(roomName).removeClientAddress((address));
										sessionsList.get(roomName).clearScores();
										sendTo(address, "200START");
										broadcast(roomName, "201RESTART");
										sessionsList.get(roomName).addClientAddress(address);
										System.out.println("200START >> " + address.getPort());
										System.out.println("201RESTART >> all client in session");
									} else {
										broadcast(roomName, "200START");
										System.out.println("200START >> all client in session");
									}
								}
								//JOINED
								else {
									sendTo(address, "101JOINED");
									System.out.println("101JOINED >> " + address.getPort());
								}
							}
							//ROOM FULL
							else {
								sendTo(address, "402FULL");
								System.out.println("402FULL >> " + address.getPort());
							}
						}
					}

					//CREATE SESSION
					else if (msg.split(" ").length == 3) {
						int numberOfPlayers = Integer.parseInt(msg.split(" ")[2]);

						//CREATING
						if (!sessionsList.containsKey(roomName)) {
							sessionsList.put(roomName, new Session(numberOfPlayers));
							sessionsList.get(roomName).addClientAddress(address);
							sendTo(address, "100CREATE");
							System.out.println("100CREATE >> " + address.getPort());
							//START GAME
							if (sessionsList.get(roomName).isFull()) {
								if (sessionsList.get(roomName).getHasPlayed()) {
									sessionsList.get(roomName).removeClientAddress((address));
									sessionsList.get(roomName).clearScores();
									sendTo(address, "200START");
									broadcast(roomName, "201RESTART");
									sessionsList.get(roomName).addClientAddress(address);
									System.out.println("200START >> " + address.getPort());
									System.out.println("201RESTART >> all client in session");
								} else {
									broadcast(roomName, "200START");
									System.out.println("200START >> all client in session");
								}
							}
						}
						//ALREADY CREATED
						else {
							sendTo(address, "403CREATED");
							System.out.println("403CREATED >> " + address.getPort());
						}
					}

				}

				//FINISHED QUIZ
				else if (msg.split(" ")[0].equals("FINISH")) {
					String roomName = msg.split(" ")[1];
					String name = msg.split(" ")[2];
					int score = Integer.parseInt(msg.split(" ")[3]);
					Session session = sessionsList.get(roomName);


					//ADD SCORE TO HASH MAP
					session.putToScoresMap(name, score);

					System.out.println(session.getScores().size());

					//GET WINNER IF ALL PLAYERS IN SESSION HAS FINISH QUIZ
					if (session.hasWinner()) {
						if (session.getClientsAddress().size() == 1) {
							sendTo(address, "300WON");
							System.out.println("300WON >> " + address.getPort());
						} else {
							broadcast(roomName, "301WINNER " + session.getWinner());
							System.out.println("301WINNER " + session.getWinner() + " >> all client in session");
						}
					}
				}

				//RESTART
				else if (msg.split(" ")[0].equals("RESTART")) {
					String roomName = msg.split(" ")[1];
					sessionsList.get(roomName).clearScores();
					broadcast(roomName, "201RESTART");
					System.out.println("201RESTART >> all client in session");
				}

				//QUIT
				else if (msg.split(" ")[0].equals("QUIT")) {
					String roomName = msg.split(" ")[1];
					Session session = sessionsList.get(roomName);
					session.removeClientAddress(address);
					if (session.getClientsAddress().size() > 0) {
						broadcast(roomName, "102QUIT " + msg.split(" ")[2]);
						System.out.println("102QUIT " + msg.split(" ")[2] + " >> all client in session");
					} else {
						sessionsList.remove(roomName);
						System.out.println("REMOVE " + roomName);
					}
				}

			} 
			catch (IOException e)
			{
				break;
			}
		}
	}

	public void sendTo(InetSocketAddress address, String msg) throws IOException
	{
		byte[] buffer = msg.getBytes();
		
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		packet.setSocketAddress(address);
		
		socket.send(packet);
	}

	public void broadcast(String roomName, String msg) throws IOException {
		for (InetSocketAddress address: sessionsList.get(roomName).getClientsAddress()) {
			sendTo(address, msg);
		}
	}

}
