package com.app.djapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.app.dj.vis.LineRenderer;
import com.app.dj.vis.VisualizerViewFx;
import com.app.dj.vis.VisualizerViewMain;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class SecondActivity_FX extends Activity implements OnClickListener,
		OnItemSelectedListener {

	private LinearLayout ll_fx_draw;
	private Equalizer mEqualizer;
	short bands;
	private Button bt_fx_left, bt_fx_right;
	VisualizerViewFx mVisualizerView;
	Spinner spinner_second_fx;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		MainActivity.flagPause = true;

	}

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

		bt_fx_left = (Button) findViewById(R.id.bt_fx_left);
		bt_fx_right = (Button) findViewById(R.id.bt_fx_right);

		spinner_second_fx = (Spinner) findViewById(R.id.spinner_second_fx);

		bt_fx_left.setOnClickListener(this);
		bt_fx_right.setOnClickListener(this);

		List<String> categories = new ArrayList<String>();
		// / Flanger, Phaser, Gate, Reverb, Bit crusher, 3D,

		categories.add("Flanger");
		categories.add("Phaser");
		categories.add("Gate");
		categories.add("Reverb");
		categories.add("Bit Crusher");
		categories.add("3D");

		// Spinner click listener
		spinner_second_fx.setOnItemSelectedListener(this);

		// Creating adapter for spinner

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, categories);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spinner_second_fx.setAdapter(dataAdapter);

	}

	private void initVisSecondFirst(MediaPlayer secondFirstMediaPlayer) {

		if (secondFirstMediaPlayer != null) {
			mVisualizerView = (VisualizerViewFx) findViewById(R.id.vis_fx);
			mVisualizerView.link(secondFirstMediaPlayer);
			// Start with just line renderer
			addLineRenderer();
		}

	}

	private void addLineRenderer() {
		Paint linePaint = new Paint();
		linePaint.setStrokeWidth(3f);
		linePaint.setAntiAlias(true);
		// linePaint.setColor(Color.argb(88, 0, 128, 255));
		linePaint.setColor(Color.BLUE);
		Paint lineFlashPaint = new Paint();
		lineFlashPaint.setStrokeWidth(3f);
		lineFlashPaint.setAntiAlias(true);
		// lineFlashPaint.setColor(Color.argb(188, 255, 255, 255));

		lineFlashPaint.setColor(Color.BLUE);

		LineRenderer lineRenderer = new LineRenderer(linePaint, lineFlashPaint,
				true);
		mVisualizerView.addRenderer(lineRenderer);
	}

	@Override
	protected void onPause() {
		/*
		 * if (MainActivity.mediaPlayer1.isPlaying()) {
		 * MainActivity.mediaPlayer1.stop();
		 * MainActivity.mediaPlayer1.release(); } if
		 * (MainActivity.mediaPlayer2.isPlaying()) {
		 * MainActivity.mediaPlayer2.stop();
		 * MainActivity.mediaPlayer2.release(); }
		 */
		super.onPause();
	}

	public void initMedia(String posMedia) {

		if (mVisualizerView != null) {
			mVisualizerView.release();
		}

		if (posMedia.equalsIgnoreCase("left")
				&& MainActivity.mediaPlayer1 != null) {
			mEqualizer = new Equalizer(0,
					MainActivity.mediaPlayer1.getAudioSessionId());
			mEqualizer.setEnabled(true);

			Toast.makeText(getApplicationContext(),
					"ddd  " + mEqualizer.getNumberOfPresets(),
					Toast.LENGTH_LONG).show();
			initVisSecondFirst(MainActivity.mediaPlayer1);

		} else if (posMedia.equalsIgnoreCase("right")
				&& MainActivity.mediaPlayer2 != null) {
			mEqualizer = new Equalizer(0,
					MainActivity.mediaPlayer2.getAudioSessionId());
			mEqualizer.setEnabled(true);

			initVisSecondFirst(MainActivity.mediaPlayer2);
		}

	}

	@Override
	public void onClick(View viewID) {
		switch (viewID.getId()) {

		case R.id.bt_fx_left:

			if (mEqualizer != null)
				mEqualizer.release();

			initMedia("left");
			break;

		case R.id.bt_fx_right:
			if (mEqualizer != null)
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
		if (mEqualizer != null) {
			for (int i = 0; i < mEqualizer.getNumberOfBands(); i++) {
				int k = r.nextInt(mEqualizer.getBandLevelRange()[1]);
				mEqualizer.setBandLevel((short) i, (short) k);

			}
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
			long arg3) {
if(mEqualizer != null)	
		   mEqualizer.usePreset((short) pos);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
