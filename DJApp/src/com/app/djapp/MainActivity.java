package com.app.djapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.djapp.utils.TunnelPlayerWorkaround;
import com.app.djapp.visualizer.VisualizerView;
import com.app.djapp.visualizer.renderer.BarGraphRenderer;
import com.app.djapp.visualizer.renderer.CircleBarRenderer;
import com.app.djapp.visualizer.renderer.CircleRenderer;
import com.app.djapp.visualizer.renderer.LineRenderer;

public class MainActivity extends Activity implements OnClickListener {

	
	public static Boolean setLoad;
	
	public static Context ctx;
	private Intent intent;
	private Cursor cursor;
	private static ArrayList<GSC> songs = null;

	private MediaPlayer mPlayer;
	private MediaPlayer mSilentPlayer; /* to avoid tunnel player issue */
	private VisualizerView mVisualizerView;
	public static MediaPlayer mediaPlayer1, mediaPlayer2;
	private MediaPlayer mediaPlayer3;
	SoundManager snd;
	int explode, pickup;
	Bundle b1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ctx = this;
		initUI();

		musicArrayList.clear();
		bluetoothArrayList.clear();
		othersArrayList.clear();

		bindAllSongs();
	}

	private void initUI() {
		findViewById(R.id.bt_mainactivity_fx).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_mixer).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_loadfirst).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_loadsecond).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_first_load_pause).setOnClickListener(
				this);
		findViewById(R.id.bt_mainactivity_first_load_play).setOnClickListener(
				this);
		findViewById(R.id.bt_mainactivity_second_load_pause)
				.setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_second_load_play).setOnClickListener(
				this);
		findViewById(R.id.bt_mainactivity_record).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_load_mixer).setOnClickListener(this);

		findViewById(R.id.bt_mainactivity_f_f1).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f2).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f3).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f4).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f5).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f6).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f7).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f8).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f9).setOnClickListener(this);

		wheel1 = (ImageView) findViewById(R.id.iv_speaker_left);
		wheel2 = (ImageView) findViewById(R.id.iv_speaker_right);

		snd = new SoundManager(getApplicationContext());

		// Set volume rocker mode to media volume
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		explode = snd.load(R.raw.explosion);
		pickup = snd.load(R.raw.pickup);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		Intent callIntent = new Intent(MainActivity.this, RecordService.class);
		stopService(callIntent);
		/*
		 * mediaPlayer1.stop(); mediaPlayer2.stop();
		 */

	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View viewID) {
		switch (viewID.getId()) {

		case R.id.bt_mainactivity_f_f1:
			snd.play(explode);
			break;
		case R.id.bt_mainactivity_f_f2:
			snd.play(pickup);
			break;

		case R.id.bt_mainactivity_f_f3:
			snd.play(explode);
			break;
		case R.id.bt_mainactivity_f_f4:
			snd.play(pickup);
			break;

		case R.id.bt_mainactivity_f_f5:
			snd.play(explode);
			break;
		case R.id.bt_mainactivity_f_f6:
			snd.play(pickup);
			break;

		case R.id.bt_mainactivity_f_f7:
			snd.play(explode);
			break;
		case R.id.bt_mainactivity_f_f8:
			snd.play(pickup);
			break;

		case R.id.bt_mainactivity_f_f9:
			snd.play(explode);
			break;

		// //////////////////

		case R.id.bt_mainactivity_first_load_pause:

			if (mediaPlayer1 != null)
				mediaPlayer1.pause();
			wheelSet1.cancel();
			break;

		case R.id.bt_mainactivity_first_load_play:
			if (mediaPlayer1 != null)
				mediaPlayer1.start();
			rotatedWheel1();
			break;

		case R.id.bt_mainactivity_second_load_pause:
			if (mediaPlayer2 != null)
				mediaPlayer2.pause();
			wheelSet2.cancel();
			break;

		case R.id.bt_mainactivity_second_load_play:
			if (mediaPlayer2 != null)
				mediaPlayer2.start();
			rotatedWheel2();
			break;

		case R.id.bt_mainactivity_fx:
			intent = new Intent(MainActivity.this, SecondActivity_FX.class);
			startActivity(intent);
			break;

		case R.id.bt_mainactivity_mixer:
			intent = new Intent(MainActivity.this, ThirtdActivity_MIXER.class);
			startActivity(intent);
			break;
		case R.id.bt_mainactivity_loadfirst:
			try {
				/*
				 * mediaPlayer1 = new MediaPlayer();
				 * mediaPlayer1.setDataSource(path); mediaPlayer1.prepare();
				 * mediaPlayer1.start(); rotatedWheel1();
				 */

				/*
				 * Intent intent = new
				 * Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
				 * startActivity(intent); String d = intent.getData().getPath();
				 */

				 init();
				 rotatedWheel1();
				setLoad = false;
				mediaPlayer1 = new MediaPlayer();
				Intent i = new Intent(MainActivity.this, TabPagerActivity.class);
				startActivity(i);

			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case R.id.bt_mainactivity_loadsecond:

			/*try {
				mediaPlayer2 = new MediaPlayer();

				mediaPlayer1 = new MediaPlayer();
				

				mediaPlayer2.setDataSource(path);
				mediaPlayer2.prepare();
				mediaPlayer2.start();
				rotatedWheel2();
			} catch (Exception e) {
				// TODO: handle exception
			}*/
			setLoad = true;
			mediaPlayer2 = new MediaPlayer();
			Intent i = new Intent(MainActivity.this, TabPagerActivity.class);
			 rotatedWheel2();

		 
			startActivity(i);
			break;

		case R.id.bt_mainactivity_record:

			Toast.makeText(MainActivity.this,
					"Recorging audio path : /sdcard/AudioRecordingDj ",
					Toast.LENGTH_LONG).show();

			Intent callIntent = new Intent(MainActivity.this,
					RecordService.class);
			startService(callIntent);
		
			
			break;

		case R.id.bt_mainactivity_load_mixer:

			if(mediaPlayer1 !=null )
			mediaPlayer1.pause();
			if(mediaPlayer2 !=null )
			mediaPlayer2.pause();
			callIntent = new Intent(MainActivity.this, RecordService.class);
			stopService(callIntent);

			/*try {
				mediaPlayer3 = new MediaPlayer();
				mediaPlayer3.setDataSource(path);
				mediaPlayer3.prepare();
				mediaPlayer3.start();
				rotatedWheel2();
			} catch (Exception e) {
				// TODO: handle exception
			}*/
						
			Intent intent = new Intent();  
			intent.setAction(android.content.Intent.ACTION_VIEW);  
			File file = new File("/sdcard/AudioRecordingDj");  
			intent.setDataAndType(Uri.fromFile(file), "*/*");  
			startActivity(intent);
			
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

	AnimatorSet wheelSet1, wheelSet2;
	ImageView wheel1, wheel2;

	@SuppressLint("NewApi")
	public void rotatedWheel1() {
		// load the wheel animation
		wheelSet1 = (AnimatorSet) AnimatorInflater.loadAnimator(this,
				R.animator.wheel_spin);

		wheelSet1.setTarget(wheel1);
		wheelSet1.start();

	}

	@SuppressLint("NewApi")
	public void rotatedWheel2() {
		wheelSet2 = (AnimatorSet) AnimatorInflater.loadAnimator(this,
				R.animator.wheel_spin);
		// set the view as target

		wheelSet2.setTarget(wheel2);
		// start the animation
		wheelSet2.start();

	}

	// //////////////////////

	@Override
	protected void onResume() {
		super.onResume();
		initTunnelPlayerWorkaround();

	}

	@Override
	protected void onPause() {
		cleanUp();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		cleanUp();
		super.onDestroy();
	}

	String path;
	String ss1 = "", ss2 = "", ss3 = "";
	public static ArrayList<String> musicArrayList = new ArrayList<String>();
	public static ArrayList<String> bluetoothArrayList = new ArrayList<String>();
	public static ArrayList<String> othersArrayList = new ArrayList<String>();

	private void bindAllSongs() {

		System.out.println("dddddddd dfsd ");

		/** Making custom drawable */

		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
		final String[] projection = new String[] {
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.ALBUM };
		final String sortOrder = MediaStore.Audio.AudioColumns.ALBUM
				+ " COLLATE LOCALIZED ASC";

		try {
			// the uri of the table that we want to query
			Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			// query the db
			cursor = getBaseContext().getContentResolver().query(uri,
					projection, selection, null, sortOrder);
			if (cursor != null) {
				songs = new ArrayList<GSC>(cursor.getCount());
				cursor.moveToFirst();
				GSC gsc = new GSC();
				while (!cursor.isAfterLast()) {

					gsc.songTitle = cursor.getString(0);
					gsc.songArtist = cursor.getString(1);
					gsc.songData = cursor.getString(2);
					gsc.songAlbum = cursor.getString(3);
					songs.add(gsc);
					path = gsc.songData;

					if (gsc.songAlbum.equalsIgnoreCase("Music")) {
						musicArrayList.add(gsc.songTitle + "~" + path);
					} else if (gsc.songAlbum.equalsIgnoreCase("bluetooth")) {
						bluetoothArrayList.add(gsc.songTitle + "~" + path);
					} else {
						othersArrayList.add(gsc.songTitle + "~" + path);
					}

					cursor.moveToNext();
				}

				System.out.println("ddddddddd " + musicArrayList + " , "
						+ bluetoothArrayList + " , " + othersArrayList);
			}
		} catch (Exception ex) {

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

	}

	public class GSC {
		String songAlbum = "";
		String songTitle = "";
		String songArtist = "";
		String songData = "";
		String isChecked = "false";
	}

	private void init() {
		mPlayer = MediaPlayer.create(this, R.raw.test);
		mPlayer.setLooping(true);
		mPlayer.start();

		// We need to link the visualizer view to the media player so that
		// it displays something
		mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);
		mVisualizerView.link(mPlayer);

		// Start with just line renderer
		addLineRenderer();
	}

	private void cleanUp() {
		if (mPlayer != null) {
			mVisualizerView.release();
			mPlayer.release();
			mPlayer = null;
		}

		if (mSilentPlayer != null) {
			mSilentPlayer.release();
			mSilentPlayer = null;
		}
	}

	// Workaround (for Galaxy S4)
	//
	// "Visualization does not work on the new Galaxy devices"
	// https://github.com/felixpalmer/android-visualizer/issues/5
	//
	// NOTE:
	// This code is not required for visualizing default "test.mp3" file,
	// because tunnel player is used when duration is longer than 1 minute.
	// (default "test.mp3" file: 8 seconds)
	//
	private void initTunnelPlayerWorkaround() {
		// Read "tunnel.decode" system property to determine
		// the workaround is needed
		if (TunnelPlayerWorkaround.isTunnelDecodeEnabled(this)) {
			mSilentPlayer = TunnelPlayerWorkaround
					.createSilentMediaPlayer(this);
		}
	}

	// Methods for adding renderers to visualizer
	private void addBarGraphRenderers() {
		Paint paint = new Paint();
		paint.setStrokeWidth(50f);
		paint.setAntiAlias(true);
		paint.setColor(Color.argb(200, 56, 138, 252));
		BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(16,
				paint, false);
		mVisualizerView.addRenderer(barGraphRendererBottom);

		Paint paint2 = new Paint();
		paint2.setStrokeWidth(12f);
		paint2.setAntiAlias(true);
		paint2.setColor(Color.argb(200, 181, 111, 233));
		BarGraphRenderer barGraphRendererTop = new BarGraphRenderer(4, paint2,
				true);
		mVisualizerView.addRenderer(barGraphRendererTop);
	}

	private void addCircleBarRenderer() {
		Paint paint = new Paint();
		paint.setStrokeWidth(8f);
		paint.setAntiAlias(true);
		paint.setXfermode(new PorterDuffXfermode(Mode.LIGHTEN));
		paint.setColor(Color.argb(255, 222, 92, 143));
		CircleBarRenderer circleBarRenderer = new CircleBarRenderer(paint, 32,
				true);
		mVisualizerView.addRenderer(circleBarRenderer);
	}

	private void addCircleRenderer() {
		Paint paint = new Paint();
		paint.setStrokeWidth(3f);
		paint.setAntiAlias(true);
		paint.setColor(Color.argb(255, 222, 92, 143));
		CircleRenderer circleRenderer = new CircleRenderer(paint, true);
		mVisualizerView.addRenderer(circleRenderer);
	}

	private void addLineRenderer() {
		Paint linePaint = new Paint();
		linePaint.setStrokeWidth(1f);
		linePaint.setAntiAlias(true);
		linePaint.setColor(Color.argb(88, 0, 128, 255));

		Paint lineFlashPaint = new Paint();
		lineFlashPaint.setStrokeWidth(5f);
		lineFlashPaint.setAntiAlias(true);
		lineFlashPaint.setColor(Color.argb(188, 255, 255, 255));
		LineRenderer lineRenderer = new LineRenderer(linePaint, lineFlashPaint,
				true);
		mVisualizerView.addRenderer(lineRenderer);
	}

	// Actions for buttons defined in xml
	public void startPressed(View view) throws IllegalStateException,
			IOException {
		if (mPlayer.isPlaying()) {
			return;
		}
		mPlayer.prepare();
		mPlayer.start();
	}

	public void stopPressed(View view) {
		mPlayer.stop();
	}

	public void barPressed(View view) {
		addBarGraphRenderers();
	}

	public void circlePressed(View view) {
		addCircleRenderer();
	}

	public void circleBarPressed(View view) {
		addCircleBarRenderer();
	}

	public void linePressed(View view) {
		addLineRenderer();
	}

	public void clearPressed(View view) {
		mVisualizerView.clearRenderers();
	}

}
