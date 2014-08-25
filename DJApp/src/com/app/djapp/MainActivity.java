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
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.app.dj.vis.VisualizerViewMain;
import com.app.djapp.utils.TimerComp;
import com.app.djapp.utils.TunnelPlayerWorkaround;

public class MainActivity extends Activity implements OnClickListener {

	public static int setLoadInt = 0;

	public static Context ctx;
	private Intent intent;
	private Cursor cursor;
	private static ArrayList<GSC> songs = null;
	VisualizerViewMain mVisualizerView, mVisSecondFirst, mVisSecondSecond;
	private MediaPlayer mPlayer;
	private MediaPlayer mSilentPlayer; /* to avoid tunnel player issue */

	public static MediaPlayer mediaPlayer1, mediaPlayer2, mediaPlayerRecord;

	SoundManager snd_speaker_left, snd_speaker_right;
	SoundManager snd_pad_left[] = new SoundManager[9];
	SoundManager snd_pad_right[] = new SoundManager[9];

	int explode, pickup;

	private AudioManager audioManager;

	ImageView iv_speaker_left, iv_speaker_right;

	private void initSoundPool() {

		snd_speaker_left = new SoundManager(getApplicationContext());

		for (int i = 0; i < snd_pad_left.length; i++) {
			snd_pad_left[i] = new SoundManager(getApplicationContext());
			if (i % 2 == 0) {
				explode = snd_pad_left[i].load(R.raw.explosion);

			} else {
				pickup = snd_pad_left[i].load(R.raw.pickup);
			}
		}
		for (int i = 0; i < snd_pad_right.length; i++) {
			snd_pad_right[i] = new SoundManager(getApplicationContext());
			if (i % 2 == 0) {
				explode = snd_pad_right[i].load(R.raw.explosion);

			} else {
				pickup = snd_pad_right[i].load(R.raw.pickup);
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ctx = this;
		initUI();
		initTunnelPlayerWorkaround();
		musicArrayList.clear();
		bluetoothArrayList.clear();
		othersArrayList.clear();

		rotatediv_speaker_left();
		rotatediv_speaker_right();

		bindAllSongs();

		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		MAX_VOLUME = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		seekBarMethod();
		initSoundPool();

		System.out.println("ddddddddon create");
	}

	@Override
	protected void onResume() {

		System.out.println("ddddddddon resume");
		if (flagPause) {
			/*
			 * if(mVisualizerView != null) mVisualizerView.release();
			 */

			if (mediaPlayer1 != null) {
				mediaPlayer1.start();
				wheelSet1.start();
				// initVisSecondFirst(mediaPlayer1);
			}
			if (mediaPlayer2 != null) {
				mediaPlayer2.start();
				wheelSet2.start();
				// initVisSecondSecond(mediaPlayer2);
			}
		}

		// seekBarMethod();
		flagPause = true;
		super.onResume();
	}

	public static boolean flagPause;

	@Override
	protected void onPause() {

		cleanUp();
		System.out.println("ddddddddon pause");

		if (flagPause) {
			if (mediaPlayer1 != null) {
				mediaPlayer1.pause();
				wheelSet1.cancel();

			}
			if (mediaPlayer2 != null) {
				mediaPlayer2.pause();
				wheelSet2.cancel();
			}

		}

		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("ddddddddon destroy");
		cleanUp();

		if (mediaPlayer1 != null) {
			mediaPlayer1.release();
			mediaPlayer1 = null;
		}
		if (mediaPlayer2 != null) {
			mediaPlayer2.release();
			mediaPlayer2 = null;
		}

		if (mediaPlayerRecord != null) {
			mediaPlayerRecord.release();
			mediaPlayerRecord = null;
		}

		Intent callIntent = new Intent(MainActivity.this, RecordService.class);
		stopService(callIntent);
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

	private TimerComp timerComp;
	private TextView tv_timer_record;
	Button bt_mainactivity_cue_left;
	Button bt_mainactivity_cue_right;

	Button bt_mainactivity_record, bt_mainactivity_fx, bt_mainactivity_mixer,
			bt_mainactivity_loadfirst, bt_mainactivity_loadsecond,
			bt_mainactivity_first_load_pause, bt_mainactivity_first_load_play,
			bt_mainactivity_second_load_pause,
			bt_mainactivity_second_load_play, bt_mainactivity_load_mixer,
			bt_mainactivity_menu, bt_mainactivity_bmp_left,
			bt_mainactivity_bmp_right;

	private void initUI() {
		tv_timer_record = (TextView) findViewById(R.id.tv_mainactivity_record_timer);
		bt_mainactivity_record = (Button) findViewById(R.id.bt_mainactivity_record);

		timerComp = new TimerComp(null, MainActivity.this, tv_timer_record,
				bt_mainactivity_record);

		// /////////////////// main button UI ///////////////////////////////

		bt_mainactivity_bmp_left = (Button) findViewById(R.id.bt_mainactivity_bmp_left);
		bt_mainactivity_bmp_right = (Button) findViewById(R.id.bt_mainactivity_bmp_right);
		bt_mainactivity_bmp_left.setOnClickListener(this);
		bt_mainactivity_bmp_right.setOnClickListener(this);

		bt_mainactivity_fx = (Button) findViewById(R.id.bt_mainactivity_fx);
		bt_mainactivity_mixer = (Button) findViewById(R.id.bt_mainactivity_mixer);
		bt_mainactivity_loadfirst = (Button) findViewById(R.id.bt_mainactivity_loadfirst);
		bt_mainactivity_loadsecond = (Button) findViewById(R.id.bt_mainactivity_loadsecond);
		bt_mainactivity_first_load_pause = (Button) findViewById(R.id.bt_mainactivity_first_load_pause);
		bt_mainactivity_first_load_play = (Button) findViewById(R.id.bt_mainactivity_first_load_play);
		bt_mainactivity_second_load_pause = (Button) findViewById(R.id.bt_mainactivity_second_load_pause);
		bt_mainactivity_second_load_play = (Button) findViewById(R.id.bt_mainactivity_second_load_play);
		bt_mainactivity_load_mixer = (Button) findViewById(R.id.bt_mainactivity_load_mixer);
		bt_mainactivity_menu = (Button) findViewById(R.id.bt_mainactivity_menu);

		bt_mainactivity_fx.setOnClickListener(this);
		bt_mainactivity_mixer.setOnClickListener(this);
		bt_mainactivity_loadfirst.setOnClickListener(this);
		bt_mainactivity_loadsecond.setOnClickListener(this);
		bt_mainactivity_first_load_pause.setOnClickListener(this);
		bt_mainactivity_first_load_play.setOnClickListener(this);
		bt_mainactivity_second_load_pause.setOnClickListener(this);
		bt_mainactivity_second_load_play.setOnClickListener(this);
		bt_mainactivity_record.setOnClickListener(this);
		bt_mainactivity_load_mixer.setOnClickListener(this);
		bt_mainactivity_menu.setOnClickListener(this);

		// /////////////// pad button UI left ///////
		Button bt_mainactivity_f_f1 = (Button) findViewById(R.id.bt_mainactivity_f_f1);
		Button bt_mainactivity_f_f2 = (Button) findViewById(R.id.bt_mainactivity_f_f2);
		Button bt_mainactivity_f_f3 = (Button) findViewById(R.id.bt_mainactivity_f_f3);
		Button bt_mainactivity_f_f4 = (Button) findViewById(R.id.bt_mainactivity_f_f4);
		Button bt_mainactivity_f_f5 = (Button) findViewById(R.id.bt_mainactivity_f_f5);
		Button bt_mainactivity_f_f6 = (Button) findViewById(R.id.bt_mainactivity_f_f6);
		Button bt_mainactivity_f_f7 = (Button) findViewById(R.id.bt_mainactivity_f_f7);
		Button bt_mainactivity_f_f8 = (Button) findViewById(R.id.bt_mainactivity_f_f8);
		Button bt_mainactivity_f_f9 = (Button) findViewById(R.id.bt_mainactivity_f_f9);

		bt_mainactivity_f_f1.setOnClickListener(this);
		bt_mainactivity_f_f2.setOnClickListener(this);
		bt_mainactivity_f_f3.setOnClickListener(this);
		bt_mainactivity_f_f4.setOnClickListener(this);
		bt_mainactivity_f_f5.setOnClickListener(this);
		bt_mainactivity_f_f6.setOnClickListener(this);
		bt_mainactivity_f_f7.setOnClickListener(this);
		bt_mainactivity_f_f8.setOnClickListener(this);
		bt_mainactivity_f_f9.setOnClickListener(this);

		// /////////////// pad button UI right ///////

		Button bt_mainactivity_s_f1 = (Button) findViewById(R.id.bt_mainactivity_s_f1);
		Button bt_mainactivity_s_f2 = (Button) findViewById(R.id.bt_mainactivity_s_f2);
		Button bt_mainactivity_s_f3 = (Button) findViewById(R.id.bt_mainactivity_s_f3);
		Button bt_mainactivity_s_f4 = (Button) findViewById(R.id.bt_mainactivity_s_f4);
		Button bt_mainactivity_s_f5 = (Button) findViewById(R.id.bt_mainactivity_s_f5);
		Button bt_mainactivity_s_f6 = (Button) findViewById(R.id.bt_mainactivity_s_f6);
		Button bt_mainactivity_s_f7 = (Button) findViewById(R.id.bt_mainactivity_s_f7);
		Button bt_mainactivity_s_f8 = (Button) findViewById(R.id.bt_mainactivity_s_f8);
		Button bt_mainactivity_s_f9 = (Button) findViewById(R.id.bt_mainactivity_s_f9);

		bt_mainactivity_s_f1.setOnClickListener(this);
		bt_mainactivity_s_f2.setOnClickListener(this);
		bt_mainactivity_s_f3.setOnClickListener(this);
		bt_mainactivity_s_f4.setOnClickListener(this);
		bt_mainactivity_s_f5.setOnClickListener(this);
		bt_mainactivity_s_f6.setOnClickListener(this);
		bt_mainactivity_s_f7.setOnClickListener(this);
		bt_mainactivity_s_f8.setOnClickListener(this);
		bt_mainactivity_s_f9.setOnClickListener(this);

		seekbar_second_first = (SeekBar) findViewById(R.id.seekbar_second_first);
		seekbar_second_second = (SeekBar) findViewById(R.id.seekbar_second_second);
		seekbarmain_first = (SeekBar) findViewById(R.id.seekbarmain_first);
		seekBar_mainactivity_seocndmain = (SeekBar) findViewById(R.id.seekBar_mainactivity_seocndmain);

		vs_mainactivity_1 = (VerticalSeekBar) findViewById(R.id.vs_mainactivity_1);
		vs_mainactivity_2 = (VerticalSeekBar) findViewById(R.id.vs_mainactivity_2);
		vs_mainactivity_3 = (VerticalSeekBar) findViewById(R.id.vs_mainactivity_3);
		vs_mainactivity_4 = (VerticalSeekBar) findViewById(R.id.vs_mainactivity_4);
		vs_mainactivity_5 = (VerticalSeekBar) findViewById(R.id.vs_mainactivity_5);
		vs_mainactivity_6 = (VerticalSeekBar) findViewById(R.id.vs_mainactivity_6);

		Button bt_mainactivity_b1 = (Button) findViewById(R.id.bt_mainactivity_b1);
		Button bt_mainactivity_b2 = (Button) findViewById(R.id.bt_mainactivity_b2);
		Button bt_mainactivity_b3 = (Button) findViewById(R.id.bt_mainactivity_b3);
		Button bt_mainactivity_b4 = (Button) findViewById(R.id.bt_mainactivity_b4);
		Button bt_mainactivity_b5 = (Button) findViewById(R.id.bt_mainactivity_b5);
		Button bt_mainactivity_b6 = (Button) findViewById(R.id.bt_mainactivity_b6);

		bt_mainactivity_b1.setOnClickListener(this);
		bt_mainactivity_b2.setOnClickListener(this);
		bt_mainactivity_b3.setOnClickListener(this);
		bt_mainactivity_b4.setOnClickListener(this);
		bt_mainactivity_b5.setOnClickListener(this);
		bt_mainactivity_b6.setOnClickListener(this);

		bt_mainactivity_cue_left = (Button) findViewById(R.id.bt_mainactivity_cue_left);
		bt_mainactivity_cue_right = (Button) findViewById(R.id.bt_mainactivity_cue_right);

		Button bt_mainactivity_sync = (Button) findViewById(R.id.bt_mainactivity_sync);

		bt_mainactivity_cue_left.setOnClickListener(this);
		bt_mainactivity_cue_right.setOnClickListener(this);

		bt_mainactivity_sync.setOnClickListener(this);

		iv_speaker_left = (ImageView) findViewById(R.id.iv_speaker_left);
		iv_speaker_right = (ImageView) findViewById(R.id.iv_speaker_right);

		iv_speaker_left.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:

					snd_speaker_left.play(explode);
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

					snd_speaker_left.play(pickup);
					break;

				default:
					break;
				}

				return true;
			}
		});

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		/*
		 * Intent callIntent = new Intent(MainActivity.this,
		 * RecordService.class); stopService(callIntent);
		 */
		/*
		 * mediaPlayer1.stop(); mediaPlayer2.stop();
		 */

	}

	/*
	 * @Override protected void onActivityResult(int requestCode, int
	 * resultCode, Intent data) { // TODO Auto-generated method stub
	 * super.onActivityResult(requestCode, resultCode, data);
	 * 
	 * if(resultCode == Activity.RESULT_OK) { if(requestCode == 0) {
	 * System.out.println("dddddddddddd sdfsdhsdfds  "); } }
	 * 
	 * System.out.println("dddddddddddd hhh "+requestCode + " ,  "+resultCode);
	 * }
	 */
	boolean flag_play_1, flag_play_2, flag_pause_1, flag_pause_2,
			flag_record_audio;

	private boolean flagForRecord;

	long timeDiffrence1, timeDiffrence2, currentTime1, currentTime2;

	public static double startTime1 = 0;
	public static double finalTime1 = 0;
	public static int forwardTime1 = 5000;

	public static double startTime2 = 0;
	public static double finalTime2 = 0;
	public static int forwardTime2 = 5000;

	public void forward(int i) {
		if (i == 1) {
			int temp = (int) startTime1;
			if ((temp + forwardTime1) <= finalTime1) {
				startTime1 = startTime1 + forwardTime1;

				if (mediaPlayer1 != null)
					mediaPlayer1.seekTo((int) startTime1);
			} else {
				Toast.makeText(getApplicationContext(),
						"Cannot jump forward 1 5 seconds", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			int temp = (int) startTime2;
			if ((temp + forwardTime2) <= finalTime2) {
				startTime2 = startTime2 + forwardTime2;

				if (mediaPlayer2 != null)
					mediaPlayer2.seekTo((int) startTime2);
			} else {
				Toast.makeText(getApplicationContext(),
						"Cannot jump forward 5 seconds", Toast.LENGTH_SHORT)
						.show();
			}

		}

	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View viewID) {
		switch (viewID.getId()) {

		case R.id.bt_mainactivity_bmp_left:
			forward(1);
			break;

		case R.id.bt_mainactivity_bmp_right:
			forward(2);
			break;

		case R.id.bt_mainactivity_loadfirst:

			flagPause = false;

			System.out.println("ddddddddon loadfirst ");
			currentTime1 = System.currentTimeMillis();
			timeDiffrence2 = System.currentTimeMillis() - currentTime2;

			flagForRecord = false;

			try {
				wheelSet1.cancel();

				if (mediaPlayer1 != null) {
					mediaPlayer1.stop();
					mediaPlayer1.release();

					mediaPlayer1 = null;
				}
				/*
				 * if (mediaPlayerRecord != null) {
				 * 
				 * mediaPlayerRecord.stop(); mediaPlayerRecord.release();
				 * mediaPlayerRecord = null; }
				 */

				startTime1 = 0;
				finalTime1 = 0;
				forwardTime1 = 30000;

				setLoadInt = 0;
				mediaPlayer1 = new MediaPlayer();
				Intent i = new Intent(MainActivity.this, TabPagerActivity.class);
				// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);

				initVisSecondFirst(mediaPlayer1);

				mediaPlayer1
						.setOnCompletionListener(new OnCompletionListener() {

							@Override
							public void onCompletion(MediaPlayer arg0) {
								// TODO Auto-generated method stub
								if (mediaPlayer1 != null) {
									mediaPlayer1.stop();
									mediaPlayer1.release();
									mediaPlayer1 = null;
								}

								wheelSet1.cancel();
							}
						});

			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case R.id.bt_mainactivity_loadsecond:

			flagPause = false;

			System.out.println("ddddddddon loadsecond ");
			currentTime2 = System.currentTimeMillis();
			timeDiffrence1 = System.currentTimeMillis() - currentTime1;

			setLoadInt = 1;

			startTime2 = 0;
			finalTime2 = 0;
			forwardTime2 = 30000;

			flagForRecord = false;
			wheelSet2.cancel();
			if (mediaPlayer2 != null) {

				mediaPlayer2.stop();
				mediaPlayer2.release();
				mediaPlayer2 = null;
			}

			/*
			 * if (mediaPlayerRecord != null) { mediaPlayerRecord.stop();
			 * mediaPlayerRecord.release(); mediaPlayerRecord = null; }
			 */

			mediaPlayer2 = new MediaPlayer();
			Intent i = new Intent(MainActivity.this, TabPagerActivity.class);
			startActivity(i);

			/*
			 * finalTime2 = mediaPlayer2.getDuration(); startTime2 =
			 * mediaPlayer2.getCurrentPosition();
			 */
			initVisSecondSecond(mediaPlayer2);

			mediaPlayer2.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub

					if (mediaPlayer2 != null) {
						mediaPlayer2.stop();
						mediaPlayer2.release();
						mediaPlayer2 = null;
					}
					wheelSet2.cancel();
				}
			});

			break;

		case R.id.bt_mainactivity_sync:

			if (currentTime1 < currentTime2) {
				if (mediaPlayer1 != null) {
					mediaPlayer1.seekTo((int) timeDiffrence1);
				}

			} else {

				if (mediaPlayer2 != null) {
					mediaPlayer2.seekTo((int) timeDiffrence2);
				}
			}

			timeDiffrence1 = timeDiffrence2 = 0;

			break;

		case R.id.bt_mainactivity_cue_left:
			if (mediaPlayer1 != null) {
				mediaPlayer1.setLooping(true);
				mediaPlayer1
						.setOnCompletionListener(new OnCompletionListener() {

							@Override
							public void onCompletion(MediaPlayer arg0) {
								// TODO Auto-generated method stub

							}
						});
			}
			break;

		case R.id.bt_mainactivity_cue_right:

			if (mediaPlayer2 != null) {
				mediaPlayer2.setLooping(true);
			}
			break;

		case R.id.bt_mainactivity_menu:
			intent = new Intent(MainActivity.this, MenuActivity.class);
			startActivity(intent);

			break;

		// ///////////////// first pad /////////////////
		case R.id.bt_mainactivity_f_f1:
			snd_pad_left[0].play(explode);
			break;
		case R.id.bt_mainactivity_f_f2:
			snd_pad_left[1].play(pickup);
			break;

		case R.id.bt_mainactivity_f_f3:
			snd_pad_left[2].play(explode);
			break;
		case R.id.bt_mainactivity_f_f4:
			snd_pad_left[3].play(pickup);
			break;

		case R.id.bt_mainactivity_f_f5:
			snd_pad_left[4].play(explode);
			break;
		case R.id.bt_mainactivity_f_f6:
			snd_pad_left[5].play(pickup);
			break;

		case R.id.bt_mainactivity_f_f7:
			snd_pad_left[6].play(explode);
			break;
		case R.id.bt_mainactivity_f_f8:
			snd_pad_left[7].play(pickup);
			break;

		case R.id.bt_mainactivity_f_f9:
			snd_pad_left[8].play(explode);
			break;

		// /////// second pad ///////////

		case R.id.bt_mainactivity_s_f1:
			snd_pad_right[0].play(explode);
			break;
		case R.id.bt_mainactivity_s_f2:
			snd_pad_right[1].play(pickup);
			break;

		case R.id.bt_mainactivity_s_f3:
			snd_pad_right[2].play(explode);
			break;
		case R.id.bt_mainactivity_s_f4:
			snd_pad_right[3].play(pickup);
			break;

		case R.id.bt_mainactivity_s_f5:
			snd_pad_right[4].play(explode);
			break;
		case R.id.bt_mainactivity_s_f6:
			snd_pad_right[5].play(pickup);
			break;

		case R.id.bt_mainactivity_s_f7:
			snd_pad_right[6].play(explode);
			break;
		case R.id.bt_mainactivity_s_f8:
			snd_pad_right[7].play(pickup);
			break;

		case R.id.bt_mainactivity_s_f9:
			snd_pad_right[8].play(explode);
			break;

		// //////////////////////////// six btn ///////////////////////////////

		case R.id.bt_mainactivity_b1:
			snd_speaker_left.play(pickup);
			break;

		case R.id.bt_mainactivity_b2:
			snd_speaker_left.play(explode);
			break;
		case R.id.bt_mainactivity_b3:
			snd_speaker_left.play(pickup);
			break;

		case R.id.bt_mainactivity_b4:
			snd_speaker_left.play(explode);
			break;
		case R.id.bt_mainactivity_b5:
			snd_speaker_left.play(pickup);
			break;

		case R.id.bt_mainactivity_b6:
			snd_speaker_left.play(explode);

			break;

		// ////////////////////////////

		// /////////////////////////

		case R.id.bt_mainactivity_first_load_play:

			// if (flag_play_1)

			// {
			// bt_mainactivity_first_load_play.setBackgroundResource(R.drawable.image2_1);
			if (mediaPlayer1 != null)
				mediaPlayer1.start();

			wheelSet1.start();

			flag_pause_1 = false;
			flag_pause_1 = true;

			// bt_mainactivity_first_load_pause.setBackgroundResource(R.drawable.image3);
			// }

			break;

		case R.id.bt_mainactivity_first_load_pause:

			// if(!flag_pause_1)
			// {
			// bt_mainactivity_first_load_pause.setBackgroundResource(R.drawable.image3_1);
			if (mediaPlayer1 != null)
				mediaPlayer1.pause();

			wheelSet1.cancel();
			flag_play_1 = true;
			flag_pause_1 = true;

			// bt_mainactivity_first_load_play.setBackgroundResource(R.drawable.image2);

			// }

			break;

		case R.id.bt_mainactivity_second_load_play:

			// if (flag_play_2) {
			// bt_mainactivity_second_load_play.setBackgroundResource(R.drawable.image2_1);

			if (mediaPlayer2 != null)
				mediaPlayer2.start();

			wheelSet2.start();
			flag_pause_2 = true;
			flag_play_2 = false;
			// }

			break;

		case R.id.bt_mainactivity_second_load_pause:

			flag_play_2 = true;

			// bt_mainactivity_second_load_pause.setBackgroundResource(R.drawable.image3_1);

			if (mediaPlayer2 != null)
				mediaPlayer2.pause();

			wheelSet2.cancel();
			break;

		case R.id.bt_mainactivity_fx:
			flagPause = false;
			if (mVisualizerView != null) {
				mVisualizerView.release();
			}

			intent = new Intent(MainActivity.this, SecondActivity_FX.class);
			startActivity(intent);
			break;

		case R.id.bt_mainactivity_mixer:
			flagPause = false;

			if (mVisualizerView != null) {
				mVisualizerView.release();
			}
			intent = new Intent(MainActivity.this, ThirtdActivity_MIXER.class);
			startActivity(intent);

			break;

		case R.id.bt_mainactivity_record:

			if (!flagForRecord) {
				timerComp.resetTimer();
				timerComp.startTimer();

				Intent callIntent = new Intent(MainActivity.this,
						RecordService.class);
				startService(callIntent);

				flagForRecord = true;
			} else {
				timerComp.resetTimer();
				timerComp.stopTimer();
				Intent callIntent = new Intent(MainActivity.this,
						RecordService.class);
				stopService(callIntent);

				flagForRecord = false;
			}

			break;

		case R.id.bt_mainactivity_load_mixer:

			flagPause = false;

			timerComp.resetTimer();
			timerComp.stopTimer();
			flagForRecord = false;

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
			Intent callIntent = new Intent(MainActivity.this,
					RecordService.class);
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

	public static AnimatorSet wheelSet1, wheelSet2;

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

		if (secondFirstMediaPlayer != null) {
			mVisualizerView = (VisualizerViewMain) findViewById(R.id.vis_second_first);

			mVisualizerView.link(secondFirstMediaPlayer);
			// Start with just line renderer
			addLineRenderer();
		}

	}

	private void initVisSecondSecond(MediaPlayer secondSecondMediaPlayer) {

		if (secondSecondMediaPlayer != null) {
			mVisualizerView = (VisualizerViewMain) findViewById(R.id.vis_second_second);
			mVisualizerView.link(secondSecondMediaPlayer);
			// Start with just line renderer
			addLineRenderer();
		}

	}

	private void initVisMain(MediaPlayer mainMediaPlayer) {

		mVisualizerView = (VisualizerViewMain) findViewById(R.id.visualizerView);
		mVisualizerView.link(mainMediaPlayer);

		// Start with just line renderer
		addLineRenderer();
	}

	private void initTunnelPlayerWorkaround() {
		// Read "tunnel.decode" system property to determine
		// the workaround is needed
		if (TunnelPlayerWorkaround.isTunnelDecodeEnabled(this)) {
			// / mSilentPlayer =
			// TunnelPlayerWorkaround.createSilentMediaPlayer(this);
		}
		mSilentPlayer = TunnelPlayerWorkaround.createSilentMediaPlayer(this);
		initVisMain(mSilentPlayer);
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

	SeekBar seekbar_second_first, seekbar_second_second, seekbarmain_first,
			seekBar_mainactivity_seocndmain;
	VerticalSeekBar vs_mainactivity_1, vs_mainactivity_2, vs_mainactivity_3,
			vs_mainactivity_4, vs_mainactivity_5, vs_mainactivity_6;

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
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2);
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

						float volume = (float) (1 - (Math.log(MAX_VOLUME
								- progress) / Math.log(MAX_VOLUME)));

						if (mediaPlayer1 != null)
							mediaPlayer1.setVolume(volume, volume);
					}
				});

		seekbar_second_second.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		seekbar_second_second.setProgress(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2);
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

		vs_mainactivity_1
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
						snd_pad_left[0].setVolume((float) progress / 100.0f);
						snd_pad_left[3].setVolume((float) progress / 100.0f);
						snd_pad_left[6].setVolume((float) progress / 100.0f);
					}
				});

		vs_mainactivity_2
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
						snd_pad_left[1].setVolume((float) progress / 100.0f);
						snd_pad_left[4].setVolume((float) progress / 100.0f);
						snd_pad_left[7].setVolume((float) progress / 100.0f);
					}
				});

		vs_mainactivity_3
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
						snd_pad_left[2].setVolume((float) progress / 100.0f);
						snd_pad_left[5].setVolume((float) progress / 100.0f);
						snd_pad_left[8].setVolume((float) progress / 100.0f);
					}
				});

		vs_mainactivity_4
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
						snd_pad_right[0].setVolume((float) progress / 100.0f);
						snd_pad_right[3].setVolume((float) progress / 100.0f);
						snd_pad_right[6].setVolume((float) progress / 100.0f);
					}
				});

		vs_mainactivity_5
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
						snd_pad_right[1].setVolume((float) progress / 100.0f);
						snd_pad_right[4].setVolume((float) progress / 100.0f);
						snd_pad_right[7].setVolume((float) progress / 100.0f);
					}
				});

		vs_mainactivity_6
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
						snd_pad_right[2].setVolume((float) progress / 100.0f);
						snd_pad_right[5].setVolume((float) progress / 100.0f);
						snd_pad_right[8].setVolume((float) progress / 100.0f);
					}
				});

		seekBar_mainactivity_seocndmain.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		seekBar_mainactivity_seocndmain.setProgress(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2);
		seekBar_mainactivity_seocndmain
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

						/*
						 * if (mediaPlayer1 != null && mediaPlayer2 != null) {
						 * mediaPlayer2.setVolume(volume, volume);
						 * mediaPlayer1.setVolume(1 - volume, 1 - volume); }
						 */
						if (volume > 1) {
							volume = 1;
						}
						if (volume < 0) {
							volume = 0;
						}

						snd_pad_left[0].setVolume(1 - volume);
						snd_pad_left[1].setVolume(1 - volume);
						snd_pad_left[2].setVolume(1 - volume);
						snd_pad_left[3].setVolume(1 - volume);
						snd_pad_left[4].setVolume(1 - volume);
						snd_pad_left[5].setVolume(1 - volume);
						snd_pad_left[6].setVolume(1 - volume);
						snd_pad_left[7].setVolume(1 - volume);
						snd_pad_left[8].setVolume(1 - volume);

						snd_pad_right[0].setVolume(volume);
						snd_pad_right[1].setVolume(volume);
						snd_pad_right[2].setVolume(volume);
						snd_pad_right[3].setVolume(volume);
						snd_pad_right[4].setVolume(volume);
						snd_pad_right[5].setVolume(volume);
						snd_pad_right[6].setVolume(volume);
						snd_pad_right[7].setVolume(volume);
						snd_pad_right[8].setVolume(volume);
					}
				});

	}
}
