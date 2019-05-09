package com.example.callautoreceiver;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    TextView textView;
    Button button;
    BroadcastReceiver broadcastReceiver;
    String phoneNumber;
    int state = -1;
    List<String[]> numberList = new ArrayList();
    DevicePolicyManager mDPM;
    ComponentName mAdminName;
    int REQUEST_CODE = 0;
    MediaRecorder recorder;
    File audiofile;
    boolean recordstarted = false;
    static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CHANGE_NETWORK_STATE}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WAKE_LOCK}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);

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
                record();
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
                                textView.setText(incomingNumber.length() > 0 ? incomingNumber : "No Incoming");
                                button.callOnClick();
                            }
                        }
                    }
                }, PhoneStateListener.LISTEN_CALL_STATE);
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.PHONE_STATE"));

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_IN);
        this.registerReceiver(new CallBr(), filter);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.PHONE_STATE"));
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_IN);
        this.registerReceiver(new CallBr(), filter);
    }


    private void pickCall() {
        try {
            Runtime.getRuntime().exec("input keyevent " + Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));

        } catch (IOException e) {
            try {
                Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
                localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                localIntent1.putExtra("state", 1);
                localIntent1.putExtra("microphone", 1);
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
                localIntent4.putExtra("microphone", 1);
                localIntent4.putExtra("name", "Headset");
                sendOrderedBroadcast(localIntent4, "android.permission.CALL_PRIVILEGED");


            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void record() {
        try {
            mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            mAdminName = new ComponentName(this, DeviceAdminDemo.class);

            if (!mDPM.isAdminActive(mAdminName)) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to secure your application.");
                startActivityForResult(intent, REQUEST_CODE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public class CallBr extends BroadcastReceiver {
        Bundle bundle;
        String state;
        String inCall;
        public boolean wasRinging = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_IN)) {
                if ((bundle = intent.getExtras()) != null) {
                    state = bundle.getString(TelephonyManager.EXTRA_STATE);
                    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        inCall = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        wasRinging = true;
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                        if (wasRinging == true) {
                            if (phoneNumber != null && phoneNumber.length() > 1) {
                                boolean isNotSave = true;
                                for (String[] strings : numberList) {
                                    if (ServiceData.pureNumber(phoneNumber.replace("-", ""), strings[1].replace("-", ""))) {
                                        isNotSave = false;
                                        break;
                                    }
                                }
                                if (isNotSave) {
                                    startRecord("incoming");
                                }
                            }
                        }
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        wasRinging = false;
                        if (recordstarted) {
                            recorder.stop();
                            recordstarted = false;
                        }
                        new AsyncTask<Void, String, String>() {
                            @Override
                            protected void onPreExecute() {
                                try {
                                    File ff = new File(Environment.getExternalStorageDirectory(), "/unknown-call-recoded");
                                    File[] li = ff.listFiles();
                                    for (File f : li) {
                                        int file_size = Integer.parseInt(String.valueOf(f.length() / 1024));
                                        if (file_size == 0) {
                                            f.delete();
                                        }
                                    }
                                } catch (Exception ex) {
                                }
                            }

                            @Override
                            protected String doInBackground(Void... params) {
                                return null;
                            }

                            @Override
                            protected void onPostExecute(String msg) {
                            }
                        }.execute(null, null, null);
                    }
                }
            }
        }

        private void startRecord(String seed) {
            File sampleDir = new File(Environment.getExternalStorageDirectory(), "/unknown-call-recoded");
            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }
            String file_name = "Record-" + phoneNumber != null ? phoneNumber.replaceAll("\\+|-", "") : seed;
            try {
                audiofile = File.createTempFile(file_name, ".amr", sampleDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
            recorder = new MediaRecorder();

            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(audiofile.getAbsolutePath());
            try {
                recorder.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                recorder.start();
                recordstarted = true;
            } catch (Exception ex) {
                recordstarted = false;
            }
        }
    }
}