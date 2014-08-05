package com.app.djapp.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.djapp.fragment.BluetoothFragment;
import com.app.djapp.fragment.MusicFragment;
import com.app.djapp.fragment.OtherFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Games fragment activity
			return new MusicFragment();
		case 1:
			// Movies fragment activity
			return new BluetoothFragment();
		case 2:

			// Top Rated fragment activity
			return new OtherFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
