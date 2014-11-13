package com.example.opengltest;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {

	
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	
	private final MyGLRenderer mRenderer;
	private float mPreviousX;
	private float mPreviousY;
	
	// Constructor
	public MyGLSurfaceView (Context context) {
		super(context);
	
		// Create an OpenGL ES 2.0 context
		setEGLContextClientVersion(2);	
		
		// Set the Renderer for drawing on the GLSurfaceView
		mRenderer = new MyGLRenderer();
		setRenderer(mRenderer);
		
		//OPTIONAL
		// Render the view only when there is a change in the drawing data
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		setDebugFlags(DEBUG_LOG_GL_CALLS);
	}
	
	@Override
	public boolean onTouchEvent (MotionEvent e) {
		
	    // MotionEvent reports input details from the touch screen
	    // and other input controls. In this case, you are only
	    // interested in events where the touch position changed.
		
		float x = e.getX();
		float y = e.getY();
		
		switch (e.getAction()) {
		
			case MotionEvent.ACTION_MOVE:
				
				float dx = x - mPreviousX;
				float dy = y - mPreviousY;
				
				if (y > getHeight() / 2) {
					dx = dx * -1;
				}
				if (x < getWidth() / 2) {
					dy = dy * -1;
				}
				
				mRenderer.setAngle(mRenderer.getAngle() +((dx + dy) * TOUCH_SCALE_FACTOR));
				requestRender(); // force redraw
		}
		mPreviousX = x;
	    mPreviousY = y;
		return true;
	}
}
