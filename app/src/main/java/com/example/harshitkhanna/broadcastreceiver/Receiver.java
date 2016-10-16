package com.example.harshitkhanna.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {

    String TAG="BCRECEIVER";
    static int prev_state=0;

    public Receiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()=="android.intent.action.PHONE_STATE") {
            String st = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (st.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                Toast.makeText(context, "OFFHOOK", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Off hook");
                prev_state = 2;
            } else if (st.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//            Log.i(TAG,st);
                Log.i(TAG, intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));
                String str = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Toast.makeText(context, "RINGING", Toast.LENGTH_SHORT).show();
                prev_state = 1;
            } else if (st.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                Log.i(TAG, st);
                Toast.makeText(context, "IDLE", Toast.LENGTH_SHORT).show();
                if (prev_state == 2) {
                    Log.i(TAG, "yo inside!");
                    Intent intent1 = new Intent(context.getApplicationContext(), MainActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
                prev_state = 0;
            }

//        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//        telephony.listen(new PhoneStateListener(){
//            @Override
//            public void onCallStateChanged(int state, String incomingNumber) {
//                super.onCallStateChanged(state, incomingNumber);
//                System.out.println(state+ " incomingNumber : "+incomingNumber);
//            }
//        }, PhoneStateListener.LISTEN_CALL_STATE);
        }
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
//            Log.i(TAG,intent.getStringExtra())
            Bundle bundle = intent.getExtras();

            SmsMessage[] msgs = null;

            String str = "";

            if (bundle != null) {
                // Retrieve the SMS Messages received
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];

                // For every SMS message received
                for (int i=0; i < msgs.length; i++) {
                    // Convert Object array
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    // Sender's phone number
                    str += "SMS from " + msgs[i].getOriginatingAddress() + " : ";
                    // Fetch the text message
                    str += msgs[i].getMessageBody().toString();
                    // Newline 🙂
                    str += "\n";
                }

                // Display the entire SMS Message
                Log.d(TAG, str);
        }
    }
}
}
