// ISMSAppInterface.aidl
package com.arhiser.sms.module;

// Declare any non-default types here with import statements

interface ISMSAppInterface {

    boolean hasPermissions();

    void sendSms(String phone, String body);
}
