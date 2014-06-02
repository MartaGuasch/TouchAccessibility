package com.example.accessibility;


import java.lang.reflect.Method;

import android.accessibilityservice.AccessibilityService;
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
	private long clickTime=2000;
	private long clickTimeSec=clickTime/20;
	private int deg=0;
	private float clickx,clicky;
	private String button="clear";
	private boolean timer=false;
	private static final Method METHOD_performGlobalAction = CompatUtils.getMethod(
            AccessibilityService.class, "performGlobalAction", int.class);
	private states current_state = states.WAIT_EVENT;

    //int right =  LV.getWidth() - LV.getPaddingRight();
    //int bottom = LV.getHeight() - LV.getPaddingBottom();
	
	public enum states{
		WAIT_EVENT,
		WAIT_T1,
		HOME_CONTEXTUAL_MENU,
		BACK_CONTEXTUAL_MENU
	}
	
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
		if (event.getEventType() ==  AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
		
		}
	}
	
	@Override
    public void onDestroy() {
       destroyOverlayView(mContext);
        super.onDestroy();
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		Log.i("prints","on touch "+current_state);
		stateMachineProcessInput(event);
		
		//Log.i("prints",button);
		//LV.onDrawNodeBorder(node, "hola");
		//Log.i("prints","entra onTouch del Overlay");
		// TODO Auto-generated method stub
		
		/*
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			n=0;i=0;
			Log.i("prints","eventDown");
			tsTempDown = event.getDownTime();
			int x= (int) event.getX();
			int y= (int) event.getY();
			createFeedbackClickView(mContext);
			mFCV.setXY(x, y);	
		}
		
		else if((event.getAction()==MotionEvent.ACTION_UP)&&(button.equals("home"))){
			destroyFeedbackClickView(mContext);
			createFeedbackClickView(mContext);
			Log.i("prints","eventUp2");
			long tsTempUp = event.getEventTime();
			tsTempUp=tsTempUp-tsTempDown;
			
			float tempx = event.getX();
			float tempy = event.getY();
	        int right =  LV.getWidth() - LV.getPaddingRight();
	        int bottom = LV.getHeight() - LV.getPaddingBottom();
			
			if (tsTempUp>clickTime)
			{	destroyOverlayView(mContext);
				if ((tempx>=right-250)&&(tempx<=right-90)&&(tempy>=bottom-170)&&(tempy<=bottom-10)){
					Log.i("prints","click");
					mFCV.setMenuContextual("clear");
					click(x,y);
					
				}
				else if ((tempx>=right-250)&&(tempx<=right-90)&&(tempy>=bottom-340)&&(tempy<=bottom-180)){
					Log.i("prints","GlobalActionHome");
					performGlobalAction(this,GLOBAL_ACTION_HOME);
					mFCV.setMenuContextual("clear");
					mHandler.sendEmptyMessage(0);
					
				}
				else{
					Log.i("prints","menucontextual fuera");
					mFCV.setMenuContextual("clear");
					mHandler.sendEmptyMessage(0);
				}
				button="clear";
				
			}
		}
		
		else if(event.getAction()==MotionEvent.ACTION_UP){
			//scroll(true);
			destroyFeedbackClickView(mContext);
			Log.i("prints","eventUp");
			long tsTempUp = event.getEventTime();
			tsTempUp=tsTempUp-tsTempDown;
			//Log.i("prints","temps de click"+tsTempUp);
			
			x = event.getX();
			y = event.getY(); 
			int left = LV.getPaddingLeft();
	        int top = LV.getPaddingTop();
	        int right =  LV.getWidth() - LV.getPaddingRight();
	        int bottom = LV.getHeight() - LV.getPaddingBottom();
			
			
			
			if (tsTempUp>clickTime)
			{
				//createFeedbackClickView(mContext);
				//LV.onDrawNodeBorder(node,"green");
				Log.i("prints","border green");
				destroyOverlayView(mContext);
				/*
				if((x>=right-80)&&(x<=right)&&(y>=bottom/2)&&(y<=bottom/2+80)){
					Log.i("prints","Scroll right");
					//scroll(true);
					/*
					AccessibilityNodeInfoCompat node = findNode();
					if(node!=null){Log.i("prints", "el node no es null ");}
					AccessibilityNodeInfoCompat compn=null;
					while(compn==null){
						compn = findComponentScrollable(node);
						if(compn!=null){Log.w("prints","El component es scrollable");}
					}
					compn.performAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
					
				}
				else if((x>=left)&&(x<=left+80)&&(y>=bottom/2)&&(y<=bottom/2+80)){
					Log.i("prints","Scroll left");
					//scroll(false);
				}
				else if((x>=right/2-40)&&(x<=right/2+40)&&(y>=top+30)&&(y<=top+110)){
					Log.i("prints","Scroll top");
					//scroll(false);
				}
				else if((x>=right/2-40)&&(x<=right/2+40)&&(y>=bottom-90)&&(y<=bottom-10)){
					Log.i("prints","Scroll down");
					//scroll(true);
				}*/
		/*
				if((x>=right-80)&&(x<=right)&&(y>=bottom-90)&&(y<=bottom-10)){
					Log.i("prints","Home");
					AccessibilityNodeInfoCompat node = findNode();
					AccessibilityNodeInfoCompat comp = findComponentClickable(node,(int) x, (int) y);
					if (comp!=null){
						Log.i("prints"," Menu contextual");
						createFeedbackClickView(mContext);
						Log.i("prints","createdFeedbackClickView");
						button="home";
						mFCV.setMenuContextual(button);
						Log.i("prints","menu contextual seted");
					/*
						if (event.getAction() == MotionEvent.ACTION_DOWN){
							Log.i("prints","eventDown2");
							tsTempDown = event.getDownTime();
							x= (int) event.getX();
							y= (int) event.getY();
							mFCV.setXY((int)x,(int) y);	
							if(event.getAction()==MotionEvent.ACTION_UP){
								destroyFeedbackClickView(mContext);
								Log.i("prints","eventUp2");
								tsTempUp = event.getEventTime();
								tsTempUp=tsTempUp-tsTempDown;
								//Log.i("prints","temps de click"+tsTempUp);
								
								x = event.getX();
								y = event.getY();
								
								if (tsTempUp>clickTime)
								{
									if ((x>=right-250)&&(x<=right-90)&&(y>=bottom-170)&&(y<=bottom-10)){
										click(x,y);
										//mFCV.setMenuContextual("clear");
									}
									else if ((x>=right-250)&&(x<=right-90)&&(y>=bottom-340)&&(y<=bottom-380)){
										performGlobalAction(this,GLOBAL_ACTION_HOME);
										button="clear";
										mFCV.setMenuContextual(button);
										mHandler.sendEmptyMessage(0);
										
									}
									else{
										button="clear";
										mFCV.setMenuContextual(button);
										mHandler.sendEmptyMessage(0);
									}
								}
							}
							else{
								long tsTempMid=event.getEventTime()-tsTempDown;
								if(tsTempMid<clickTimeSec+n){
									mFCV.setDeg(18+i);
								}
								else{
									n=n+clickTimeSec;
									i=i+18;
								}
								
								tsTempMid=event.getEventTime()-tsTempDown;
								
								
							}
						}*/
	/*					mHandler.sendEmptyMessage(0);
					}
					else{
						performGlobalAction(this,GLOBAL_ACTION_HOME);
						mHandler.sendEmptyMessage(0);
					}
				}

				else if((x>=left)&&(x<=left+80)&&(y>=bottom-90)&&(y<=bottom-10)){
					Log.i("prints","Back");
					performGlobalAction(this,GLOBAL_ACTION_BACK);
					mHandler.sendEmptyMessage(0);
				}
				
				
				else{
					click(x,y);
				}
				
			}
		}
		
		
	
		else{
			long tsTempMid=event.getEventTime()-tsTempDown;
			if(tsTempMid<clickTimeSec+n){
				//Log.w("prints","entra if i hi ha "+i+" graus i "+n+" ms");
				mFCV.setDeg(18+i);
			}
			else{
				n=n+clickTimeSec;
				i=i+18;
			}
			
			tsTempMid=event.getEventTime()-tsTempDown;
			//Log.i("prints","temps de click "+tsTempMid+" n "+n);
			
			
			
		}
			
        */
        return true;
    }
	

	ListenerView getListenerView() {
		return LV;
	}
 
	void createFeedbackClickView(Context context){
		//Log.i("prints","entra createFeedBackClickView del Overlay");
		
		mContext=context;
		// ////////////////////////////////////////////////////////////////////////////////
        // FeedbackClickView
        LayoutParams feedbackParams = new LayoutParams();
        feedbackParams.setTitle("OverlayManager");

        // Set a transparent background
        feedbackParams.format = PixelFormat.TRANSLUCENT;

        // Create an always on top type of window:
         //  TYPE_SYSTEM_ALERT   = touch events are intercepted
        feedbackParams.type = LayoutParams.TYPE_SYSTEM_ALERT | LayoutParams.TYPE_PHONE	| LayoutParams.TYPE_SYSTEM_OVERLAY; 
        
        // The whole screen is covered (including status bar)
        feedbackParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_LAYOUT_IN_SCREEN;
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
		//Log.i("prints","entra createOverlayView del Overlay");
	
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
       
        /*
        AccessibilityNodeInfoCompat cursor = getCursor();
        final AccessibilityNodeInfoCompat root = getRoot(cursor);
        //final AccessibilityNodeInfoCompat searched = AccessibilityNodeInfo.searchFromBfs(mContext, root, isScrollable());

        
        AccessibilityNodeInfoCompat node = findNode();
		if(node!=null){Log.i("prints", "el node no es null ");}
		AccessibilityNodeInfoCompat compn=null;
		compn = findComponentScrollable(root);
		if(compn!=null){Log.w("prints","El component es scrollable");}
		else {
			compn=null; 
		
		}
		*/
        
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
	    	Log.i("prints","acaba destroyFeedbackClickView");
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
	
	public void scroll(boolean forward){
		AccessibilityNodeInfoCompat node = findNode();
		if(node!=null){Log.i("prints", "el node no es null ");}
		AccessibilityNodeInfoCompat compn=null;
		compn = findComponentScrollable(node);
		
		if(compn!=null){
			Log.w("prints","El component es scrollable");
			mFCV.setNodeNull(false);
			mFCV.setNode(compn);
			/*boolean t;
			if (forward){
				boolean s = compn.performAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
			}
			else
				t = compn.performAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);*/
		}
		else {
			mFCV.setNodeNull(true);
		}
		//mHandler.sendEmptyMessage(0);
	}

	//Touch Overlay Listener 
	public void click(float x,float y){
		destroyOverlayView(mContext);
		//Log.i("prints","entra click del Overlay");
		int posx = (int)x;
		int posy = (int) y;

		AccessibilityNodeInfoCompat node = findNode();
		if(node!=null){Log.i("prints", "el node no es null "+posx+"  "+posy);}	
		AccessibilityNodeInfoCompat comp = findComponentClickable(node,posx, posy);

		if (comp != null) {
			Log.i("prints","injecta click del Overlay");
					boolean s = comp.performAction(AccessibilityNodeInfoCompat.ACTION_CLICK);
					}
		
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
	
	private AccessibilityNodeInfoCompat findComponentScrollable(AccessibilityNodeInfoCompat root) {
		try {
			Log.i("prints","entra findComponentScrollable del Overlay");
			Rect window = new Rect();
			AccessibilityNodeInfoCompat node = null;

			for (int i = 0; i < root.getChildCount(); i++) {
				//root.getChild(i).getBoundsInScreen(window);
					if (root.getChild(i).getChildCount() > 0) {
						//count++;
						node = findComponentScrollable(root.getChild(i));
						//count--;
					}
					if (node == null && root.getChild(i).isScrollable()) {
						node = root.getChild(i);

					}

				}

			//Log.i("prints","surt findComponentScrollable del Overlay");
	
			return node;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
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
	
	public void stateMachineProcessInput (MotionEvent me){
		switch (current_state){
			case WAIT_EVENT:
				Log.i("prints","WAIT_EVENT");
				process_wait(me);
				break;
			case WAIT_T1:
				Log.i("prints","WAIT_T1");
				process_wait_t1(me);
				//Log.i("prints","endwaitt1");
				break;
			case HOME_CONTEXTUAL_MENU:
				Log.i("prints","HOME_CONTEXTUAL_MENU");
				//home_contextual_menu(me);
				break;
			case BACK_CONTEXTUAL_MENU:
				Log.i("prints","HOME_CONTEXTUAL_MENU");
				break;
			default:
				Log.i("prints","Any state");
				break;
				
		
			}
	}
	
	public void process_wait (MotionEvent me){
		h3.sendEmptyMessage(0);
		if (me.getAction()==MotionEvent.ACTION_DOWN){
			//createFeedbackClickView(mContext);
			Log.i("prints","eventdown");
			current_state = states.WAIT_T1;
			tsTempDown = me.getEventTime();
			timer=false;
			h1.sendEmptyMessageDelayed(0,clickTime);
			h2.sendEmptyMessageDelayed(0, clickTimeSec);
			if(mFCV!=null){
				mFCV.setXY((int) me.getX(), (int) me.getY());
			}
			else{
				createFeedbackClickView(mContext);
				mFCV.setXY((int) me.getX(), (int) me.getY());
			}
		}
	}
	public void process_wait_t1 (MotionEvent me){
		if((me.getAction() == MotionEvent.ACTION_UP)&&(!timer)){
			Log.i("prints","eventup");
			timer=false;
			h1.removeMessages(0);
			deg=0;
			mFCV.setDeg(deg);
			h2.removeMessages(0);
			current_state = states.WAIT_EVENT;
			destroyFeedbackClickView(mContext);
			Log.i("prints","end wait t1");
		}
		if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(!isHomeButton((int)me.getX(),(int) me.getY()))){
			Log.i("prints","eventup+click");
			timer=false;
			h1.removeMessages(0);
			deg=0;
			mFCV.setDeg(deg);
			h2.removeMessages(0);
			click(me.getX(),me.getY());
			current_state = states.WAIT_EVENT;
			destroyFeedbackClickView(mContext);
			stateMachineProcessInput(me);
			
		}
		if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(isHomeButton((int) me.getX(), (int) me.getY()))){
			timer=false;
			h1.removeMessages(0);
			deg=0;
			mFCV.setDeg(deg);
			h2.removeMessages(0);
			destroyFeedbackClickView(mContext);
			AccessibilityNodeInfoCompat node = findNode();
			AccessibilityNodeInfoCompat comp = findComponentClickable(node,(int) me.getX(), (int) me.getY());
			if(comp!=null){
				button="home";
				current_state = states.HOME_CONTEXTUAL_MENU;
				Log.i("prints","menu contextual");
				clickx=me.getX();
				clicky=me.getY();
				stateMachineProcessInput(me);
				/*
				if(mFCV!=null){
					mFCV.setMenuContextual("home");
				}
				else{
					createFeedbackClickView(mContext);
					mFCV.setMenuContextual("home");
				}*/
				
				/*
				destroyFeedbackClickView(mContext);
				destroyOverlayView(mContext);
				mHandler.sendEmptyMessage(0);
				h3.sendEmptyMessage(0);
				h4.sendEmptyMessage(0);*/
				//stateMachineProcessInput(null);
			
			}
			else{
				Log.i("prints","actionhome");
				destroyOverlayView(mContext);
				performGlobalAction(this,GLOBAL_ACTION_HOME);
				mHandler.sendEmptyMessage(0);
				current_state = states.WAIT_EVENT;
				destroyFeedbackClickView(mContext);
				//stateMachineProcessInput(me);
			}
			
		}
	}
	public void home_contextual_menu (MotionEvent me){
		if (me==null){
			Log.i("prints","motionevent null");
		}
		else{
			Log.i("prints","motion event no null");
			if (me.getAction()==MotionEvent.ACTION_DOWN){
				//createFeedbackClickView(mContext);
				Log.i("prints","eventdown");
				tsTempDown = me.getEventTime();
				timer=false;
				h1.sendEmptyMessageDelayed(0,clickTime);
				h2.sendEmptyMessageDelayed(0, clickTimeSec);
				if(mFCV!=null){
					mFCV.setXY((int) me.getX(), (int) me.getY());
				}
				else{
					createFeedbackClickView(mContext);
					mFCV.setXY((int) me.getX(), (int) me.getY());
				}
			}
			
			if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(isCMHomeButton((int) me.getX(), (int) me.getY()))){
				Log.i("prints","actionhome");
				destroyOverlayView(mContext);
				performGlobalAction(this,GLOBAL_ACTION_HOME);
				mHandler.sendEmptyMessage(0);
				current_state = states.WAIT_EVENT;
				destroyFeedbackClickView(mContext);
				stateMachineProcessInput(me);
			}
			if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(isCMClick((int) me.getX(), (int) me.getY()))){
				Log.i("prints","eventup+click");
				timer=false;
				h1.removeMessages(0);
				deg=0;
				mFCV.setDeg(deg);
				h2.removeMessages(0);
				click(clickx,clicky);
				current_state = states.WAIT_EVENT;
				destroyFeedbackClickView(mContext);
				stateMachineProcessInput(me);
				
			}
			if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(!isCMClick((int) me.getX(), (int) me.getY()))&&(!isCMHomeButton((int) me.getX(), (int) me.getY()))){
				timer=false;
				h1.removeMessages(0);
				deg=0;
				mFCV.setDeg(deg);
				h2.removeMessages(0);
				current_state = states.WAIT_EVENT;
				destroyFeedbackClickView(mContext);
			}
		}
	}

	private Handler h1 = new Handler() {
    	@Override
    	public void handleMessage (Message msg) {  
    		Log.i("prints","2 seg");
			timer=true;
			current_state = states.WAIT_T1;
    	} 
	};
	
	private Handler h2 = new Handler() {
    	@Override
    	public void handleMessage (Message msg) {  
    		if(deg<360){
    			//createFeedbackClickView(mContext);
    			mFCV.setDeg(18+deg);
    			//Log.i("prints"," "+deg);
    			deg=18+deg;
    			h2.sendEmptyMessageDelayed(0, clickTimeSec);
    		}
			
    	}
	};
	private Handler h3 = new Handler() {
    	@Override
    	public void handleMessage (Message msg) {  
    		createFeedbackClickView(mContext);
			
    	}
	};
	
	private Handler h4 = new Handler() {
    	@Override
    	public void handleMessage (Message msg) {  
    		mFCV.setMenuContextual("home");
    	}
	};
	
	public boolean isHomeButton(int posx, int posy){
		int right =  LV.getWidth() - LV.getPaddingRight();
	    int bottom = LV.getHeight() - LV.getPaddingBottom();
	    
		 if ((posx>=right-80)&&(posx<=right)&&(posy>=bottom-90)&&(posy<=bottom-10)){
			 return true;
		 }
		 else{
			 return false;
		 }
	}
	public boolean isCMHomeButton(int posx, int posy){
		int right =  LV.getWidth() - LV.getPaddingRight();
	    int bottom = LV.getHeight() - LV.getPaddingBottom();
		 if ((posx>=right-250)&&(posx<=right-90)&&(posy>=bottom-170)&&(posy<=bottom-10)){
			 return true;
		 }
		 else{
			 return false;
		 }
	}
	public boolean isCMClick(int posx, int posy){
		int right =  LV.getWidth() - LV.getPaddingRight();
	    int bottom = LV.getHeight() - LV.getPaddingBottom();
		 if ((posx>=right-250)&&(posx<=right-90)&&(posy>=bottom-340)&&(posy<=bottom-180)){
			 return true;
		 }
		 else{
			 return false;
		 }
	}
	/*
	public static AccessibilityNodeInfoCompat getRoot(AccessibilityNodeInfoCompat node) {
        if (node == null) {
            return null;
        }

        AccessibilityNodeInfoCompat current = null;
        AccessibilityNodeInfoCompat parent = AccessibilityNodeInfoCompat.obtain(node);

        do {
            current = parent;
            parent = current.getParent();
        } while (parent != null);

        return current;
    }
*/


	
	}




