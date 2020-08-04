package com.codestrela.product.viewmodels;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.BindingAdapter;

import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.fragments.MyOrdersFragment;
import com.codestrela.product.fragments.OrderDetailFragment;
import com.codestrela.product.util.BindableString;

public class RowOrderListViewModel {
    public BindableString orderId=new BindableString();
    public BindableString commodityName=new BindableString();
    MyOrdersFragment myOrdersFragment;
    String requestId;

    public RowOrderListViewModel(MyOrdersFragment myOrdersFragment, String requestId) {
        this.myOrdersFragment=myOrdersFragment;
        this.requestId=requestId;
    }

    public void onOrderClick(View view){
        OrderDetailFragment fragment=new OrderDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putString("orderReference",requestId);
        fragment.setArguments(bundle);
        OrderDetailFragment.addFragment((BaseActivity) myOrdersFragment.getActivity(),fragment);
    }
}
