package com.nick.permissiondemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //安卓6.0,API23以后  应用在使用敏感权限时,需要向用户申请权限,才能继续使用敏感权限
        //如果API版本高于23,则往下走
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断是否有这个权限,
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED) {
                //shouldShowRequestPermissionRationale这个方法,应用安装后第一次调用时,返回false,
                //当在第一次申请权限时,用户拒绝后,下次申请时,这个方法返回true,此时可以提示用户为什么需要这个权限,
                // 如果用户一直拒绝且没有点击不在提示时,这个方法仍然返回true
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                    Log.i(TAG, "拒绝申请后显示 ");
                    Toast.makeText(MainActivity.this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
                } else {
                    //当用户在第二次申请时,并再次拒绝并点击不再提示时,这个方法返回false
                    Log.i(TAG, "不在提示后显示显示 ");
                    Toast.makeText(MainActivity.this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
                }
                //申请权限
                //第一个参数是需要申请的所有权限,第二个参数是请求码
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                Log.i(TAG, "申请权限");
                Toast.makeText(MainActivity.this, "申请权限", Toast.LENGTH_SHORT).show();
            } else {
                //已有权限走这里
                Toast.makeText(MainActivity.this, "已有权限", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "已有权限");
            }
        } else {
            //如果版本低于API23,则不需要申请权限
            Toast.makeText(MainActivity.this, "版本低于6.0不用申请", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "版本低于6.0不用申请");
        }


    }

    /**
     * 申请权限是系统的自行调用的异步申请,申请结果需要重写onRequestPermissionsResult,在此判断权限是否申请成功
     * @param requestCode  请求码
     * @param permissions  申请的所有权限
     * @param grantResults  申请的权限返回的结果码  0为申请成功  -1为申请成功
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println(requestCode + ":" + Arrays.toString(permissions) + ":" + Arrays.toString(grantResults));
    }
}
