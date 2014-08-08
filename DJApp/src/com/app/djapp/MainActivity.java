package com.app.djapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dj.vis.LineRenderer;
import com.app.dj.vis.VisualizerView;
import com.app.djapp.utils.TimerComp;
import com.app.djapp.utils.TunnelPlayerWorkaround;

public class MainActivity extends Activity implements OnClickListener {

	public static int setLoadInt = 0;

	TextView tv_timer_record;
	public static Context ctx;
	private Intent intent;
	private Cursor cursor;
	private static ArrayList<GSC> songs = null;
	private VisualizerView mVisualizerView, mVisSecondFirst, mVisSecondSecond;
	private MediaPlayer mPlayer;
	private MediaPlayer mSilentPlayer; /* to avoid tunnel player issue */

	public static MediaPlayer mediaPlayer1, mediaPlayer2, mediaPlayerRecord;

	SoundManager snd;
	int explode, pickup;

	private AudioManager audioManager;

	ImageView iv_speaker_left, iv_speaker_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ctx = this;
		initUI();

		musicArrayList.clear();
		bluetoothArrayList.clear();
		othersArrayList.clear();
		rotatediv_speaker_left();
		rotatediv_speaker_right();
		bindAllSongs();

		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		MAX_VOLUME = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		seekBarMethod();
	}

	private int MAX_VOLUME;

	private boolean flagForSpeakerSpin, flagForSpeakerMoveLoad_First,
			flagForSpeakerMoveLoad_Second;
	public static boolean flagMoveSpeakerSelectAudio;
	SeekBar seekbar_second_first, seekbar_second_second, seekbarmain_first;

	private void seekBarMethod() {

		seekbarmain_first.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		seekbarmain_first.setProgress(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2);
		seekbarmain_first
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
						// TODO Auto-generated method stub
						/*
						 * audioManager.setStreamVolume(AudioManager.STREAM_MUSIC
						 * , progress, 0);
						 */

						System.out.println("ffffffffffffffff v2   " + progress);
						float volume = (float) (1 - (Math.log(MAX_VOLUME
								- progress) / Math.log(MAX_VOLUME)));

						if (mediaPlayer1 != null && mediaPlayer2 != null) {
							mediaPlayer2.setVolume(volume, volume);
							mediaPlayer1.setVolume(1 - volume, 1 - volume);
						}

					}
				});

		seekbar_second_first.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		seekbar_second_first.setProgress(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		seekbar_second_first
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
						// TODO Auto-generated method stub
						/*
						 * audioManager.setStreamVolume(AudioManager.STREAM_MUSIC
						 * , progress, 0);
						 */

						System.out.println("ffffffffffffffff v2   " + progress);
						float volume = (float) (1 - (Math.log(MAX_VOLUME
								- progress) / Math.log(MAX_VOLUME)));

						if (mediaPlayer1 != null)
							mediaPlayer1.setVolume(volume, volume);
					}
				});

		seekbar_second_second.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		seekbar_second_second.setProgress(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		seekbar_second_second
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
						// TODO Auto-generated method stub
						/*
						 * audioManager.setStreamVolume(AudioManager.STREAM_MUSIC
						 * , progress, 0);
						 */
						float volume = (float) (1 - (Math.log(MAX_VOLUME
								- progress) / Math.log(MAX_VOLUME)));

						if (mediaPlayer2 != null)
							mediaPlayer2.setVolume(volume, volume);
					}
				});
	}

	private TimerComp timerComp;

	private void initUI() {
		tv_timer_record = (TextView) findViewById(R.id.tv_mainactivity_record);
		Button bb = (Button) findViewById(R.id.bt_mainactivity_record);
		timerComp = new TimerComp(null, MainActivity.this, tv_timer_record , bb);

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

		// //////////////////////
		findViewById(R.id.bt_mainactivity_f_f1).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f2).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f3).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f4).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f5).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f6).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f7).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f8).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_f_f9).setOnClickListener(this);

		// ///////////////////////////////

		findViewById(R.id.bt_mainactivity_s_f1).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_s_f2).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_s_f3).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_s_f4).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_s_f5).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_s_f6).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_s_f7).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_s_f8).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_s_f9).setOnClickListener(this);

		seekbar_second_first = (SeekBar) findViewById(R.id.seekbar_second_first);
		seekbar_second_second = (SeekBar) findViewById(R.id.seekbar_second_second);
		seekbarmain_first = (SeekBar) findViewById(R.id.seekbarmain_first);

		findViewById(R.id.bt_mainactivity_b1).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_b2).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_b3).setOnClickListener(this);

		findViewById(R.id.bt_mainactivity_b4).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_b5).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_b6).setOnClickListener(this);

		findViewById(R.id.bt_mainactivity_cue_left).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_cue_right).setOnClickListener(this);

		findViewById(R.id.bt_mainactivity_bmp).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_sync).setOnClickListener(this);

		iv_speaker_left = (ImageView) findViewById(R.id.iv_speaker_left);
		iv_speaker_right = (ImageView) findViewById(R.id.iv_speaker_right);

		iv_speaker_left.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					if (flagForSpeakerSpin && flagForSpeakerMoveLoad_First)
						snd.play(explode);
					break;

				default:
					break;
				}

				return true;
			}
		});

		iv_speaker_right.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					if (flagForSpeakerSpin && flagForSpeakerMoveLoad_Second)
						snd.play(pickup);
					break;

				default:
					break;
				}

				return true;
			}
		});

		snd = new SoundManager(getApplicationContext());
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

		// ///////////////// first pad /////////////////
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

		// /////// second pad ///////////

		case R.id.bt_mainactivity_s_f1:
			snd.play(explode);
			break;
		case R.id.bt_mainactivity_s_f2:
			snd.play(pickup);
			break;

		case R.id.bt_mainactivity_s_f3:
			snd.play(explode);
			break;
		case R.id.bt_mainactivity_s_f4:
			snd.play(pickup);
			break;

		case R.id.bt_mainactivity_s_f5:
			snd.play(explode);
			break;
		case R.id.bt_mainactivity_s_f6:
			snd.play(pickup);
			break;

		case R.id.bt_mainactivity_s_f7:
			snd.play(explode);
			break;
		case R.id.bt_mainactivity_s_f8:
			snd.play(pickup);
			break;

		case R.id.bt_mainactivity_s_f9:
			snd.play(explode);
			break;

		// //////////////////////////// six btn ///////////////////////////////

		case R.id.bt_mainactivity_b1:
			snd.play(pickup);
			break;

		case R.id.bt_mainactivity_b2:
			snd.play(explode);
			break;
		case R.id.bt_mainactivity_b3:
			snd.play(pickup);
			break;

		case R.id.bt_mainactivity_b4:
			snd.play(explode);
			break;
		case R.id.bt_mainactivity_b5:
			snd.play(pickup);
			break;

		case R.id.bt_mainactivity_b6:
			snd.play(explode);
			break;

		// ////////////////////////////

		case R.id.bt_mainactivity_sync:
			flagForSpeakerSpin = true;
			break;

		// /////////////////////////

		case R.id.bt_mainactivity_first_load_pause:

			if (mediaPlayer1 != null)
				mediaPlayer1.pause();
			flagForSpeakerMoveLoad_First = false;
			wheelSet1.cancel();
			break;

		case R.id.bt_mainactivity_first_load_play:
			if (mediaPlayer1 != null)
				mediaPlayer1.start();
			if (flagMoveSpeakerSelectAudio)
				wheelSet1.start();
			flagForSpeakerMoveLoad_First = true;
			break;

		case R.id.bt_mainactivity_second_load_pause:
			if (mediaPlayer2 != null)
				mediaPlayer2.pause();
			flagForSpeakerMoveLoad_Second = false;
			wheelSet2.cancel();
			break;

		case R.id.bt_mainactivity_second_load_play:
			if (mediaPlayer2 != null)
				mediaPlayer2.start();
			if (flagMoveSpeakerSelectAudio)
				wheelSet2.start();

			flagForSpeakerMoveLoad_Second = true;

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

				flagForSpeakerMoveLoad_First = true;
				if (mediaPlayer1 != null) {
					mediaPlayer1.stop();
					mediaPlayer1.release();

					mediaPlayer1 = null;
				}
				if (mediaPlayerRecord != null) {

					mediaPlayerRecord.stop();
					mediaPlayerRecord.release();
					mediaPlayerRecord = null;
				}

				setLoadInt = 0;
				mediaPlayer1 = new MediaPlayer();
				Intent i = new Intent(MainActivity.this, TabPagerActivity.class);
				startActivity(i);

				wheelSet1.start();

				initVisSecondFirst(mediaPlayer1);

			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case R.id.bt_mainactivity_loadsecond:

			/*
			 * try { mediaPlayer2 = new MediaPlayer(); mediaPlayer1 = new
			 * MediaPlayer(); mediaPlayer2.setDataSource(path);
			 * mediaPlayer2.prepare(); mediaPlayer2.start();
			 * rotatediv_speaker_right(); } catch (Exception e) { // TODO:
			 * handle exception }
			 */

			flagForSpeakerMoveLoad_Second = true;
			setLoadInt = 1;

			if (mediaPlayer2 != null) {

				mediaPlayer2.stop();
				mediaPlayer2.release();
				mediaPlayer2 = null;
			}
			if (mediaPlayerRecord != null) {

				mediaPlayerRecord.stop();
				mediaPlayerRecord.release();
				mediaPlayerRecord = null;
			}

			mediaPlayer2 = new MediaPlayer();
			Intent i = new Intent(MainActivity.this, TabPagerActivity.class);
			startActivity(i);
			if (flagMoveSpeakerSelectAudio)
				wheelSet2.start();
			initVisSecondSecond(mediaPlayer2);

			break;

		case R.id.bt_mainactivity_record:
			
			timerComp.resetTimer();
			timerComp.startTimer();

			Intent callIntent = new Intent(MainActivity.this,
					RecordService.class);
			startService(callIntent);
			Button b = (Button) findViewById(R.id.bt_mainactivity_record);

			b.setEnabled(false);

			break;

		case R.id.bt_mainactivity_load_mixer:

			Button b1 = (Button) findViewById(R.id.bt_mainactivity_record);

			b1.setEnabled(true);

			timerComp.resetTimer();
			timerComp.stopTimer();

			flagForSpeakerSpin = false;

			if (mediaPlayer1 != null)
				mediaPlayer1.pause();
			if (mediaPlayer2 != null)
				mediaPlayer2.pause();

			if (wheelSet2 != null)
				wheelSet2.cancel();
			if (wheelSet1 != null)
				wheelSet1.cancel();

			if (mediaPlayerRecord != null) {

				mediaPlayerRecord.stop();
				mediaPlayerRecord.release();
				mediaPlayerRecord = null;
			}

			mediaPlayerRecord = new MediaPlayer();

			setLoadInt = 2;
			callIntent = new Intent(MainActivity.this, RecordService.class);
			stopService(callIntent);
			Intent i1 = new Intent(MainActivity.this, RecordFileActivity.class);
			startActivity(i1);

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

	@SuppressLint("NewApi")
	public void rotatediv_speaker_left() {
		// load the wheel animation

		wheelSet1 = (AnimatorSet) AnimatorInflater.loadAnimator(this,
				R.animator.wheel_spin);

		wheelSet1.setTarget(iv_speaker_left);

	}

	@SuppressLint("NewApi")
	public void rotatediv_speaker_right() {

		wheelSet2 = (AnimatorSet) AnimatorInflater.loadAnimator(this,
				R.animator.wheel_spin);
		// set the view as target

		wheelSet2.setTarget(iv_speaker_right);

		// start the animation

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
		if (mediaPlayer1 != null) {
			mediaPlayer1.release();
			mediaPlayer1 = null;
		}
		if (mediaPlayer2 != null) {
			mediaPlayer2.release();
			mediaPlayer2 = null;
		}
		super.onDestroy();
	}

	String path;
	String ss1 = "", ss2 = "", ss3 = "";
	public static ArrayList<String> musicArrayList = new ArrayList<String>();
	public static ArrayList<String> bluetoothArrayList = new ArrayList<String>();
	public static ArrayList<String> othersArrayList = new ArrayList<String>();

	// /////////////////////////////////////////////////////

	// ///////////////////////////////////
	private void bindAllSongs() {

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

	private void initVisSecondFirst(MediaPlayer secondFirstMediaPlayer) {

		mVisualizerView = (VisualizerView) findViewById(R.id.vis_second_first);
		mVisualizerView.link(secondFirstMediaPlayer);

		// Start with just line renderer
		addLineRenderer();
	}

	private void initVisSecondSecond(MediaPlayer secondSecondMediaPlayer) {

		mVisualizerView = (VisualizerView) findViewById(R.id.vis_second_second);
		mVisualizerView.link(secondSecondMediaPlayer);

		// Start with just line renderer
		addLineRenderer();
	}

	private void initVisMain(MediaPlayer mainMediaPlayer) {

		mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);
		mVisualizerView.link(mainMediaPlayer);

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

	private void initTunnelPlayerWorkaround() {
		// Read "tunnel.decode" system property to determine
		// the workaround is needed
		if (TunnelPlayerWorkaround.isTunnelDecodeEnabled(this)) {
			mSilentPlayer = TunnelPlayerWorkaround
					.createSilentMediaPlayer(this);
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

	public void linePressed(View view) {
		addLineRenderer();
	}

	public void clearPressed(View view) {
		mVisualizerView.clearRenderers();
	}

}
