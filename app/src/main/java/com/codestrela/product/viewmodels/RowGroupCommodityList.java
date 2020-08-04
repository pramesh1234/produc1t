package com.codestrela.product.viewmodels;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.fragments.GroupCommodityListFragment;
import com.codestrela.product.fragments.ImageFragment;
import com.codestrela.product.fragments.RequestFragment;
import com.codestrela.product.fragments.SentRequestDialogFragment;
import com.codestrela.product.util.BindableString;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;

public class RowGroupCommodityList {
    RequestFragment fragment;
    FragmentManager fm;
    private static final String TAG = "RowGroupCommodityList";
    FirebaseFirestore db;
    String quantity;
    public BindableString commodityName = new BindableString();
    public BindableString commodityPrice = new BindableString();
    public BindableString commodityImage = new BindableString();
    String commodityId,requestedTo,requestedBy;
    Fragment context;

    public RowGroupCommodityList(String commodityId, String requestedTo, String requestedBy, Fragment context,String quantity) {
        this.commodityId=commodityId;
        this.requestedTo=requestedTo;
        this.requestedBy=requestedBy;
        db=FirebaseFirestore.getInstance();
        this.quantity=quantity;
        fragment=new RequestFragment();
        fm= context.getActivity().getSupportFragmentManager();
        this.context=context;

    }
    public RowGroupCommodityList() {

    }


    public void onRequestClick(View view){

        Bundle args = new Bundle();
        args.putString("requestedBy",requestedBy);
        args.putString("requestedTo",requestedTo);
        args.putString("commodityId",commodityId);
        args.putString("commodityName",commodityName.get());
        args.putString("quantity",quantity);
        fragment.setArguments(args);
        //Log.e(TAG, "onRequestClick: commodity id "+commodityId+" created by "+createdBy);
        fragment.addFragment((BaseActivity)context.getActivity(),fragment );

    }
    public void onImageClick(View view){
        ImageFragment imageFragment=new ImageFragment();
        Dialog dialogFrg = imageFragment.getDialog();
        if (dialogFrg != null && dialogFrg.isShowing()) {

        } else {

            Bundle bundle=new Bundle();
            bundle.putString("imageUrl",commodityImage.get());
            imageFragment.setArguments(bundle);
            imageFragment.show(fm, "fma");
        }

    }

}
