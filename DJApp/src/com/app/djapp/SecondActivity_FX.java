package com.app.djapp;

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
import android.widget.Button;
import android.widget.LinearLayout;

public class SecondActivity_FX extends Activity implements OnClickListener {

	private LinearLayout ll_fx_draw;
	private Equalizer mEqualizer;
	short bands;
	private Button bt_fx_left, bt_fx_right;
	VisualizerViewFx mVisualizerView;

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
		bt_fx_left.setOnClickListener(this);
		bt_fx_right.setOnClickListener(this);

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
			bands = mEqualizer.getNumberOfBands();

			initVisSecondFirst(MainActivity.mediaPlayer1);

		} else if (posMedia.equalsIgnoreCase("right")
				&& MainActivity.mediaPlayer2 != null) {
			mEqualizer = new Equalizer(0,
					MainActivity.mediaPlayer2.getAudioSessionId());
			mEqualizer.setEnabled(true);
			bands = mEqualizer.getNumberOfBands();
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
}
