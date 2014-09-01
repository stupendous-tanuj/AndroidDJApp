package com.app.djapp;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

public class RecordFileActivity extends Activity {

	private ArrayAdapter<String> arrayAdapter;
	ListView musicListView;
	Button bt_record_file_activity_play, bt_record_file_activity_soundcloud;
	String audioName = "";
	private File mArtwork;
	private static final int SHARE_SOUND = 1;

	private static boolean AAC_SUPPORTED = Build.VERSION.SDK_INT >= 10;

	private static final Uri MARKET_URI = Uri
			.parse("market://details?id=com.soundcloud.android");
	private static final int DIALOG_NOT_INSTALLED = 0;

	// Replace with the client id of your registered app!
	// see http://soundcloud.com/you/apps/
	private static final String CLIENT_ID = "fecfc092de134a960dc48e53c044ee91";

	public void onCreate(Bundle b) {
		super.onCreate(b);

		ArrayList<String> allRecordFile = getPlayListRecording();

		setContentView(R.layout.recordfileactivity);

		bt_record_file_activity_play = (Button) findViewById(R.id.bt_record_file_activity_play);
		bt_record_file_activity_soundcloud = (Button) findViewById(R.id.bt_record_file_activity_soundcloud);

		musicListView = (ListView) findViewById(R.id.listView_record_audio);

		arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_list_item_single_choice, allRecordFile);
		musicListView.setAdapter(arrayAdapter);

		bt_record_file_activity_play
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						try {

							MainActivity.mediaPlayerRecord
									.setDataSource(MEDIA_PATH + audioName);
							MainActivity.mediaPlayerRecord.prepare();
							MainActivity.mediaPlayerRecord.start();
						} catch (Exception e) {
							System.out.println("eeeeeee  " + e.getMessage());
						}

					}
				});

		bt_record_file_activity_soundcloud
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							if (!isCompatibleSoundCloudInstalled(RecordFileActivity.this)) {
								showDialog(DIALOG_NOT_INSTALLED);
							}

							shareSound();
						} catch (Exception e) {
							System.out.println("eeeeeee  " + e.getMessage());
						}

					}
				});

		musicListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long arg3) {
				CheckedTextView textView = (CheckedTextView) view;
				for (int i = 0; i < musicListView.getCount(); i++) {
					textView = (CheckedTextView) musicListView.getChildAt(i);
					if (textView != null) {
						textView.setTextColor(Color.BLACK);
					}
				}

				textView = (CheckedTextView) view;
				if (textView != null) {
					textView.setTextColor(Color.BLUE);
				}

				if (MainActivity.mediaPlayerRecord != null) {
					MainActivity.mediaPlayerRecord.reset();
				}
				Toast.makeText(getApplicationContext(),
						"HHH  " + textView.getText().toString(),
						Toast.LENGTH_LONG).show();

				audioName = textView.getText().toString() + ".mp3";
				musicListView.invalidate();
			}
		});

	}

	private static boolean isCompatibleSoundCloudInstalled(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					"com.soundcloud.android", PackageManager.GET_META_DATA);

			// intent sharing only got introduced with version 22
			return info != null && info.versionCode >= 22;
		} catch (PackageManager.NameNotFoundException e) {
			// not installed at all
			return false;
		}
	}

	final String MEDIA_PATH = new String("/sdcard/AudioRecordingDj/");
	private ArrayList<String> songsList = new ArrayList<String>();
	private String mp3Pattern = ".mp3";

	public ArrayList<String> getPlayListRecording() {

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
			songsList.add(song.getName().substring(0,
					(song.getName().length() - 4)));
			/* + "~" + song.getPath()); */
		}
		System.out.println("eeeeeeee " + song.getPath());
	}

	private Location getLocation() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		return locationManager
				.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
	}

	// the actual sharing happens here
	private void shareSound() {
		Intent intent = new Intent("com.soundcloud.android.SHARE")
				.putExtra(Intent.EXTRA_STREAM, MEDIA_PATH + audioName)
				// here you can set metadata for the track to be uploaded
				.putExtra("com.soundcloud.android.extra.title",
						"SoundCloud Android Intent Demo upload")
				.putExtra("com.soundcloud.android.extra.where", "Somewhere")
				.putExtra("com.soundcloud.android.extra.description",
						"This is a demo track.")
				.putExtra("com.soundcloud.android.extra.public", true)
				.putExtra(
						"com.soundcloud.android.extra.tags",
						new String[] {
								"demo",
								"post lolcat bluez",
								"soundcloud:created-with-client-id="
										+ CLIENT_ID })
				.putExtra("com.soundcloud.android.extra.genre",
						"Easy Listening")
				.putExtra("com.soundcloud.android.extra.location",
						getLocation());

		try {
			startActivityForResult(intent, SHARE_SOUND);
		} catch (ActivityNotFoundException notFound) {
			// use doesn't have SoundCloud app installed, show a dialog box
			showDialog(DIALOG_NOT_INSTALLED);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SHARE_SOUND:
			// callback gets executed when the SoundCloud app returns
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "audio share successfully",
						Toast.LENGTH_SHORT).show();
			} else {
				// canceled
				Toast.makeText(this, "audio not share", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (MainActivity.mediaPlayerRecord != null) {
			MainActivity.mediaPlayerRecord.release();
			MainActivity.mediaPlayerRecord = null;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle data) {
		if (DIALOG_NOT_INSTALLED == id) {
			return new AlertDialog.Builder(this)
					.setTitle("SoundCloud App not found or not compatible!")
					.setMessage("Do you want to install it now?")
					.setPositiveButton(android.R.string.yes,
							new Dialog.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Intent market = new Intent(
											Intent.ACTION_VIEW, MARKET_URI);
									startActivity(market);
								}
							})
					.setNegativeButton(android.R.string.no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).create();
		} else {
			return null;
		}
	}

}
