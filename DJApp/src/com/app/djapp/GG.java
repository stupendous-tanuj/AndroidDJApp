package com.app.djapp;

import java.io.IOException;
import java.util.ArrayList;

import com.app.djapp.MainActivity.GSC;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.media.MediaPlayer;
	
public class GG extends Activity {

	public class GSC {
		String songTitle = "";
		String songArtist = "";
		String songData = "";
		String isChecked = "false";
	}
	
	private Cursor cursor;
	private static ArrayList<GSC> songs = null;
	
	private static MediaRecorder mediaRecorder;
	private static MediaPlayer mediaPlayer;

	private static String audioFilePath;
	private static Button stopButton;
	private static Button playButton;
	private static Button recordButton;
	
	private boolean isRecording = false;

	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.gg);
		recordButton = (Button) findViewById(R.id.button3);
		playButton = (Button) findViewById(R.id.button1);
		stopButton = (Button) findViewById(R.id.button2);
	
		/*if (!hasMicrophone())
		{
			stopButton.setEnabled(false);
			playButton.setEnabled(false);
			recordButton.setEnabled(false);
		} else {
			playButton.setEnabled(false);
			stopButton.setEnabled(false);
		}*/
		bindAllSongs();
		
		audioFilePath = 
				
            Environment.getExternalStorageDirectory().getAbsolutePath() 
                 + "/myaudio.3gp";

	}
	String path;
	private MediaRecorder recorder;
	
	private void bindAllSongs() {
		System.out.println("dddddddd  sss " );
		/** Making custom drawable */
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
		final String[] projection = new String[] {
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DATA };
		final String sortOrder = MediaStore.Audio.AudioColumns.TITLE
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
					songs.add(gsc);
					System.out.println("dddddddd  " + gsc.songTitle + " , "
							+ gsc.songArtist + " , " + gsc.songData);
					cursor.moveToNext();
				}

				  path = gsc.songData;
				/*MediaPlayer mp = new MediaPlayer();
				mp.setDataSource(path);
				mp.prepare();
				mp.start();*/
			}
		} catch (Exception ex) {

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

	}
	
	public void recordAudio (View view) throws IOException
	{
		
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setDataSource("/sdcard/AudioRecording"+"umesh.mp3");
		mediaPlayer.prepare();
		mediaPlayer.start();
		
	/*	
	   isRecording = true;
	   
		   
	   try {
	     mediaRecorder = new MediaRecorder();
	     mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	     mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	     mediaRecorder.setOutputFile(audioFilePath);
	     mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	     mediaRecorder.prepare();
	   } catch (Exception e) {
		   e.printStackTrace();
	   }

	   mediaRecorder.start();	*/		
	}
	public void stopClicked (View view)
	{
		
		stopButton.setEnabled(false);
		playButton.setEnabled(true);
			
		if (isRecording)
			
		{	
			recordButton.setEnabled(false);
			
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
			isRecording = false;
		} else {
			mediaPlayer.release();
		        mediaPlayer = null;
			recordButton.setEnabled(true);
		}
		
		Intent callIntent = new Intent(GG.this, RecordService.class);
	     stopService(callIntent);
	}
	
	  private void startRecord() throws IllegalStateException, IOException{
            recorder = new MediaRecorder(); 
           recorder.setAudioSource(MediaRecorder.AudioSource.MIC);  //ok so I say audio source is the microphone, is it windows/linux microphone on the emulator? 
           recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); 
           recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); 
           recorder.setOutputFile("/sdcard/Music/"+System.currentTimeMillis()+".amr"); 
           recorder.prepare(); 
           recorder.start();      
       }

       private void stopRecord(){
           recorder.stop();
         //recorder.release();
       }

	
	public void playAudio (View view) throws IOException
	{
		
		 Intent callIntent = new Intent(GG.this, RecordService.class);
		     startService(callIntent); 
		/*playButton.setEnabled(false);
		recordButton.setEnabled(false);*/
	/*	stopButton.setEnabled(true);

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setDataSource(path);
		mediaPlayer.prepare();
		mediaPlayer.start();
		//startRecord();
*/		
		Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
		startActivity(intent);
		
	}
}