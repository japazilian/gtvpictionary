package edu.purdue.sigapp.picto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
    }
    
    @Override
    public void onResume() {
        super.onResume();
        play_btn = (ImageButton)findViewById(R.id.imageButton1);
        play_btn.setOnClickListener(this);
        opts_btn = (ImageButton)findViewById(R.id.imageButton2);
        opts_btn.setOnClickListener(this);
        
        sm = new SoundManager(this);
        sm.PlaySound(SoundManager.MAIN_SOUND);
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
    		
    		final CharSequence[] items = {"Group Viewer", "Drawing Pad"};

    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Pick a mode");
    		//builder.setMessage("Picto requires two Android devices, one to act"
    		//		+ " as a drawing pad, and one to act as a group viewer.");
    		builder.setItems(items, new DialogInterface.OnClickListener() {
    		    public void onClick(DialogInterface dialog, int item) {

    	    		sm.PlaySound(SoundManager.POSITIVE_SOUND);
    		        deviceTypeDialogClosed(item);
    		    }
    		});
    		AlertDialog alert = builder.create();
    		alert.show();
    	}
    	else if (arg0.getId() == R.id.imageButton2) {
    		sm.PlaySound(SoundManager.NEGATIVE_SOUND);
    	}
    }
    
    private final int SERVER = 0;
    private final int CLIENT = 1;
    private void deviceTypeDialogClosed(int type) {		
    	switch(type) {
    	case SERVER:
    		
    		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
    		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    		int ipAddress = wifiInfo.getIpAddress();
    		String s = String.format("%d.%d.%d.%d",
    				(ipAddress & 0xff),
    				(ipAddress >> 8 & 0xff),
    				(ipAddress >> 16 & 0xff),
    				(ipAddress >> 24 & 0xff));
    		
    		
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Group Viewer Code");
    		builder.setMessage(s);
    		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

		    		sm.PlaySound(SoundManager.POSITIVE_SOUND);
		    		sm.StopSound();
		    		Intent sni = new Intent(StartScreen.this, MainGame.class);
		    		sni.putExtra("isGTV", true);	
		    		startActivity(sni);				
				}
    			
    		});
    		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					
					
				}
			});
    		
    		AlertDialog alert = builder.create();
    		alert.show(); 
    		break;
    	case CLIENT:
    		final EditText editView = new EditText(this);
    		AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
    		builder2.setTitle("Enter Group Viewer Code");
    		builder2.setView(editView);
    		builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

		    		sm.PlaySound(SoundManager.POSITIVE_SOUND);
		    		sm.StopSound();
					Intent sni = new Intent(StartScreen.this, MainGame.class);
		    		sni.putExtra("isGTV", false);
		    		sni.putExtra("code", editView.getText().toString());
		    		startActivity(sni);					
				}
    			
    		});
    		builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					
					
				}
			});
    		
    		AlertDialog alert2 = builder2.create();
    		alert2.show(); 
    		break;
    	}
    }
}