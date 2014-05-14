package com.example.accessibility;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.Point;
import android.view.ViewGroup;

public class ListenerView extends ViewGroup{
    Path triangle= new Path();
    Path tLeft= new Path();
    Path tTop= new Path();
    Path tBottom= new Path();
    
    Paint paintBlack = new Paint();
	Paint paintWhite = new Paint();
	
	Point a = new Point();
	Point b = new Point();
	Point c = new Point();
    
	public ListenerView(Context context) {
		super(context);
        paintBlack.setColor(Color.BLACK);
        paintWhite.setColor(Color.WHITE);
       
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		//super.onLayout(changed, l, t, r, b);

        //getLocationOnScreen(SCREEN_LOCATION);

		
		
	}
	
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
        paintWhite.setStrokeWidth(4);
        paintWhite.setARGB(128,255,255,255);
        paintWhite.setStyle(Paint.Style.FILL_AND_STROKE);
        paintWhite.setAntiAlias(true);
        
        paintBlack.setStrokeWidth(4);
        paintBlack.setARGB(128,0,0,0);
        paintBlack.setStyle(Paint.Style.FILL_AND_STROKE);
        paintBlack.setAntiAlias(true);
        
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right =  getWidth() - getPaddingRight();
        int bottom = getHeight() - getPaddingBottom();
        
        
       //canvas.drawLine(left, top, right, bottom, paint);
        
        //-----------------------------------------------------------------------------//
        //Drawing general buttons
        canvas.drawRect(right-80, bottom-90, right, bottom-10, paintWhite);
        canvas.drawRect(right-70, bottom-55, right-10, bottom-20, paintBlack);
        
        a.set(right-70, bottom-55);
        b.set(right-10, bottom-55);
        c.set(right-40, bottom-80);
        tLeft.moveTo(a.x, a.y);
        tLeft.lineTo(b.x, b.y);
        tLeft.lineTo(c.x, c.y);
        tLeft.lineTo(a.x, a.y);
        tLeft.close();
        canvas.drawPath(tLeft, paintBlack);
        
        
        //-----------------------------------------------------------------------------//
        //Drawing scroll buttons
        
        //Drawing the squares
        canvas.drawRect(right-80, bottom/2+80, right, bottom/2, paintWhite);
        canvas.drawRect(left, bottom/2+80, left+80, bottom/2, paintWhite);
        canvas.drawRect(right/2-40, bottom-90, right/2+40, bottom-10, paintWhite);
        canvas.drawRect(right/2-40, top+30, right/2+40, top+110, paintWhite);
        
        //Drawing triangle (right arrow)
        a.set(right-70, bottom/2+70);
        b.set(right-70, bottom/2+10);
        c.set(right-10, bottom/2+40);
        //tRight.setStyle(Paint.Style.FILL_AND_STROKE);
        triangle.moveTo(a.x, a.y);
        triangle.lineTo(b.x, b.y);
        //tRight.moveTo(bL.x, bL.y);
        triangle.lineTo(c.x,c.y);
        //tRight.moveTo(cL.x,cL.y);
        triangle.lineTo(a.x, a.y);
        triangle.close();
        canvas.drawPath(triangle, paintBlack);
        
        
        //Drawing triangle (left arrow)
        a.set(left+70, bottom/2+70);
        b.set(left+70, bottom/2+10);
        c.set(left+10, bottom/2+40);
        //tLeft.setFillType(FillType.EVEN_ODD);
        tLeft.moveTo(a.x, a.y);
        tLeft.lineTo(b.x, b.y);
        //tLeft.moveTo(bR.x, bR.y);
        tLeft.lineTo(c.x, c.y);
        //tLeft.moveTo(cR.x, cR.y);
        tLeft.lineTo(a.x, a.y);
        tLeft.close();
        canvas.drawPath(tLeft, paintBlack);
        
        //Drawing triangle (top arrow)
        a.set(right/2-30, top+100);
        b.set(right/2, top+40);
        c.set(right/2+30, top+100);
        //tRight.setFillType(FillType.EVEN_ODD);
        triangle.moveTo(a.x, a.y);
        triangle.lineTo(b.x, b.y);
        //tRight.moveTo(bT.x, b.y);
        triangle.lineTo(c.x,c.y);
        //tRight.moveTo(cT.x,cT.y);
        triangle.lineTo(a.x, a.y);
        triangle.close();
        canvas.drawPath(triangle, paintBlack);
        
        //Drawing triangle (bottom arrow)
        a.set(right/2-30, bottom-80);
        b.set(right/2, bottom-20);
        c.set(right/2+30, bottom-80);
        //tLeft.setFillType(FillType.EVEN_ODD);
        tLeft.moveTo(a.x, a.y);
        tLeft.lineTo(b.x, b.y);
        //tLeft.moveTo(bB.x, bB.y);
        tLeft.lineTo(c.x, c.y);
        //tLeft.moveTo(cB.x, cB.y);
        tLeft.lineTo(a.x, a.y);
        tLeft.close();
        canvas.drawPath(tLeft, paintBlack);
        
        /*
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