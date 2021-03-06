/**
 * TouchAccessibitily, an accessibility service that helps people with cerebral palsy to access Android devices. You can only click if you press more than two seconds.
 * 
 * Copyright (C) 2014 Marta Guasch
 * 
 * This file is part of TouchAccessibility.
 * 
 * TouchAccessibility is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * TouchAccessibility is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * See the GNU General Public License for more details. You should have received a copy of the GNU
 * General Public License along with TouchAccessibility. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Authors: TODO
 */


package com.example.accessibility;



import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.RectF;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.ViewGroup;

public class FeedbackClickView extends ViewGroup{
	
    private RectF oval;

    private int posx,posy, deg;
    
    List <AccessibilityNodeInfoCompat> scrollableAreas = new ArrayList <AccessibilityNodeInfoCompat> ();
    
    AccessibilityNodeInfoCompat node=null;
    Rect outBounds = new Rect();
	String mButton;
    private Paint mGreen = new Paint();
    private final Paint mBlack = new Paint();
    private final Paint mYellow = new Paint();
    
    Paint paintBlack = new Paint();
	Paint paintWhite = new Paint();
    
    Path path = new Path();
    Path path1 = new Path();
    Point a = new Point();
	Point b = new Point();
	Point c = new Point();
	Point d = new Point();
	Point e = new Point();
	Point f = new Point();
	Point g = new Point();
	Point h = new Point();
	
    
    public FeedbackClickView (Context context){
    	
    	super (context);
    	
        paintWhite.setColor(Color.WHITE);
        
        paintWhite.setStrokeWidth(4);
        paintWhite.setARGB(128,255,255,255);
        paintWhite.setStyle(Paint.Style.FILL_AND_STROKE);
        paintWhite.setAntiAlias(true);
        
        paintBlack.setStrokeWidth(4);
        paintBlack.setARGB(128,0,0,0);
        paintBlack.setStyle(Paint.Style.FILL_AND_STROKE);
        paintBlack.setAntiAlias(true);
    	
    	mGreen = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGreen.setARGB(128,0,255,0);
        mGreen.setStyle(Style.FILL);
        
        mYellow.setStyle(Style.FILL_AND_STROKE);
        mYellow.setStrokeWidth(3);
        mYellow.setColor(Color.YELLOW);
        
        mBlack.setStyle(Style.FILL_AND_STROKE);
        mBlack.setStrokeWidth(3);
        mBlack.setColor(Color.BLACK);
        
        mButton="clear";
        oval = new RectF(50,50,150,150);
        deg=0;

    }
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
	}
	
	public void onDraw(Canvas canvas){
		
		
        int right =  getWidth() - getPaddingRight();
        int bottom = getHeight() - getPaddingBottom();
        
        //Scroll buttons are drawn in the different scrollable areas.
        scrollableAreas=getScrollableAreas();
        for (int i=0;i<scrollableAreas.size();i++){
	    	if (scrollableAreas.get(i)==null){
	    		Log.w("prints","LV: scrollable es null");
	    		}
	    	
	    	else {
	    		scrollableAreas.get(i).getBoundsInScreen(outBounds);
	    		if((outBounds.right>right-90)&&(outBounds.bottom>bottom-90)){
	    			//If the scroll button is over home button, we draw it in another position.
	    			canvas.drawRect(outBounds.right-80, bottom-180, outBounds.right, bottom-100, paintWhite);
	    			a.set(outBounds.right-50, bottom-130);
		            b.set(outBounds.right-30, bottom-130);
		            c.set(outBounds.right-30, bottom-150);
		            d.set(outBounds.right-10, bottom-140);
		            e.set(outBounds.right-40, bottom-110);
		            f.set(outBounds.right-50, bottom-150);
		            g.set(outBounds.right-40, bottom-170);
		            h.set(outBounds.right-70, bottom-140);
		            path1.moveTo(a.x, a.y);
		            path1.lineTo(f.x, f.y);
		            path1.moveTo(f.x, f.y);
		            path1.lineTo(c.x, c.y);
		            path1.moveTo(c.x, c.y);
		            path1.lineTo(g.x, g.y);
		            path1.moveTo(g.x, g.y);
		            path1.lineTo(f.x, f.y);
		            path1.moveTo(f.x, f.y);
		            path1.lineTo(h.x, h.y);
		            path1.moveTo(h.x, h.y);
		            path1.lineTo(a.x, a.y);
		            path1.moveTo(a.x, a.y);
		            path1.lineTo(b.x, b.y);
		            path1.lineTo(c.x, c.y);
		            path1.lineTo(d.x, d.y);
		            path1.lineTo(b.x, b.y);
		            path1.lineTo(e.x, e.y);
		            path1.lineTo(a.x, a.y);
		            path1.close();
		            canvas.drawPath(path1, paintBlack);
	    		}
	    		else{
	    			canvas.drawRect(outBounds.right-80, outBounds.bottom-80, outBounds.right, outBounds.bottom, paintWhite);
	    			a.set(outBounds.right-50, outBounds.bottom-30);
		            b.set(outBounds.right-30, outBounds.bottom-30);
		            c.set(outBounds.right-30, outBounds.bottom-50);
		            d.set(outBounds.right-10, outBounds.bottom-40);
		            e.set(outBounds.right-40, outBounds.bottom-10);
		            f.set(outBounds.right-50, outBounds.bottom-50);
		            g.set(outBounds.right-40, outBounds.bottom-70);
		            h.set(outBounds.right-70, outBounds.bottom-40);
		            path1.moveTo(a.x, a.y);
		            path1.lineTo(f.x, f.y);
		            path1.moveTo(f.x, f.y);
		            path1.lineTo(c.x, c.y);
		            path1.moveTo(c.x, c.y);
		            path1.lineTo(g.x, g.y);
		            path1.moveTo(g.x, g.y);
		            path1.lineTo(f.x, f.y);
		            path1.moveTo(f.x, f.y);
		            path1.lineTo(h.x, h.y);
		            path1.moveTo(h.x, h.y);
		            path1.lineTo(a.x, a.y);
		            path1.moveTo(a.x, a.y);
		            path1.lineTo(b.x, b.y);
		            path1.lineTo(c.x, c.y);
		            path1.lineTo(d.x, d.y);
		            path1.lineTo(b.x, b.y);
		            path1.lineTo(e.x, e.y);
		            path1.lineTo(a.x, a.y);
		            path1.close();
		            canvas.drawPath(path1, paintBlack);
	    		}
	    		canvas.drawRect(outBounds.left, outBounds.top, outBounds.left+80, outBounds.top+80, paintWhite);
	    		a.set(outBounds.left+30, outBounds.top+50);
	            b.set(outBounds.left+50, outBounds.top+50);
	            c.set(outBounds.left+50, outBounds.top+30);
	            d.set(outBounds.left+70, outBounds.top+40);
	            e.set(outBounds.left+40, outBounds.top+70);
	            f.set(outBounds.left+30, outBounds.top+30);
	            g.set(outBounds.left+40, outBounds.top+10);
	            h.set(outBounds.left+10, outBounds.top+40);
	            path.moveTo(a.x, a.y);
	            path.lineTo(b.x, b.y);
	            path.moveTo(b.x, b.y);
	            path.lineTo(c.x, c.y);
	            path.moveTo(c.x, c.y);
	            path.lineTo(d.x, d.y);
	            path.moveTo(d.x, d.y);
	            path.lineTo(b.x, b.y);
	            path.moveTo(b.x, b.y);
	            path.lineTo(e.x, e.y);
	            path.moveTo(e.x, e.y);
	            path.lineTo(a.x, a.y);
	            path.moveTo(a.x, a.y);
	            path.lineTo(f.x, f.y);
	            path.lineTo(c.x, c.y);
	            path.lineTo(g.x, g.y);
	            path.lineTo(f.x, f.y);
	            path.lineTo(h.x, h.y);
	            path.lineTo(a.x, a.y);
	            path.close();
	            canvas.drawPath(path, paintBlack);
	    		
	    	}
        }
    	
        //Home contexual menu
        if (getMenuContextual().equals("home")){
        	
        	//Two yellow squares that represent menu options
        	canvas.drawRect(right/2-120, bottom/2-260, right/2+120, bottom/2-20, mYellow);
        	canvas.drawRect(right/2-120, bottom/2+20, right/2+120, bottom/2+260, mYellow);
        	
        	//House that represents Home option
        	canvas.drawRect(right/2-90, bottom/2-130, right/2+90, bottom/2-50, mBlack);
        	a.set(right/2-90, bottom/2-130);
            b.set(right/2+90, bottom/2-130);
            c.set(right/2, bottom/2-230);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            path.lineTo(a.x, a.y);
            path.close();
            canvas.drawPath(path, mBlack);
            
            //Arrow that represents click option
        	a.set(right/2-90, bottom/2+50);
            b.set(right/2-75, bottom/2+170);
            c.set(right/2+30, bottom/2+65);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            path.lineTo(a.x, a.y);
            path.close();
            canvas.drawPath(path, mBlack);
            
            a.set(right/2-30, bottom/2+110);
            b.set(right/2+90, bottom/2+230);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.close();
            canvas.drawPath(path, mBlack);	
        }
        
        //Scroll backward contextual menu
        if (getMenuContextual().equals("scrollbackward")){
        	Log.i("prints","scroll backward contextual menu");
        	//Two yellow squares that represent menu options
        	canvas.drawRect(right/2-120, bottom/2-260, right/2+120, bottom/2-20, mYellow);
        	canvas.drawRect(right/2-120, bottom/2+20, right/2+120, bottom/2+260, mYellow);
        	
        	//Picture that represents scroll backward option
    		a.set(right/2-30,bottom/2-110);
            b.set(right/2+30, bottom/2-110);
            c.set(right/2+30, bottom/2-170);
            d.set(right/2+90, bottom/2-140);
            e.set(right/2, bottom/2-50);
            f.set(right/2-30, bottom/2-170);
            g.set(right/2, bottom/2-230);
            h.set(right/2-90, bottom/2-140);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.moveTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            path.moveTo(c.x, c.y);
            path.lineTo(d.x, d.y);
            path.moveTo(d.x, d.y);
            path.lineTo(b.x, b.y);
            path.moveTo(b.x, b.y);
            path.lineTo(e.x, e.y);
            path.moveTo(e.x, e.y);
            path.lineTo(a.x, a.y);
            path.moveTo(a.x, a.y);
            path.lineTo(f.x, f.y);
            path.lineTo(c.x, c.y);
            path.lineTo(g.x, g.y);
            path.lineTo(f.x, f.y);
            path.lineTo(h.x, h.y);
            path.lineTo(a.x, a.y);
            path.close();
            canvas.drawPath(path, paintBlack);
            
            //Arrow that represents click option
        	a.set(right/2-90, bottom/2+50);
            b.set(right/2-75, bottom/2+170);
            c.set(right/2+30, bottom/2+65);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            path.lineTo(a.x, a.y);
            path.close();
            canvas.drawPath(path, mBlack);
            
            a.set(right/2-30, bottom/2+110);
            b.set(right/2+90, bottom/2+230);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.close();
            canvas.drawPath(path, mBlack);	
        	
        }
        if (getMenuContextual().equals("scrollforward")){
        	Log.i("prints","scroll backward contextual menu");
        	
        	//Two yellow squares that represent menu options
        	canvas.drawRect(right/2-120, bottom/2-260, right/2+120, bottom/2-20, mYellow);
        	canvas.drawRect(right/2-120, bottom/2+20, right/2+120, bottom/2+260, mYellow);
        	
        	//Picture that represents scroll backward option
    		a.set(right/2-30,bottom/2-110);
            b.set(right/2+30, bottom/2-110);
            c.set(right/2+30, bottom/2-170);
            d.set(right/2+90, bottom/2-140);
            e.set(right/2, bottom/2-50);
            f.set(right/2-30, bottom/2-170);
            g.set(right/2, bottom/2-230);
            h.set(right/2-90, bottom/2-140);
            path.moveTo(a.x, a.y);
            path.lineTo(f.x, f.y);
            path.moveTo(f.x, f.y);
            path.lineTo(c.x, c.y);
            path.moveTo(c.x, c.y);
            path.lineTo(g.x, g.y);
            path.moveTo(g.x, g.y);
            path.lineTo(f.x, f.y);
            path.moveTo(f.x, f.y);
            path.lineTo(h.x, h.y);
            path.moveTo(h.x, h.y);
            path.lineTo(a.x, a.y);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            path.lineTo(d.x, d.y);
            path.lineTo(b.x, b.y);
            path.lineTo(e.x, e.y);
            path.lineTo(a.x, a.y);
            path.close();
            canvas.drawPath(path, paintBlack);
            
          //Arrow that represents click option
        	a.set(right/2-90, bottom/2+50);
            b.set(right/2-75, bottom/2+170);
            c.set(right/2+30, bottom/2+65);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            path.lineTo(a.x, a.y);
            path.close();
            canvas.drawPath(path, mBlack);
            
            a.set(right/2-30, bottom/2+110);
            b.set(right/2+90, bottom/2+230);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.close();
            canvas.drawPath(path, mBlack);	
        	
        }
        if (getMenuContextual().equals("back")){
        	Log.i("prints","scroll backward contextual menu");
        	
        	//Two yellow squares that represent menu options
        	canvas.drawRect(right/2-120, bottom/2-260, right/2+120, bottom/2-20, mYellow);
        	canvas.drawRect(right/2-120, bottom/2+20, right/2+120, bottom/2+260, mYellow);
        	paintBlack.setStyle(Paint.Style.STROKE);
        	
        	//Arrow that represents back option
        	canvas.drawRect(right/2-30, bottom/2-110, right/2+60, bottom/2-110, paintBlack);
            oval.set(right/2+30, bottom/2-170, right/2+90,bottom/2-110);
            canvas.drawArc(oval, 270, 180, true, paintBlack);
            canvas.drawRect(right/2-90, bottom/2-170, right/2+60, bottom/2-170, paintBlack);		
            a.set(right/2-60, bottom/2-140);
            b.set(right/2-90, bottom/2-170);
            c.set(right/2-60, bottom/2-200);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.moveTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            canvas.drawPath(path, paintBlack);
            
            
          //Arrow that represents click option
        	a.set(right/2-90, bottom/2+50);
            b.set(right/2-75, bottom/2+170);
            c.set(right/2+30, bottom/2+65);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            path.lineTo(a.x, a.y);
            path.close();
            canvas.drawPath(path, mBlack);
            
            a.set(right/2-30, bottom/2+110);
            b.set(right/2+90, bottom/2+230);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.close();
            canvas.drawPath(path, mBlack);	
        	
        }
        
        else if(mButton.equals("clear")){
        	//Log.i("prints","clear");
        }
        
        if (posy<100){
        	oval.set(getX()-50, getY()+20, getX()+50, getY()+120);
        }
        else{
        	oval.set(getX()-50, getY()-120, getX()+50, getY()-20);
        }
		
		canvas.drawArc(oval, 270, getDegr(), true, mGreen);

    }
	
	void setXY(int x, int y){
		posx=x;
		posy=y;
	}
	
	public float getX(){
		return posx;
	}
	public float getY(){
		return posy;
	}
	void setDeg(int degr){
		//feedback=true;
		invalidate();
		deg=degr;
	}
	public int getDegr(){
		return deg;
	}
	public void setMenuContextual(String button){
		Log.i("prints", " el node compn NO es null");
		invalidate();
		mButton=button;
	}
	public String getMenuContextual(){
		return mButton;
	}
	public void setNode(AccessibilityNodeInfoCompat mnode) {
    	Log.i("prints","entrasetNode");
        node = mnode;
    }
  
    public AccessibilityNodeInfoCompat getNode(){
    	return node;
    }
    public void setScrollableAreas (List <AccessibilityNodeInfoCompat> scrollables){
    	invalidate();
    	scrollableAreas.clear();
    	for (int i=0;i<scrollables.size();i++){
	        scrollableAreas.add(scrollables.get(i));
    	}
    }
    public List <AccessibilityNodeInfoCompat> getScrollableAreas (){
    	return scrollableAreas;
    }
   
	
	
}

