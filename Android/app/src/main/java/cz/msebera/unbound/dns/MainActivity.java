package cz.msebera.unbound.dns;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;

public final class MainActivity extends AppCompatActivity {

    private Button btn_StartStop;
    private Button btn_Reload;
    private UnboundService.UnboundServiceBinder serviceBinder;

    private View.OnClickListener btnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mainButtonStartStop:
                    if (serviceBinder.getService().isRunning()) {
                        serviceBinder.getService().stop();
                    } else {
                        serviceBinder.getService().start();
                    }
                    setCorrectButtonsText();
                    break;
                case R.id.mainButtonReload:
                    new RunnableThread(null, new File(getFilesDir(), "package"), "unbound-control", new String[]{"-c", "unbound.conf", "reload"}, "lib").start();
                    break;
            }
        }
    };

    private void setCorrectButtonsText() {
        if (serviceBinder != null && serviceBinder.getService() != null) {
            boolean running = serviceBinder.getService().isRunning();
            btn_StartStop.setText(running ? "STOP UNBOUND" : "START UNBOUND");
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            setButtonsEnabled(true);
            serviceBinder = (UnboundService.UnboundServiceBinder) service;
            setCorrectButtonsText();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            setButtonsEnabled(false);
            serviceBinder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_StartStop = (Button) findViewById(R.id.mainButtonStartStop);
        btn_Reload = (Button) findViewById(R.id.mainButtonReload);
        ViewPager viewPager = (ViewPager) findViewById(R.id.mainViewPager);

        btn_StartStop.setOnClickListener(btnOnClickListener);
        btn_Reload.setOnClickListener(btnOnClickListener);
        setButtonsEnabled(false);

        UnboundFragmentAdapter adapter = new UnboundFragmentAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
    }

    private void setButtonsEnabled(boolean enabled) {
        btn_StartStop.setEnabled(enabled);
        btn_Reload.setEnabled(enabled);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, UnboundService.class));
        bindService(new Intent(this, UnboundService.class), connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(connection);
    }
}
