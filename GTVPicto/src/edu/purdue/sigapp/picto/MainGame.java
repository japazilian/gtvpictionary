package edu.purdue.sigapp.picto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.purdue.sigapp.picto.GTVPhoneComm.GTVServer;
import edu.purdue.sigapp.picto.GTVPhoneComm.PhoneClient;

/**
 * 
 * @author rodrigo
 * drawing canvas used from http://www.tutorialforandroid.com/2010/11/drawing-with-canvas-in-android-renewed.html
 */
public class MainGame extends Activity implements OnTouchListener, OnClickListener {

	private boolean mGameDone = false;
	private final int STARTROUND = 0;
	private final int ROUND = 1;
	private final int ENDROUND = 2;
	private final int ENDGAME = 3;
	private final int ASKREADY = 4;
	private final int COUNTDOWN = 5;
	private final int ASKENDROUND = 6;
	public int mState = STARTROUND;
	
	private int mCurrTeam = 1;
	private int mCurrRound = 1;
	private int[] mTeamScores;
	private int mCountdownValue = 3;
	private StopWatch mStopWatch;
	private Paint mCurPaint;
	private DrawingPoint mCurDrawingPoint;
	
	// Set by main screen
	private int mTeamNum = 2; 
	private int mRoundTime = 60;
	private int mRoundNum = 3;
	
	private RelativeLayout rl_dialogs, rl_screen; 
	private TextView txt_timer, txt_word;
	private ImageButton btn_clear, btn_tools, btn_correct, btn_incorrect;
	public DrawingSurface ds_canvas;
	private PhoneClient mPhoneClient;
	private boolean isGTV = true;
	public ProgressDialog progdialog;
	private WordBank mWordBank;
	
	
	// @CORNDAWG
	SoundManager sm;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        setUpGUI();
        
        if (isGTV) {
        	GTVServer server = new GTVServer(this);
        	server.startServer();
        }
        else {
        	mPhoneClient = new PhoneClient(this);
        	mPhoneClient.startClient();
        }
        
        // @CORNDAWG
        sm = new SoundManager(this);
        
        mWordBank = new WordBank();
        mWordBank.loadWords();
        
