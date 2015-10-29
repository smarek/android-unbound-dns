package cz.msebera.unbound.dns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public final class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, UnboundService.class));
    }
}
