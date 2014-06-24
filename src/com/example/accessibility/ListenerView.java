package com.example.accessibility;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.ViewGroup;

public class ListenerView extends ViewGroup{
    Path triangle= new Path();
    Path path =new Path();
    
    Paint paintBlack = new Paint();
	Paint paintWhite = new Paint();
	
	Point a = new Point();
	Point b = new Point();
	Point c = new Point();
	RectF oval;
	
	AccessibilityNodeInfoCompat node;
	
	public ListenerView(Context context) {
		super(context);
        paintBlack.setColor(Color.BLACK);
        paintWhite.setColor(Color.WHITE);
        oval = new RectF (50, 40,70,-60);
        
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
	}

    @Override
    public void onDraw(Canvas canvas) {
    	
    	paintWhite.setStrokeWidth(4);
        paintWhite.setARGB(128,255,255,255);
        paintWhite.setStyle(Paint.Style.FILL_AND_STROKE);
        paintWhite.setAntiAlias(true);
        
        paintBlack.setStrokeWidth(4);
        paintBlack.setARGB(128,0,0,0);
        paintBlack.setStyle(Paint.Style.FILL_AND_STROKE);
        paintBlack.setAntiAlias(true);
        
        int left = getPaddingLeft();
        //int top = getPaddingTop();
        int right =  getWidth() - getPaddingRight();
        int bottom = getHeight() - getPaddingBottom();
        
    	
        
        //-----------------------------------------------------------------------------//
        //Drawing general buttons
        canvas.drawRect(right-80, bottom-90, right, bottom-10, paintWhite);
        canvas.drawRect(right-70, bottom-55, right-10, bottom-20, paintBlack);
        canvas.drawRect(left, bottom-90, left+80, bottom-10, paintWhite);
        
        a.set(right-70, bottom-55);
        b.set(right-10, bottom-55);
        c.set(right-40, bottom-80);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();
        canvas.drawPath(path, paintBlack);
        
        paintBlack.setStyle(Paint.Style.STROKE);
        canvas.drawRect(left+30, bottom-40, left+60, bottom-40, paintBlack);
        oval.set(left+50, bottom-60, left+70,bottom-40);
        canvas.drawArc(oval, 270, 180, true, paintBlack);
        canvas.drawRect(left+10, bottom-60, left+60, bottom-60, paintBlack);
        //canvas.drawRect(left+10, bottom-70, left+20, bottom-60, paintBlack);
        //canvas.drawRect(left+10, bottom-60, left+20, bottom-50, paintBlack);			
        a.set(left+20, bottom-50);
        b.set(left+10, bottom-60);
        c.set(left+20, bottom-70);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.moveTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        canvas.drawPath(path, paintBlack);
        paintBlack.setStyle(Paint.Style.FILL_AND_STROKE);
        
       
    }
   

}