        GameEngine gameEngine = new GameEngine();
        gameEngine.start();     
        
        
    }
	
	public void setUpGUI() {
        rl_dialogs = (RelativeLayout) findViewById(R.id.relativeLayout1);
        rl_screen = (RelativeLayout) findViewById(R.id.relativeLayout2);
        txt_timer = (TextView) findViewById(R.id.txt_timer);
        txt_word = (TextView) findViewById(R.id.txt_word);
        btn_correct = (ImageButton) findViewById(R.id.btn_correct);
        btn_correct.setOnClickListener(this);
        btn_incorrect = (ImageButton) findViewById(R.id.btn_incorrect);
        btn_incorrect.setOnClickListener(this);
        btn_tools = (ImageButton) findViewById(R.id.btn_draw_tools);
        btn_tools.setOnClickListener(this);
        btn_clear = (ImageButton) findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);
        ds_canvas = (DrawingSurface) findViewById(R.id.surfaceView1);
        ds_canvas.setZOrderOnTop(true);    // necessary
        SurfaceHolder h = ds_canvas.getHolder();
        h.setFormat(PixelFormat.TRANSPARENT);
        
        if (isGTV) {
        	txt_word.setVisibility(View.INVISIBLE);
        	btn_correct.setVisibility(View.INVISIBLE);
        	btn_incorrect.setVisibility(View.INVISIBLE);
        	btn_clear.setVisibility(View.INVISIBLE);
        	btn_tools.setVisibility(View.INVISIBLE);
        }
        
		mStopWatch = new StopWatch();
		mTeamScores = new int[mTeamNum];
		for (int i=0; i<mTeamNum; i++) {
			mTeamScores[i] = 0;
		}
	}
	
	private void askRoundStart() {
		runOnUiThread(new Runnable() {
		     public void run() {
		    	 txt_timer.setText(""+mRoundTime);
		    	 txt_timer.setTextColor(getResources().getColor(R.color.text_color));
		    	 txt_word.setText("Picto");
				btn_correct.setEnabled(false);
				btn_incorrect.setEnabled(false);
				btn_tools.setEnabled(false);
				btn_clear.setEnabled(false);
				rl_dialogs.setVisibility(View.VISIBLE);
				ds_canvas.setOnTouchListener(null);
		 		// Ask if team is ready
		 		// Let's put in a textview for now, maybe we can use an image later
		 		TextView txt_ready = new TextView(MainGame.this);
		 		txt_ready.setGravity(Gravity.CENTER);
		 		txt_ready.setText("Team "+mCurrTeam+" ready?\n"+"Touch here to begin.");
		 		txt_ready.setTextColor(getResources().getColor(R.color.text_color));
		 		txt_ready.setTextSize(30);
		 		rl_dialogs.addView(txt_ready);
		 		final Animation animation = 
		 			AnimationUtils.loadAnimation(MainGame.this, R.anim.round_start);
		 		animation.reset();
		 		animation.setAnimationListener(showRoundStartListener);
		 		rl_dialogs.startAnimation(animation);
		 		mState = ASKREADY;
		    }
		});
        
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

		btn_correct.setEnabled(true);
		btn_incorrect.setEnabled(true);
		btn_tools.setEnabled(true);
		btn_clear.setEnabled(true);
		rl_dialogs.setVisibility(View.GONE);
		if (!isGTV) {
			ds_canvas.setOnTouchListener(this);
		}
		setCurrentPaint();
		txt_word.setText(mWordBank.getNextWord());
	}
	
	private void showEndRound() {
		mState = ASKENDROUND;
		runOnUiThread(new Runnable() {
		     public void run() {
				btn_correct.setEnabled(false);
				btn_incorrect.setEnabled(false);
				btn_tools.setEnabled(false);
				btn_clear.setEnabled(false);
				rl_dialogs.setVisibility(View.VISIBLE);
				ds_canvas.setOnTouchListener(null);
				ds_canvas.clearDrawingPath();
				ds_canvas.invalidate();
				if (!isGTV) {
					mPhoneClient.sendClear();
				}
		 		TextView txt_ready = new TextView(MainGame.this);
		 		txt_ready.setTextColor(getResources().getColor(R.color.text_color));
		 		txt_ready.setTextSize(30);
		 		txt_ready.setText(getGameScore());
		 		rl_dialogs.addView(txt_ready);
		 		final Animation animation = 
					AnimationUtils.loadAnimation(MainGame.this, R.anim.end_round);
				animation.reset();
				animation.setAnimationListener(showRoundStartListener);
				rl_dialogs.startAnimation(animation);
		    }
		});
		
		if (mCurrTeam == mTeamNum) {
			if (mCurrRound == mRoundNum) {
				// end of game
				mState = ENDGAME;
				mWordBank.resetUsedWords();
			}
			else {
				// go to next round
				mCurrTeam = 1;
				mCurrRound++;
			}
		}
		else {
			mCurrTeam++;
		}
	}
	
	private String getGameScore() {
		StringBuilder ret = new StringBuilder();
		ret.append("Scoreboard\n\n");
		for (int i=1; i<mTeamNum+1; i++) {
			ret.append("Team "+i+": ");
			ret.append(mTeamScores[i-1]+" points\n");
		}
		
		return ret.toString();
	}

	private class GameEngine extends Thread {

		@Override
		public synchronized void run()
		{
			while(!mGameDone) // outer game loop
			{
				try {
					Thread.sleep(500);
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
						if (timeLeft == 0) {
							mState = ENDROUND;
						}
						break;
					case ENDROUND:
						showEndRound();
						break;
					case ASKENDROUND:
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
	
	private void setCurrentPaint(){
	    mCurPaint = new Paint();
	    mCurPaint.setDither(true);
	    mCurPaint.setColor(Color.BLACK);
	    mCurPaint.setStyle(Paint.Style.STROKE);
	    mCurPaint.setStrokeJoin(Paint.Join.ROUND);
	    mCurPaint.setStrokeCap(Paint.Cap.ROUND);
	    mCurPaint.setStrokeWidth(3);
	}

	public boolean onTouch(View v, MotionEvent motionEvent) {
		switch(v.getId()) {
		case R.id.relativeLayout1:
			switch(mState) {
			case ASKREADY:
				rl_dialogs.removeAllViews();
				rl_dialogs.setOnTouchListener(null);
				mState = COUNTDOWN;
				sm.PlaySound(SoundManager.COUNTDOWN_SOUND);
				roundCountdown();				
				break;
			case COUNTDOWN:
				break;
			case ROUND:
				break;
			case ASKENDROUND:
				rl_dialogs.removeAllViews();
				rl_dialogs.setOnTouchListener(null);
				mState = STARTROUND;				
				break;
			}
			if (!isGTV) {
				mPhoneClient.sendTouch();
			}
			break;
		case R.id.surfaceView1:
			if(motionEvent.getAction() == MotionEvent.ACTION_DOWN ){
				DrawingPoint point = new DrawingPoint();
				point.paint = mCurPaint;
				point.point = new Point((int)motionEvent.getX(), (int)motionEvent.getY());
				point.newPath = true;
				ds_canvas.addDrawingPath(point);
				ds_canvas.invalidate(); 
				if (!isGTV) {
					mPhoneClient.sendPoint(point);
				}
				return true;
			}/*else if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
				mCurDrawingPoint.path.lineTo(motionEvent.getX(), motionEvent.getY());
				return true;
			}*/else if(motionEvent.getAction() == MotionEvent.ACTION_UP ||
					motionEvent.getAction() == MotionEvent.ACTION_MOVE){
				DrawingPoint point = new DrawingPoint();
				point.paint = mCurPaint;
				point.point = new Point((int)motionEvent.getX(), (int)motionEvent.getY());
				ds_canvas.addDrawingPath(point);
				ds_canvas.invalidate();
				if (!isGTV) {
					mPhoneClient.sendPoint(point);
				}
				return true;
			}
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
			case ASKENDROUND:
				rl_dialogs.setOnTouchListener(MainGame.this);
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

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.btn_correct:
			ds_canvas.clearDrawingPath();
			ds_canvas.invalidate();
			mTeamScores[mCurrTeam-1]++;
			sm.PlaySound(SoundManager.POSITIVE_SOUND);
			if (!isGTV) {
				mPhoneClient.sendBtnCorrect();
			}
			txt_word.setText(mWordBank.getNextWord());
			break;
		case R.id.btn_incorrect:
			// change word
			ds_canvas.clearDrawingPath();
			ds_canvas.invalidate();
			sm.PlaySound(SoundManager.NEGATIVE_SOUND);
			if (!isGTV) {
				mPhoneClient.sendBtnIncorrect();
			}
			txt_word.setText(mWordBank.getNextWord());
			break;
		case R.id.btn_draw_tools:
			break;
		case R.id.btn_clear:
			ds_canvas.clearDrawingPath();
			ds_canvas.invalidate();
			sm.PlaySound(SoundManager.CLEAR_SOUND);
			if (!isGTV) {
				mPhoneClient.sendBtnClear();
			}
			break;
		}
		
	}
}

