package br.com.developen.sig.widget;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ConfigurationPagerAdapter extends FragmentPagerAdapter {

    public ConfigurationPagerAdapter(FragmentManager fm) {

        super(fm);

    }

    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){

            case 1:

                break;

            default:

                fragment = WelcomeFragment.newInstance();

                break;

        }

        return fragment;

    }

    public int getCount() {

        return 1;

    }

}
