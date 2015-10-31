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
package cz.msebera.unbound.dns.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cz.msebera.unbound.dns.R;

public final class MainLogFragment extends Fragment implements TailerListener {

    static final String TAG = "MainLogFragment";
    static final int MENU_CLEAR = 0xfab;
    static final int MENU_EMPTY = 0xbaf;
    TextView textarea;

    public MainLogFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void init(Tailer tailer) {
    }

    @Override
    public void fileNotFound() {
        if (textarea != null) {
            appendToTextArea("Logfile cannot be found \n");
        }
    }

    @Override
    public void fileRotated() {
        if (textarea != null) {
            appendToTextArea("Logfile rotated \n");
        }
    }

    @Override
    public void handle(String line) {
        if (textarea != null) {
            appendToTextArea(line + "\n");
        }
    }

    private void appendToTextArea(final String text) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (textarea != null) {
                    textarea.append(text);
                }
            }
        });
    }

    @Override
    public void handle(Exception ex) {
        Log.e(TAG, "handle Exception", ex);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.textview, container, false);
        textarea = (TextView) v.findViewById(R.id.textview);
        File logFile = new File(getActivity().getFilesDir(), "package/bin/mainlog");
        Tailer.create(logFile, this, 400);
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_EMPTY:
                try {
                    FileWriter fw = new FileWriter(new File(getActivity().getFilesDir(), "package/bin/unbound.conf"), false);
                    fw.write("");
                    fw.close();
                    Toast.makeText(getActivity(), "New Configuration Saved", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Log.e(TAG, "Cannot write into unbound.conf", e);
                }
            case MENU_CLEAR:
                textarea.setText("");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, MENU_CLEAR, Menu.NONE, "Clear").setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(Menu.NONE, MENU_EMPTY, Menu.NONE, "Remove Log File Contents")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
