package edu.purdue.sigapp.picto.GTVPhoneComm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;
import edu.purdue.sigapp.picto.DrawingPoint;
import edu.purdue.sigapp.picto.MainGame;
import edu.purdue.sigapp.picto.R;

public class GTVServer {

	ServerSocket serverSocket = null;
    Socket clientSocket = null;
    MainGame mainGame;
    
    public GTVServer (MainGame mainGame) {
    	this.mainGame = mainGame;
    }
	
	public void startServer() {
		
		Thread thread = new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				try {
			        serverSocket = new ServerSocket(4444);
			    } catch (IOException e1) {
			    	e1.printStackTrace();
			    }

			    try {
			        clientSocket = serverSocket.accept();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			    
			    try {
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(
					        new InputStreamReader(
					        clientSocket.getInputStream()));
					String inputLine, outputLine = null;

					while ((inputLine = in.readLine()) != null) {
					     processLine(inputLine);
					}
					out.close();
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		thread.start();
		
	}

	private void processLine(String inputLine) {
		if (inputLine.startsWith(""+Constants.POINT)) {
			String[] attr = inputLine.split(",");
			DrawingPoint p = new DrawingPoint();
			p.point = new Point();
			p.paint = new Paint();

		    p.paint.setDither(true);
		    //p.paint.setColor(Color.BLACK);
		    p.paint.setStyle(Paint.Style.STROKE);
		    p.paint.setStrokeJoin(Paint.Join.ROUND);
		    p.paint.setStrokeCap(Paint.Cap.ROUND);
		    p.paint.setStrokeWidth(3);
		    
			p.point.x = Integer.parseInt(attr[1]);
			p.point.y = Integer.parseInt(attr[2]);
			p.paint.setColor(Integer.parseInt(attr[3]));
			p.newPath = Boolean.parseBoolean(attr[4]);
			final DrawingPoint pfinal = p;
			mainGame.runOnUiThread(new Runnable() {

				public void run() {
					mainGame.ds_canvas.addDrawingPath(pfinal);
					mainGame.ds_canvas.invalidate();
				}
				
			});			
		}
		else if (inputLine.startsWith(""+Constants.CLEAR)) {
			mainGame.runOnUiThread(new Runnable() {

				public void run() {
					mainGame.ds_canvas.clearDrawingPath();
					mainGame.ds_canvas.invalidate();
				}
				
			});
		}
		else if (inputLine.startsWith(""+Constants.TOUCH)) {
			mainGame.runOnUiThread(new Runnable() {

				public void run() {
					mainGame.onTouch(mainGame.findViewById(R.id.relativeLayout1), null);
				}
				
			});
		}
		else if (inputLine.startsWith(""+Constants.BTNCORRECT)) {
			mainGame.runOnUiThread(new Runnable() {

				public void run() {
					View v = new View(mainGame);
					v.setId(R.id.btn_correct);
					mainGame.onClick(v);
				}
				
			});
		}
		else if (inputLine.startsWith(""+Constants.BTNINCORRECT)) {
			mainGame.runOnUiThread(new Runnable() {

				public void run() {
					View v = new View(mainGame);
					v.setId(R.id.btn_incorrect);
					mainGame.onClick(v);
				}
				
			});
		}
		else if (inputLine.startsWith(""+Constants.BTNCLEAR)) {
			mainGame.runOnUiThread(new Runnable() {

				public void run() {
					View v = new View(mainGame);
					v.setId(R.id.btn_clear);
					mainGame.onClick(v);
				}
				
			});
		}
		
	}
	
	
	public void close() {
		try {
			clientSocket.close();
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
   
}
