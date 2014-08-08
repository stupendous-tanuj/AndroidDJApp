package com.app.djapp;

import java.io.File;
import java.util.ArrayList;

import com.app.djapp.fragment.ListViewAdapterClass;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class RecordFileActivity extends Activity {

	public void onCreate(Bundle b) {
		super.onCreate(b);
		ArrayList<String> allRecordFile = 	getPlayListRecording();
		setContentView(R.layout.recordfileactivity);
		ListView musicListView = (ListView) findViewById(R.id.listView_record_audio);
		ListViewAdapterClass ad = new ListViewAdapterClass(MainActivity.ctx , allRecordFile);
		musicListView.setAdapter(ad);
	}

	final String MEDIA_PATH = new String("/sdcard/AudioRecordingDj/");
	private ArrayList<String> songsList = new ArrayList<String>();
	private String mp3Pattern = ".mp3";
	
	public ArrayList<String>  getPlayListRecording() {
	    System.out.println(MEDIA_PATH);
	    if (MEDIA_PATH != null) {
	        File home = new File(MEDIA_PATH);
	        File[] listFiles = home.listFiles();
	        if (listFiles != null && listFiles.length > 0) {
	            for (File file : listFiles) {
	                System.out.println(file.getAbsolutePath());
	                if (file.isDirectory()) {
	                    scanDirectory(file);
	                } else {
	                    addSongToList(file);
	                }
	            }
	        }
	    }
	    // return songs list array
	    return songsList;
	}

	private void scanDirectory(File directory) {
	    if (directory != null) {
	        File[] listFiles = directory.listFiles();
	        if (listFiles != null && listFiles.length > 0) {
	            for (File file : listFiles) {
	                if (file.isDirectory()) {
	                    scanDirectory(file);
	                } else {
	                    addSongToList(file);
	                }

	            }
	        }
	    }
	}

	private void addSongToList(File song) {
	    if (song.getName().endsWith(mp3Pattern)) {	       
	        songsList.add(song.getName().substring(0, (song.getName().length() - 4))+"~"+song.getPath());
	    }
	    
	    
	   
	}
	
	
}
