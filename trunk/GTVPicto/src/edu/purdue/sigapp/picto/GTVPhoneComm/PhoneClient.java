package edu.purdue.sigapp.picto.GTVPhoneComm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.purdue.sigapp.picto.DrawingPoint;

public class PhoneClient {
	
	private String host = "10.0.0.5";
	private int port = 4444;
    PrintWriter out = null;
    BufferedReader in = null;
	
	public void startClient() {
        Socket clientSocket = null;
 
        try {
            clientSocket = new Socket(host, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        /*BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;
 
        while ((fromServer = in.readLine()) != null) {
            System.out.println("Server: " + fromServer);
            if (fromServer.equals("Bye."))
                break;
             
            fromUser = stdIn.readLine();
	        if (fromUser != null) {
	                System.out.println("Client: " + fromUser);
	                out.println(fromUser);
	        }
        }*/
	}
	
	public void sendPoint(DrawingPoint p) {
		String s = ""+Constants.POINT+","+p.point.x+","+p.point.y+","+p.paint.getColor()+","+p.newPath;
		sendMessage(s);
	}
	
	public void sendClear() {
		sendMessage(""+Constants.CLEAR);
	}
	
	public void sendTouch() {
		sendMessage(""+Constants.TOUCH);
	}
	
	public void sendMessage(String s) {
		out.println(s);
	}
	
	public void close() {
		try {
			out.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
