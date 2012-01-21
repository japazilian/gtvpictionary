package edu.purdue.sigapp.picto;


public class GameEngine extends Thread {
	
	public boolean done = false;
	
	/**
	 * The main game engine, updates the positions of all objects in GameObjects
	 */
	@Override
	public synchronized void run()
	{
		while(!done) // outer game loop
		{
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
