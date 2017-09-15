package controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Klasse zum testen des ServerControllers
 * 
 * @author Friedemann Runte
 *
 */
public class ServerControllerTest {
	ServerController server;
	ServerController client;
	MPCController mpcController;

	/**
	 * Erstellt einen MPCController, einen Server und einen Client.
	 * 
	 */
	@Before
	public void setUp() {
		mpcController = new MPCController();
		server = new ServerController(mpcController);
		client = new ServerController(mpcController);
	}

	/**
	 * Testet, ob der Server beim Hosten auf Online gesetzt wird.
	 */
	@Test
	public void testIsOnline() {
		assertFalse(server.isOnline());
		server.openServer();
		client.connect("127.0.0.1");
		assertTrue(server.isOnline());
		server.disconnect();
		client.disconnect();
	}

	/**
	 * Testet, ob ein Server gestartet werden kann und sich jemand dazu verbinden
	 * kann.
	 */
	@Test
	public void testOpenServer() {
		server.openServer();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.connect("127.0.0.1");
		assertTrue(server.isOnline());
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server.disconnect();
		client.disconnect();
	}

	/**
	 * 
	 * Testet, ob ein Server gestartet werden kann und sich jemand dazu verbinden
	 * kann.
	 *
	 */
	@Test
	public void testConect() {
		server.openServer();
		client.connect("127.0.0.1");
		assertTrue(client.isOnline());
		server.disconnect();
		client.disconnect();
	}

	/**
	 * Testet, ob man sich vom Server wieder disconnecten kann.
	 */
	@Test
	public void testDisconnect() {
		server.openServer();
		client.connect("127.0.0.1");
		server.disconnect();
		client.disconnect();
		assertTrue(!client.isOnline());
		assertTrue(!server.isOnline());
	}

	/**
	 * Überprüft ob eine Nachricht gesendet und empfangen werden kann.
	 */
	public void testSendReceiveMessage() {
		server.openServer();
		client.connect("127.0.0.1");
		server.sendMessage("Hallo");
		assertEquals(client.receiveMessage(), "Hallo");
		server.disconnect();
		client.disconnect();
	}

}
