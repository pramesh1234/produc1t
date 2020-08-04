package com.codestrela.product.viewmodels;

import android.os.Bundle;

import com.codestrela.product.adapters.RequestReceivedAdapter;
import com.codestrela.product.adapters.RequestTabAdapter;
import com.codestrela.product.fragments.RequestDetailFragment;
import com.codestrela.product.util.BindableString;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class RequestDetailViewModel {
    public RequestReceivedAdapter mViewPagerAdapter;
    RequestDetailFragment requestDetailFragment;

    String mode,specification,commodityName,quantity,requestedTo,requestedBy,requestRef,request,requester;
    public RequestDetailViewModel(RequestDetailFragment requestDetailFragment) {
        this.requestDetailFragment=requestDetailFragment;
        Bundle bundle = requestDetailFragment.getArguments();
        mode=bundle.getString("mode");
        specification=bundle.getString("specification");
        commodityName=bundle.getString("commodityName");
        quantity=bundle.getString("quantity");
        requestedBy=bundle.getString("requestedBy");
        requestedTo=bundle.getString("requestedTo");
        requestRef=bundle.getString("requestRef");
        request=bundle.getString("request");
        requester=bundle.getString("requester");


        mViewPagerAdapter = new RequestReceivedAdapter(requestDetailFragment.getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,mode,quantity,specification,commodityName,requestedTo,requestedBy,requestRef,request,requester);

    }
}
