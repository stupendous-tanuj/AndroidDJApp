package com.app.djapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.djapp.MainActivity;
import com.app.djapp.R;

public class MusicFragment extends Fragment {
 	 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_music, container, false);
		
		  
		ListView musicListView = (ListView) rootView.findViewById(R.id.listView_music);
		 ListViewAdapterClass ad = new ListViewAdapterClass(MainActivity.ctx , MainActivity.musicArrayList);
	     musicListView.setAdapter(ad);
	 
		return rootView;
	}
}
