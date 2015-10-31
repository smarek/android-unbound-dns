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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cz.msebera.unbound.dns.R;

public final class UnboundConfiguration extends Fragment {

    private static final int MENU_SAVE = 0xade;
    private static final String TAG = "UnboundConfiguration";
    TextView textarea;

    public UnboundConfiguration() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.textarea, container, false);
        textarea = (TextView) v.findViewById(R.id.textarea);
        textarea.clearFocus();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(getActivity().getFilesDir(), "package/bin/unbound.conf")));
            String line;
            while ((line = br.readLine()) != null) {
                textarea.append(line);
                textarea.append("\n");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SAVE:
                if (textarea == null) {
                    break;
                }
                try {
                    FileWriter fw = new FileWriter(new File(getActivity().getFilesDir(), "package/bin/unbound.conf"), false);
                    fw.write(textarea.getText().toString());
                    fw.close();
                    Toast.makeText(getActivity(), "New Configuration Saved", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Log.e(TAG, "Cannot write into unbound.conf", e);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, MENU_SAVE, Menu.NONE, "Save Changes").setIcon(android.R.drawable.ic_menu_save).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
