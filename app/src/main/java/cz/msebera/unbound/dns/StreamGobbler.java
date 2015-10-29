package cz.msebera.unbound.dns;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class StreamGobbler extends Thread {
    InputStream is;
    String type;

    public StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null)
                Log.d(type, line);
        } catch (IOException ioe) {
            Log.e(type, ioe.getMessage(), ioe);
        }
    }
}