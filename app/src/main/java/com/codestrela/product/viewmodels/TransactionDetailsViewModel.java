package com.codestrela.product.viewmodels;

import android.os.Bundle;

import com.codestrela.product.fragments.TransactionDetailsFragment;
import com.codestrela.product.util.BindableString;

public class TransactionDetailsViewModel {
    public BindableString bindCommodityName=new BindableString();
    public BindableString bindReferenceId=new BindableString();
    public BindableString bindQuantity=new BindableString();
    public BindableString bindMode=new BindableString();
    public BindableString bindRequesterName=new BindableString();
    public BindableString bindPrice=new BindableString();
    public BindableString bindSellerBuyer=new BindableString();




    TransactionDetailsFragment transactionDetailsFragment;
    public TransactionDetailsViewModel(TransactionDetailsFragment transactionDetailsFragment) {
        this.transactionDetailsFragment=transactionDetailsFragment;
        Bundle bundle=transactionDetailsFragment.getArguments();
        String referenceId=bundle.getString("referenceId");
        String mode=bundle.getString("mode");
        String price=bundle.getString("price");
        String commodityName=bundle.getString("commodityName");
        String requesterName=bundle.getString("requesterName");
        String sellerByer=bundle.getString("sellerBuyer");
        String quantity=bundle.getString("quantity");
        bindCommodityName.set(commodityName);
        bindMode.set(mode);
        bindPrice.set(price);
        bindQuantity.set(quantity);
        bindReferenceId.set(referenceId);
        bindRequesterName.set(requesterName);
        bindSellerBuyer.set(sellerByer);


    }
}
