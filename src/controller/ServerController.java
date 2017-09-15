package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import application.MpcApplication;
import exceptions.InconsistentMoveException;
import javafx.application.Platform;
import model.GameMove;

/**
 * Der ServerController wird für den Multiplayer genutzt. Er verwaltet die
 * Sockets und eingehende/ausgehende Nachrichten.
 * 
 * @author Friedemann Runte
 *
 */
public class ServerController {
	private static final int PORT = 3333;

	/**
	 * Socket welcher auf eine Verbindung wartet, wenn sich das Programm als Host
	 * agieren soll
	 */
	private volatile ServerSocket serverSocket;

	/**
	 * Socket über welchen die aktive Kommunikation zwischen zwei MPC-Programmen
	 * stattfindet
	 */
	private volatile Socket activeSocket;

	/** Thread in dem die eingehenden Nachrichten verarbeitet werden */
	private volatile Thread receiveThread;

	/** Thread in dem die ausgehenden Nachrichten verarbeitet werden */
	private volatile Thread sendThread;

	/** Liste ide als Buffer für die eingehenden Nachrichten fungiert */
	private volatile ArrayList<String> receivedMessages = new ArrayList<String>();

	/** Liste ide als Buffer für die ausgehenden Nachrichten fungiert */
	private volatile ArrayList<String> outgoingMessages = new ArrayList<String>();
	private MPCController mpcController;
	private volatile Thread readMovesThread;
	private boolean isOnline = false;

	/**
	 * Gibt zurück, ob der Server zur Zeit online ist
	 * 
	 * @return Gibt <i>true</i> zurück, falls der ServerController online ist,
	 *         <i>false</i> sonst.
	 */
	public boolean isOnline() {
		return isOnline;
	}

	/**
	 * Öffnet ein ServerSocket in einem Thread welches auf eine Verbindung wartet
	 * Das Programm befindet sich somit effektiv im Host-Modus
	 */
	public void openServer() {
		receiveThread = new Thread(() -> {
			// Thread currentThread = Thread.currentThread();
			try {
				serverSocket = new ServerSocket(PORT);
				activeSocket = serverSocket.accept();
				this.isOnline = true;
				startThreads();
			} catch (IOException e) {
				// Brauchen wir nicht
			}

		});
		receiveThread.setDaemon(true);
		receiveThread.start();
	}

	/**
	 * Verbindet sich über das Netzwerk mit eiem Anderen Programm welches sich im
	 * Host-Modus befindet
	 */
	public void connect(String hostname) {
		isOnline = true;
		try {
			if (serverSocket != null) {
				serverSocket.close();
				serverSocket = null;
			}
			activeSocket = new Socket(hostname, PORT);
			startThreads();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			disconnect();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			disconnect();
			e.printStackTrace();
		}
	}

	/**
	 * Schließt die Verbindung zu allen Sockets, leert alle Buffer und schließt die Threads.
	 */
	public void disconnect() {
		if(this.isOnline) {
			this.isOnline = false;
			try {
				if (serverSocket != null) {
					serverSocket.close();
					serverSocket = null;
				}
				if (activeSocket != null) {
					activeSocket.close();
				}
			} catch (IOException e) {
			}
			try {
				if(sendThread!=null)
				sendThread.join();
				if(receiveThread!=null)
				receiveThread.join();
				if(readMovesThread!=null)
				readMovesThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			outgoingMessages.clear();
			receivedMessages.clear();
		}
	}

	/**
	 * Startet die beiden Threads welche das Netzwerk-IO verwalten als Daemons mit
	 * den jeweiligen Methoden
	 */
	private void startThreads() {
		System.out.println("Threads started");
		this.isOnline = true;
		receiveThread = new Thread(() -> receiveLoop());
		sendThread = new Thread(() -> sendLoop());
		receiveThread.start();
		sendThread.start();
	}

	/** Methode welche vom Thread zur input-Verarbeitung aufgerufen wird */
	private void receiveLoop() {
		// Thread thisThread = Thread.currentThread();
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(activeSocket.getInputStream()));
			while (isOnline && !activeSocket.isClosed()) {
				String nextMessage = inFromClient.readLine();
				if (nextMessage == null) {
					activeSocket.close();
					sendThread = null;
					receiveThread = null;
					break;
				}
				receivedMessages.add(nextMessage);
				System.out.println("Chat-Partner: " + nextMessage);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			disconnect();
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			disconnect();
			//e.printStackTrace();
		}
	}

	/**
	 * Liest Moves aus Messages und sendet diese direkt an die Spiellogik.
	 */
	public void readMoves() {
		while (this.isOnline) {
			String receive = receiveMessage();
			if (receive != null) {
				// if (receive.equals("ENDE")) {
				// System.out.println("ENDE gelesen");
				// Platform.runLater(() -> mpcController.getGameLogicController().);
				// } else {
				System.out.println("Move gelesen: " + receive);
				GameMove move = GameMove.fromProtocol(receive, mpcController.getBattlefield());
				Platform.runLater(() -> {
					try {
						mpcController.getGameLogicController().executeMove(move);
					} catch (InconsistentMoveException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				// }
				receive = null;
			}
		}
		
	}

	/**
	 * Gibt das ServerSocket zurück.
	 * @return das derzeitige ServerSocket
	 */
	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public String receiveMessage() {
		System.out.println("waiting for message");
		while (receivedMessages.size() == 0) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (receivedMessages.size() != 0) {
			String receive = receivedMessages.get(0);
			receivedMessages.remove(receive);
			return receive;
		}
		return null;
	}

	/** Methode welche vom Thread 
	 * zur output-Verarbeitung aufgerufen wird 
	 * 
	 */
	private void sendLoop() {
		Thread thisThread = Thread.currentThread();
		try {
			PrintWriter outToClient = new PrintWriter(activeSocket.getOutputStream());
			while (thisThread == sendThread && !activeSocket.isClosed() && isOnline) {

				if (!outgoingMessages.isEmpty()) {
					System.out.println("Printed: " + outgoingMessages.get(0));
					outToClient.println(outgoingMessages.get(0));
					outToClient.flush();
					outgoingMessages.remove(0);
				}
				Thread.sleep(50);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			disconnect();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			disconnect();
		}
	}

	/**
	 * Schreibt eine Nachricht in den Sendebuffer welche dann von dem Send-Thread
	 * versandt wird
	 */
	public void sendMessage(String msg) {
		if (activeSocket.isConnected()) {
			System.out.println("Message queued: " + msg);
			outgoingMessages.add(msg);
		}
	}

	/**
	 * Setzt den mccController
	 * @param mpcController der derzeitige MPCController 
	 */
	public ServerController(MPCController mpcController) {
		this.mpcController = mpcController;
		
		MpcApplication.closeRequestHandlers.add((WindowEvent) ->{
			disconnect();
		});
	}

	/**
	 * Gibt das readMovesThread zurück zum lesen von Moves
	 * @return das aktuelle Thread zum lesen von Moves.
	 */
	public Thread getReadMovesThread() {
		return readMovesThread;
	}

	/**
	 * Setzt das readMovesThread zum lesen von Moves.
	 * @param readMovesThread das neue Thread zum lesen von Moves
	 */
	public void setReadMovesThread(Thread readMovesThread) {
		this.readMovesThread = readMovesThread;
	}

}
