package edu.purdue.sigapp.picto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawingSurface extends SurfaceView implements SurfaceHolder.Callback {
	  //private Boolean _run;
	  //protected DrawThread thread;

	    private List<DrawingPoint> mDrawingPoints;

	  public DrawingSurface(Context context, AttributeSet attrs) {
		  super(context, attrs);
	      mDrawingPoints = Collections.synchronizedList(new ArrayList<DrawingPoint>());
		 // getHolder().addCallback(this);
		 // thread = new DrawThread(getHolder());
	  }

	  public void addDrawingPath (DrawingPoint drawingPoint){
	    //thread.addDrawingPoint(drawingPoint);
	    mDrawingPoints.add( drawingPoint );
	  }
	  
	  public void clearDrawingPath() {
		  mDrawingPoints.clear();
	  }

	  /*class DrawThread extends  Thread{
	    private SurfaceHolder mSurfaceHolder;
	    public DrawThread(SurfaceHolder surfaceHolder){
	      mSurfaceHolder = surfaceHolder;
	      mDrawingPoints = Collections.synchronizedList(new ArrayList<DrawingPoint>());
	    }


	    public void addDrawingPoint(DrawingPoint drawingPoint){
	      mDrawingPoints.add( drawingPoint );
	    }

	    @Override
	    public void run() {
	      Canvas canvas = null;
	      while (_run){
	        try{
	          canvas = mSurfaceHolder.lockCanvas(null);
	          DrawingPoint prev = null;
	          synchronized(mDrawingPoints) {
	            Iterator i = mDrawingPoints.iterator();
	            while (i.hasNext()){
	            	DrawingPoint next = (DrawingPoint) i.next();
	            	if (next.newPath) {
	            		prev = next;
	            		continue;
	            	}
	            	else {
	  	              final DrawingPoint drawingPoint = (DrawingPoint) i.next();
	  	              canvas.drawLine(prev.point.x,
	  	            		  prev.point.y,
	  	            		  drawingPoint.point.x,
	  	            		  drawingPoint.point.y,
	  	            		  prev.paint);
	  	              prev = drawingPoint;
	            	}
	            }
	          }
	        } finally {
	          mSurfaceHolder.unlockCanvasAndPost(canvas);
	        }
	      }
	    }
	  }
	  
	*/

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
        DrawingPoint prev = null;
		
		Iterator i = mDrawingPoints.iterator();
        while (i.hasNext()){
        	DrawingPoint next = (DrawingPoint) i.next();
        	if (next.newPath) {
        		prev = next;
        		continue;
        	}
        	else {
	              final DrawingPoint drawingPoint = next;
	              canvas.drawLine(prev.point.x,
	            		  prev.point.y,
	            		  drawingPoint.point.x,
	            		  drawingPoint.point.y,
	            		  prev.paint);
	              prev = drawingPoint;
        	}
        }
		
		
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// _run = (true);
        //thread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		/*boolean retry = true;
        _run = (false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }*/
	}
}