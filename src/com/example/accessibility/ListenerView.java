package com.example.accessibility;


import java.util.HashSet;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.ViewGroup;

public class ListenerView extends ViewGroup{
	

	public ListenerView(Context context) {
		super(context);
        paint.setColor(Color.BLACK);
       
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		//super.onLayout(changed, l, t, r, b);

        //getLocationOnScreen(SCREEN_LOCATION);

		
		
	}
	Paint paint = new Paint();
	/*
	public void onDrawNodeBorder(AccessibilityNodeInfoCompat node, int color){
			Log.i("prints","entra al onDrawNodeBorder");
	        //canvas.translate(-SCREEN_LOCATION[0], -SCREEN_LOCATION[1]);
	        //canvas.setMatrix(mMatrix);

	        mPaint.setColor(mHighlightColor);

	        //node.getBoundsInScreen(mTemp);
	        canvas.drawRect(mTemp, mPaint);
	            
	        Log.i("prints","acaba el onDrawNodeBorder");
	

	}*/

    @Override
    public void onDraw(Canvas canvas) {
    	
    	/*
    	final int saveCount = canvas.save();
        canvas.translate(-SCREEN_LOCATION[0], -SCREEN_LOCATION[1]);
        canvas.setMatrix(mMatrix);

        mPaint.setColor(mHighlightColor);

        for (AccessibilityNodeInfoCompat node : mNodes) {
            node.getBoundsInScreen(mTemp);
            canvas.drawRect(mTemp, mPaint);
        }

        canvas.restoreToCount(saveCount);

   
    	super.onDraw(canvas);
        Paint paint = new Paint();*/
    	/*mHighlightColor=getHighlightColor();
    	node=getNode();
    	if (node!=null){Log.i("prints","node no es null");}
    	Log.i("prints","Color: "+mHighlightColor);
    	
    	if((mHighlightColor==Color.YELLOW)&&(node!=null)){
    		Log.i("prints","entra condicio x highlight");
    		onDrawNodeBorder(node,mHighlightColor);
    		
    	}*/
    	
    	
    	/*paint.setColor(android.graphics.Color.BLACK);
        canvas.drawPaint(paint);
        */
        paint.setStrokeWidth(4);
        paint.setColor(android.graphics.Color.RED);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right =  getWidth() - getPaddingRight();
        int bottom = getHeight() - getPaddingBottom();
        
       //canvas.drawLine(left, top, right, bottom, paint);
        
        //Dibuixem el triangle de la dreta (per fer scroll dreta)
        Point aD = new Point(right-80, bottom/2+50);
        Point bD = new Point(right-80, bottom/2-50);
        Point cD = new Point(right, bottom/2);
        
        Path tDret = new Path();
        tDret.setFillType(FillType.EVEN_ODD);
        tDret.moveTo(aD.x, aD.y);
        tDret.lineTo(bD.x, bD.y);
        //tDret.moveTo(bD.x, bD.y);
        tDret.lineTo(cD.x,cD.y);
        //ºtDret.moveTo(cD.x, cD.y);
        tDret.lineTo(aD.x, aD.y);
        tDret.close();
        
        canvas.drawPath(tDret, paint);
        
        
        //Dibujamos el triangulo a la izq , que al pulsar, hará scroll hacia esa dirección
        
        Point aI = new Point(left+80, bottom/2+50);
        Point bI = new Point(left+80, bottom/2-50);
        Point cI = new Point(left, bottom/2);
        
        Path tIzq = new Path();
        tIzq.setFillType(FillType.EVEN_ODD);
        tIzq.moveTo(aI.x, aI.y);
        tIzq.lineTo(bI.x, bI.y);
        tIzq.moveTo(bI.x, bI.y);
        tIzq.lineTo(cI.x,cI.y);
        tIzq.moveTo(cI.x, cI.y);
        tIzq.lineTo(aI.x, aI.y);
        tIzq.close();
        
        canvas.drawPath(tIzq, paint);
        /*
        //Dibujamos el triangulo superior , que al pulsar, hará scroll hacia esa dirección
        
        Point aT = new Point(left+80, bottom/2+50);
        Point bT = new Point(left+80, bottom/2-50);
        Point cT = new Point(left, bottom/2);
        
        Path tTop = new Path();
        tTop.setFillType(FillType.EVEN_ODD);
        tTop.moveTo(aT.x, aT.y);
        tTop.lineTo(bT.x, bT.y);
        tTop.moveTo(bT.x, bT.y);
        tTop.lineTo(cT.x,cT.y);
        tTop.moveTo(cT.x, cT.y);
        tTop.lineTo(aT.x, aT.y);
        tTop.close();
        
        canvas.drawPath(tTop, paint);*/
    }
    /*
    public void setHighlightColor(int color) {
    	Log.i("prints","entra setHighlightColor");
        mHighlightColor = color;
    }
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

}