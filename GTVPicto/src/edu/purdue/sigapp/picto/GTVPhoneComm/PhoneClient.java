package edu.purdue.sigapp.picto.GTVPhoneComm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import edu.purdue.sigapp.picto.DrawingPoint;
import edu.purdue.sigapp.picto.MainGame;

public class PhoneClient {
	
	private String host = "10.0.0.5";
	private int port = 4444;
    PrintWriter out = null;
    BufferedReader in = null;
    private Socket clientSocket;
    private MainGame mainGame;
    private Thread mClientConnectThread = null;
    
    public PhoneClient(MainGame mainGame, String code) {
    	this.mainGame = mainGame;
    	host = code;
    }
	
	public void startClient() {
		
		mClientConnectThread = new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				clientSocket = null;
		        while (clientSocket == null) {
			        try {
			            clientSocket = new Socket(host, port);
			            out = new PrintWriter(clientSocket.getOutputStream(), true);
			            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			        } catch (UnknownHostException e) {
			            e.printStackTrace();
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
		        }
		        
		        mainGame.progdialog.dismiss();
			}
			
		});
		mainGame.progdialog = new ProgressDialog(mainGame);
		mainGame.progdialog.setCancelable(false);
		mainGame.progdialog.setTitle("Searching");
		mainGame.progdialog.setMessage("Searching for Group Viewer on your " +
				"network, please launch Picto on your other Android device");
		mainGame.progdialog.setButton("Cancel", 
				new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				PhoneClient.this.close();			
				mainGame.finish();
			}
		});		
		mainGame.progdialog.show();
		
		mClientConnectThread.start();
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
	
	public void sendBtnCorrect() {
		sendMessage(""+Constants.BTNCORRECT);
	}
	
	public void sendBtnIncorrect() {
		sendMessage(""+Constants.BTNINCORRECT);
	}
	
	public void sendBtnClear() {
		sendMessage(""+Constants.BTNCLEAR);
	}
	
	public void sendMessage(String s) {
		out.println(s);
	}
	
	public void close() {
		try {
			out.close();
			in.close();
			clientSocket.close();
			if (mClientConnectThread != null) {
				mClientConnectThread.stop();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
