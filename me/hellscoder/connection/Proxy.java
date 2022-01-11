package me.hellscoder.connection;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.nio.charset.StandardCharsets;

import org.json.JSONObject;


public class Proxy implements Runnable {

    private final Socket in;
    private final Socket out;
    
    private final ProxyType type;
    

    public Proxy(Socket in, Socket out, ProxyType type) {
        this.in = in;
        this.out = out;
        this.type = type;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = getInputStream();
            OutputStream outputStream = getOutputStream();

            if (inputStream == null || outputStream == null) {
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = reader.readLine();
            while (line != null && !this.out.isClosed() && this.out.isConnected() && !this.in.isClosed() && this.in.isConnected()) {
            	
            	JSONObject obj = new JSONObject(line);
            	
            	
            	if(this.type == ProxyType.OUT) {
            	
                	if(obj.getString("method").equalsIgnoreCase("mining.subscribe")) {
                		line = "{\"id\":"+obj.get("id")+",\"method\":\"mining.subscribe\",\"params\":[\"HellMiner/1.0.0\",null,\""+System.getProperty("remoteStratum")+"\", "+System.getProperty("remotePort")+"]}";
                	}
                	
                	if(obj.getString("method").equalsIgnoreCase("mining.authorize")) {
                		String currentWorker = obj.getJSONArray("params").getString(0); 
                		System.out.println("Change worker name from \"" + currentWorker + "\" to \"" + System.getProperty("worker") + "\"");
                		line = "{\"id\":"+obj.get("id")+",\"method\":\"mining.authorize\",\"params\":[\""+System.getProperty("worker")+"\",\""+System.getProperty("password")+"\"]}";
                	}
                	System.out.println("--> " + obj.getString("method"));
            	}else {
            		if(obj.has("method")) {
            			System.out.println("<-- " + obj.getString("method"));
            		}
            	}
            	
            	if(System.getProperty("debug").equalsIgnoreCase("Y")) {
            		System.out.println(line);
            	}
            	byte[] bytes = (line + "\n").getBytes(StandardCharsets.UTF_8);
            	outputStream.write(bytes, 0, bytes.length);
                line = reader.readLine();
            }
            System.out.println("End of stream.");
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private InputStream getInputStream() {
        try {
            return in.getInputStream();
        } catch (IOException e) {
            ;
        }

        return null;
    }

    private OutputStream getOutputStream() {
        try {
            return out.getOutputStream();
        } catch (IOException e) {
            ;
        }

        return null;
    }

}
