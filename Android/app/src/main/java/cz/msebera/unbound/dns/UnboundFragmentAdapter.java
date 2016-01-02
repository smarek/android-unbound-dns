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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cz.msebera.unbound.dns.fragments.MainLogFragment;
import cz.msebera.unbound.dns.fragments.SettingsFragment;
import cz.msebera.unbound.dns.fragments.UnboundCheckConf;
import cz.msebera.unbound.dns.fragments.UnboundConfiguration;
import cz.msebera.unbound.dns.fragments.UnboundControlConsole;
import cz.msebera.unbound.dns.fragments.UnboundHostConsole;

public final class UnboundFragmentAdapter extends FragmentStatePagerAdapter {

    @SuppressWarnings("unchecked")
    private final Class<? extends Fragment>[] fragments = new Class[]{
            SettingsFragment.class,
            MainLogFragment.class,
            UnboundConfiguration.class,
            UnboundControlConsole.class,
            UnboundCheckConf.class,
            UnboundHostConsole.class
    };
    private final String[] titles;

    public UnboundFragmentAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        titles = new String[]{
                mContext.getString(R.string.fragment_title_settings),
                mContext.getString(R.string.fragment_title_main_log),
                mContext.getString(R.string.fragment_title_configuration),
                mContext.getString(R.string.fragment_title_control),
                mContext.getString(R.string.fragment_title_checkconf),
                mContext.getString(R.string.fragment_title_host_console)
        };
    }

    @Override
    public Fragment getItem(int position) {
        try {
            return fragments[position].newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
