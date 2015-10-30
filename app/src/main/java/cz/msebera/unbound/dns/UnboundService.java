/*
    Copyright (c) 2015 Marek Sebera <marek.sebera@gmail.com>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package cz.msebera.unbound.dns;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.File;

public final class UnboundService extends Service {

    public static final String TAG = "UNBOUND_DNS";
    public static final int NOTIFICATION_ID = 0x31337;
    public static final int REQUEST_STOP = 0xdead;
    public static final String ACTION_STOP = "stop";
    private boolean isForeground = false;
    private RunnableThread mainRunnable;

    public final class UnboundServiceBinder extends Binder {
        UnboundService getService() {
            return UnboundService.this;
        }
    }

    public interface UnboundServiceCallback {
        void threadFinished();
    }

    private final IBinder mBinder = new UnboundServiceBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        goForeground();
        updateZipFromAssets();
        startUnboundControlSetup();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (ACTION_STOP.equalsIgnoreCase(intent.getAction())) {
            Log.d(TAG, "stop foreground");
            stopForeground(true);
            isForeground = false;
            return mBinder;
        }
        return mBinder;
    }


    private void startUnboundControlSetup() {
        if (mainRunnable != null) {
            mainRunnable.interrupt();
        }
        mainRunnable = new RunnableThread(new UnboundServiceCallback() {
            @Override
            public void threadFinished() {
                startUnboundAnchor();
            }
        }, new File(getFilesDir(), "package"), "unbound-control-setup", null, "lib");
        mainRunnable.start();
    }

    private void startUnboundAnchor() {
        if (mainRunnable != null) {
            mainRunnable.interrupt();
        }
        mainRunnable = new RunnableThread(new UnboundServiceCallback() {
            @Override
            public void threadFinished() {
                startUnbound();
            }
        }, new File(getFilesDir(), "package"), "unbound-anchor", new String[]{"-C", "unbound.conf", "-v"}, "lib");
        mainRunnable.start();
    }

    private void startUnbound() {
        if (mainRunnable != null) {
            mainRunnable.interrupt();
        }
        mainRunnable = new RunnableThread(new UnboundServiceCallback() {
            @Override
            public void threadFinished() {
                stopForeground(true);
            }
        }, new File(getFilesDir(), "package"), "unbound", new String[]{"-c", "unbound.conf"}, "lib");
        mainRunnable.start();
    }

    private void updateZipFromAssets() {
        IOUtils.copyFile(getApplicationContext(), "package.zip");
        IOUtils.unzip(new File(getFilesDir(), "package.zip"), getFilesDir());
        IOUtils.createConfigFromDefault(getFilesDir());
    }

    private void goForeground() {
        if (isForeground) {
            return;
        }

        Intent stopIntent = new Intent(getApplicationContext(), UnboundService.class);
        stopIntent.setAction(ACTION_STOP);
        PendingIntent stopPendingIntent = PendingIntent
                .getService(
                        getApplicationContext(),
                        REQUEST_STOP, stopIntent,
                        PendingIntent.FLAG_ONE_SHOT
                );
        NotificationCompat.Action stopAction = new NotificationCompat.Action
                .Builder(android.R.drawable.ic_media_pause, "Stop", stopPendingIntent)
                .build();
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Unbound DNS")
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(stopAction)
                .setWhen(System.currentTimeMillis())
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setOngoing(true)
                .build();
        startForeground(NOTIFICATION_ID, notification);
        isForeground = true;
        Log.d(TAG, "start foreground");
    }
}
