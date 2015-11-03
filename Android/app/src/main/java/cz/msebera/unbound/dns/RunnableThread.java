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

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

public final class RunnableThread extends Thread {

    private final File workDir;
    private final File binDir;
    private final String binDirPath;
    private final File binFile;
    private final String[] args;
    private final String TAG;
    private final OutputStream outputStream;
    private final RunnableThreadCallback callback;

    public RunnableThread(RunnableThreadCallback callback, File workDir, String binName, String[] args) {
        this(callback, workDir, binName, args, null);
    }

    public RunnableThread(RunnableThreadCallback callback, File workDir, String binName, String[] args, OutputStream output) {
        this.workDir = workDir;
        this.binFile = new File(workDir, "bin/" + binName);
        this.TAG = "RunnableThread:" + binName;
        this.binDir = this.binFile.getParentFile();
        this.binDirPath = this.binDir.getAbsolutePath();
        this.callback = callback;
        this.outputStream = output;
        this.args = args;
    }

    @Override
    public void run() {
        if (!binFile.exists()) {
            Log.e(TAG, "Bin File does not exist: " + binFile.getAbsolutePath());
            return;
        }
        if (!binFile.setExecutable(true, true)) {
            Log.e(TAG, "Could not set the binary as executable: " + binFile.getAbsolutePath());
            return;
        }
        if (this.binDir.isDirectory()) {
            for (File binary : this.binDir.listFiles()) {
                if (!binary.setExecutable(true)) {
                    Log.d(TAG, "Could not set the binary as executable: " + binary.getAbsolutePath());
                }
            }
        }

        String[] launch = new String[1 + (args == null ? 0 : args.length)];
        launch[0] = binFile.getAbsolutePath();
        if (args != null) {
            int i = 1;
            for (String arg : args) {
                launch[i] = arg;
                i++;
            }
        }
        Log.d(TAG, "Launch command: " + TextUtils.join(" ", launch));
        ProcessBuilder pb = new ProcessBuilder(launch);
        pb.redirectErrorStream(true);
        pb.directory(this.binDir);
        Map<String, String> env = pb.environment();
        env.put("PATH", env.get("PATH") + ":" + this.binDirPath);
        env.put("HOME", this.binDirPath);
        Process javap = null;
        try {
            javap = pb.start();
            StreamGobbler inputGobbler;
            if (this.outputStream != null) {
                inputGobbler = new StreamGobbler(javap.getInputStream(), TAG, outputStream);
            } else {
                inputGobbler = new StreamGobbler(javap.getInputStream(), TAG, new File(binDir, "mainlog"));
            }
            inputGobbler.start();
            javap.waitFor();
            inputGobbler.interrupt();
        } catch (Throwable t) {
            Log.e(TAG, "Error while executing", t);
        } finally {
            if (javap != null) {
                javap.destroy();
            }
        }
        if (callback != null) {
            callback.threadFinished();
        }
    }

    public interface RunnableThreadCallback {
        void threadFinished();
    }
}
