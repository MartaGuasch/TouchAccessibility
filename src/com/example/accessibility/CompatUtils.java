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

	import android.text.TextUtils;
	import android.util.Log;

	import java.lang.reflect.Constructor;
	import java.lang.reflect.Field;
	import java.lang.reflect.Method;

	public class CompatUtils {
	    private static final String TAG = CompatUtils.class.getSimpleName();

	    public static Class<?> getClass(String className) {
	        try {
	            return Class.forName(className);
	        } catch (ClassNotFoundException e) {
	            return null;
	        }
	    }

	    public static Method getMethod(Class<?> targetClass, String name,
	            Class<?>... parameterTypes) {
	        if (targetClass == null || TextUtils.isEmpty(name))
	            return null;
	        try {
	            return targetClass.getMethod(name, parameterTypes);
	        } catch (SecurityException e) {
	            // ignore
	        } catch (NoSuchMethodException e) {
	            // ignore
	        }
	        return null;
	    }

	    public static Field getField(Class<?> targetClass, String name) {
	        if (targetClass == null || TextUtils.isEmpty(name))
	            return null;
	        try {
	            return targetClass.getDeclaredField(name);
	        } catch (SecurityException e) {
	            // ignore
	        } catch (NoSuchFieldException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    public static Constructor<?> getConstructor(Class<?> targetClass, Class<?>... types) {
	        if (targetClass == null || types == null)
	            return null;
	        try {
	            return targetClass.getConstructor(types);
	        } catch (SecurityException e) {
	            // ignore
	        } catch (NoSuchMethodException e) {
	            // ignore
	        }
	        return null;
	    }

	    public static Object newInstance(Constructor<?> constructor, Object... args) {
	        if (constructor == null)
	            return null;
	        try {
	            return constructor.newInstance(args);
	        } catch (Exception e) {
	            Log.e(TAG, "Exception in newInstance: " + e.getClass().getSimpleName());
	        }
	        return null;
	    }

	    public static Object invoke(
	            Object receiver, Object defaultValue, Method method, Object... args) {
	        if (method == null)
	            return defaultValue;
	        try {
	            return method.invoke(receiver, args);
	        } catch (Exception e) {
	            Log.e(TAG, "Exception in invoke: " + e.getClass().getSimpleName());
	            e.printStackTrace();
	        }
	        return defaultValue;
	    }

	    public static Object getFieldValue(Object receiver, Object defaultValue, Field field) {
	        if (field == null)
	            return defaultValue;
	        try {
	            return field.get(receiver);
	        } catch (Exception e) {
	            Log.e(TAG, "Exception in getFieldValue: " + e.getClass().getSimpleName());
	        }
	        return defaultValue;
	    }

	    public static void setFieldValue(Object receiver, Field field, Object value) {
	        if (field == null)
	            return;
	        try {
	            field.set(receiver, value);
	        } catch (Exception e) {
	            Log.e(TAG, "Exception in setFieldValue: " + e.getClass().getSimpleName());
	        }
	    }

	    private CompatUtils() {
	        // This class is non-instantiable.
	    }
	}

