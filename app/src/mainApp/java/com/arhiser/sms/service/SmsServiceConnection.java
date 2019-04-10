package com.arhiser.sms.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;

import com.arhiser.sms.module.ISMSAppInterface;

public class SmsServiceConnection {

    private ISMSAppInterface smsInterface;

    private ServiceConnection connection;

    private Context context;

    private boolean isConnected;

    public SmsServiceConnection(Context context) {
        this.context = context;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void connect(Runnable doOnConnection) {
        if (!isConnected) {
            connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    isConnected = true;
                    smsInterface = ISMSAppInterface.Stub.asInterface(iBinder);
                    if (doOnConnection != null) {
                        doOnConnection.run();
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isConnected = false;
                }
            };
        }
        context.bindService(getIntentForService(), connection, Context.BIND_AUTO_CREATE);
    }

    public boolean hasPermissions() {
        if (isConnected) {
            try {
                return smsInterface.hasPermissions();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean sendSms(String phone, String body) {
        if (isConnected) {
            try {
                smsInterface.sendSms(phone, body);
                return true;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public int getModuleVersion() {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo("com.arhiser.sms.module", 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    public void requestPermissionActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.arhiser.sms.module",
                "com.arhiser.sms.SmsMainActivity"));
        activity.startActivity(intent);
    }

    private Intent getIntentForService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.arhiser.sms.module",
                "com.arhiser.sms.service.SmsService"));
        return intent;
    }
}
