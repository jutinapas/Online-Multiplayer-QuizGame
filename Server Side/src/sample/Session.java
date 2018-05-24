//นายจุตินภัส คลังเจริญกุล 5810400973

package sample;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Session {

    private ArrayList<InetSocketAddress> clientsAddress = new ArrayList<>();
    private Map<String, Integer> scores = new HashMap<>();
    private int numberOfPlayers;
    private boolean hasPlayed = false;

    public Session(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void addClientAddress(InetSocketAddress address) {
        clientsAddress.add(address);
    }

    public void removeClientAddress(InetSocketAddress address) { clientsAddress.remove(address); }

    public boolean isFull() {
        return (numberOfPlayers == clientsAddress.size()) ? true : false;
    }

    public void putToScoresMap(String name, int score) {
        scores.put(name, score);
    }

    public boolean hasWinner() {
        return  (scores.size() == clientsAddress.size()) ? true:false;
    }

    public boolean getHasPlayed() {
        return  hasPlayed;
    }

    public String getWinner() {
		String winner = "";
		int max = 0;
		for (String name: scores.keySet()) {
			if (scores.get(name) > max) {
				max = scores.get(name);
				winner = name;
			}
		}
		hasPlayed = true;
		return winner + " " + max;
	}

	public void clearScores() {
        scores.clear();
    }

    public ArrayList<InetSocketAddress> getClientsAddress() {
        return clientsAddress;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }
}
