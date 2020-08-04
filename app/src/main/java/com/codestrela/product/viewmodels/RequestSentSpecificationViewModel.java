package com.codestrela.product.viewmodels;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.codestrela.product.fragments.RequestSentSpecificationFragment;
import com.codestrela.product.util.BindableString;
import com.google.firebase.firestore.FirebaseFirestore;

public class RequestSentSpecificationViewModel {
    private static final String TAG = "RequestSentSpecific";
    public BindableString modeBindable = new BindableString();
    public BindableString quantityBindable = new BindableString();
    public BindableString commodityNameBindable = new BindableString();
    public BindableString specificationBindable = new BindableString();
    public BindableString requestedToBindable=new BindableString();
    RequestSentSpecificationFragment requestSentSpecificationFragment;
    String commodityName, mode, specification, quantity,requestedTo,requestedBy,requestRef,requester;
    public RequestSentSpecificationViewModel(RequestSentSpecificationFragment requestSentSpecificationFragment) {
        this.requestSentSpecificationFragment=requestSentSpecificationFragment;
        Bundle bundle = requestSentSpecificationFragment.getArguments();
        commodityName = bundle.getString("commodityName");
        mode = bundle.getString("mode");
        requestedBy=bundle.getString("requestedBy");
        requestedTo=bundle.getString("requestedTo");
        specification = bundle.getString("specification");
        requestRef=bundle.getString("requestRef");
        quantity = bundle.getString("quantity");
        requester=bundle.getString("requester");
        modeBindable.set(mode);
        quantityBindable.set(quantity);
        commodityNameBindable.set(commodityName);
        specificationBindable.set(specification);
        requestedToBindable.set(requester);
        Log.e(TAG, "RequestReceivedSpecificationViewModel: " + mode + " oiha " + specification + " oihoia " + commodityName + " oijoa " + quantity);

    }
}
