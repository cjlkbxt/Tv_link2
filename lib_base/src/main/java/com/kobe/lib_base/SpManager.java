package com.kobe.lib_base;

import android.content.Context;
import android.content.SharedPreferences;

public class SpManager {
    private final SharedPreferences mSp;
    private final SharedPreferences.Editor mEditor;

    private static SpManager mSpManager = null;

    private SpManager(Context context) {
        mSp = context.getApplicationContext().getSharedPreferences("sys_setting", 0);
        mEditor = mSp.edit();
    }

    public static SpManager getInstance(Context context) {
        if (mSpManager == null) {
            mSpManager = new SpManager(context);
        }
        return mSpManager;
    }

    public static SpManager getInstance() {
        return mSpManager;
    }

    public void put(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void put(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void put(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public void put(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public String get(String key, String defValue) {
        return mSp.getString(key, defValue);
    }

    public int get(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }

    public long get(String key, long defValue) {
        return mSp.getLong(key, defValue);
    }

    public boolean get(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }
}
