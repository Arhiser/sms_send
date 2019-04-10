package com.arhiser.sms.service;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;

import com.arhiser.sms.module.ISMSAppInterface;

import java.util.ArrayList;

public class SmsModule extends ISMSAppInterface.Stub {

    Context context;

    public SmsModule(Context context) {
        this.context = context;
    }

    @Override
    public boolean hasPermissions() throws RemoteException {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void sendSms(String phone, String body) throws RemoteException {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(body);
        if (parts.size() > 1) {
            smsManager.sendMultipartTextMessage(phone, null, parts,
                    null, null);
        } else {
            SmsManager.getDefault().sendTextMessage(phone, null, body,
                    null, null);
        }
    }
}
