package com.app.djapp;

import java.util.Random;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class SecondActivity_FX extends Activity implements OnClickListener {

	private LinearLayout ll_fx_draw;
	private Equalizer mEqualizer;
	short bands;
	private Button bt_fx_left, bt_fx_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_activity_fx);
		ll_fx_draw = (LinearLayout) findViewById(R.id.ll_fx_draw);
		new Handler().postDelayed(new Runnable() {
			private DrawingViewFX view;

			@Override
			public void run() {
				view = new DrawingViewFX(SecondActivity_FX.this, ll_fx_draw
						.getWidth(), ll_fx_draw.getHeight());
				ll_fx_draw.addView(view);
			}
		}, 500);
	
		bt_fx_left =  (Button) findViewById(R.id.bt_fx_left);
		bt_fx_right =  (Button) findViewById(R.id.bt_fx_right);
		bt_fx_left.setOnClickListener(this);
		bt_fx_right.setOnClickListener(this);
		
	}

	@Override
	protected void onPause() {
/*
		if (MainActivity.mediaPlayer1.isPlaying()) {
			MainActivity.mediaPlayer1.stop();
			MainActivity.mediaPlayer1.release();
		}
		if (MainActivity.mediaPlayer2.isPlaying()) {
			MainActivity.mediaPlayer2.stop();
			MainActivity.mediaPlayer2.release();
		}
*/
		super.onPause();
	}

	public void initMedia(String posMedia) {
		if (posMedia.equalsIgnoreCase("left")) {
			mEqualizer = new Equalizer(0,
					MainActivity.mediaPlayer1.getAudioSessionId());
			mEqualizer.setEnabled(true);
			bands = mEqualizer.getNumberOfBands();
		} else if (posMedia.equalsIgnoreCase("right")) {
			mEqualizer = new Equalizer(0,
					MainActivity.mediaPlayer2.getAudioSessionId());
			mEqualizer.setEnabled(true);
			bands = mEqualizer.getNumberOfBands();
		}

	}

	@Override
	public void onClick(View viewID) {
		switch (viewID.getId()) {

		case R.id.bt_fx_left:

			if(mEqualizer != null)
			mEqualizer.release();

			initMedia("left");
			break;

		case R.id.bt_fx_right:
			if(mEqualizer != null)
			mEqualizer.release();
			initMedia("right");
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void ontouch() {
		Random r = new Random();
		Log.v("sasa", "" + mEqualizer.getBandLevelRange()[1]);
		for (int i = 0; i < mEqualizer.getNumberOfBands(); i++) {
			int k = r.nextInt(mEqualizer.getBandLevelRange()[1]);
			Log.v("RR", "" + k);
			mEqualizer.setBandLevel((short) i, (short) k);

		}
	}
}
