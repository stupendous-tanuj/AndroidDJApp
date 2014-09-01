package com.app.djapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainWheel extends ImageView {

	private Bitmap imageOriginal, imageScaled; // variables for original and
												// re-sized image
	private Matrix matrix; // Matrix used to perform rotations
	private int wheelHeight, wheelWidth; // height and width of the view

	private Context context;

	private float angle = 0;

	public MainWheel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	// initializations
	private void init(Context context) {
		this.context = context;

		if (matrix == null) {
			matrix = new Matrix();

		}
		// touch events listener
		setWheelImage(R.drawable.image5);
		this.setOnTouchListener(new WheelTouchListener());

	}

	public void setWheelImage(int drawableId) {
		imageOriginal = BitmapFactory.decodeResource(context.getResources(),
				drawableId);
	}

	/*
	 * We need this to get the dimensions of the view. Once we get those, We can
	 * scale the image to make sure it's proper, Initialize the matrix and align
	 * it with the views center.
	 */

	/*public void imageChange(int drawableId) {

		setWheelImage(drawableId);

		wheelHeight = h;
		wheelWidth = w;
		// resize the image
		Matrix resize = new Matrix();
		resize.postScale((float) Math.min(wheelWidth, wheelHeight)
				/ (float) imageOriginal.getWidth(),
				(float) Math.min(wheelWidth, wheelHeight)
						/ (float) imageOriginal.getHeight());

		imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0,
				imageOriginal.getWidth(), imageOriginal.getHeight(), resize,
				false);
		// translate the matrix to the image view's center
		float translateX = wheelWidth / 2 - imageScaled.getWidth() / 2;
		float translateY = wheelHeight / 2 - imageScaled.getHeight() / 2;
		matrix.postTranslate(translateX, translateY);
		MainWheel.this.setImageBitmap(imageScaled);
		MainWheel.this.setImageMatrix(matrix);

	}*/

	int h, w;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		System.out.println("dddddddddd  onSizeChange ");

		this.h = h;
		this.w = w;

		// method called multiple times but initialized just once
		if (wheelHeight == 0 || wheelWidth == 0) {

			wheelHeight = h;
			wheelWidth = w;
			// resize the image
			Matrix resize = new Matrix();
			resize.postScale((float) Math.min(wheelWidth, wheelHeight)
					/ (float) imageOriginal.getWidth(),
					(float) Math.min(wheelWidth, wheelHeight)
							/ (float) imageOriginal.getHeight());

			imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0,
					imageOriginal.getWidth(), imageOriginal.getHeight(),
					resize, false);
			// translate the matrix to the image view's center
			float translateX = wheelWidth / 2 - imageScaled.getWidth() / 2;
			float translateY = wheelHeight / 2 - imageScaled.getHeight() / 2;
			matrix.postTranslate(translateX, translateY);
			MainWheel.this.setImageBitmap(imageScaled);
			MainWheel.this.setImageMatrix(matrix);
		}
	}

	/**
	 * get the angle of a touch event.
	 */
	private double getAngle(double x, double y) {
		x = x - (wheelWidth / 2d);
		y = wheelHeight - y - (wheelHeight / 2d);

		switch (getQuadrant(x, y)) {
		case 1:
			return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
		case 2:
			return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
		case 3:
			return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
		case 4:
			return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
		default:
			return 0;
		}
	}

	/**
	 * get the quadrant of the wheel which contains the touch point (x,y)
	 * 
	 * @return quadrant 1,2,3 or 4
	 */
	private static int getQuadrant(double x, double y) {
		if (x >= 0) {
			return y >= 0 ? 1 : 4;
		} else {
			return y >= 0 ? 2 : 3;
		}
	}

	// listener for touch events on the wheel
	private class WheelTouchListener implements View.OnTouchListener {
		private double startAngle;

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:

				startAngle = getAngle(event.getX(), event.getY());

				if (5 <= startAngle && startAngle <= 45) {

					System.out.println("ddddddddd   play");

					if (MainActivity.mediaPlayer1 != null
							&& !MainActivity.mediaPlayer1.isPlaying()) {

						if (MainActivity.wheelSet1 != null) {
							MainActivity.wheelSet1.start();

						}
						MainActivity.mediaPlayer1.start();
					}

					if (MainActivity.mediaPlayer2 != null
							&& !MainActivity.mediaPlayer2.isPlaying()) {

						if (MainActivity.wheelSet2 != null) {

							MainActivity.wheelSet2.start();
						}
						MainActivity.mediaPlayer2.start();

					}

					if (MainActivity.mPlayerPadLeft != null) {
						for (int i = 0; i < MainActivity.mPlayerPadLeft.length; i++) {
							if (!MainActivity.mPlayerPadLeft[i].isPlaying()) {
								MainActivity.mPlayerPadLeft[i].start();
							}

						}
					}

					// imageChange(R.drawable.image5);

				} else if (50 <= startAngle && startAngle < 85) {

					// imageChange(R.drawable.image5_5);
					System.out.println("ddddddddd   stop");
					if (MainActivity.mediaPlayer1 != null) {
						MainActivity.mediaPlayer1.release();
						MainActivity.mediaPlayer1 = null;
					}
					if (MainActivity.mediaPlayer2 != null) {
						MainActivity.mediaPlayer2.release();
						MainActivity.mediaPlayer2 = null;
					}

					if (MainActivity.mPlayerPadLeft != null) {
						for (int i = 0; i < MainActivity.mPlayerPadLeft.length; i++) {

							MainActivity.mPlayerPadLeft[i].release();

						}

					}

				} else if (275 <= startAngle && startAngle < 310) {

					// imageChange(R.drawable.image5_5);
					System.out.println("ddddddddd   record");

				} else if (315 <= startAngle && startAngle < 355) {

					// imageChange(R.drawable.image5_1);

					if (MainActivity.mediaPlayer1 != null
							&& MainActivity.mediaPlayer1.isPlaying()) {

						if (MainActivity.wheelSet1 != null) {
							MainActivity.wheelSet1.cancel();

						}

						MainActivity.mediaPlayer1.pause();

					}

					if (MainActivity.mediaPlayer2 != null
							&& MainActivity.mediaPlayer2.isPlaying()) {

						if (MainActivity.wheelSet2 != null) {

							MainActivity.wheelSet2.cancel();
						}
						MainActivity.mediaPlayer2.pause();

					}

					if (MainActivity.mPlayerPadLeft != null) {
						for (int i = 0; i < MainActivity.mPlayerPadLeft.length; i++) {
							if (MainActivity.mPlayerPadLeft[i].isPlaying()) {
								MainActivity.mPlayerPadLeft[i].pause();
							}

						}
					}

					System.out.println("ddddddddd   pause");
				}

				break;

			}

			return true;
		}

	}

}