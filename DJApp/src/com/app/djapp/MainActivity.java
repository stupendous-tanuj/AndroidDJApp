package com.app.djapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	private Intent intent;
	private Cursor cursor;
	private static ArrayList<GSC> songs = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		initUI();
	}

	private void initUI() {
		findViewById(R.id.bt_mainactivity_menu).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_fx).setOnClickListener(this);
		findViewById(R.id.bt_mainactivity_mixer).setOnClickListener(this);
	}

	@Override
	public void onClick(View viewID) {
		switch (viewID.getId()) {
		case R.id.bt_mainactivity_menu:
			bindAllSongs();
			break;

		case R.id.bt_mainactivity_fx:
			intent = new Intent(MainActivity.this, SecondActivity_FX.class);
			startActivity(intent);
			break;
		case R.id.bt_mainactivity_mixer:
			intent = new Intent(MainActivity.this, ThirtdActivity_MIXER.class);
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

	

	private  void bindAllSongs() {
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
				while (!cursor.isAfterLast()) {
					GSC gsc = new GSC();
					gsc.songTitle = cursor.getString(0);
					gsc.songArtist = cursor.getString(1);
					gsc.songData = cursor.getString(2);
					songs.add(gsc);

					System.out.println("dddddddd  " + gsc.songTitle + " , "
							+ gsc.songArtist + gsc.songData);
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
		String songTitle = "";
		String songArtist = "";
		String songData = "";
		String isChecked = "false";
	}
}
