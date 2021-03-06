package com.codestrela.product.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.codestrela.product.fragments.GroupTabFragment;
import com.codestrela.product.fragments.RequestTabFragment;
import com.codestrela.product.fragments.SentRequestDialogFragment;
import com.codestrela.product.fragments.UserDetailRequestFragment;

public class RequestTabAdapter extends FragmentStatePagerAdapter{
    String title;
    String commodityName,commodityId,requestedTo,requestedBy,quantity;
    public RequestTabAdapter(@NonNull FragmentManager fm, int behavior,String commodityName,String commodityId,String requestedBy,String requestedTo,String quantity){
        super(fm, behavior);
        this.commodityName=commodityName;
        this.commodityId=commodityId;
        this.requestedBy=requestedBy;
        this.requestedTo=requestedTo;
        this.quantity=quantity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            Bundle bundle=new Bundle();
            fragment = new SentRequestDialogFragment();
            bundle.putString("commodityName",commodityName);
            bundle.putString("commodityId",commodityId);
            bundle.putString("requestedTo",requestedTo);
            bundle.putString("requestedBy",requestedBy);
            bundle.putString("quantity",quantity);
            fragment.setArguments(bundle);
        } else if (position == 1) {
            fragment = new UserDetailRequestFragment();
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
            title = "Specification";
        } else if (position == 1) {
            title = "User Details";
        }
        return title;
    }
}
