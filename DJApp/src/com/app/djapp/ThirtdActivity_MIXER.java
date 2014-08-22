package com.app.djapp;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.widget.SeekBar;

import com.app.dj.vis.LineRenderer;
import com.app.dj.vis.VisualizerViewMX;

public class ThirtdActivity_MIXER extends Activity {

	private VerticalSeekBar sb_mx_vseekBar1, sb_mx_vseekBar2, sb_mx_vseekBar3,
			sb_mx_vseekBar4, sb_mx_vseekBar5;
	
	private VisualizerViewMX mVisualizerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thirtd_activity_mixer);
		initUI();
		seekBarMethod();
		initVisSecondSecond(MainActivity.mediaPlayer1);
		System.out.println("ddddddddddd TH");
	}

	private void initUI() {
		sb_mx_vseekBar1 = (VerticalSeekBar) findViewById(R.id.sb_mx_vseekBar1);
		sb_mx_vseekBar2 = (VerticalSeekBar) findViewById(R.id.sb_mx_vseekBar2);
		sb_mx_vseekBar3 = (VerticalSeekBar) findViewById(R.id.sb_mx_vseekBar3);
		sb_mx_vseekBar4 = (VerticalSeekBar) findViewById(R.id.sb_mx_vseekBar4);
		sb_mx_vseekBar5 = (VerticalSeekBar) findViewById(R.id.sb_mx_vseekBar5);
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

						if (MainActivity.mediaPlayer1 != null
								&& MainActivity.mediaPlayer2 != null) {

							MainActivity.mediaPlayer1.setVolume(volume, volume);
							MainActivity.mediaPlayer2.setVolume(volume, volume);
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
