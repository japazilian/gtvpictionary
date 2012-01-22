package edu.purdue.sigapp.picto;

import java.io.IOException;
import java.util.Random;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class SoundManager implements OnCompletionListener {
	public static final int POSITIVE_SOUND = 0;
	public static final int NEGATIVE_SOUND = 1;
	public static final int COUNTDOWN_SOUND = 2;
	public static final int TIMESUP_SOUND = 3;
	public static final int CLEAR_SOUND = 4;
	public static final int MAIN_SOUND = 5;
	
	MediaPlayer pos_snd = new MediaPlayer();
	MediaPlayer neg_snd = new MediaPlayer();
	MediaPlayer cd_snd = new MediaPlayer();
	MediaPlayer tu_snd = new MediaPlayer();
	MediaPlayer clear_snd = new MediaPlayer();
	
	MediaPlayer game1_snd = new MediaPlayer();
	MediaPlayer game2_snd = new MediaPlayer();
	MediaPlayer game3_snd = new MediaPlayer();
	MediaPlayer game4_snd = new MediaPlayer();
	
	private MediaPlayer game_snds[] = {game1_snd, game2_snd, game3_snd, game4_snd};
	private int mNextGameSnd = 0;
	
	private boolean mPlayMainMusic = true;
	
	SoundManager(Activity act) {
		pos_snd = MediaPlayer.create(act.getBaseContext(), R.raw.positive);
		neg_snd = MediaPlayer.create(act.getBaseContext(), R.raw.negative);
		cd_snd = MediaPlayer.create(act.getBaseContext(), R.raw.countdown);
		tu_snd = MediaPlayer.create(act.getBaseContext(), R.raw.timesup);
		clear_snd = MediaPlayer.create(act.getBaseContext(), R.raw.clear);

		game1_snd = MediaPlayer.create(act.getBaseContext(), R.raw.game1);
		game2_snd = MediaPlayer.create(act.getBaseContext(), R.raw.game2);
		game3_snd = MediaPlayer.create(act.getBaseContext(), R.raw.game3);
		game4_snd = MediaPlayer.create(act.getBaseContext(), R.raw.game4);
		
		try {
			pos_snd.prepare();
			neg_snd.prepare();
			cd_snd.prepare();
			tu_snd.prepare();
			clear_snd.prepare();
			
			game1_snd.prepare();
			//game2_snd.prepare();
			//game3_snd.prepare();
			//game4_snd.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pos_snd.setOnCompletionListener(this);
		neg_snd.setOnCompletionListener(this);
		cd_snd.setOnCompletionListener(this);
		tu_snd.setOnCompletionListener(this);
		clear_snd.setOnCompletionListener(this);
		
		game1_snd.setOnCompletionListener(this);
		game2_snd.setOnCompletionListener(this);
		game3_snd.setOnCompletionListener(this);
		game4_snd.setOnCompletionListener(this);
		
	}
	
	void PlaySound(int _id) {
		switch(_id) {
			case POSITIVE_SOUND:
				pos_snd.seekTo(0);
				pos_snd.start();
				break;
			case NEGATIVE_SOUND:
				neg_snd.seekTo(0);
				neg_snd.start();
				break;
			case COUNTDOWN_SOUND:
				cd_snd.seekTo(0);
				cd_snd.start();
				break;
			case TIMESUP_SOUND:
				tu_snd.seekTo(0);
				tu_snd.start();
				break;
			case CLEAR_SOUND:
				clear_snd.seekTo(0);
				clear_snd.start();
				break;
			case MAIN_SOUND:
				game1_snd.seekTo(0);
				game1_snd.start();
				Random r = new Random();
				r.setSeed(System.currentTimeMillis());
				//mNextGameSnd = r.nextInt(3);
				int i;
				while ((i = r.nextInt(3))==mNextGameSnd) {
				}
				mNextGameSnd = i;
				try {
					game_snds[mNextGameSnd].prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				neg_snd.seekTo(0);
				neg_snd.start();
				break;
		}
	}
	
	void StopSound() {
		mPlayMainMusic = false;
		try {
			for (int i=0; i<4; i++) {
				game_snds[i].pause();
			}
		} catch (Exception e) {}
		
	}

	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		try {
			if ((mp.equals(game1_snd) || mp.equals(game2_snd) ||
					mp.equals(game3_snd) || mp.equals(game4_snd))
					&&mPlayMainMusic) {
				game_snds[mNextGameSnd].seekTo(0);
				game_snds[mNextGameSnd].start();				
				Random r = new Random();
				r.setSeed(System.currentTimeMillis());
				mNextGameSnd = r.nextInt(3);
				game_snds[mNextGameSnd].prepare();
			}
					
			mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
