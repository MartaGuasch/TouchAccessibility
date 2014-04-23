package com.example.accessibility;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
	
	}
	Paint paint = new Paint();

    @Override
    public void onDraw(Canvas canvas) {
    	Log.i("prints","entra onDraw ListenerView");
            canvas.drawLine(0, 0, 200, 200, paint);
            canvas.drawLine(200, 0, 0, 200, paint);
    }

}