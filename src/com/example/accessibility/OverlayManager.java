package com.example.accessibility;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
	private ListenerView mLV;
	private FeedbackClickView mFCV;
	private Context mContext;
	
	//clickTime -> time to make click 
	private long clickTime=2000;
	private long clickTimeSec=clickTime/20;
	private int deg=0;
	private float clickx,clicky;
	
	//Button that is pressed
	private String button="clear";
	
	//Timer true when time is enough to click
	private boolean timer=false;
	private static final Method METHOD_performGlobalAction = CompatUtils.getMethod(
            AccessibilityService.class, "performGlobalAction", int.class);
	private states current_state = states.WAIT_EVENT;
	private List <AccessibilityNodeInfoCompat> scrollables = new ArrayList <AccessibilityNodeInfoCompat> ();
	private AccessibilityNodeInfoCompat scrollableNode;
	
	
	//State machine states
	public enum states{
		WAIT_EVENT,
		WAIT_T1,
		CONTEXTUAL_MENU
	}
	
	@Override
    public void onServiceConnected() {
		Log.i("prints","onServiceConnected");
		triggerNotification();
		createOverlayView(mContext);
		
    }
	


	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub
		Log.i("prints","onInterrupt");
	}
	
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		
		// TODO Auto-generated method stub
		if (event.getEventType() ==  AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
			if(mFCV!=null){
				destroyFeedbackClickView(mContext);
			}
			showScrollButtons();
	        
		}
	}
	
	@Override
    public void onDestroy() {
       destroyOverlayView(mContext);
        super.onDestroy();
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		
		//Log.i("prints","on touch "+current_state);
		stateMachineProcessInput(event);
		
		
        return true;
    }
	

	ListenerView getListenerView() {
		return mLV;
	}
 
	void createFeedbackClickView(Context context){
		//The overlay with visual feedback and scroll buttons is created here. It's a FeedbackClickView.
		
		mContext=context;
		// ////////////////////////////////////////////////////////////////////////////////
        // FeedbackClickView
        LayoutParams feedbackParams = new LayoutParams();
        feedbackParams.setTitle("OverlayManager");

        // Set a transparent background
        feedbackParams.format = PixelFormat.TRANSLUCENT;

        // Create an always on top type of window:
         //  TYPE_SYSTEM_ALERT   = touch events are intercepted
        feedbackParams.type = LayoutParams.TYPE_PHONE	| LayoutParams.TYPE_SYSTEM_OVERLAY; 
        
        // The whole screen is covered (including status bar)
        feedbackParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        // ////////////////////////////////////////////////////////////////////////////////

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        
        if ( mFCV == null ) {
        	
            mFCV = new FeedbackClickView(this.getApplicationContext());

            showScrollButtons();
        	wm.addView(mFCV, feedbackParams);
        }
        Log.i("prints","acaba createFeedbackClickView del Overlay");
	}
	
 
	void createOverlayView(Context context) {
		//The overlay with global buttons that listens touch events is created here. It's a ListenerView.
		
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
       
        showScrollButtons();
        
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        
        if ( mLV == null ) {
            mLV = new ListenerView(this.getApplicationContext());
            //LV.setTouchable(true);
            mLV.setOnTouchListener(this);
        	wm.addView(mLV, listenerParams);
        }
        Log.i("prints","acaba createOverlayView del Overlay");
	}
	
	/* package */ void destroyFeedbackClickView(Context context)
	{
    	WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	    
	    if (mLV != null )
	    {
	    	wm.removeViewImmediate(mFCV);
	    	mFCV = null;
	    	Log.i("prints","end of destroyFeedbackClickView");
	    }
	}
	/* package */ void destroyOverlayView(Context context)
	{
    	WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	    
	    if (mLV != null )
	    {
	    	wm.removeViewImmediate(mLV);
	    	mLV = null;
	    	Log.i("prints","end of destroyOverlayView");
	    }
	}
	

	private Handler mHandler = new Handler() {
    	@Override
    	public void handleMessage (Message msg) {  
			createOverlayView(mContext);
    	}
	};
	

	public void click(float x,float y){
		//inject the click 
		
		destroyOverlayView(mContext);
		int posx = (int)x;
		int posy = (int) y;

		AccessibilityNodeInfoCompat node = findNode();
		if(node!=null){Log.i("prints", "node is not null "+posx+"  "+posy);}	
		AccessibilityNodeInfoCompat comp = findComponentClickable(node,posx, posy);

		if (comp != null) {
			Log.i("prints","inject click");
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
	
	private void findComponentScrollable(AccessibilityNodeInfoCompat root) {
		try {
			if(root.isScrollable()){
				Log.w("prints","isScrollable");
				scrollables.add(root);
			}

			for (int i = 0; i < root.getChildCount(); i++) {
				findComponentScrollable(root.getChild(i));
				}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
}
	
	private AccessibilityNodeInfoCompat findComponentClickable(AccessibilityNodeInfoCompat root, int posx, int posy) {
			try {
				Log.i("prints","findComponentClickable");
				Rect window = new Rect();
				AccessibilityNodeInfoCompat node = null;

				for (int i = 0; i < root.getChildCount(); i++) {
					root.getChild(i).getBoundsInScreen(window);
					if (window.contains(posx, posy)) {
						if (root.getChild(i).getChildCount() > 0) {
							node = findComponentClickable(root.getChild(i), posx,
									posy);
						}
						if (node == null && root.getChild(i).isClickable()) {
							node = root.getChild(i);

						}

					}

				}Log.i("prints","end of findComponentClickable");
		
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
		//The idea is that this notification allows you to switch off the service. Now you can only access the application information and stop it there.
	    
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
        .setContentText("Hola hola")
        .setSmallIcon(R.drawable.images)
        .setWhen(System.currentTimeMillis())
        .build();
        
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
        contentView.setImageViewResource(R.id.img_notification, R.drawable.images);
        contentView.setTextViewText(R.id.txt_notification, "Switch off TouchAccessibility.");
        
        notification.contentView = contentView;
       
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
				break;
			case CONTEXTUAL_MENU:
				Log.i("prints","CONTEXTUAL_MENU");
				contextual_menu(me);
				break;
			default:
				Log.i("prints","Any state");
				break;
				
		
			}
	}
	
	public void process_wait (MotionEvent me){
		h3.sendEmptyMessage(0);
		if (me.getAction()==MotionEvent.ACTION_DOWN){
			Log.i("prints","eventdown");
			current_state = states.WAIT_T1;
			timer=false;
			//We send a delayed message to know when the time is enough to click.
			h1.sendEmptyMessageDelayed(0,clickTime);
			//We send this delayed message to update the visual feedback
			h2.sendEmptyMessageDelayed(0, clickTimeSec);
			
			if(mFCV!=null){
				//The position of the finger to know visual feedback position
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
			//time is not enough to make any action
			Log.i("prints","eventup");
			timer=false;
			h1.removeMessages(0);
			deg=0;
			mFCV.setDeg(deg);
			h2.removeMessages(0);
			current_state = states.WAIT_EVENT;
			Log.i("prints","end wait t1");
		}
		if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(!isHomeButton((int)me.getX(),(int) me.getY()))&&(!isScrollForwardButton((int)me.getX(),(int) me.getY()))&&(!isScrollBackwardButton((int)me.getX(),(int) me.getY()))&&(!isBackButton((int)me.getX(),(int) me.getY()))){
			//Time is enough to click and there isn't any overlay button under the finger.
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
		if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(isScrollForwardButton((int)me.getX(),(int) me.getY()))){
			//The user has pressed enough time on the scroll forward button.
			Log.i("prints","eventup+click");
			timer=false;
			h1.removeMessages(0);
			deg=0;
			mFCV.setDeg(deg);
			h2.removeMessages(0);
			AccessibilityNodeInfoCompat node = findNode();
			AccessibilityNodeInfoCompat comp = findComponentClickable(node,(int) me.getX(), (int) me.getY());
			
			if(comp!=null){
				//There is a clickable component below. The contextual menu is going to be shown.
				button="scrollforward";
				mFCV.setMenuContextual(button);
				destroyFeedbackClickView(mContext);
				createFeedbackClickView(mContext);
				current_state = states.CONTEXTUAL_MENU;
				Log.i("prints","menu contextual");
				clickx=me.getX();
				clicky=me.getY();
				stateMachineProcessInput(null);
				
			
			}
			else{
				//There isn't any clickable component below, so the action is made.
				scrollableNode.performAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
				current_state = states.WAIT_EVENT;
				stateMachineProcessInput(me);
			}
			
		}
		
		if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(isScrollBackwardButton((int)me.getX(),(int) me.getY()))){
			//Time is enough and scroll backward button is pressed.
			Log.i("prints","eventup+click");
			timer=false;
			h1.removeMessages(0);
			deg=0;
			mFCV.setDeg(deg);
			h2.removeMessages(0);
			AccessibilityNodeInfoCompat node = findNode();
			AccessibilityNodeInfoCompat comp = findComponentClickable(node,(int) me.getX(), (int) me.getY());
			
			if(comp!=null){
				//There is a clickable component below. The contextual menu is going to be shown.
				button="scrollbackward";
				mFCV.setMenuContextual(button);
				destroyFeedbackClickView(mContext);
				createFeedbackClickView(mContext);
				current_state = states.CONTEXTUAL_MENU;
				Log.i("prints","menu contextual");
				clickx=me.getX();
				clicky=me.getY();
				stateMachineProcessInput(null);
				
			
			}
			else{
				//There isn't any clickable component below, so the action is made.
				scrollableNode.performAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
				current_state = states.WAIT_EVENT;
				stateMachineProcessInput(me);
			}
			
			
		}
		
		if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(isBackButton((int) me.getX(), (int) me.getY()))){
			//Time is enough and back button is pressed.
			timer=false;
			h1.removeMessages(0);
			deg=0;
			mFCV.setDeg(deg);
			h2.removeMessages(0);
			//destroyFeedbackClickView(mContext);
			AccessibilityNodeInfoCompat node = findNode();
			AccessibilityNodeInfoCompat comp = findComponentClickable(node,(int) me.getX(), (int) me.getY());
			
			if(comp!=null){
				//There is a clickable component below. The contextual menu is going to be shown.
				button="back";
				mFCV.setMenuContextual(button);
				destroyFeedbackClickView(mContext);
				current_state = states.CONTEXTUAL_MENU;
				Log.i("prints","menu contextual");
				clickx=me.getX();
				clicky=me.getY();
				stateMachineProcessInput(null);
				
			
			}
			
			else{
				//There isn't any clickable component below, so the action is made.
				destroyFeedbackClickView(mContext);
				Log.i("prints","actionhome");
				destroyOverlayView(mContext);
				performGlobalAction(this,GLOBAL_ACTION_BACK);
				mHandler.sendEmptyMessage(0);
				current_state = states.WAIT_EVENT;
				destroyFeedbackClickView(mContext);
			}
			
		}
		if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(isHomeButton((int) me.getX(), (int) me.getY()))){
			//Time is enough and back button is pressed.
			timer=false;
			h1.removeMessages(0);
			deg=0;
			mFCV.setDeg(deg);
			h2.removeMessages(0);
			//destroyFeedbackClickView(mContext);
			AccessibilityNodeInfoCompat node = findNode();
			AccessibilityNodeInfoCompat comp = findComponentClickable(node,(int) me.getX(), (int) me.getY());
			
			if(comp!=null){
				//There is a clickable component below. The contextual menu is going to be shown.
				button="home";
				mFCV.setMenuContextual(button);
				destroyFeedbackClickView(mContext);
				//createFeedbackClickView(mContext);
				current_state = states.CONTEXTUAL_MENU;
				Log.i("prints","menu contextual");
				clickx=me.getX();
				clicky=me.getY();
				stateMachineProcessInput(null);
				
			
			}
			
			else{
				//There isn't any clickable component below, so the action is made.
				destroyFeedbackClickView(mContext);
				Log.i("prints","actionhome");
				destroyOverlayView(mContext);
				performGlobalAction(this,GLOBAL_ACTION_HOME);
				mHandler.sendEmptyMessage(0);
				current_state = states.WAIT_EVENT;
				destroyFeedbackClickView(mContext);
			}
			
		}
		
	}
	public void contextual_menu (MotionEvent me){
		
		if (me==null){
			Log.i("prints","motionevent null");
			button=getButton();
			if(mFCV!=null){
				destroyFeedbackClickView(mContext);	
			}
			createFeedbackClickView(mContext);
			mFCV.setMenuContextual(button);
		}
		else{
			Log.i("prints","motion event no null");
			if (me.getAction()==MotionEvent.ACTION_DOWN){
				Log.w("prints","eventdown");
				timer=false;
				h4.sendEmptyMessageDelayed(0,clickTime);
				h2.sendEmptyMessageDelayed(0, clickTimeSec);
				mFCV.setXY((int) me.getX(), (int) me.getY());
			}
			if((me.getAction() == MotionEvent.ACTION_UP)&&(!timer)){
				//time is not enough to make any action
				Log.i("prints","eventup");
				timer=false;
				h4.removeMessages(0);
				deg=0;
				mFCV.setDeg(deg);
				h2.removeMessages(0);
				current_state = states.CONTEXTUAL_MENU;
			}
			
			if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(isCMGlobalOrScrollButton((int) me.getX(), (int) me.getY()))){
				//Time is enough and global or scroll option from contextual menu is pressed.
				Log.w("prints","HOME");
				timer=false;
				h4.removeMessages(0);
				deg=0;
				mFCV.setDeg(deg);
				h2.removeMessages(0);
				destroyOverlayView(mContext);
				if(getButton().equals("home")){
					performGlobalAction(this,GLOBAL_ACTION_HOME);
				}
				if(getButton().equals("back")){
					performGlobalAction(this,GLOBAL_ACTION_BACK);
				}
				if(getButton().equals("scrollbackward")){
					scrollableNode.performAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
				}
				if(getButton().equals("scrollforward")){
					scrollableNode.performAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
				}
				
				
				mHandler.sendEmptyMessage(0);
				current_state = states.WAIT_EVENT;
				button="clear";
				mFCV.setMenuContextual(button);
				destroyFeedbackClickView(mContext);
				stateMachineProcessInput(me);
			}
			if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(isCMClick((int) me.getX(), (int) me.getY()))){
				//Time is enough and click option from contextual menu is pressed.
				Log.w("prints","click");
				timer=false;
				h4.removeMessages(0);
				deg=0;
				mFCV.setDeg(deg);
				h2.removeMessages(0);
				click(clickx,clicky);
				current_state = states.WAIT_EVENT;
				button="clear";
				mFCV.setMenuContextual(button);
				destroyFeedbackClickView(mContext);
				stateMachineProcessInput(me);
				
			}
			if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(!isCMClick((int) me.getX(), (int) me.getY()))&&(!isCMGlobalOrScrollButton((int) me.getX(), (int) me.getY()))){
				//Time is enough and any option from contextual menu is pressed. Contextual menu is not shown anymore.
				Log.w("prints","menu contextual fuera");
				timer=false;
				h4.removeMessages(0);
				deg=0;
				mFCV.setDeg(deg);
				h2.removeMessages(0);
				current_state = states.WAIT_EVENT;
				button="clear";
				mFCV.setMenuContextual(button);
				destroyFeedbackClickView(mContext);
			}
		}
	}

	//Message to know if the time is enough to click.
	private Handler h1 = new Handler() {
    	@Override
    	public void handleMessage (Message msg) {  
    		Log.i("prints","2 seg");
			timer=true;
			current_state = states.WAIT_T1;
    	} 
	};
	
	//Update the visual feedback
	private Handler h2 = new Handler() {
    	@Override
    	public void handleMessage (Message msg) {  
    		if(deg<360){
    			mFCV.setDeg(18+deg);
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
	
	//Message to know if the time is enough to click and contextual menu must be shown.
	private Handler h4 = new Handler() {
    	@Override
    	public void handleMessage (Message msg) {  
    		Log.i("prints","2 seg");
			timer=true;
			current_state = states.CONTEXTUAL_MENU;
    	} 
	};
	
	//isHomeButton returns true if the position of the finger is over the home button on the overlay.
	public boolean isHomeButton(int posx, int posy){
		int right =  mLV.getWidth() - mLV.getPaddingRight();
	    int bottom = mLV.getHeight() - mLV.getPaddingBottom();
	    
		 if ((posx>=right-80)&&(posx<=right)&&(posy>=bottom-90)&&(posy<=bottom-10)){
			 return true;
		 }
		 else{
			 return false;
		 }
	}
	
	//isBackButton returns true if the position of the finger is over the back button on the overlay.
	public boolean isBackButton(int posx, int posy){
		Log.e("prints","Bb");
		int left =  mLV.getPaddingLeft();
	    int bottom = mLV.getHeight() - mLV.getPaddingBottom();
	    
		 if ((posx>=left)&&(posx<=left+80)&&(posy>=bottom-90)&&(posy<=bottom-10)){
			 return true;
		 }
		 else{
			 return false;
		 }
	}
	
	//isScrollForwardButton returns true if the position of the finger is over the forward scroll button on the overlay.
	public boolean isScrollForwardButton(int posx, int posy){

		int right =  mLV.getWidth() - mLV.getPaddingRight();
	    int bottom = mLV.getHeight() - mLV.getPaddingBottom();
	    
		Rect outBounds = new Rect ();
	    
	    for(int i=0;i<scrollables.size();i++){
	    	scrollables.get(i).getBoundsInScreen(outBounds);
	    	if((outBounds.right>right-90)&&(outBounds.bottom>bottom-90)){
	    		if((posx>=outBounds.right-80)&&(posx<=outBounds.right)&&(posy>=bottom-180)&&(posy<=bottom-100)){
	    			scrollableNode=scrollables.get(i);	
	    			return true;
	    		}
	    	}
       		else{
				 if ((posx>=outBounds.right-80)&&(posx<=outBounds.right)&&(posy>=outBounds.bottom-80)&&(posy<=outBounds.bottom-10)){
					 scrollableNode=scrollables.get(i);
					 return true;
				 }
       		}
	    }
	    return false;
	}
	
	//isScrollBackwardButton returns true if the position of the finger is over the backward scroll button on the overlay.
	public boolean isScrollBackwardButton (int posx, int posy){
	    
		Rect outBounds = new Rect ();
	    
	    for(int i=0;i<scrollables.size();i++){
	    	scrollables.get(i).getBoundsInScreen(outBounds);
	    	if ((posx>=outBounds.left)&&(posx<=outBounds.left+80)&&(posy>=outBounds.top)&&(posy<=outBounds.top+80)){
				scrollableNode=scrollables.get(i);
				return true;
			}
	    }
	    return false;
	}
	
	//isCMClick returns true if the position of the finger is over the click option of the contextual menu on the overlay.
	public boolean isCMClick(int posx, int posy){
		int right =  mLV.getWidth() - mLV.getPaddingRight();
	    int bottom = mLV.getHeight() - mLV.getPaddingBottom();
		 if ((posx>=right/2-120)&&(posx<=right/2+120)&&(posy>=bottom/2+20)&&(posy<=bottom/2+260)){
			 return true;
		 }
		 else{
			 return false;
		 }
	}
	
	//isCMGlobalOrScrollButton returns true if the position of the finger is over the global or scroll option of the contextual menu on the overlay.
	public boolean isCMGlobalOrScrollButton(int posx, int posy){
		int right =  mLV.getWidth() - mLV.getPaddingRight();
	    int bottom = mLV.getHeight() - mLV.getPaddingBottom();
		 if ((posx>=right/2-120)&&(posx<=right/2+120)&&(posy>=bottom/2-260)&&(posy<=bottom/2-20)){
			 return true;
		 }
		 else{
			 return false;
		 }
	}
	
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
	
	public void showScrollButtons(){
		scrollables.clear();
		Log.i("prints","Window content changed");
		AccessibilityNodeInfoCompat node = findNode();
		if(node!=null){Log.i("prints", "el node x scrollable no es null ");}
		else {
			Log.w("prints","el node x scrollable es null");
		}
		Log.i("prints",""+scrollables.size());
        findComponentScrollable(node);
        createFeedbackClickView(mContext);
        if (scrollables!=null){
        	mFCV.setScrollableAreas(scrollables);
        }
	}
	public String getButton (){
		return button;
	}


	
	}




