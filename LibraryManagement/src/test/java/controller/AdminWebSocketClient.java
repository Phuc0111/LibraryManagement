package controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class AdminWebSocketClient {

	private static final String SERVER_URI = "ws://localhost:12345/admin";

	private Session session;
	private CountDownLatch latch = new CountDownLatch(1);

	public AdminWebSocketClient() throws DeploymentException, IOException {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			try {
				container.connectToServer(this, new URI(SERVER_URI));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			latch.await();
		} catch (URISyntaxException | DeploymentException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		latch.countDown();
	}

	@OnMessage
	public void onMessage(String message) {
		System.out.println("Received message from server: " + message);
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		System.out.println("Connection closed: " + closeReason);
	}

	public void sendMessage(String message) {
		session.getAsyncRemote().sendText(message);
	}

	public static void main(String[] args) throws DeploymentException, IOException {
		new AdminWebSocketClient();
	}
}
