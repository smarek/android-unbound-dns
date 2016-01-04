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
import android.util.Log;

import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootShell.execution.Shell;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public final class RunnableThread extends Thread {

    private static final String DEFAULT_LOG_FILE_NAME = "mainlog";
    private final File mBinDir;
    private final String mBinDirPath;
    private final File mBinFile;
    private final boolean mRunAsRoot;
    private final String[] mArgs;
    private final String TAG;
    private final PrintWriter mOutputWriter;
    private final RunnableThreadCallback mCallback;
    private boolean mFinishedFired = false;
    private final List<String> mOutput = new ArrayList<>();
    private final boolean mCollectOutput;

    public RunnableThread(RunnableThreadCallback callback, Context context, String binName, String[] args) {
        this(callback, new File(context.getFilesDir(), "package"), binName, false, args, null);
    }

    public RunnableThread(RunnableThreadCallback callback, Context context, String binName, boolean runAsRoot, String[] args) {
        this(callback, new File(context.getFilesDir(), "package"), binName, runAsRoot, args, null);
    }

    public RunnableThread(RunnableThreadCallback callback, Context context, String binName, boolean runAsRoot, String[] args, boolean collectOutput) {
        this(callback, new File(context.getFilesDir(), "package"), binName, runAsRoot, args, null, collectOutput);
    }

    public RunnableThread(RunnableThreadCallback callback, Context context, String binName, boolean runAsRoot, String[] args, OutputStream output) {
        this(callback, new File(context.getFilesDir(), "package"), binName, runAsRoot, args, output);
    }

    public RunnableThread(RunnableThreadCallback callback, Context context, String binName, boolean runAsRoot, String[] args, OutputStream output, boolean collectOutput) {
        this(callback, new File(context.getFilesDir(), "package"), binName, runAsRoot, args, output, collectOutput);
    }

    public RunnableThread(RunnableThreadCallback callback, File workDir, String binName, boolean runAsRoot, String[] args, OutputStream output) {
        this(callback, workDir, binName, runAsRoot, args, output, false);
    }

    public RunnableThread(RunnableThreadCallback callback, File workDir, String binName, boolean runAsRoot, String[] args, OutputStream output, boolean collectOutput) {
        this.mBinFile = new File(workDir, "bin/" + binName);
        this.TAG = "RunnableThread:" + binName;
        this.mBinDir = this.mBinFile.getParentFile();
        this.mBinDirPath = this.mBinDir.getAbsolutePath();
        this.mCallback = callback;
        this.mArgs = args;
        this.mRunAsRoot = runAsRoot;
        this.mCollectOutput = collectOutput;
        PrintWriter tmpPrintWriter;
        if (output == null) {
            try {
                tmpPrintWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(mBinDir, DEFAULT_LOG_FILE_NAME), true)));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                tmpPrintWriter = new PrintWriter(System.err);
            }
        } else {
            tmpPrintWriter = new PrintWriter(output);
        }
        this.mOutputWriter = tmpPrintWriter;
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

        Map<String, String> env = new HashMap<>();
        env.put("PATH", env.get("PATH") + ":" + this.mBinDirPath);
        env.put("HOME", this.mBinDirPath);

        List<String> launchCommand = new ArrayList<>();
        launchCommand.add(mBinFile.getAbsolutePath());
        if (mArgs != null) {
            Collections.addAll(launchCommand, mArgs);
        }
        String[] launchCommandArray = new String[launchCommand.size()];
        launchCommandArray = launchCommand.toArray(launchCommandArray);

        try {
            Shell rootShell = RootShell.getShell(mRunAsRoot);
            String[] cmds = new String[2 + env.size()];
            cmds[0] = "cd " + mBinDirPath;
            int i = 1;
            for (Map.Entry<String, String> entry : env.entrySet()) {
                cmds[i] = String.format("export %s=%s", entry.getKey(), entry.getValue());
                i++;
            }
            cmds[cmds.length - 1] = IOUtils.join(" ", launchCommandArray);
            rootShell.add(new Command(0, cmds) {
                @Override
                public void commandOutput(int id, String line) {
                    super.commandOutput(id, line);
                    if (line == null || line.trim().length() == 0) {
                        return;
                    }
                    if (mOutputWriter != null) {
                        mOutputWriter.println(line);
                    }
                    if (mCollectOutput) {
                        mOutput.add(line);
                    }
                }

                @Override
                public void commandCompleted(int id, int exitcode) {
                    finishUp();
                }

                @Override
                public void commandTerminated(int id, String reason) {
                    finishUp();
                }
            });
        } catch (IOException | TimeoutException | RootDeniedException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void finishUp() {
        if (mOutputWriter != null) {
            mOutputWriter.flush();
            mOutputWriter.close();
        }
        if (mCallback != null && !mFinishedFired) {
            mFinishedFired = true;
            mCallback.threadFinished(mOutput);
        }
    }

    public interface RunnableThreadCallback {
        void threadFinished(List<String> optionalOutput);
    }
}
