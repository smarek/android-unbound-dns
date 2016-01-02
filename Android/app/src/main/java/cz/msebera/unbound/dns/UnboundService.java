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
    public static final String ACTION_STOP = "action_service_stop";
    private final IBinder mBinder = new UnboundServiceBinder();
    private boolean mIsForeground = false;
    private boolean mIsStarted = false;
    private RunnableThread mMainRunnable;

    public boolean isRunning() {
        return mIsForeground;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start();
        return START_NOT_STICKY;
    }

    public void start() {
        goForeground();
        startUnboundControlSetup();
    }

    public void stop() {
        stopForeground(true);
        mIsForeground = false;
        if (mMainRunnable != null && !mMainRunnable.isInterrupted()) {
            mMainRunnable.interrupt();
        }
        mIsStarted = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (ACTION_STOP.equalsIgnoreCase(intent.getAction())) {
            stop();
        } else {
            start();
        }
        return mBinder;
    }

    private void startUnboundControlSetup() {
        if (mMainRunnable != null) {
            if (mIsStarted) {
                return;
            }
            mMainRunnable.interrupt();
        }
        updateZipFromAssets();
        mMainRunnable = new RunnableThread(new RunnableThread.RunnableThreadCallback() {
            @Override
            public void threadFinished() {
                startUnboundAnchor();
            }
        }, this, getString(R.string.filename_unbound_control_setup), new String[]{"-d", "."});
        mMainRunnable.start();
        mIsStarted = true;
    }

    private void startUnboundAnchor() {
        if (mMainRunnable != null) {
            mMainRunnable.interrupt();
        }
        mMainRunnable = new RunnableThread(new RunnableThread.RunnableThreadCallback() {
            @Override
            public void threadFinished() {
                startUnbound();
            }
        }, this, getString(R.string.filename_unbound_anchor), new String[]{"-C", getString(R.string.filename_unbound_conf), "-v"});
        mMainRunnable.start();
    }

    private void startUnbound() {
        if (mMainRunnable != null) {
            mMainRunnable.interrupt();
        }
        mMainRunnable = new RunnableThread(new RunnableThread.RunnableThreadCallback() {
            @Override
            public void threadFinished() {
                stop();
            }
        }, this, getString(R.string.filename_unbound), new String[]{"-c", getString(R.string.filename_unbound_conf)});
        mMainRunnable.start();
    }

    private void updateZipFromAssets() {
        Log.d(TAG, "updateZipFromAssets");
        IOUtils.copyFile(getApplicationContext(), getString(R.string.filename_package_zip));
        IOUtils.unzip(new File(getFilesDir(), getString(R.string.filename_package_zip)), getFilesDir());
        IOUtils.createConfigFromDefault(getFilesDir(), this);
    }

    private void goForeground() {
        if (mIsForeground) {
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
                .Builder(android.R.drawable.ic_media_pause, getString(R.string.menu_stop), stopPendingIntent)
                .build();
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(stopAction)
                .setWhen(System.currentTimeMillis())
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setOngoing(true)
                .build();
        startForeground(NOTIFICATION_ID, notification);
        mIsForeground = true;
    }

    public final class UnboundServiceBinder extends Binder {
        UnboundService getService() {
            return UnboundService.this;
        }
    }
}
