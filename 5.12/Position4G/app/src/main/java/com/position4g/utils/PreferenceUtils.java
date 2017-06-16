package com.position4g.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.position4g.MyApp;

public class PreferenceUtils {
	static Context mContext =MyApp.getInstance();

	// 设置String的内容
		public static void putString(String key, String value) {
			SharedPreferences sp = mContext.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			sp.edit().putString(key, value).commit();
		}

		// 获取String的内容
		public static String getString(String key, String defvalue) {
			SharedPreferences sp = mContext.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			return sp.getString(key, defvalue);
		}

		// 获取String的内容
		public static String getString(String key) {
			return getString( key, "");
		}

		// boolean:设置
		public static void putBoolean(String key, boolean value) {
			SharedPreferences sp = mContext.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			sp.edit().putBoolean(key, value).commit();
		}

		// 获取boolean的值
		public static boolean getBoolean(String key,
				boolean defvalue) {
			SharedPreferences sp = mContext.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			return sp.getBoolean(key, defvalue);
		}

		// 获取boolean的值
		public static boolean getBoolean(String key) {
			return getBoolean(key, false);
		}

		// int
		public static void putInt(String key, int value) {
			SharedPreferences sp = mContext.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			sp.edit().putInt(key, value).commit();
		}

		public static int getInt(String key, int defvalue) {
			SharedPreferences sp = mContext.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			return sp.getInt(key, defvalue);
		}

		public static int getInt(String key) {
			return getInt(key,-1);
		}
		
		//删除数据 记得要commit
		public static void clearData(){
			SharedPreferences sp = mContext.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			sp.edit().clear().commit();
		}
}
