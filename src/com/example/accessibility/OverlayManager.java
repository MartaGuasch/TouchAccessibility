package com.example.accessibility;



import android.app.Instrumentation;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

public class OverlayManager extends Service implements OnTouchListener 
{
	
	private ListenerView LV;
	private OnTouchListener OTL;
	private long tsTempDown;
	private Context mContext;
	
	public void onCreate() {
		super.onCreate();
	}
	
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public boolean onUnbind(Intent intent)
	{
		destroyOverlayView(mContext);
		return super.onUnbind(intent);
	}

	/*public void onDestroy()
	{
		super.onDestroy();
		destroyOverlayView();
	}*/

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.i("prints","entra onTouch del Overlay");
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			//tsTempDown = new Timestamp(System.currentTimeMillis()).getTime();
			tsTempDown = event.getDownTime();
		}
			//Toast.makeText(v.getContext(), x+" "+y, Toast.LENGTH_SHORT).show();
		else if(event.getAction()==MotionEvent.ACTION_UP){
			//long tsTempUp = new Timestamp(System.currentTimeMillis()).getTime();
			long tsTempUp = event.getEventTime();
			tsTempUp=tsTempUp-tsTempDown;
			Float x= event.getX();
			Float y= event.getY();
			if (tsTempUp>2000)
			{
				destroyOverlayView(mContext);
				click(x,y);
			}
		}
			
            //textView.setText("Touch coordinates : " +String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
        
        return true;
    }
	
	
	View getListenerView() {
		return LV;
	}
 
	void setOnTouchListener(OnTouchListener listener) {
		OTL = listener;
 }

 
	void createOverlayView(Context context) {
		mContext=context;
		// ////////////////////////////////////////////////////////////////////////////////
        // ListenerView
        LayoutParams listenerParams = new LayoutParams();
        listenerParams.setTitle("OverlayManager");

        // Set a transparent background
        listenerParams.format = PixelFormat.TRANSLUCENT;

        // Create an always on top type of window:
         //  TYPE_SYSTEM_ALERT   = touch events are intercepted
        listenerParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
        
        // The whole screen is covered (including status bar)
        listenerParams.flags = LayoutParams.FLAG_LAYOUT_INSET_DECOR | LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        // ////////////////////////////////////////////////////////////////////////////////
       

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        
        if ( LV == null ) {
            LV = new ListenerView(context);
            //LV.setTouchable(true);
            LV.setOnTouchListener(this);
        	wm.addView(LV, listenerParams);
        }
	}
	
	/* package */ void destroyOverlayView(Context context)
	{
    	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    
	    if (LV != null )
	    {
	    	wm.removeViewImmediate(LV);
	    	LV = null;
	    }
	}
	
	private Handler mHandler = new Handler() {
    	@Override
    	public void handleMessage (Message msg) {  
			createOverlayView(mContext);
    	}
	};
	
	private void click(final float x, final float y) {
    	new Thread(new Runnable() {
			@Override
			public void run() {
				Log.w("prints", "entra click");
				Instrumentation i = new Instrumentation();
				MotionEvent down = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),MotionEvent.ACTION_DOWN,x, y, 0);
				MotionEvent up = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0);
				i.sendPointerSync(down);
				i.sendPointerSync(up);
				up.recycle();
				down.recycle();
				Log.i("prints","final click");
				mHandler.sendEmptyMessage(0);
			}
    	}).start();
    }

}
