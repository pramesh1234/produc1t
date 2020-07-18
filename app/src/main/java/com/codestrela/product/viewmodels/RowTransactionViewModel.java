package com.codestrela.product.viewmodels;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.fragments.SellingTransactionFragment;
import com.codestrela.product.fragments.TransactionDetailsFragment;
import com.codestrela.product.util.BindableBoolean;
import com.codestrela.product.util.BindableString;
import com.google.android.gms.dynamic.IFragmentWrapper;

public class RowTransactionViewModel {
    public BindableString referenceId=new BindableString();
    public BindableString commodityName=new BindableString();
    public BindableString requesterName=new BindableString();
    Fragment fragment;
    String quantity,mode;
    String price;
    String sellerBuyer;

    public RowTransactionViewModel(Fragment fragment, String quantity, String mode,String price,String sellerBuyer) {
    this.fragment=fragment;
    this.quantity=quantity;
    this.mode=mode;
    this.price=price;
    this.sellerBuyer=sellerBuyer;
    }
    public void onTransactionClick(View view){
        Bundle bundle=new Bundle();
        bundle.putString("referenceId",referenceId.get());
        bundle.putString("commodityName",commodityName.get());
        bundle.putString("requesterName",requesterName.get());
        bundle.putString("quantity",quantity);
        bundle.putString("sellerBuyer",sellerBuyer);
        bundle.putString("price",price);

        bundle.putString("mode",mode);
        TransactionDetailsFragment transactionDetailsFragment=new TransactionDetailsFragment();
        transactionDetailsFragment.setArguments(bundle);
        TransactionDetailsFragment.addFragment((BaseActivity) fragment.getActivity() ,transactionDetailsFragment);

    }
}
