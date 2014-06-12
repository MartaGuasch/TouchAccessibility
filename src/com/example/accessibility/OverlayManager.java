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
	//private OnTouchListener OTL;
	//private long tsTempDown;
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
	private List <AccessibilityNodeInfoCompat> scrollables = new ArrayList <AccessibilityNodeInfoCompat> ();
	private AccessibilityNodeInfoCompat scrollableNode;

    //int right =  LV.getWidth() - LV.getPaddingRight();
    //int bottom = LV.getHeight() - LV.getPaddingBottom();
	
	public enum states{
		WAIT_EVENT,
		WAIT_T1,
		CONTEXTUAL_MENU
	}
	
	@Override
    public void onServiceConnected() {
		Log.i("prints","entra onServiceConnected del Overlay");
		triggerNotification();
		createOverlayView(mContext);
		//showScrollButtons();
		
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
		
		Log.i("prints","on touch "+current_state);
		stateMachineProcessInput(event);
		
		
        return true;
    }
	

	ListenerView getListenerView() {
		return mLV;
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
        feedbackParams.type = LayoutParams.TYPE_PHONE	| LayoutParams.TYPE_SYSTEM_OVERLAY; 
        
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
	    	Log.i("prints","acaba destroyFeedbackClickView");
	    }
	}
	/* package */ void destroyOverlayView(Context context)
	{
    	WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	    
	    if (mLV != null )
	    {
	    	wm.removeViewImmediate(mLV);
	    	mLV = null;
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
	
	private void findComponentScrollable(AccessibilityNodeInfoCompat root) {
		//scrollables.clear();
		try {
			if(root.isScrollable()){
				Log.w("prints","isScrollable");
				scrollables.add(root);
				//return root;
			}

			for (int i = 0; i < root.getChildCount(); i++) {
				findComponentScrollable(root.getChild(i));
				}

		} catch (Exception e) {
			e.printStackTrace();
			//return null;
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
			case CONTEXTUAL_MENU:
				Log.i("prints","HOME_CONTEXTUAL_MENU");
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
			//createFeedbackClickView(mContext);
			Log.i("prints","eventdown");
			current_state = states.WAIT_T1;
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
			//destroyFeedbackClickView(mContext);
			Log.i("prints","end wait t1");
		}
		if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(!isHomeButton((int)me.getX(),(int) me.getY()))&&(!isScrollForwardButton((int)me.getX(),(int) me.getY()))&&(!isScrollBackwardButton((int)me.getX(),(int) me.getY()))&&(!isBackButton((int)me.getX(),(int) me.getY()))){
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
		if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(isScrollForwardButton((int)me.getX(),(int) me.getY()))){
			Log.i("prints","eventup+click");
			timer=false;
			h1.removeMessages(0);
			deg=0;
			mFCV.setDeg(deg);
			h2.removeMessages(0);
			AccessibilityNodeInfoCompat node = findNode();
			AccessibilityNodeInfoCompat comp = findComponentClickable(node,(int) me.getX(), (int) me.getY());
			
			if(comp!=null){
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
				scrollableNode.performAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
				current_state = states.WAIT_EVENT;
				stateMachineProcessInput(me);
			}
			
		}
		
		if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(isScrollBackwardButton((int)me.getX(),(int) me.getY()))){
			Log.i("prints","eventup+click");
			timer=false;
			h1.removeMessages(0);
			deg=0;
			mFCV.setDeg(deg);
			h2.removeMessages(0);
			AccessibilityNodeInfoCompat node = findNode();
			AccessibilityNodeInfoCompat comp = findComponentClickable(node,(int) me.getX(), (int) me.getY());
			
			if(comp!=null){
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
				scrollableNode.performAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
				current_state = states.WAIT_EVENT;
				stateMachineProcessInput(me);
			}
			
			
		}
		
		if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(isBackButton((int) me.getX(), (int) me.getY()))){
			timer=false;
			h1.removeMessages(0);
			deg=0;
			mFCV.setDeg(deg);
			h2.removeMessages(0);
			//destroyFeedbackClickView(mContext);
			AccessibilityNodeInfoCompat node = findNode();
			AccessibilityNodeInfoCompat comp = findComponentClickable(node,(int) me.getX(), (int) me.getY());
			if(comp!=null){
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
			timer=false;
			h1.removeMessages(0);
			deg=0;
			mFCV.setDeg(deg);
			h2.removeMessages(0);
			//destroyFeedbackClickView(mContext);
			AccessibilityNodeInfoCompat node = findNode();
			AccessibilityNodeInfoCompat comp = findComponentClickable(node,(int) me.getX(), (int) me.getY());
			if(comp!=null){
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
				//if(mFCV!=null){
				mFCV.setXY((int) me.getX(), (int) me.getY());
			}
			if((me.getAction() == MotionEvent.ACTION_UP)&&(!timer)){
				Log.i("prints","eventup");
				timer=false;
				h4.removeMessages(0);
				deg=0;
				mFCV.setDeg(deg);
				h2.removeMessages(0);
				current_state = states.CONTEXTUAL_MENU;
			}
			
			if((me.getAction() == MotionEvent.ACTION_UP)&&(timer)&&(isCMGlobalOrScrollButton((int) me.getX(), (int) me.getY()))){
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
    		Log.i("prints","2 seg");
			timer=true;
			current_state = states.CONTEXTUAL_MENU;
    	} 
	};
	
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




