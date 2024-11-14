package com.example.trialapp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NoiseBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "NoiseBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Not needed as VoiceRecognitionService now handles launching the MainActivity directly.
        Log.d(TAG, "Received noise detection broadcast.");
    }
}
