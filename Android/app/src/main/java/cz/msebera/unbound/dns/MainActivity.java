package cz.msebera.unbound.dns;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public final class MainActivity extends AppCompatActivity {

    private Button mStartStopButton;
    private Button mReloadButton;
    private UnboundService.UnboundServiceBinder mServiceBinder;
    private Handler mHandler = new Handler();

    private View.OnClickListener mButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mainButtonStartStop:
                    if (mServiceBinder.getService().isRunning()) {
                        mServiceBinder.getService().stop();
                    } else {
                        mServiceBinder.getService().start();
                    }
                    setCorrectButtonsText();
                    break;
                case R.id.mainButtonReload:
                    new RunnableThread(null, MainActivity.this, getString(R.string.filename_unbound_control), new String[]{"-c", getString(R.string.filename_unbound_conf), "reload"}).start();
                    break;
            }
        }
    };
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            setButtonsEnabled(true);
            mServiceBinder = (UnboundService.UnboundServiceBinder) service;
            pingService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            setButtonsEnabled(false);
            mServiceBinder = null;
        }
    };

    private void pingService() {
        setCorrectButtonsText();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pingService();
            }
        }, 1000);
    }

    private void setCorrectButtonsText() {
        if (mServiceBinder != null && mServiceBinder.getService() != null) {
            boolean running = mServiceBinder.getService().isRunning();
            mStartStopButton.setText(running ? getString(R.string.stop_unbound) : getString(R.string.start_unbound));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);
        mStartStopButton = (Button) findViewById(R.id.mainButtonStartStop);
        mReloadButton = (Button) findViewById(R.id.mainButtonReload);
        ViewPager viewPager = (ViewPager) findViewById(R.id.mainViewPager);

        mStartStopButton.setOnClickListener(mButtonOnClickListener);
        mReloadButton.setOnClickListener(mButtonOnClickListener);
        setButtonsEnabled(false);

        UnboundFragmentAdapter adapter = new UnboundFragmentAdapter(getSupportFragmentManager(), this);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
    }

    private void setButtonsEnabled(boolean enabled) {
        mStartStopButton.setEnabled(enabled);
        mReloadButton.setEnabled(enabled);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, UnboundService.class));
        bindService(new Intent(this, UnboundService.class), connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mServiceBinder != null && connection != null) {
            unbindService(connection);
        }
        setCorrectButtonsText();
    }
}
