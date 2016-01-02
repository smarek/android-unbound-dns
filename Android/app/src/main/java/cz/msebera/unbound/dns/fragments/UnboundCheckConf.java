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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;

import cz.msebera.unbound.dns.R;
import cz.msebera.unbound.dns.RunnableThread;

public final class UnboundCheckConf extends Fragment {

    private static final int MENU_RUN = 0xbeef;
    TextView mTextArea;
    RunnableThread mMainRunnable;
    private OutputStream mTextAreaOutputStream = new OutputStream() {

        @Override
        public void write(@NonNull byte[] buffer) throws IOException {
            appendToTextArea(new String(buffer));
        }

        @Override
        public void write(int oneByte) throws IOException {
            appendToTextArea(String.valueOf((char) oneByte));
        }
    };

    public UnboundCheckConf() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.textview, container, false);
        mTextArea = (TextView) v.findViewById(R.id.textview);
        mTextArea.setText(R.string.checkconf_click_check_to_run);
        return v;
    }

    private void appendToTextArea(final String text) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mTextArea != null) {
                    mTextArea.append(text);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RUN:
                runCheck();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void runCheck() {
        if (mMainRunnable != null) {
            mMainRunnable.interrupt();
            mMainRunnable = null;
        }
        mMainRunnable = new RunnableThread(null, getActivity(), getString(R.string.filename_unbound_checkconf), new String[]{getString(R.string.filename_unbound_conf)}, mTextAreaOutputStream);
        mTextArea.setText(String.format(getString(R.string.checkconf_console_command_run), getString(R.string.filename_unbound_conf)));
        mMainRunnable.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, MENU_RUN, Menu.NONE, R.string.checkconf_menu_run_check).setIcon(android.R.drawable.ic_media_play)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
