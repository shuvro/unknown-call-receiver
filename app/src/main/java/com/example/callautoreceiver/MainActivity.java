package com.example.callautoreceiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    TextView textView;
    Button button;
    BroadcastReceiver broadcastReceiver;
    String phoneNumber;
    int state = -1;
    List<String[]> numberList = new ArrayList();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);

        textView = findViewById(R.id.textview);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickCall();
                //Toast.makeText(MainActivity.this, "Picking", Toast.LENGTH_SHORT).show();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                numberList = ServiceData.getContact(MainActivity.this);
            }
        }, 500);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                telephony.listen(new PhoneStateListener() {
                    @Override
                    public void onCallStateChanged(int state1, String incomingNumber) {
                        super.onCallStateChanged(state1, incomingNumber);
                        textView.setText("No Incoming");
                        state = state1;
                        phoneNumber = incomingNumber;
                        if (incomingNumber != null) {
                            boolean isNotSave = true;
                            for (String[] strings : numberList) {
                                if (ServiceData.pureNumber(incomingNumber.replace("-", ""), strings[1].replace("-", ""))) {
                                    textView.setText(strings[0] + " Calling");
                                    isNotSave = false;
                                    break;
                                }
                            }
                            if (isNotSave) {
                                textView.setText(incomingNumber);
                                button.callOnClick();
                            }
                        }
                    }
                }, PhoneStateListener.LISTEN_CALL_STATE);
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.PHONE_STATE"));
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.PHONE_STATE"));
    }


    private void pickCall() {
        try {
            Runtime.getRuntime().exec("input keyevent " + Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));

        } catch (IOException e) {
            try {
                Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
                localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                localIntent1.putExtra("state", 1);
                localIntent1.putExtra("microphone", 0);
                localIntent1.putExtra("name", "Headset");
                sendOrderedBroadcast(localIntent1, "android.permission.CALL_PRIVILEGED");

                Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
                localIntent2.putExtra(Intent.EXTRA_KEY_EVENT, localKeyEvent1);
                sendOrderedBroadcast(localIntent2, "android.permission.CALL_PRIVILEGED");

                Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                localIntent3.putExtra(Intent.EXTRA_KEY_EVENT, localKeyEvent2);
                sendOrderedBroadcast(localIntent3, "android.permission.CALL_PRIVILEGED");

                Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
                localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                localIntent4.putExtra("state", 0);
                localIntent4.putExtra("microphone", 0);
                localIntent4.putExtra("name", "Headset");
                sendOrderedBroadcast(localIntent4, "android.permission.CALL_PRIVILEGED");
                pickCall();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        /*switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                Toast.makeText(MainActivity.this, "Call Ended..", Toast.LENGTH_LONG).show();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:

                int outputBufferSize = AudioTrack.getMinBufferSize(160, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
                AudioTrack track = new AudioTrack(
                        AudioManager.STREAM_VOICE_CALL,
                        160,
                        AudioFormat.CHANNEL_CONFIGURATION_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        outputBufferSize,
                        AudioTrack.MODE_STREAM
                );
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                break;
        }*/
    }
}
