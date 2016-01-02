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

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

public final class RunnableThread extends Thread {

    private static final String DEFAULT_LOG_FILE_NAME = "mainlog";
    private final File mBinDir;
    private final String mTmpDirPath;
    private final String mBinDirPath;
    private final File mBinFile;
    private final String[] mArgs;
    private final String TAG;
    private final OutputStream mOutputStream;
    private final RunnableThreadCallback mCallback;

    public RunnableThread(RunnableThreadCallback callback, Context context, String binName, String[] args) {
        this(callback, new File(context.getFilesDir(), "package"), binName, args, null);
    }

    public RunnableThread(RunnableThreadCallback callback, Context context, String binName, String[] args, OutputStream output) {
        this(callback, new File(context.getFilesDir(), "package"), binName, args, output);
    }

    public RunnableThread(RunnableThreadCallback callback, File workDir, String binName, String[] args, OutputStream output) {
        this.mBinFile = new File(workDir, "bin/" + binName);
        this.TAG = "RunnableThread:" + binName;
        this.mBinDir = this.mBinFile.getParentFile();
        this.mBinDirPath = this.mBinDir.getAbsolutePath();
        this.mTmpDirPath = this.mBinDir.getAbsolutePath();
        this.mCallback = callback;
        this.mOutputStream = output;
        this.mArgs = args;
    }

    @Override
    public void run() {
        if (!mBinFile.exists()) {
            Log.e(TAG, "Bin File does not exist: " + mBinFile.getAbsolutePath());
            return;
        }
        if (!mBinFile.setExecutable(true, true)) {
            Log.e(TAG, "Could not set the binary as executable: " + mBinFile.getAbsolutePath());
            return;
        }
        if (this.mBinDir.isDirectory()) {
            for (File binary : this.mBinDir.listFiles()) {
                if (!binary.setExecutable(true)) {
                    Log.d(TAG, "Could not set the binary as executable: " + binary.getAbsolutePath());
                }
            }
        }

        String[] launch = new String[1 + (mArgs == null ? 0 : mArgs.length)];
        launch[0] = mBinFile.getAbsolutePath();
        if (mArgs != null) {
            int i = 1;
            for (String arg : mArgs) {
                launch[i] = arg;
                i++;
            }
        }
        Log.d(TAG, "Launch command: " + TextUtils.join(" ", launch));
        ProcessBuilder pb = new ProcessBuilder(launch);
        pb.redirectErrorStream(true);
        pb.directory(this.mBinDir);
        Map<String, String> env = pb.environment();
        for (Map.Entry<String, String> e : env.entrySet()) {
            Log.d(TAG, "ENV[" + e.getKey() + "] " + e.getValue());
        }
        env.put("TMP", this.mTmpDirPath);
        env.put("TEMP", this.mTmpDirPath);
        env.put("PATH", env.get("PATH") + ":" + this.mBinDirPath);
        env.put("HOME", this.mBinDirPath);
        Process javap = null;
        try {
            javap = pb.start();
            StreamGobbler inputGobbler;
            if (this.mOutputStream != null) {
                inputGobbler = new StreamGobbler(javap.getInputStream(), TAG, mOutputStream);
            } else {
                inputGobbler = new StreamGobbler(javap.getInputStream(), TAG, new File(mBinDir, DEFAULT_LOG_FILE_NAME));
            }
            inputGobbler.start();
            javap.waitFor();
            inputGobbler.interrupt();
        } catch (Throwable t) {
            Log.e(TAG, t.getMessage(), t);
        } finally {
            if (javap != null) {
                javap.destroy();
            }
        }
        if (mCallback != null) {
            mCallback.threadFinished();
        }
    }

    public interface RunnableThreadCallback {
        void threadFinished();
    }
}
