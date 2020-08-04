package com.codestrela.product.adapters;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.codestrela.product.fragments.RequestReceivedSpecificationFragment;
import com.codestrela.product.fragments.RequestSentSpecificationFragment;
import com.codestrela.product.fragments.SentRequestDialogFragment;
import com.codestrela.product.fragments.UserDetailRequestFragment;

public class RequestReceivedAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "RequestReceivedAdapter";
    String title,mode,quantity,commodityName,specification,requestedBy,requestedTo,requestRef,request,requester;
    public RequestReceivedAdapter(@NonNull FragmentManager fm, int behavior,String mode, String quantity, String specification, String commodityName,String requestedBy,String requestedTo,String requestRef,String request,String requester) {
        super(fm, behavior);
        this.mode=mode;
        this.specification=specification;
        this.commodityName=commodityName;
        this.quantity=quantity;
        this.requestedBy=requestedBy;
        this.requestedTo=requestedTo;
        this.requestRef=requestRef;
        this.request=request;
        this.requester=requester;
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
            bundle.putString("requestRef",requestRef);
            bundle.putString("requester",requester);

            if(request.equals("sent")){
                fragment = new RequestSentSpecificationFragment();
                fragment.setArguments(bundle);
                Log.e(TAG, "getItem: sent "+request );
            }else{
                fragment = new RequestReceivedSpecificationFragment();
                Log.e(TAG, "getItem: recived "+request );

                fragment.setArguments(bundle);
            }


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
