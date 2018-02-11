package com.sharenew.screenoff;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button buttonOff;
    Button buttonOn;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    turnOnScreen();
                    break;
                case 2:

                    break;
            }
        }
    };
    DevicePolicyManager policyManager;
    ComponentName adminReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonOff = (Button) findViewById(R.id.button_off);
        adminReceiver = new ComponentName(MainActivity.this, ScreenOffAdminReceiver.class);
        buttonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                boolean admin = policyManager.isAdminActive(adminReceiver);
                if (admin) {
                    policyManager.lockNow();
                    handler.sendEmptyMessageDelayed(1,3000);
                } else {
                    Toast.makeText(MainActivity.this,"没有设备管理权限",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        buttonOn = (Button) findViewById(R.id.button_on);
        buttonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnScreen();
            }
        });
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        policyManager = (DevicePolicyManager) MainActivity.this.getSystemService(Context.DEVICE_POLICY_SERVICE);

        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,  adminReceiver);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"开启后就可以使用锁屏功能了...");//显示位置见图二

        startActivityForResult(intent, 0);
    }


    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;

    public void turnOnScreen() {
        // turn on screen
        Log.v("ProximityActivity", "ON!");
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
        mWakeLock.acquire();
        mWakeLock.release();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isOpen();
    }

    /**
     * 检测用户是否开启了超级管理员
     */
    private void isOpen() {
        if(policyManager.isAdminActive(adminReceiver)){//判断超级管理员是否激活

            Toast.makeText(MainActivity.this,"设备已被激活",
                    Toast.LENGTH_LONG).show();

        }else{

            Toast.makeText(MainActivity.this,"设备没有被激活",
                    Toast.LENGTH_LONG).show();

        }
    }






}
