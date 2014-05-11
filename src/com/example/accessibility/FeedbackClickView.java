package com.example.accessibility;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import android.view.ViewGroup;

public class FeedbackClickView extends ViewGroup{
	private Paint paint = new Paint();
    private RectF oval;

    private int posx,posy;
    //private int heightAvailableForCircle;
    //private int widthAvailableForCircle;
    //private int maxRadius;

    
    public FeedbackClickView (Context context){
    	
    	super (context);
    	paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Style.FILL);
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        oval = new RectF(posx+50, posy+50, posx+150, posy+150);

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
        //paint.setColor(Color.BLUE);
        //setRadius(whatPercentOfSeekBarIsSelected);
		//oval = new RectF(posx+50, posy+50, posx+150, posy+150);
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //drawable.draw(canvas);
        canvas.drawArc(oval, 30, 90, true, paint);
        //canvas.drawCircle( (canvas.getWidth())/2, (canvas.getHeight() - 30)/2, radius, paint);
    }
	
	void setXY(int x, int y){
		posx=x;
		posy=y;
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

