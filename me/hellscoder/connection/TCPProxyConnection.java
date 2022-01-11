package me.hellscoder.connection;

import java.net.ServerSocket;
import java.net.Socket;

public class TCPProxyConnection {

	private final String remoteIp;
    private final int remotePort;
    private final int port;

    public TCPProxyConnection(String remoteIp, int remotePort, int port) {
        this.remoteIp = remoteIp;
        this.remotePort = remotePort;
        this.port = port;
    }

    @SuppressWarnings("resource")
	public void listen() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New connection " + socket.getInetAddress().getHostName());
                startThread(new Connection(socket, remoteIp, remotePort));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void startThread(Connection connection) {
        Thread t = new Thread(connection);
        t.start();
    }
	
}
