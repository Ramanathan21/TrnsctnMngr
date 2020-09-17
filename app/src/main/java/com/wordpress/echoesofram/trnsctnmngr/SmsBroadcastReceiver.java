package com.wordpress.echoesofram.trnsctnmngr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static MessageListener listener;
    private static final String TAG = "SmsBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder smsBody = new StringBuilder();
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String smsSender = "";

            Bundle smsBundle = intent.getExtras();
            if (smsBundle != null) {
                Object[] pdus = (Object[]) smsBundle.get("pdus");
                if (pdus == null) {
                    Log.e(TAG, "SmsBundle had no pdus key");
                    return;
                }
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < messages.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    smsBody.append(messages[i].getMessageBody());
                }
                smsSender = messages[0].getOriginatingAddress();
            }
        }
        if (listener != null) {
            // TODO: currently only the sms content is sent. You shall add smsSender to this if required
            listener.messageReceived(smsBody.toString());
        }

    }

    public static void bindListener(MessageListener messageListener) {
        listener = messageListener;
    }
}

interface MessageListener {
    void messageReceived(String message);
}