package com.example.accessibility;
//package com.example.android.apis.accessibility;


import android.accessibilityservice.AccessibilityService;
import android.app.Instrumentation;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RemoteViews;

public class OverlayManager extends AccessibilityService implements OnTouchListener
{
	private final int NOTIFICATION_ID = 1010;
	private ListenerView LV;
	private OnTouchListener OTL;
	private long tsTempDown;
	private Context mContext;
	
	
	
	@Override
    public void onServiceConnected() {
		Log.i("prints","entra onServiceConnected del Overlay");
		triggerNotification();
		createOverlayView(mContext);
    }
	


	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub
		Log.i("prints","entra onInterrupt del Overlay");
	}
	
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub
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
	public boolean onTouch(View v, MotionEvent event) {
		Log.i("prints","entra onTouch del Overlay");
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			Log.i("prints","eventDown");
			tsTempDown = event.getDownTime();
		}
		
		else if(event.getAction()==MotionEvent.ACTION_UP){
			Log.i("prints","eventUp");
			long tsTempUp = event.getEventTime();
			tsTempUp=tsTempUp-tsTempDown;
			Log.i("prints","temps de click"+tsTempUp);
			Float x= event.getX();
			Float y= event.getY();
			if (tsTempUp>2000)
			{
				destroyOverlayView(mContext);
			
				click(x,y);
			}
		}
			
        
        return true;
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
            LV.setOnTouchListener(this);
        	wm.addView(LV, listenerParams);
        }
        Log.i("prints","acaba createOverlayView del Overlay");
	}
	
	/* package */ void destroyOverlayView(Context context)
	{
    	WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	    
	    if (LV != null )
	    {
	    	wm.removeViewImmediate(LV);
	    	LV = null;
	    	Log.i("prints","acaba destroyOverlayView");
	    }
	}
	
	/* package */ PendingIntent destroyOverlay(Context context)
	{
    	WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	    
	    if (LV != null )
	    {
	    	wm.removeViewImmediate(LV);
	    	LV = null;
	    	Log.i("prints","acaba destroyOverlayView");
	    }
		return null;
	}
	
	 
	private Handler mHandler = new Handler() {
    	@Override
    	public void handleMessage (Message msg) {  
			createOverlayView(mContext);
    	}
	};
	
	/*private void click(final float x, final float y) {
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
    }*/

	//Touch Overlay Listener 
	public void click(float x,float y){
		//Log.i("prints","entra click del Overlay");
		int posx = (int)x;
		int posy = (int) y;

		//AccessibilityNodeInfo activeRoot = this.getRootInActiveWindow(); //this is an AccessibilityService instance
		AccessibilityNodeInfoCompat node = getCursor();
		int contador = 0;
		synchronized (mHandler) {

			while (node == null && contador < 20) {
				node = getCursor();
				contador++;
				try {
					mHandler.wait(400);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(node!=null){Log.i("prints", "el node no es null");}	
		AccessibilityNodeInfoCompat comp = findComponentClickable(node,posx, posy);
		if (comp != null) {
			Log.i("prints","injecta click del Overlay");
					boolean s = comp.performAction(AccessibilityNodeInfoCompat.ACTION_CLICK);
					}	
		mHandler.sendEmptyMessage(0);
	}					
							
	private AccessibilityNodeInfoCompat findComponentClickable(AccessibilityNodeInfoCompat root, int posx, int posy) {
			try {
				Log.i("prints","entra findComponentClickable del Overlay");
				Rect window = new Rect();
				AccessibilityNodeInfoCompat node = null;

				for (int i = 0; i < root.getChildCount(); i++) {
					root.getChild(i).getBoundsInScreen(window);
					if (window.contains(posx, posy)) {
						if (root.getChild(i).getChildCount() > 0) {
							//count++;
							node = findComponentClickable(root.getChild(i), posx,
									posy);
							//count--;
						}
						if (node == null && root.getChild(i).isClickable()) {
							node = root.getChild(i);

						}

					}

				}Log.i("prints","surt findComponentClickable del Overlay");
		
				return node;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
	}
	public AccessibilityNodeInfoCompat getCursor() {
		final AccessibilityNodeInfo activeRoot = this.getRootInActiveWindow();
		if (activeRoot == null) {
			Log.d("LOG_TAG", "active root not found");
			return null;
		}

		final AccessibilityNodeInfoCompat compatRoot = new AccessibilityNodeInfoCompat(
				activeRoot);
		final AccessibilityNodeInfoCompat focusedNode = compatRoot
				.findFocus(AccessibilityNodeInfoCompat.FOCUS_ACCESSIBILITY);

		// TODO: If there's no focused node, we should either mimic following
		// focus from new window or try to be smart for things like list views.
		if (focusedNode == null) {
			Log.d("LOG_TAG", "focused null");
			return compatRoot;
		}

		return focusedNode;
	}
	
	
	private void triggerNotification(){
	    
		//Intent intent = new Intent(OverlayManager.destroyOverlayView(mContext));
		
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //Notification notification = new Notification(R.drawable.images, "¡Nuevo mensaje!", System.currentTimeMillis());
        Notification notification = new Notification.Builder(this)
        .setContentText("Hola hola")
        .setSmallIcon(R.drawable.images)
        .setWhen(System.currentTimeMillis())
        .addAction(R.drawable.ic_launcher, "Apaga TouchAccessibility",destroyOverlay(mContext) )
        .build();
        
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
        contentView.setImageViewResource(R.id.img_notification, R.drawable.images);
        contentView.setTextViewText(R.id.txt_notification, "Ey mundo! Esta es mi notificación personalizada.");
        
        notification.contentView = contentView;
        /*
        Intent notificationIntent = new Intent(this, OverlayManager.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.contentIntent = contentIntent;*/
        
        
        
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
	
	}


