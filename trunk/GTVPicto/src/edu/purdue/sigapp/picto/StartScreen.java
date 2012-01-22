package edu.purdue.sigapp.picto;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class StartScreen extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	ImageButton play_btn, opts_btn;
	
	SoundManager sm;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        play_btn = (ImageButton)findViewById(R.id.imageButton1);
        play_btn.setOnClickListener(this);
        opts_btn = (ImageButton)findViewById(R.id.imageButton2);
        opts_btn.setOnClickListener(this);
        
        sm = new SoundManager(this);
    }
    
    public void onClick(View arg0) {
    	if (arg0.getId() == R.id.imageButton1) {
    		sm.PlaySound(SoundManager.POSITIVE_SOUND);
    	}
    	else if (arg0.getId() == R.id.imageButton2) {
    	}
    }
}