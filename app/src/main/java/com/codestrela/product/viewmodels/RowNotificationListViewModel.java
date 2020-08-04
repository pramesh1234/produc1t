package com.codestrela.product.viewmodels;

import android.os.Bundle;
import android.view.View;

import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.fragments.NotificationListFragment;
import com.codestrela.product.fragments.OrderDetailFragment;
import com.codestrela.product.util.BindableString;

public class RowNotificationListViewModel {
    public BindableString orderNo=new BindableString();
    public BindableString title=new BindableString();
    NotificationListFragment notificationListFragment;
    String orderReference;

    public RowNotificationListViewModel(NotificationListFragment notificationListFragment, String orderReference) {
        this.notificationListFragment=notificationListFragment;
        this.orderReference=orderReference;
    }

    public void onNotificationClick(View view){
        OrderDetailFragment fragment=new OrderDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putString("orderReference",orderReference);
        fragment.setArguments(bundle);
        OrderDetailFragment.addFragment((BaseActivity) notificationListFragment.getActivity(),fragment);
    }
}
