package org.gemafrzen.meinwetter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.gemafrzen.meinwetter.WeatherFragment;

/**
 * Created by Erik on 28.05.2017.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public int getCount(){
        return 2;
    }

    public Fragment getItem(int position){
        if(position == 0)
            return WeatherFragment.newInstance("Berlin");
        else return  WeatherFragment.newInstance("Stockholm");
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
