package com.codestrela.product.viewmodels;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;

import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.fragments.AdminOrderListFragment;
import com.codestrela.product.fragments.AdminOrderPanelFragment;
import com.codestrela.product.util.BindableBoolean;
import com.codestrela.product.util.BindableString;
import com.google.firebase.firestore.FirebaseFirestore;

public class RowAdminOrder {
    public BindableString commodityName=new BindableString();
    public BindableString orderId=new BindableString();
    public BindableString orderDate=new BindableString();
    public BindableString orderStatus=new BindableString();
    FirebaseFirestore db;
    String document,requestedBy,referenceId;
    AdminOrderListFragment adminOrderListFragment;

    public RowAdminOrder(AdminOrderListFragment adminOrderListFragment,String document,String requestedBy,String referenceId) {
        this.adminOrderListFragment=adminOrderListFragment;
        this.document=document;
        this.requestedBy=requestedBy;
        this.referenceId=referenceId;
        db=FirebaseFirestore.getInstance();

    }

    public void onOrderClick(View view){
        AdminOrderPanelFragment fragment=new AdminOrderPanelFragment();
        Bundle bundle=new Bundle();
        bundle.putString("documentId",document);
        bundle.putString("requestedBy",requestedBy);
        bundle.putString("referenceId",referenceId);

        fragment.setArguments(bundle);
        AdminOrderPanelFragment.addFragment((BaseActivity)adminOrderListFragment.getActivity(),fragment );
    }
}
