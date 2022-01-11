package me.hellscoder;

import java.util.Objects;

import me.hellscoder.connection.TCPProxyConnection;

public class StratumStart {

	public static void main(String[] args) {
	
		
		if(System.getProperty("debug") == null) {
			System.setProperty("debug", "N");
		}
		
        Objects.requireNonNull(System.getProperty("remoteStratum"), "remoteStratum cannot be null. use -DremoteStratum=scrypt.eu-west.nicehash.com");
        Objects.requireNonNull(System.getProperty("remotePort"), "remotePort cannot be null. use -DremotePort=3333");
        Objects.requireNonNull(System.getProperty("worker"), "worker cannot be null. user -Dworker=pool_worker");
        Objects.requireNonNull(System.getProperty("password"), "password cannot be null. user -Dpassword=pool_worker");
		
        String remoteHost = System.getProperty("remoteStratum");
        int remotePort = Integer.parseInt(System.getProperty("remotePort"));
        
   

        System.out.println("Stratum server was started on port 3333");

        TCPProxyConnection tcpIpProxy = new TCPProxyConnection(remoteHost, remotePort, 3333);
        tcpIpProxy.listen();

	}

}
