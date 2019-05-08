//package com.example.callautoreceiver.receiver;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.telephony.PhoneStateListener;
//import android.telephony.TelephonyManager;
//
//import com.example.callautoreceiver.inter.CallPhoneCallback;
//
//public class ServiceReceiver extends BroadcastReceiver {
//    private CallPhoneCallback callPhone;
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        callPhone = (CallPhoneCallback) context;
//        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        telephony.listen(new PhoneStateListener() {
//            @Override
//            public void onCallStateChanged(int state, String incomingNumber) {
//                super.onCallStateChanged(state, incomingNumber);
//                callPhone.getPhone(state, incomingNumber);
//            }
//        }, PhoneStateListener.LISTEN_CALL_STATE);
//    }
//}
