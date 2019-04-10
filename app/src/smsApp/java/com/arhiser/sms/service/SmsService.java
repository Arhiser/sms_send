package com.arhiser.sms.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class SmsService extends Service {

    private static SmsModule smsModule;

    public static SmsModule getSmsModule() {
        return smsModule;
    }

    public SmsService() {
        smsModule = new SmsModule(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return smsModule;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}