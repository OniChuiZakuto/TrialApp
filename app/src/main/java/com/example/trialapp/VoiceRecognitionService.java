package com.example.trialapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import java.util.ArrayList;

public class VoiceRecognitionService extends Service {
    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private static final String CHANNEL_ID = "VoiceRecognitionChannel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start the service as a foreground service with a notification
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Voice Recognition Service")
                .setContentText("Listening for 'BERT'")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        startForeground(1, notification);

        startListening();
        return START_STICKY;
    }

    private void startListening() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d("VoiceRecognitionService", "Ready for speech");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d("VoiceRecognitionService", "Beginning of speech");
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // Monitor the volume changes during speech
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
                Log.d("VoiceRecognitionService", "End of speech");
            }

            @Override
            public void onError(int error) {
                Log.e("VoiceRecognitionService", "Error occurred: " + error);
                startListening();  // Restart listening on error
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    for (String result : matches) {
                        Log.d("VoiceRecognitionService", "Heard: " + result);
                        if (result.equalsIgnoreCase("BERT")) {
                            activateApp();
                        }
                    }
                }
                startListening();  // Restart listening after results
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });

        speechRecognizer.startListening(recognizerIntent);
    }

    private void activateApp() {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.yourapp");
        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(launchIntent);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }

    // Create the notification channel required for the foreground service
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Voice Recognition Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
