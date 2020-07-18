package com.codestrela.product.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.codestrela.product.fragments.AcceptRequestFragment;
import com.codestrela.product.fragments.SentRequestDialogFragment;
import com.codestrela.product.fragments.SentRequestFragment;
import com.codestrela.product.fragments.UserDetailRequestFragment;

public class RequestViewPagerAdapter extends FragmentPagerAdapter {
    String title;
    public RequestViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {

            fragment = new SentRequestFragment();
        } else if (position == 1) {
            fragment = new AcceptRequestFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            title = "Sent Request";
        } else if (position == 1) {
            title = "Accept Request";
        }
        return title;
    }
}
