package com.app.djapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MenuActivity extends Activity implements OnClickListener
{

	@Override
	public void onCreate(Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.menu_activity);
		
		findViewById(R.id.bt_menu_mixing).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
		
		case R.id.bt_menu_mixing:
			
			Intent i = new Intent(this , ThirtdActivity_MIXER.class);
			startActivity(i);
			
			break;

		default:
			break;
		}
	}
}
