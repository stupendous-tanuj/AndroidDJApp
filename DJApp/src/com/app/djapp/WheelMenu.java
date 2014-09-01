package com.app.djapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class WheelMenu extends ImageView {

	private Bitmap imageOriginal, imageScaled; // variables for original and
												// re-sized image
	private Matrix matrix; // Matrix used to perform rotations
	private int wheelHeight, wheelWidth; // height and width of the view

	float vo = 0.5f;
	float vor = 0.5f;

	// wheel to the center of a div or not
	private Context context;

	private float angle = 0;

	public WheelMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		setVolume(vo, vor, "");
	}

	private void setVolume(float l1, float l2, String knobMoveString) {

		if (knobMoveString.equals("1")) {
			if (MainActivity.mediaPlayer1 != null)
				MainActivity.mediaPlayer1.setVolume(l1, l2);
			System.out.println("ddddddddd first ");
		}

		if (knobMoveString.equals("2")) {
			if (MainActivity.mediaPlayer2 != null)
				MainActivity.mediaPlayer2.setVolume(l1, l2);
			System.out.println("dddddddddddd second ");
		}

		if (knobMoveString.equals("3")) {
			if (MainActivity.mPlayerPadLeft != null) {
				for (int i = 0; i < MainActivity.mPlayerPadLeft.length; i++) {
					MainActivity.mPlayerPadLeft[i].setVolume(l1, l2);
				}
			}
		}

		if (knobMoveString.equals("5")) {

			if (MainActivity.mediaPlayer1 != null)
				MainActivity.mediaPlayer1.setVolume(l1, l2);

			if (MainActivity.mediaPlayer2 != null)
				MainActivity.mediaPlayer2.setVolume(l1, l2);

			if (MainActivity.mPlayerPadLeft != null) {
				for (int i = 0; i < MainActivity.mPlayerPadLeft.length; i++) {
					MainActivity.mPlayerPadLeft[i].setVolume(l1, l2);
				}
			}
		}

	}

	// initializations
	private void init(Context context) {
		this.context = context;

		if (matrix == null) {
			matrix = new Matrix();

		}
		// touch events listener

		setWheelImage(R.drawable.image8);
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
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
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
			WheelMenu.this.setImageBitmap(imageScaled);
			WheelMenu.this.setImageMatrix(matrix);
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
			if (v.getId() == R.id.iv_mx_knob_1) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					// get the total angle rotated in 360 degrees
					startAngle = getAngle(event.getX(), event.getY());

					if (0 <= startAngle && startAngle <= 90) { // for right

						if (angle != 180) {
							anim(60f, true, "1");
						}
					} else if (90 <= startAngle && startAngle < 180) { // for
																		// left
						System.out.println("aaaaaaaaa left move ");
						if (angle != -180) {
							anim(-60f, false, "1");
						}
					}
					break;
				}
			}
			if (v.getId() == R.id.iv_mx_knob_2) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					// get the total angle rotated in 360 degrees
					startAngle = getAngle(event.getX(), event.getY());

					if (0 <= startAngle && startAngle <= 90) { // for right

						if (angle != 180) {
							anim(60f, true, "2");
						}
					} else if (90 <= startAngle && startAngle < 180) { // for
																		// left
						System.out.println("aaaaaaaaa left move ");
						if (angle != -180) {
							anim(-60f, false, "2");
						}
					}
					break;
				}
			}
			if (v.getId() == R.id.iv_mx_knob_3) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					// get the total angle rotated in 360 degrees
					startAngle = getAngle(event.getX(), event.getY());

					if (0 <= startAngle && startAngle <= 90) { // for right

						if (angle != 180) {
							anim(60f, true, "3");
						}
					} else if (90 <= startAngle && startAngle < 180) { // for
																		// left
						System.out.println("aaaaaaaaa left move ");
						if (angle != -180) {
							anim(-60f, false, "3");
						}
					}
					break;
				}
			}
			if (v.getId() == R.id.iv_mx_knob_4) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					// get the total angle rotated in 360 degrees
					startAngle = getAngle(event.getX(), event.getY());

					if (0 <= startAngle && startAngle <= 90) { // for right

						if (angle != 180) {
							anim(60f, true, "4");
						}
					} else if (90 <= startAngle && startAngle < 180) { // for
																		// left
						System.out.println("aaaaaaaaa left move ");
						if (angle != -180) {
							anim(-60f, false, "4");
						}
					}
					break;
				}
			}

			if (v.getId() == R.id.iv_mx_knob_5) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					// get the total angle rotated in 360 degrees
					startAngle = getAngle(event.getX(), event.getY());

					if (0 <= startAngle && startAngle <= 90) { // for right

						if (angle != 180) {
							anim(60f, true, "5");
						}
					} else if (90 <= startAngle && startAngle < 180) { // for
																		// left
						System.out.println("aaaaaaaaa left move ");
						if (angle != -180) {
							anim(-60f, false, "5");
						}
					}
					break;
				}
			}

			return true;
		}

	}

	@SuppressLint("NewApi")
	private void anim(final double ang, final boolean ffff, final String s) {

		RotateAnimation rAnim = new RotateAnimation(angle, angle + (float) ang,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rAnim.setDuration(400);
		rAnim.setFillAfter(true);
		rAnim.setFillEnabled(true);
		this.startAnimation(rAnim);
		rAnim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

				angle = angle + (float) ang;

				if (ffff)
					leftDC_And_RightIN(angle, s);
				else
					leftIN_And_RightDC(angle, s);

			}

			private void leftDC_And_RightIN(float angle, String s) {
				// TODO Auto-generated method stub

				if (angle == 0) // equal volume
				{
					vo = 0.5f;
					vor = 0.5f;
					setVolume(vo, vor, s);
				}
				if (angle != 0) {
					vo = vo - 0.2f;
					if (vo < 0) {
						vo = 0.0f;
					}
					vor = vor + 0.2f;
					if (vor > 1) {
						vor = 1.0f;
					}
					setVolume(vo, vor, s);
				}
				if (angle == 180) {
					vo = 0.0f;
					vor = 0.5f;
					setVolume(0.0f, 1.0f, s);
				}

			}

			private void leftIN_And_RightDC(float angle, String s) {

				if (angle == 0) // equal volume
				{
					vo = 0.5f;
					vor = 0.5f;
					setVolume(vo, vor, s);
				}
				if (angle != 0) {
					vor = vor - 0.2f;
					if (vor < 0) {
						vor = 0.0f;
					}
					vo = vo + 0.2f;
					if (vo > 1) {
						vo = 1.0f;
					}
					setVolume(vo, vor, s);
				}

				if (angle == -180) {
					vo = 1.0f;
					vor = 0.0f;
					setVolume(1.0f, 0.0f, s);
				}

			}
		});
	}
}