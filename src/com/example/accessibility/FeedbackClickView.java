package com.example.accessibility;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.view.ViewGroup;

public class FeedbackClickView extends ViewGroup{
	private Paint paint = new Paint();
    //private int width = 0;
    //private int height = 0;
    private int radius;
    //private int heightAvailableForCircle;
    //private int widthAvailableForCircle;
    //private int maxRadius;

    
    public FeedbackClickView (Context context){
    	
    	super (context);
    	
    }
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
	}
	
	public void onDraw(Canvas canvas){
        //paint.setColor(Color.WHITE);
        //setHeightAvailableForCircle(getViewHeight() - barEndY);
        //setWidthAvailableForCircle(getViewWidth() - 40);
        //paint.setColor(Color.BLUE);
        //setMaxRadius(heightAvailableForCircle, widthAvailableForCircle);
        paint.setColor(Color.BLUE);
        //setRadius(whatPercentOfSeekBarIsSelected);
        canvas.drawCircle( (canvas.getWidth())/2, (canvas.getHeight() - 30)/2, radius, paint);
    }
	

	void setRadius(int per){
	
	    this.radius = per;
	}
	/*
	private void setRadius(int total, int current){

        System.out.println("total: "+total);
        System.out.println("current: "+current);
        this.radius = ( ( (getMaxRadius()/2) * current) / 100);
        System.out.println("radius: "+this.radius);
    }
    private void setMaxRadius(int h, int w){

        this.maxRadius = h < w ? h/2 : w/2 ;
    }
    private int getMaxRadius(){

        return this.maxRadius;
    }
    private void setWidth(int w){

        this.width = w;
    }

    private void setHeight(int h){

        this.height = h;
    }

    private int getViewWidth(){

        return this.width;
    }

    private int getViewHeight() {

        return this.height;
    }

    private void setHeightAvailableForCircle(int availableHeightForCircle){

        this.heightAvailableForCircle = availableHeightForCircle;
    }
    private int getAvailableHeightForCircle(){

        return this.heightAvailableForCircle;
    }

    private void setWidthAvailableForCircle(int wid){

        this.widthAvailableForCircle = wid;
    }

    private int getWidthAvailableForCircle(){

        return this.widthAvailableForCircle;
    }
	*/


}

