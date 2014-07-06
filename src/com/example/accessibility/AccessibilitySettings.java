/**
 * TouchAccessibitily, an accessibility service that helps people with cerebral palsy to access Android devices. You can only click if you press more than two seconds.
 * 
 * Copyright (C) 2014 Marta Guasch
 * 
 * This file is part of TouchAccessibility.
 * 
 * TouchAccessibility is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * TouchAccessibility is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * See the GNU General Public License for more details. You should have received a copy of the GNU
 * General Public License along with TouchAccessibility. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Authors: TODO
 */

package com.example.accessibility;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

 public class AccessibilitySettings extends PreferenceActivity {
   
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }
    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}