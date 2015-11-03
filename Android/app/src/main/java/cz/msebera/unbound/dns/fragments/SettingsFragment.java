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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import cz.msebera.unbound.dns.C;
import cz.msebera.unbound.dns.R;
import eu.chainfire.libsuperuser.Shell;

public final class SettingsFragment extends Fragment {

    private CheckBox root, boot, cell, wifi;
    private SharedPreferences preferences;
    private CheckBox.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.settings_override_cellular:
                    if (preferences != null) {
                        preferences.edit().putBoolean(C.PREF_CELL, isChecked).apply();
                    }
                    break;
                case R.id.settings_override_wifi:
                    if (preferences != null) {
                        preferences.edit().putBoolean(C.PREF_WIFI, isChecked).apply();
                    }
                    break;
                case R.id.settings_runas_root:
                    if (preferences != null) {
                        preferences.edit().putBoolean(C.PREF_ROOT, isChecked).apply();
                    }
                    break;
                case R.id.settings_runon_boot:
                    if (preferences != null) {
                        preferences.edit().putBoolean(C.PREF_BOOT, isChecked).apply();
                    }
                    break;
            }
        }
    };

    public SettingsFragment() {
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);
        root = (CheckBox) v.findViewById(R.id.settings_runas_root);
        boot = (CheckBox) v.findViewById(R.id.settings_runon_boot);
        wifi = (CheckBox) v.findViewById(R.id.settings_override_wifi);
        cell = (CheckBox) v.findViewById(R.id.settings_override_cellular);

        root.setOnCheckedChangeListener(onCheckedChangeListener);
        boot.setOnCheckedChangeListener(onCheckedChangeListener);
        wifi.setOnCheckedChangeListener(onCheckedChangeListener);
        cell.setOnCheckedChangeListener(onCheckedChangeListener);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (preferences != null) {
            root.setChecked(preferences.getBoolean(C.PREF_ROOT, false));
            boot.setChecked(preferences.getBoolean(C.PREF_BOOT, true));
            cell.setChecked(preferences.getBoolean(C.PREF_CELL, false));
            wifi.setChecked(preferences.getBoolean(C.PREF_WIFI, false));
        }
        root.setEnabled(Shell.SU.available());
    }
}
