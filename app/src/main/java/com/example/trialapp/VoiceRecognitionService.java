package com.example.trialapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import android.content.pm.PackageManager;
import android.Manifest;

public class VoiceRecognitionService extends Service {
    private static final String TAG = "VoiceRecognitionService";
    private static final String CHANNEL_ID = "VoiceRecognitionChannel";
    private static final int SAMPLE_RATE = 44100; // Sample rate for recording
    private static final int BUFFER_SIZE = 4096; // Buffer size for recording
    private static final int THRESHOLD = 1000; // Set your threshold for noise detection
    private AudioRecord audioRecord;
    private boolean isRecording = false;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForegroundService();
        // Check for permissions before starting the noise detection
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startNoiseDetection();
        } else {
            Log.e(TAG, "Permission for RECORD_AUDIO not granted");
            stopSelf(); // Stop the service if permission is not granted
        }
    }

    private void startForegroundService() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Voice Recognition")
                .setContentText("Listening for voice commands...")
                .setSmallIcon(android.R.drawable.ic_menu_camera) // Use a default drawable for testing
                .build();
        startForeground(1, notification);
    }

    private void startNoiseDetection() {
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);

        isRecording = true;
        audioRecord.startRecording();

        new Thread(() -> {
            short[] buffer = new short[BUFFER_SIZE];
            while (isRecording) {
                int readSize = audioRecord.read(buffer, 0, buffer.length);
                if (readSize > 0) {
                    int amplitude = 0;
                    for (short s : buffer) {
                        amplitude += Math.abs(s);
                    }
                    amplitude /= readSize;

                    if (amplitude > THRESHOLD) {
                        // Loud noise detected
                        Log.d(TAG, "Loud noise detected, broadcasting...");
                        Intent launchIntent = new Intent(this, MainActivity.class);
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(launchIntent);  // Launch the MainActivity
                    }
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRecording = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not binding to any component
    }

    private void createNotificationChannel() {
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
