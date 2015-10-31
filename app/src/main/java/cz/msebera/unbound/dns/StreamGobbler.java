package cz.msebera.unbound.dns;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public final class StreamGobbler extends Thread {
    InputStream is;
    String type;
    PrintWriter outFilePrinter;

    public StreamGobbler(InputStream is, String type, OutputStream outStream) {
        this.is = is;
        this.type = type;
        this.outFilePrinter = new PrintWriter(outStream);
    }

    public StreamGobbler(InputStream is, String type, File outFile) {
        this.is = is;
        this.type = type;
        try {
            outFilePrinter = new PrintWriter(new BufferedWriter(new FileWriter(outFile, true)));
        } catch (IOException e) {
            Log.e(type, "error", e);
            outFilePrinter = null;
        }
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                Log.d(type, line);
                if (outFilePrinter != null) {
                    outFilePrinter.println(line);
                }
            }
        } catch (IOException ioe) {
            Log.e(type, ioe.getMessage(), ioe);
        }
        if (outFilePrinter != null) {
            outFilePrinter.close();
        }
    }
}