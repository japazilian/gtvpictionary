package edu.purdue.sigapp.picto;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainGame extends Activity implements OnTouchListener {

	private boolean mGameDone = false;
	private final int STARTROUND = 0;
	private final int ROUND = 1;
	private final int ENDROUND = 2;
	private final int ENDGAME = 3;
	private final int ASKREADY = 4;
	private final int COUNTDOWN = 5;
	public int mState = STARTROUND;
	
	private int mCurrTeam = 0;
	private int mCountdownValue = 3;
	private StopWatch mStopWatch;
	
	// Set by main screen
	private int mTeamNum = 2; 
	private int mRoundTime = 30;
	private int mRoundNum = 3;
	
	private RelativeLayout rl_dialogs; 
	private TextView txt_timer, txt_word;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        setUpGUI();
        
        GameEngine gameEngine = new GameEngine();
        gameEngine.start();     
        
        
    }
	
	public void setUpGUI() {
        rl_dialogs = (RelativeLayout) findViewById(R.id.relativeLayout1);
        txt_timer = (TextView) findViewById(R.id.txt_timer);
		mStopWatch = new StopWatch();
	}
	
	private void askRoundStart() {

        txt_timer.setText(""+mRoundTime);
		// Ask if team is ready
		// Let's put in a textview for now, maybe we can use an image later
		TextView txt_ready = new TextView(this);
		txt_ready.setText("Team "+mCurrTeam+" ready?");
		txt_ready.setTextColor(R.color.text_color);
		txt_ready.setTextSize(30);
		rl_dialogs.addView(txt_ready);
		final Animation animation = 
			AnimationUtils.loadAnimation(this, R.anim.round_start);
		animation.reset();
		animation.setAnimationListener(showRoundStartListener);
		rl_dialogs.startAnimation(animation);
		mState = ASKREADY;
	}
	
	private void roundCountdown() {
		TextView txt_ready = new TextView(this);
		if (mCountdownValue > 0) {
			txt_ready.setText(""+mCountdownValue);
			mCountdownValue--;
		}
		else {
			txt_ready.setText("Start!");
			// this is no mistake, we need this in order to get the 
			// animation listener to call start round, without it, 
			// we keep repeating the start animation
			mCountdownValue--; 
		}
		txt_ready.setTextColor(getResources().getColor(R.color.text_color));
		txt_ready.setTextSize(50);
		rl_dialogs.addView(txt_ready);
		final Animation animation = 
			AnimationUtils.loadAnimation(this, R.anim.countdown);
		animation.reset();
		animation.setAnimationListener(showRoundStartListener);
		rl_dialogs.startAnimation(animation);
	}
	
	private void roundStart() {
		mCountdownValue = 3;
		mStopWatch.setSW();
		mState = ROUND;		
	}

	private class GameEngine extends Thread {

		@Override
		public synchronized void run()
		{
			while(!mGameDone) // outer game loop
			{
				try {
					Thread.sleep(10);
					switch (mState) {
					case STARTROUND:
						askRoundStart();
						break;
					case ROUND:
						final int timeLeft = mStopWatch.getTimeLeft(mRoundTime);
						
						if (timeLeft <= 10) {
							runOnUiThread(new Runnable() {
							     public void run() {
							    	 txt_timer.setTextColor(getResources().
							    			 getColor(R.color.timer_danger));
									 txt_timer.setText(""+timeLeft);
							    }
							});
						}
						else {
							runOnUiThread(new Runnable() {
							     public void run() {
							    	 txt_timer.setTextColor(getResources().
							    			 getColor(R.color.text_color));
							    	 txt_timer.setText(""+timeLeft);
							    }
							});
						}
						break;
					case ENDROUND:
						break;
					case ENDGAME:
						break;
					}
					
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public boolean onTouch(View v, MotionEvent event) {
		switch(v.getId()) {
		case R.id.relativeLayout1:
			switch(mState) {
			case ASKREADY:
				rl_dialogs.removeAllViews();
				rl_dialogs.setOnTouchListener(null);
				mState = COUNTDOWN;
				roundCountdown();				
				break;
			case COUNTDOWN:
				break;
			case ROUND:
				break;
			}
			break;
		}
		return false;
	}
	
	private AnimationListener showRoundStartListener = new AnimationListener() {

		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			switch (mState) {
			case ASKREADY:
				rl_dialogs.setOnTouchListener(MainGame.this);	
				break;
			case COUNTDOWN:
				rl_dialogs.removeAllViews();
				if (mCountdownValue > -1) {
					roundCountdown();
				}
				else {
					rl_dialogs.removeAllViews();
					roundStart();
				}
				break;
			case ROUND:
				break;
			}	
		}

		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
	};
}

