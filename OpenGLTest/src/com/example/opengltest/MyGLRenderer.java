package com.example.opengltest;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer {

	// Member definitions
	Triangle mTriangle;
	Square   mSquare;

    private final float[] mMVPMatrix        = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix       = new float[16];
    private final float[] mRotationMatrix   = new float[16];
    
    private float mAngle;
	
	@Override
	public void onDrawFrame(GL10 arg0) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
		// Set the camera position (View matrix)
		Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		
		// Combine the view and project matrix
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		
		// Create rotation transformation
		float[] scratch = new float[16];
		long time   = SystemClock.uptimeMillis() % 4000L;
		float angle = 0.090f * ((int) time); // use this instead of mAngle to auto-rotate
		Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);
		
	    // Combine the rotation matrix with the projection and camera view
	    // Note that the mMVPMatrix factor *must be first* in order
	    // for the matrix multiplication product to be correct.
		Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
		
		//printMatrix("Test", mRotationMatrix);
		
		mSquare.draw(mMVPMatrix);
		mTriangle.draw(scratch);
		
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		
		float ratio = (float) width / height;
		
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
		
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		
		// set the Framebuffer clearing color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		// Unless objects plan to change, it's best to initialize them
		// in this method
		mSquare   = new Square();
		mTriangle = new Triangle();
		
	}
	
	 public float getAngle() {
	        return mAngle;
    }


    public void setAngle(float angle) {
        mAngle = angle;
    }
    
    public void printMatrix(String name, float[] matrix) {
    	
    	for(int i = 0; i < matrix.length; i+=4) {
    		Log.d("matrix", matrix[i] + "\t");
    		Log.d("matrix", matrix[i+1] + "\t");
    		Log.d("matrix", matrix[i+2] + "\t");
    		Log.d("matrix", matrix[i+3] + "\t");
    		Log.d("matrix","------------------------\n");
    	}
    }

}
