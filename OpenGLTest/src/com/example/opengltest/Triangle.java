package com.example.opengltest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class Triangle {

	static final int  COORDS_PER_VERTEX = 3;
	private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
	private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

	
	private final int mProgram;
	private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
	
	static float triangleCoords[] = {  // in counterclockwise order:
        0.0f,  0.622008459f, 0.0f,     // top
       -0.5f, -0.311004243f, 0.0f,     // bottom left
        0.5f, -0.311004243f, 0.0f      // bottom right
	};
    
	
	float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f }; //RGBA
	
	private FloatBuffer vertexBuffer;
	
	//*******************************************
	// Default Constructor 
	//*******************************************
	public Triangle() {
		
		// load the shaders here. This is an expensive process so do it
		// in the constructor of the object to be drawn so it happens
		// only once.
		int vertexShader   = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
		
		mProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(mProgram, vertexShader);
		GLES20.glAttachShader(mProgram, fragmentShader);
		GLES20.glLinkProgram(mProgram);
		
		// initialize vertex byte buffer for shape coordinates
		// (number of coordinate values * 4 bytes per float)
		ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
		
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());
		
		// create a floating point buffer from the ByteBuffer
		vertexBuffer = bb.asFloatBuffer();
		
		// add the coordinates to the FloatBuffer
		vertexBuffer.put(triangleCoords);
		
		// set the buffer to read the first coordinate
		vertexBuffer.position(0);
		
	}
	//*******************************************
	// Draw()
	//*******************************************
	public void draw(float[] mvpMatrix) { // pass in the calculated transformation matrix
		
		// Add program to OpenGL ES environment
		GLES20.glUseProgram(mProgram);
		
		// get handle to vertex shader's vPosition member
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		
		// Enable a handle to the triangle vertices
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		
		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, vertexBuffer);
		
		// get handle to fragment shader's vColor member
	    mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
	    
	    // get handle to shape's transformation matrix
	    mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	    
	    // Pass the projection and view transformation to the shader
	    GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

	    // Set color for drawing the triangle
	    GLES20.glUniform4fv(mColorHandle, 1, color, 0);

	    // Draw the triangle
	    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

	    // Disable vertex array
	    GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
	
	//*******************************************
	// loadShader()
	//*******************************************
	public static int loadShader(int type, String shaderCode) {
		
		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
	    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);
		
		// add shader source and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		
		return shader;
	}
	
	//*******************************************
	// Vertex Shader Code
	//*******************************************
	private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            // the matrix must be included as a modifier of gl_Position
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";

	//*******************************************
	// Fragment Shader Code
	//*******************************************
	private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";
}
