package com.codestrela.product.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.codestrela.product.fragments.BuyingTransactionFragment;
import com.codestrela.product.fragments.SellingTransactionFragment;
import com.codestrela.product.fragments.SentRequestDialogFragment;
import com.codestrela.product.fragments.UserDetailRequestFragment;

public class TransactionTabAdapter extends FragmentStatePagerAdapter {
    String title;
    public TransactionTabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new BuyingTransactionFragment();
        } else if (position == 1) {
            fragment = new SellingTransactionFragment();
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
            title = "Purchased";
        } else if (position == 1) {
            title = "Sold";
        }
        return title;
    }
}
