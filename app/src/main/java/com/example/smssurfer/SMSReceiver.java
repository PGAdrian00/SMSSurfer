package com.example.smssurfer;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.text.SimpleDateFormat;

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG =  SMSReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";

    public DBManager dbManager;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        dbManager = DBManagerSingleton.getDBManager(context);

        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM =
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                String timestamp = new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
                String shortdate = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
                String longdate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());

                dbManager.addNewMessage(msgs[i].getOriginatingAddress(), msgs[i].getMessageBody(),1, timestamp,shortdate,longdate);
            }
    }
}
}

