package com.example.accessibility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.view.View;

 public class AccessibilitySettings extends Activity {
	 private static final String PREFERENCES = "preferences";
	 Spinner spinner;
	 SharedPreferences shpreferences;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);
        
        spinner = (Spinner) findViewById(R.id.Spinner);
        spinner.setSelection(1,false);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener (){
        	public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Editor ed = shpreferences.edit();
				Log.d("AccService","itemSelected: " + getResources().getStringArray(R.array.pref_time)[arg2]);
				ed.putString("longclick",
						getResources().getStringArray(R.array.pref_time)[arg2]);
				ed.commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
        });
       
    }
   
}