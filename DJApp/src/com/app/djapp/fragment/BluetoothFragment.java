package com.app.djapp.fragment;
 
import com.app.djapp.MainActivity;
import com.app.djapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class BluetoothFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_bluetooth, container, false);
		  
			ListView musicListView = (ListView) rootView.findViewById(R.id.listView_bluetooth);
			ListViewAdapterClass ad = new ListViewAdapterClass(MainActivity.ctx , MainActivity.bluetoothArrayList);
		    musicListView.setAdapter(ad);
		return rootView;
	}

}
