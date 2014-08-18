package com.app.djapp.utils;

import java.util.Timer;
import java.util.TimerTask;

import com.app.djapp.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimerComp {

	private Timer timer = new Timer();
	private TimerTask task;

	private long sec;
	private Handler handler;
	private View view;

	public long getSec() {
		return sec;
	}
	Context ctx;
	 View viewRecord;
	public TimerComp(Handler handler, Context ctx , View view , View viewRecord) {
		this.handler = handler;
		this.view = view;
		this.viewRecord = viewRecord;
		this.ctx = ctx;
	}

	public long calculateMin(long sec) {
		return sec / 60;
	}

	public long calculateSec(long sec) {
		return sec % 60;
	}

	private void setDisplay() {
		String format = getTimeToString();

		Message msg = new Message();
		msg.obj = format;
		if (handler != null) {
			handler.sendMessage(msg);
		}
		if (view != null) {
			view.post(new Runnable() {

				@Override
				public void run() {
					String format = getTimeToString();
					((TextView) view).setText(format);
					
					if(sec % 2 == 0)
					{
						((Button)viewRecord).setBackgroundResource(R.drawable.stop_btn);
						
					}
					else {
							((Button)viewRecord).setBackgroundResource(R.drawable.stop_btn1);
						
					}
					
				}
			});
		}
	}

	public String getTimeToString() {
		return String.format("%02d:%02d", calculateMin(sec), calculateSec(sec));
	}

	public void resetTimer() {
		stopTimer();
		sec = 0;
		setDisplay();
	 		
	}

	public void startTimer() {
		stopTimer();
		task = new TimerTask() {

			@Override
			public void run() {
				sec++;
				setDisplay();
			}
		};
		timer.schedule(task, 0, 1000);
	}

	public void stopTimer() {
		if (task != null) {
			task.cancel();
			task = null;
			setDisplay();
		}
	}

}
