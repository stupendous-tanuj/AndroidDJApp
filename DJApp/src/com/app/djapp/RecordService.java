package com.app.djapp;

import java.io.File;
import java.io.IOException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class RecordService extends Service implements
		MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener {
	private static final String TAG = "DJRecorder";

	
	public static final String DEFAULT_STORAGE_LOCATION = "/sdcard/AudioRecordingDj";
	private static final int RECORDING_NOTIFICATION_ID = 1;
	private MediaRecorder recorder = null;
	private boolean isRecording = false;
	private File recording = null;;
	private final int audioformat = 3;

	private File makeOutputFile(SharedPreferences prefs) 
	{
	 
		
		File dir = new File(DEFAULT_STORAGE_LOCATION);
		// test dir for existence and writeability
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				 
				return null;
			}
		} else {
			if (!dir.canWrite()) {
				 
				return null;
			}
		}

		// test size

		// create filename based on call data
		String prefix = "dj_";
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd_HH:MM:SS");
		// prefix = sdf.format(new Date()) + "-callrecording";

		// add info to file name about what audio channel we were recording
		String d = System.currentTimeMillis()+"";
		prefix += d.substring(12, d.length()) ;

		// create suffix based on format
		String suffix = ".mp3";
		/*switch (audioformat) {
		case MediaRecorder.OutputFormat.THREE_GPP:
			suffix = ".3gpp";
			break;
		case MediaRecorder.OutputFormat.MPEG_4:
			suffix = ".mpg";
			break;
		case MediaRecorder.OutputFormat.RAW_AMR:
			suffix = ".amr";
			break;
*/		//}

		try {
			return File.createTempFile(prefix, suffix, dir);
		} catch (IOException e) {
			 
			return null;
		}
	}

	public void onCreate() {
		super.onCreate();
		recorder = new MediaRecorder();
	 
	}

	 
	public void onStart(Intent intent, int startId) {
		
		if (isRecording)
			return;
		Context c = getApplicationContext();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(c);
 
		recording = makeOutputFile(prefs);
		if (recording == null) {
			recorder = null;
			return; // return 0;
		}

		try {
		 
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	 
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			 recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(recording.getAbsolutePath());
			 
			recorder.setOnInfoListener(this);
			recorder.setOnErrorListener(this);

			try {
				recorder.prepare();
			} catch (java.io.IOException e) {
			 
				recorder = null;
				return; // return 0; //START_STICKY;
			}
		 
			recorder.start();
			isRecording = true;
			 
			updateNotification(true);
			
		} catch (java.lang.Exception e) {
		 
			recorder = null;
		}

		return; // return 0; //return START_STICKY;
	}

	public void onDestroy() {
		super.onDestroy();

		if (null != recorder) {
			 
			isRecording = false;
			recorder.release();
			 
		}

		updateNotification(false);
	}

	// methods to handle binding the service

	public IBinder onBind(Intent intent) {
		return null;
	}

	public boolean onUnbind(Intent intent) {
		return false;
	}

	public void onRebind(Intent intent) {
	}

	private void updateNotification(Boolean status) {
		Context c = getApplicationContext();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(c);

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		if (status) {
			int icon = R.drawable.ic_launcher;
			CharSequence tickerText = "Recording audio in SD Card ";// + prefs.getString(Preferences.PREF_AUDIO_SOURCE, "1");
			long when = System.currentTimeMillis();

			Notification notification = new Notification(icon, tickerText, when);

			Context context = getApplicationContext();
			CharSequence contentTitle = "DJRecorder Status";
			CharSequence contentText = "Recording audio in SD Card ";
			Intent notificationIntent = new Intent(this, RecordService.class);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					notificationIntent, 0);

			notification.setLatestEventInfo(context, contentTitle, contentText,
					contentIntent);
			mNotificationManager
					.notify(RECORDING_NOTIFICATION_ID, notification);
		} else {
			mNotificationManager.cancel(RECORDING_NOTIFICATION_ID);
		}
	}

	// MediaRecorder.OnInfoListener
	public void onInfo(MediaRecorder mr, int what, int extra) {
		 
		isRecording = false;
	}

	// MediaRecorder.OnErrorListener
	public void onError(MediaRecorder mr, int what, int extra) {
	 
		isRecording = false;
		mr.release();
	}
}