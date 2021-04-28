package com.kobe.lib_base;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    //申请运行时权限的Code
    private static final int PERMISSION_REQUEST_CODE = 1000;

    //申明所需权限
    private final String[] mPermissions = {
            Manifest.permission.CAMERA,
    };

    //保存没有同意的权限
    private final List<String> mPermissionList = new ArrayList<>();
    //保存没有同意的失败权限
    private final List<String> mPermissionFailedList = new ArrayList<>();

    private OnPermissionsResultListener mOnPermissionsResultListener;


    /**
     * 一个方法请求权限
     */
    protected void requestAll(OnPermissionsResultListener onPermissionsResultListener) {
        if (!checkAllPermissions()) {
            requestAllPermissions(onPermissionsResultListener);
        }
    }

    /**
     * 判断单个权限
     */
    protected boolean checkPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int check = checkSelfPermission(permission);
            return check == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    /**
     * 判断是否需要申请权限
     */
    protected boolean checkAllPermissions() {
        mPermissionList.clear();
        for (String mPermission : mPermissions) {
            boolean check = checkPermission(mPermission);
            //如果不同意则请求
            if (!check) {
                mPermissionList.add(mPermission);
            }
        }
        return mPermissionList.size() <= 0;
    }

    /**
     * 请求权限
     */
    protected void requestPermission(String[] permissions, OnPermissionsResultListener onPermissionsResultListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.mOnPermissionsResultListener = onPermissionsResultListener;
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 请求权限
     */
    protected void requestPermission(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 申请所有权限
     */
    protected void requestAllPermissions(OnPermissionsResultListener onPermissionsResultListener) {
        this.mOnPermissionsResultListener = onPermissionsResultListener;
        requestPermission(mPermissionList.toArray(new String[mPermissionList.size()]));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mPermissionFailedList.clear();
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        //你有失败的权限
                        mPermissionFailedList.add(permissions[i]);
                    }
                }
                if (mOnPermissionsResultListener != null) {
                    if (mPermissionFailedList.size() == 0) {
                        mOnPermissionsResultListener.OnSuccess();
                    } else {
                        mOnPermissionsResultListener.OnFail(mPermissionFailedList);
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected interface OnPermissionsResultListener {
        void OnSuccess();

        void OnFail(List<String> failedPermissionList);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideKeyboard(v);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    //隐藏虚拟键盘
    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

}
