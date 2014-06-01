package com.example.accessibility;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Paint.Join;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.RectF;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.ViewGroup;

public class FeedbackClickView extends ViewGroup{
	private Paint paint = new Paint();
    private RectF oval;

    private int posx,posy, deg;
    
AccessibilityNodeInfoCompat node=null;
	boolean nodeNull;
	String mButton;
	private final Rect mTemp = new Rect();
    private final Paint mPaint = new Paint();
    private final Paint mGreen = new Paint();
    private final Paint mBlack = new Paint();
    
    Path path = new Path();
    Point a = new Point();
	Point b = new Point();
	Point c = new Point();
    //private int heightAvailableForCircle;
    //private int widthAvailableForCircle;
    //private int maxRadius;

    
    public FeedbackClickView (Context context){
    	
    	super (context);
    	paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setARGB(128,0,255,0);
        paint.setStyle(Style.FILL);
        
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeJoin(Join.ROUND);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.RED);
        
        mGreen.setStyle(Style.FILL_AND_STROKE);
        mGreen.setStrokeWidth(3);
        mGreen.setColor(Color.GREEN);
        
        mBlack.setStyle(Style.FILL_AND_STROKE);
        mBlack.setStrokeWidth(3);
        mBlack.setColor(Color.BLACK);
        
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        
        mButton="clear";
        oval = new RectF(50,50,150,150);
        		//(posx+50, posy+50, posx+150, posy+150);
        deg=0;

    }
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
	}
	
	public void onDraw(Canvas canvas){
		//Log.i("prints","onDraw Feedback");
		
		//int left = getPaddingLeft();
        int top = getPaddingTop();
        int right =  getWidth() - getPaddingRight();
        int bottom = getHeight() - getPaddingBottom();
        
        if (posy<100){
        	oval.set(getX()-50, getY()+20, getX()+50, getY()+120);
        }
        else{
        	oval.set(getX()-50, getY()-120, getX()+50, getY()-20);
        }
		
        canvas.drawArc(oval, 270, getDegr(), true, paint);
        
        if ((node!=null)&&(!nodeNull)){
        	node.getBoundsInScreen(mTemp);
        	canvas.drawRect(mTemp, mPaint);
        }
        if (mButton.equals("home")){
        	//Log.i("prints","Boton de home, feedbackclickview");
        	canvas.drawRect(right-250, bottom-170, right-90, bottom-10, mGreen);
        	canvas.drawRect(right-250, bottom-180, right-90, bottom-340, mGreen);
        	canvas.drawRect(right-230, bottom-200, right-110, bottom-270, mBlack);
        	a.set(right-230, bottom-270);
            b.set(right-110, bottom-270);
            c.set(right-170, bottom-320);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            path.lineTo(a.x, a.y);
            path.close();
            canvas.drawPath(path, mBlack);
            
            a.set(right-230, bottom-150);
            b.set(right-220, bottom-70);
            c.set(right-150, bottom-140);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            path.lineTo(a.x, a.y);
            path.close();
            canvas.drawPath(path, mBlack);
            
            a.set(right-230, bottom-150);
            b.set(right-110, bottom-30);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.close();
            canvas.drawPath(path, mBlack);
        }
        else if(mButton.equals("clear")){
        	//Log.i("prints","clear");
        }
        
        invalidate();
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
		deg=degr;
	}
	public int getDegr(){
		return deg;
	}
	
	public void setNode(AccessibilityNodeInfoCompat compn){
		Log.i("prints"," es fa un set node");
		node=compn;
	}
	public void setNodeNull(boolean esNull){
		if(esNull) Log.i("prints","El node compn es null");
		else Log.i("prints", " el node compn NO es null");
		nodeNull=esNull;
	}
	public void setMenuContextual(String button){
		Log.i("prints", " el node compn NO es null");
		mButton=button;
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

