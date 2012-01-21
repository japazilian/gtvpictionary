package edu.purdue.sigapp.picto;


public class StopWatch {
	
	private long mStartTime;
	
	public void setSW() {
		mStartTime = System.currentTimeMillis();
	}
	
	public int getTimeLeft(long roundTime) {
		long now = System.currentTimeMillis();
		long ellapsed = now - mStartTime;
		int ret = (int)(((roundTime * 1000)-ellapsed)/1000);		
		return ret;
	}

}
