package me.hellscoder.connection;

import java.io.IOException;
import java.net.Socket;

public class Connection implements Runnable {
	
	private final Socket clientsocket;
    private final String remoteIp;
    private final int remotePort;
    private Socket serverConnection = null;


    public Connection(Socket clientsocket, String remoteIp, int remotePort) {
        this.clientsocket = clientsocket;
        this.remoteIp = remoteIp;
        this.remotePort = remotePort;
    }

    @Override
    public void run() {
        try {
            serverConnection = new Socket(remoteIp, remotePort);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        new Thread(new Proxy(clientsocket, serverConnection, ProxyType.OUT)).start();
        new Thread(new Proxy(serverConnection, clientsocket, ProxyType.IN)).start();
        new Thread(() -> {
            while (true) {
                if (clientsocket.isClosed()) {
                    System.out.println("[Connection.class] Connection closed " + clientsocket.getInetAddress().getHostName());
                    closeServerConnection();
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    private void closeServerConnection() {
        if (serverConnection != null && !serverConnection.isClosed()) {
            try {
                System.out.println("[Connection.class] Closing remote host connection " + serverConnection.getInetAddress().getHostName()+ ":" + serverConnection.getPort());
                serverConnection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
