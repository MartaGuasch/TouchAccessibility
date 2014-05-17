package com.example.accessibility;


import java.lang.reflect.Method;

import android.accessibilityservice.AccessibilityService;
import android.app.Instrumentation;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
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
import android.widget.Toast;

public class OverlayManager extends AccessibilityService implements OnTouchListener
{
	private final int NOTIFICATION_ID = 1010;
	private ListenerView LV;
	private FeedbackClickView mFCV;
	private OnTouchListener OTL;
	private long tsTempDown;
	private Context mContext;
	private int clickTime=2000;
	private int clickTimeSec=clickTime/20;
	private int n,i;
	private static final Method METHOD_performGlobalAction = CompatUtils.getMethod(
            AccessibilityService.class, "performGlobalAction", int.class);

	
	
	
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
	
	@Override
    public void onDestroy() {
       destroyOverlayView(mContext);
        super.onDestroy();
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
		
		//LV.onDrawNodeBorder(node, "hola");
		//Log.i("prints","entra onTouch del Overlay");
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			n=0;i=0;
			Log.i("prints","eventDown");
			tsTempDown = event.getDownTime();
			int x= (int) event.getX();
			int y= (int) event.getY();
			createFeedbackClickView(mContext);
			mFCV.setXY(x, y);	
		}
		
		else if(event.getAction()==MotionEvent.ACTION_UP){
			destroyFeedbackClickView(mContext);
			Log.i("prints","eventUp");
			long tsTempUp = event.getEventTime();
			tsTempUp=tsTempUp-tsTempDown;
			Log.i("prints","temps de click"+tsTempUp);
			
			Float x = event.getX();
			Float y = event.getY();
			int left = LV.getPaddingLeft();
	        int top = LV.getPaddingTop();
	        int right =  LV.getWidth() - LV.getPaddingRight();
	        int bottom = LV.getHeight() - LV.getPaddingBottom();
			
			
			
			if (tsTempUp>clickTime)
			{
				//LV.onDrawNodeBorder(node,"green");
				Log.i("prints","border green");
				destroyOverlayView(mContext);
				if((x>=right-80)&&(x<=right)&&(y>=bottom/2)&&(y<=bottom/2+80)){
					Log.i("prints","Scroll right");
				}
				if((x>=left)&&(x<=left+80)&&(y>=bottom/2)&&(y<=bottom/2+80)){
					Log.i("prints","Scroll left");
				}
				if((x>=right/2-40)&&(x<=right/2+40)&&(y>=top+30)&&(y<=top+110)){
					Log.i("prints","Scroll top");
				}
				if((x>=right/2-40)&&(x<=right/2+40)&&(y>=bottom-90)&&(y<=bottom-10)){
					Log.i("prints","Scroll down");
				}
				if((x>=right-80)&&(x<=right)&&(y>=bottom-90)&&(y<=bottom-10)){
					Log.i("prints","Home");
					performGlobalAction(this,GLOBAL_ACTION_HOME);
					/*
					IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getInstance().getConnection(mConnectionId);
							if (connection != null) {
							try {
							return connection.performGlobalAction(action);
							} catch (RemoteException re) {
							Log.w(LOG_TAG, "Error while calling performGlobalAction", re);
							}
							}
							return false;*/
				}
				
				click(x,y);
			}
		}
		else{
			long tsTempMid=event.getEventTime()-tsTempDown;
			if(tsTempMid<clickTimeSec+n){
				Log.w("prints","entra if i hi ha "+i+" graus i "+n+" ms");
				mFCV.setDeg(18+i);
			}
			else{
				n=n+clickTimeSec;
				i=i+18;
			}
			
			tsTempMid=event.getEventTime()-tsTempDown;
			Log.i("prints","temps de click "+tsTempMid+" n "+n);
			
			
			
		}
			
        
        return true;
    }
	

	ListenerView getListenerView() {
		return LV;
	}
 
	void createFeedbackClickView(Context context){
		Log.i("prints","entra createFeedBackClickView del Overlay");
		
		mContext=context;
		// ////////////////////////////////////////////////////////////////////////////////
        // FeedbackClickView
        LayoutParams feedbackParams = new LayoutParams();
        feedbackParams.setTitle("OverlayManager");

        // Set a transparent background
        feedbackParams.format = PixelFormat.TRANSLUCENT;

        // Create an always on top type of window:
         //  TYPE_SYSTEM_ALERT   = touch events are intercepted
        feedbackParams.type =  LayoutParams.TYPE_SYSTEM_ALERT;
        
        // The whole screen is covered (including status bar)
        feedbackParams.flags = LayoutParams.FLAG_LAYOUT_INSET_DECOR | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        // ////////////////////////////////////////////////////////////////////////////////
       

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        
        if ( mFCV == null ) {
        	
            mFCV = new FeedbackClickView(this.getApplicationContext());

            //mFCV.setXY(x,y);
            //mFCV.setRadius(per);
        	wm.addView(mFCV, feedbackParams);
        }
        Log.i("prints","acaba createFeedbackClickView del Overlay");
	}
	
 
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
        listenerParams.type = LayoutParams.TYPE_SYSTEM_ALERT | LayoutParams.TYPE_PHONE	| LayoutParams.TYPE_SYSTEM_OVERLAY; 
        
        // The whole screen is covered (including status bar)
        listenerParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_LAYOUT_IN_SCREEN;
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
	
	/* package */ void destroyFeedbackClickView(Context context)
	{
    	WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	    
	    if (LV != null )
	    {
	    	wm.removeViewImmediate(mFCV);
	    	mFCV = null;
	    	Log.i("prints","acaba destroyOverlayView");
	    }
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
	

	private Handler mHandler = new Handler() {
    	@Override
    	public void handleMessage (Message msg) {  
			createOverlayView(mContext);
    	}
	};
	

	//Touch Overlay Listener 
	public void click(float x,float y){
		//Log.i("prints","entra click del Overlay");
		int posx = (int)x;
		int posy = (int) y;

		AccessibilityNodeInfoCompat node = findNode();
		if(node!=null){Log.i("prints", "el node no es null "+posx+"  "+posy);}	
		AccessibilityNodeInfoCompat comp = findComponentClickable(node,posx, posy);
		if (comp != null) {
			Log.i("prints","injecta click del Overlay");
				//getListenerView().onDrawNodeBorder();
					boolean s = comp.performAction(AccessibilityNodeInfoCompat.ACTION_CLICK);
					}	
		mHandler.sendEmptyMessage(0);
		
	}			
	public void scrollRight(){
		//Log.i("prints","entra click del Overlay");
		Log.i("prints","injecta click del Overlay");
		
		mHandler.sendEmptyMessage(0);
		
	}				
			
	private AccessibilityNodeInfoCompat findNode(){
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
		return node;
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
	    
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //Notification notification = new Notification(R.drawable.images, "¡Nuevo mensaje!", System.currentTimeMillis());
        Notification notification = new Notification.Builder(this)
        .setContentText("Hola hola")
        .setSmallIcon(R.drawable.images)
        .setWhen(System.currentTimeMillis())
        //.addAction(R.drawable.ic_launcher, "Apaga TouchAccessibility",destroyOverlay(mContext) )
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
	public static boolean performGlobalAction(AccessibilityService service, int action) {
        return (Boolean) CompatUtils.invoke(service, false, METHOD_performGlobalAction, action);
    }

	
	}


