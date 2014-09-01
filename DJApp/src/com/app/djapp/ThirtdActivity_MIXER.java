package com.app.djapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.app.dj.vis.LineRenderer;
import com.app.dj.vis.VisualizerViewMX;

public class ThirtdActivity_MIXER extends Activity implements OnClickListener {

	private VerticalSeekBar sb_mx_vseekBar1, sb_mx_vseekBar2, sb_mx_vseekBar3,
			sb_mx_vseekBar4, sb_mx_vseekBar5;
	private WheelMenu iv_mx_knob_1, iv_mx_knob_2, iv_mx_knob_3, iv_mx_knob_4,
			iv_mx_knob_5;
	private VisualizerViewMX mVisualizerView;
	private MainWheel mainwheel;

	Button bt_mx_menu, bt_mx_fx, bt_mx_load_mixer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thirtd_activity_mixer);

		initUI();
		seekBarMethod();
		initVisSecondSecond(MainActivity.mediaPlayer1);
	}

	private ImageView iv_mx_knob_1_s, iv_mx_knob_1_m, iv_mx_knob_2_s,
			iv_mx_knob_2_m, iv_mx_knob_3_s, iv_mx_knob_3_m, iv_mx_knob_4_s,
			iv_mx_knob_4_m;

	private void initUI() {

		bt_mx_menu = (Button) findViewById(R.id.bt_mx_menu);
		bt_mx_fx = (Button) findViewById(R.id.bt_mx_fx);
		bt_mx_load_mixer = (Button) findViewById(R.id.bt_mx_load_mixer);

		bt_mx_menu.setOnClickListener(this);
		bt_mx_fx.setOnClickListener(this);
		bt_mx_load_mixer.setOnClickListener(this);

		mainwheel = (MainWheel) findViewById(R.id.mw_round);

		mainwheel.setWheelImage(R.drawable.image5);

		iv_mx_knob_1 = (WheelMenu) findViewById(R.id.iv_mx_knob_1);
		iv_mx_knob_1.setWheelImage(R.drawable.image8);

		iv_mx_knob_2 = (WheelMenu) findViewById(R.id.iv_mx_knob_2);
		iv_mx_knob_2.setWheelImage(R.drawable.image8);

		iv_mx_knob_3 = (WheelMenu) findViewById(R.id.iv_mx_knob_3);
		iv_mx_knob_3.setWheelImage(R.drawable.image8);

		iv_mx_knob_4 = (WheelMenu) findViewById(R.id.iv_mx_knob_4);
		iv_mx_knob_4.setWheelImage(R.drawable.image8);

		iv_mx_knob_5 = (WheelMenu) findViewById(R.id.iv_mx_knob_5);
		iv_mx_knob_5.setWheelImage(R.drawable.image8);

		iv_mx_knob_1_s = (ImageView) findViewById(R.id.iv_mx_knob_1_s);
		iv_mx_knob_1_m = (ImageView) findViewById(R.id.iv_mx_knob_1_m);

		iv_mx_knob_1_s.setOnClickListener(this);
		iv_mx_knob_1_m.setOnClickListener(this);

		iv_mx_knob_2_s = (ImageView) findViewById(R.id.iv_mx_knob_2_s);
		iv_mx_knob_2_m = (ImageView) findViewById(R.id.iv_mx_knob_2_m);

		iv_mx_knob_2_s.setOnClickListener(this);
		iv_mx_knob_2_m.setOnClickListener(this);

		iv_mx_knob_3_s = (ImageView) findViewById(R.id.iv_mx_knob_3_s);
		iv_mx_knob_3_m = (ImageView) findViewById(R.id.iv_mx_knob_3_m);

		iv_mx_knob_3_s.setOnClickListener(this);
		iv_mx_knob_3_m.setOnClickListener(this);

		iv_mx_knob_4_s = (ImageView) findViewById(R.id.iv_mx_knob_4_s);
		iv_mx_knob_4_m = (ImageView) findViewById(R.id.iv_mx_knob_4_m);

		iv_mx_knob_4_s.setOnClickListener(this);
		iv_mx_knob_4_m.setOnClickListener(this);

		sb_mx_vseekBar1 = (VerticalSeekBar) findViewById(R.id.sb_mx_vseekBar1);
		sb_mx_vseekBar2 = (VerticalSeekBar) findViewById(R.id.sb_mx_vseekBar2);
		sb_mx_vseekBar3 = (VerticalSeekBar) findViewById(R.id.sb_mx_vseekBar3);
		sb_mx_vseekBar4 = (VerticalSeekBar) findViewById(R.id.sb_mx_vseekBar4);

		// large

		sb_mx_vseekBar5 = (VerticalSeekBar) findViewById(R.id.sb_mx_vseekBar5);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.bt_mx_menu:
			Intent i1 = new Intent(this, MenuActivity.class);
			startActivity(i1);

			break;

		case R.id.bt_mx_fx:

			i1 = new Intent(this, SecondActivity_FX.class);
			startActivity(i1);
			break;

		case R.id.bt_mx_load_mixer:

			i1 = new Intent(this, RecordFileActivity.class);
			startActivity(i1);
			break;
		case R.id.iv_mx_knob_1_s:

			if (MainActivity.mediaPlayer1 != null
					&& !MainActivity.mediaPlayer1.isPlaying()) {
				if (MainActivity.wheelSet1 != null)
					MainActivity.wheelSet1.start();
				MainActivity.mediaPlayer1.start();
			}
			if (MainActivity.mediaPlayer2 != null
					&& MainActivity.mediaPlayer2.isPlaying()) {
				if (MainActivity.wheelSet2 != null)
					MainActivity.wheelSet2.cancel();
				MainActivity.mediaPlayer2.pause();
			}

			if (MainActivity.mPlayerPadLeft != null) {
				for (int i = 0; i < MainActivity.mPlayerPadLeft.length; i++) {
					if (MainActivity.mPlayerPadLeft[i].isPlaying()) {
						MainActivity.mPlayerPadLeft[i].pause();
					}

				}
			}

			break;
		case R.id.iv_mx_knob_1_m:
			if (MainActivity.mediaPlayer1 != null
					&& MainActivity.mediaPlayer1.isPlaying()) {
				if (MainActivity.wheelSet1 != null)
					MainActivity.wheelSet1.cancel();
				MainActivity.mediaPlayer1.pause();
			}

			break;
		case R.id.iv_mx_knob_2_s:
			if (MainActivity.mediaPlayer2 != null
					&& !MainActivity.mediaPlayer2.isPlaying()) {
				if (MainActivity.wheelSet2 != null)
					MainActivity.wheelSet2.start();
				MainActivity.mediaPlayer2.start();
			}
			if (MainActivity.mediaPlayer1 != null
					&& MainActivity.mediaPlayer1.isPlaying()) {
				if (MainActivity.wheelSet1 != null)
					MainActivity.wheelSet1.cancel();
				MainActivity.mediaPlayer1.pause();
			}
			if (MainActivity.mPlayerPadLeft != null) {
				for (int i = 0; i < MainActivity.mPlayerPadLeft.length; i++) {
					if (MainActivity.mPlayerPadLeft[i].isPlaying()) {
						MainActivity.mPlayerPadLeft[i].pause();
					}

				}
			}

			break;
		case R.id.iv_mx_knob_2_m:
			if (MainActivity.mediaPlayer2 != null
					&& MainActivity.mediaPlayer2.isPlaying()) {
				if (MainActivity.wheelSet2 != null)
					MainActivity.wheelSet2.cancel();
				MainActivity.mediaPlayer2.pause();
			}
			break;
		case R.id.iv_mx_knob_3_s:

			if (MainActivity.mPlayerPadLeft != null) {

				for (int i = 0; i < MainActivity.mPlayerPadLeft.length; i++) {
					if (!MainActivity.mPlayerPadLeft[i].isPlaying()) {
						MainActivity.mPlayerPadLeft[i].start();
					}

				}
			}

			if (MainActivity.mediaPlayer1 != null
					&& MainActivity.mediaPlayer1.isPlaying()) {
				if (MainActivity.wheelSet1 != null)
					MainActivity.wheelSet1.cancel();
				MainActivity.mediaPlayer1.pause();
			}

			if (MainActivity.mediaPlayer2 != null
					&& MainActivity.mediaPlayer2.isPlaying()) {
				if (MainActivity.wheelSet2 != null)
					MainActivity.wheelSet2.cancel();
				MainActivity.mediaPlayer2.pause();
			}

			break;
		case R.id.iv_mx_knob_3_m:

			if (MainActivity.mPlayerPadLeft != null) {

				for (int i = 0; i < MainActivity.mPlayerPadLeft.length; i++) {
					if (MainActivity.mPlayerPadLeft[i].isPlaying()) {
						MainActivity.mPlayerPadLeft[i].pause();
					}

				}
			}

			break;
		case R.id.iv_mx_knob_4_s:

			break;
		case R.id.iv_mx_knob_4_m:

			break;

		default:
			break;
		}
	}

	private void initVisSecondSecond(MediaPlayer secondSecondMediaPlayer) {

		if (secondSecondMediaPlayer != null) {

			mVisualizerView = (VisualizerViewMX) findViewById(R.id.vis_mx_vis);
			mVisualizerView.link(secondSecondMediaPlayer);
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

	private int MAX_VOLUME;
	private AudioManager audioManager;

	private void seekBarMethod() {
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		MAX_VOLUME = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		sb_mx_vseekBar1.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		sb_mx_vseekBar1.setProgress(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2);
		sb_mx_vseekBar1
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						float volume = (float) (1 - (Math.log(MAX_VOLUME
								- progress) / Math.log(MAX_VOLUME)));

						if (MainActivity.mediaPlayer1 != null) {

							MainActivity.mediaPlayer1.setVolume(volume, volume);
						}

					}
				});

		sb_mx_vseekBar2.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		sb_mx_vseekBar2.setProgress(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2);
		sb_mx_vseekBar2
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						float volume = (float) (1 - (Math.log(MAX_VOLUME
								- progress) / Math.log(MAX_VOLUME)));

						if (MainActivity.mediaPlayer2 != null) {

							MainActivity.mediaPlayer2.setVolume(volume, volume);
						}

					}
				});

		sb_mx_vseekBar3.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		sb_mx_vseekBar3.setProgress(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2);
		sb_mx_vseekBar3
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						float volume = (float) (1 - (Math.log(MAX_VOLUME
								- progress) / Math.log(MAX_VOLUME)));

						if (MainActivity.mPlayerPadLeft != null) {
							for (int i = 0; i < MainActivity.mPlayerPadLeft.length; i++) {
								MainActivity.mPlayerPadLeft[i].setVolume(
										volume, volume);
							}
						}
					}
				});

		sb_mx_vseekBar5.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		sb_mx_vseekBar5.setProgress(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2);
		sb_mx_vseekBar5
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						float volume = (float) (1 - (Math.log(MAX_VOLUME
								- progress) / Math.log(MAX_VOLUME)));

						if (MainActivity.mediaPlayer1 != null) {
							MainActivity.mediaPlayer1.setVolume(volume, volume);
						}

						if (MainActivity.mediaPlayer2 != null) {
							MainActivity.mediaPlayer2.setVolume(volume, volume);
						}
						if (MainActivity.mPlayerPadLeft != null) {
							for (int i = 0; i < MainActivity.mPlayerPadLeft.length; i++) {
								MainActivity.mPlayerPadLeft[i].setVolume(
										volume, volume);
							}
						}
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
