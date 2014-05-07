package com.example.accessibility;

import java.util.HashSet;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.ViewGroup;

public class FeedbackClickView extends ViewGroup{
	private final int[] SCREEN_LOCATION = new int[2];

    private final Rect mTemp = new Rect();
    private final Paint mPaint = new Paint();
    private final HashSet<AccessibilityNodeInfoCompat> mNodes = new HashSet<AccessibilityNodeInfoCompat>();
    private final Matrix mMatrix = new Matrix();
    Canvas canvas;

    private int mHighlightColor;
    private AccessibilityNodeInfoCompat node;
    
    public FeedbackClickView (Context context){
    	super (context);
    	mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeJoin(Join.ROUND);
        mPaint.setStrokeWidth(3);

        mHighlightColor = Color.RED;
    }


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

        getLocationOnScreen(SCREEN_LOCATION);
	}
	
	public void onDraw(Canvas canvas){
		Log.i("prints","entra onDraw FeedbackClickView");
			final int saveCount = canvas.save();
		    canvas.translate(-SCREEN_LOCATION[0], -SCREEN_LOCATION[1]);
		    canvas.setMatrix(mMatrix);
		
		    mPaint.setColor(mHighlightColor);
		    if (mNodes==null){Log.i("prints","nNodes es null");}
		   for (AccessibilityNodeInfoCompat node : mNodes) {
			    node.getBoundsInScreen(mTemp);
			    Log.i("prints","Drowing rect");
			    canvas.drawRect(mTemp, mPaint);
		    }
		
		    canvas.restoreToCount(saveCount);
		    super.onDraw(canvas);
	}
	
	  /**
     * Sets the color of the highlighted bounds.
     *
     * @param color
     */
    public void setHighlightColor(int color) {
        mHighlightColor = color;
    }
    /*
    public void setNode(AccessibilityNodeInfoCompat mnode) {
    	Log.i("prints","entrasetNode");
        node = mnode;
    }
    public int getHighlightColor(){
    	return mHighlightColor;
    }
    public AccessibilityNodeInfoCompat getNode(){
    	return node;
    }
    */
  
	private boolean isValidNode(AccessibilityNodeInfoCompat node) {
	    final AccessibilityNodeInfoCompat parent = node.getParent();
	
	    if (parent != null) {
	        parent.recycle();
	        return true;
	    }
	
	    final int childCount = node.getChildCount();
	
	    for (int i = 0; i < childCount; i++) {
	        final AccessibilityNodeInfoCompat child = node.getChild(i);
	
	        if (child != null) {
	            child.recycle();
	            return true;
	        }
	    }
	
	    return false;
	}
	
    public void clear() {
        for (AccessibilityNodeInfoCompat node : mNodes) {
            node.recycle();
        }

        mNodes.clear();
    }

    /**
     * Sets the highlighted bounds to those of the specified node.
     *
     * @param node The node to highlight.
     */
 
    public void add(AccessibilityNodeInfoCompat node) {
        if (node == null) {
            return;
        }

        final AccessibilityNodeInfoCompat clone = AccessibilityNodeInfoCompat.obtain(node);

        mNodes.add(clone);
    }

    /**
     * Removes nodes that are no longer accessible.
     */

    public void removeInvalidNodes() {
        final Iterator<AccessibilityNodeInfoCompat> iterator = mNodes.iterator();

        while (iterator.hasNext()) {
            final AccessibilityNodeInfoCompat node = iterator.next();

            if (!isValidNode(node)) {
                iterator.remove();
                node.recycle();
            }
        }
    }



}
