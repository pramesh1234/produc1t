package com.codestrela.product.viewmodels;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.codestrela.product.fragments.GroupCommodityListFragment;
import com.codestrela.product.fragments.SentRequestDialogFragment;
import com.codestrela.product.util.BindableString;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;

public class RowGroupCommodityList {
    SentRequestDialogFragment fragment;
    FragmentManager fm;
    private static final String TAG = "RowGroupCommodityList";
    FirebaseFirestore db;
    public BindableString commodityName = new BindableString();
    public BindableString commodityPrice = new BindableString();
    public BindableString commodityImage = new BindableString();
    String commodityId,requestedTo,requestedBy;
    Context context;

    public RowGroupCommodityList(String commodityId, String requestedTo, String requestedBy, GroupCommodityListFragment context) {
        this.commodityId=commodityId;
        this.requestedTo=requestedTo;
        this.requestedBy=requestedBy;
        db=FirebaseFirestore.getInstance();
        fragment=new SentRequestDialogFragment();
        fm=context.getActivity().getSupportFragmentManager();
    }
    public RowGroupCommodityList() {

    }


    public void onRequestClick(View view){

        Bundle args = new Bundle();
        args.putString("requestedBy",requestedBy);
        args.putString("requestedTo",requestedTo);
        args.putString("commodityId",commodityId);
        args.putString("commodityName",commodityName.get());
        fragment.setArguments(args);
        //Log.e(TAG, "onRequestClick: commodity id "+commodityId+" created by "+createdBy);
        fragment.show(fm,"sentDialogShow");

    }

}
