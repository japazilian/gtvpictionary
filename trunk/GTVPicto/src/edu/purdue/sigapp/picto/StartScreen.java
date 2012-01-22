package edu.purdue.sigapp.picto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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
    
    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	
        LinearLayout mainImage = (LinearLayout) findViewById(R.id.LinearLayout);
        mainImage.setBackgroundResource(R.anim.main_anim);

        AnimationDrawable mainAnimation = (AnimationDrawable) mainImage.getBackground();
       
        //mainAnimation.setVisible(false, true); //reset! see previous section
        mainAnimation.start();
    }
    
    public void onClick(View arg0) {
    	if (arg0.getId() == R.id.imageButton1) {
    		sm.PlaySound(SoundManager.POSITIVE_SOUND);
    		Intent sni = new Intent(StartScreen.this, MainGame.class);
			startActivity(sni);
    	}
    	else if (arg0.getId() == R.id.imageButton2) {
    		sm.PlaySound(SoundManager.NEGATIVE_SOUND);
    	}
    }
}