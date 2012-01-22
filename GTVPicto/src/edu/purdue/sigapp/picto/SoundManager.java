package edu.purdue.sigapp.picto;

import android.app.Activity;
import android.media.MediaPlayer;

public class SoundManager {
	public static final int POSITIVE_SOUND = 0;
	public static final int NEGATIVE_SOUND = 1;
	public static final int COUNTDOWN_SOUND = 2;
	public static final int TIMESUP_SOUND = 3;
	public static final int CLEAR_SOUND = 4;
	
	MediaPlayer pos_snd = new MediaPlayer();
	MediaPlayer neg_snd = new MediaPlayer();
	MediaPlayer cd_snd = new MediaPlayer();
	MediaPlayer tu_snd = new MediaPlayer();
	MediaPlayer clear_snd = new MediaPlayer();
	
	SoundManager(Activity act) {
		pos_snd = MediaPlayer.create(act.getBaseContext(), R.raw.positive);
		neg_snd = MediaPlayer.create(act.getBaseContext(), R.raw.negative);
		cd_snd = MediaPlayer.create(act.getBaseContext(), R.raw.countdown);
		tu_snd = MediaPlayer.create(act.getBaseContext(), R.raw.timesup);
		clear_snd = MediaPlayer.create(act.getBaseContext(), R.raw.clear);
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
			default:
				neg_snd.seekTo(0);
				neg_snd.start();
				break;
		}
	}
	
	void StopSound() {
		
	}

}
