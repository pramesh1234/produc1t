package com.codestrela.product.viewmodels;

import android.os.Bundle;
import android.util.Log;

import com.codestrela.product.adapters.HomeAdapter;
import com.codestrela.product.adapters.RequestTabAdapter;
import com.codestrela.product.fragments.RequestFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class RequestViewModel {
    public RequestTabAdapter mViewPagerAdapter;
    String data="data";
    Bundle args;
    String commodityName,commodityId,requestedBy,requestedTo;
    RequestFragment requestFragment;
    public RequestViewModel(RequestFragment requestFragment) {
        args= new Bundle();
        args=requestFragment.getArguments();
      commodityName=args.getString("commodityName");
        commodityId=args.getString("commodityId");
       requestedBy=args.getString("requestedBy");
        requestedTo=args.getString("requestedTo");
        //name.set(commodityName);
        this.requestFragment=requestFragment;
        mViewPagerAdapter = new RequestTabAdapter(requestFragment.getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,commodityName,commodityId,requestedBy,requestedTo);

    }
}
