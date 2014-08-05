package com.app.djapp.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.djapp.MainActivity;
import com.app.djapp.R;
import com.app.djapp.utils.Split;

public class ListViewAdapterClass extends BaseAdapter {

	Context ctx;
	ArrayList<String> arrayListData;
	MusicFragment musicFragment;
	private LayoutInflater inflater;

	public ListViewAdapterClass(Context ctx, ArrayList<String> arrayListData) {
		this.arrayListData = arrayListData;
		this.ctx = ctx;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/*
	 * public ListViewAdapterClass(MusicFragment musicFragment,
	 * ArrayList<String> arrayListData) { this.arrayListData = arrayListData;
	 * this.musicFragment = musicFragment;
	 * 
	 * }
	 */

	@Override
	public int getCount() {
		if (arrayListData.size() <= 0)
			return 1;
		return arrayListData.size();

	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	String d[];

	@Override
	public View getView(int pos, View arg1, ViewGroup arg2) {
		View rowView = inflater.inflate(R.layout.tab_pager_custom, null, true);
		TextView songTitle = (TextView) rowView
				.findViewById(R.id.tv_tab_pager_custom_songname);

		String dataTitle = arrayListData.get(pos);
		d = Split.splitclass(dataTitle, "~");

		if (d.length >= 0)
			songTitle.setText(d[0]);

		songTitle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					
					if(MainActivity.setLoad)
					{
						MainActivity.mediaPlayer2.setDataSource(d[1]);
						MainActivity.mediaPlayer2.prepare();
						MainActivity.mediaPlayer2.start();
					}
					else
					{
						MainActivity.mediaPlayer1.setDataSource(d[1]);
						MainActivity.mediaPlayer1.prepare();
						MainActivity.mediaPlayer1.start();
					}
				
				} catch (Exception e) {
					// TODO: handle exception
				}

				// rotatedWheel1();

			}
		});

		return rowView;
	}

}
