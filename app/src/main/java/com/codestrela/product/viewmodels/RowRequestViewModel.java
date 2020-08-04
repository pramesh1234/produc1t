package com.codestrela.product.viewmodels;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.codestrela.product.adapters.RequestTabAdapter;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentTransactionBinding;
import com.codestrela.product.fragments.RequestDetailFragment;
import com.codestrela.product.fragments.RequestFragment;
import com.codestrela.product.fragments.RequestTabFragment;
import com.codestrela.product.util.BindableString;

public class RowRequestViewModel {
    public BindableString commodityName=new BindableString();
    public BindableString quantity=new BindableString();
    public BindableString requester=new BindableString();
    String commodity_name,quantitys,mode,specification,requestedBy,requestedTo,requestRef;
    Fragment requestTabFragment;

String requestId;
    public RowRequestViewModel(RequestTabFragment requestTabFragment) {
        this.requestTabFragment=requestTabFragment;
    }

    public RowRequestViewModel(Fragment requestTabFragment, String commodityName, String quantity, String mode, String specification, String requestId, String requestedTo, String requestedBy,String requestRef) {
        this.specification=specification;
        this.requestTabFragment=requestTabFragment;
        this.mode=mode;
        this.quantitys=quantity;
        this.commodity_name=commodityName;
        this.requestedBy=requestedBy;
        this.requestedTo=requestedTo;
        this.requestRef=requestRef;
    }

    public void onRequestClicked(View view){
        Bundle bundle=new Bundle();
        bundle.putString("specification",specification);
        bundle.putString("mode",mode);
        bundle.putString("quantity",quantitys);
        bundle.putString("commodityName",commodity_name);
        bundle.putString("requestId",requestId);
        bundle.putString("requestedTo",requestedTo);
        bundle.putString("requestRef",requestRef);
        bundle.putString("requestedBy",requestedTo);
        bundle.putString("request","recieved");
        RequestDetailFragment fragment=new RequestDetailFragment();
        fragment.setArguments(bundle);
        fragment.addFragment((BaseActivity)requestTabFragment.getActivity(),fragment);
    }
    public void onRequestSentClicked(View view){
        Bundle bundle=new Bundle();
        bundle.putString("specification",specification);
        bundle.putString("mode",mode);
        bundle.putString("quantity",quantitys);
        bundle.putString("commodityName",commodity_name);
        bundle.putString("requestId",requestId);
        bundle.putString("requestedTo",requestedTo);
        bundle.putString("requestRef",requestRef);
        bundle.putString("requestedBy",requestedTo);
        bundle.putString("requester",requester.get());

        bundle.putString("request","sent");
        RequestDetailFragment fragment=new RequestDetailFragment();
        fragment.setArguments(bundle);
        fragment.addFragment((BaseActivity)requestTabFragment.getActivity(),fragment);
    }
}
