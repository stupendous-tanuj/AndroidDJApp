package com.app.djapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.djapp.MainActivity;
import com.app.djapp.R;

public class OtherFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_others, container, false);
		  
			ListView musicListView = (ListView) rootView.findViewById(R.id.listView_other);
			 ListViewAdapterClass ad = new ListViewAdapterClass(MainActivity.ctx , MainActivity.othersArrayList);
		     musicListView.setAdapter(ad);
		return rootView;
	}
}
