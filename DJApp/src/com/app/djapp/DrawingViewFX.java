package com.app.djapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawingViewFX extends View {

	public int width;
	public int height;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;
	Activity context;
	private Paint circlePaint;
	private Path circlePath;
	private int x = 0, y = 0;
	int w, h;

	public DrawingViewFX(Activity c, int w, int h) {
		super(c);
		context = c;
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		circlePaint = new Paint();
		circlePath = new Path();
		circlePaint.setAntiAlias(true);
		circlePaint.setColor(Color.BLUE);
		circlePaint.setStyle(Paint.Style.STROKE);
		circlePaint.setStrokeJoin(Paint.Join.MITER);
		circlePaint.setStrokeWidth(4f);
		this.w = w;
		this.h = h;
		Log.v("H?W", "" + h);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		mBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn);
		mCanvas = new Canvas();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawBitmap(mBitmap, x, y, mBitmapPaint);

	}

	private float mX, mY;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		 int bmp_w = mBitmap.getWidth();
		int bmp_h = mBitmap.getHeight();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:

			x = (int) event.getX();
			y = (int) event.getY();
			if ((y < h - bmp_h) && (y > 0) && ((x > 0 && x < w - bmp_w))) {
				invalidate();
				 ((SecondActivity_FX)context).ontouch();
			}
			break; 

		 }
		return true;
	}
}