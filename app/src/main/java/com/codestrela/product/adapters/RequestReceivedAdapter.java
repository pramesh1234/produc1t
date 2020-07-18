package com.codestrela.product.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.codestrela.product.fragments.RequestReceivedSpecificationFragment;
import com.codestrela.product.fragments.SentRequestDialogFragment;
import com.codestrela.product.fragments.UserDetailRequestFragment;

public class RequestReceivedAdapter extends FragmentStatePagerAdapter {
    String title,mode,quantity,commodityName,specification,requestedBy,requestedTo;
    public RequestReceivedAdapter(@NonNull FragmentManager fm, int behavior,String mode, String quantity, String specification, String commodityName,String requestedBy,String requestedTo) {
        super(fm, behavior);
        this.mode=mode;
        this.specification=specification;
        this.commodityName=commodityName;
        this.quantity=quantity;
        this.requestedBy=requestedBy;
        this.requestedTo=requestedTo;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            Bundle bundle=new Bundle();
            bundle.putString("mode",mode);
            bundle.putString("quantity",quantity);
            bundle.putString("specification",specification);
            bundle.putString("commodityName",commodityName);
            bundle.putString("requestedTo",requestedTo);
            bundle.putString("requestedBy",requestedBy);

            fragment = new RequestReceivedSpecificationFragment();
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
            title = "Specifications";
        } else if (position == 1) {
            title = "User Details";
        }
        return title;
    }
}
