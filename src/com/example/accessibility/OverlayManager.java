package com.example.accessibility;
//package com.example.android.apis.accessibility;


import android.accessibilityservice.AccessibilityService;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;

public class OverlayManager extends AccessibilityService implements OnInitListener
{
	
	private ListenerView LV;
	private OnTouchListener OTL;
	private long tsTempDown;
	private Context mContext;
	
	
	
	@Override
    public void onServiceConnected() {
		Log.i("prints","entra onServiceConnected del Overlay");
		createOverlayView(mContext);
		
    }
	


	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub
		Log.i("prints","entra onInterrupt del Overlay");
	}
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		Log.i("prints","entra onInit del Overlay");
	}


	/*
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
*/
	
	
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub
		Log.i("prints","entra onAccessibilityEvent del Overlay");
		// TODO Auto-generated method stub
		if (event.getEventType() == AccessibilityEvent.TYPE_TOUCH_INTERACTION_START){
			//tsTempDown = new Timestamp(System.currentTimeMillis()).getTime();
			tsTempDown = event.getEventTime();
		}
			//Toast.makeText(v.getContext(), x+" "+y, Toast.LENGTH_SHORT).show();
		else if(event.getEventType()== AccessibilityEvent.TYPE_TOUCH_INTERACTION_END){
			//long tsTempUp = new Timestamp(System.currentTimeMillis()).getTime();
			long tsTempUp = event.getEventTime();
			tsTempUp=tsTempUp-tsTempDown;
			int x= event.getScrollX();
			int y= event.getScrollY();
			if (tsTempUp>2000)
			{
				destroyOverlayView(mContext);
				click(x,y);
			}
			    //textView.setText("Touch coordinates : " +String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
        
        //return true;
		}	
	}

	

	View getListenerView() {
		return LV;
	}
 
	/*void setOnTouchListener(OnTouchListener listener) {
		OTL = listener;
 }*/

 
	void createOverlayView(Context context) {
		Log.i("prints","entra createOverlayView del Overlay");
	
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
       

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        
        if ( LV == null ) {
            LV = new ListenerView(this.getApplicationContext());
            //LV.setTouchable(true);
            //LV.setOnTouchListener(this);
        	wm.addView(LV, listenerParams);
        }
        Log.i("prints","acaba createOverlayView del Overlay");
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
