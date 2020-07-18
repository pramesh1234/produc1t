package com.codestrela.product.viewmodels;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.fragments.RequestReceivedSpecificationFragment;
import com.codestrela.product.fragments.SellingTransactionFragment;
import com.codestrela.product.util.BindableString;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Random;

public class RequestReceivedSpecificationViewModel {
    private static final String TAG = "RequestReceivedSpecific";
    public BindableString modeBindable = new BindableString();
    public BindableString quantityBindable = new BindableString();
    public BindableString commodityNameBindable = new BindableString();
    public BindableString specificationBindable = new BindableString();
    String commodityName, mode, specification, quantity,requestedTo,requestedBy;
    FirebaseFirestore db;
    ProgressDialog dialog;

    RequestReceivedSpecificationFragment requestReceivedSpecificationFragment;

    public RequestReceivedSpecificationViewModel(RequestReceivedSpecificationFragment requestReceivedSpecificationFragment) {
        this.requestReceivedSpecificationFragment = requestReceivedSpecificationFragment;
        Bundle bundle = requestReceivedSpecificationFragment.getArguments();
        commodityName = bundle.getString("commodityName");
        mode = bundle.getString("mode");
        requestedBy=bundle.getString("requestedBy");
        dialog=new ProgressDialog(requestReceivedSpecificationFragment.getContext());
        requestedTo=bundle.getString("requestedTo");
        specification = bundle.getString("specification");
        quantity = bundle.getString("quantity");
        modeBindable.set(mode);
        quantityBindable.set(quantity);
        commodityNameBindable.set(commodityName);
        db=FirebaseFirestore.getInstance();
        specificationBindable.set(specification);
        Log.e(TAG, "RequestReceivedSpecificationViewModel: " + mode + " oiha " + specification + " oihoia " + commodityName + " oijoa " + quantity);
    }
    public void onRequestAcceptance(View view){
        dialog.setMessage("Processing...");
        dialog.show();
        Random random = new Random();
        int randomNumber = random.nextInt(999 - 100) + 100;
        HashMap<String,Object> transaction=new HashMap<>();
        transaction.put("commodity_name",commodityName);
        transaction.put("quantity",quantity);
        transaction.put("mode",mode);
        transaction.put("requested_by",requestedBy);
        transaction.put("requested_to",requestedTo);
        transaction.put("specification",specification);
        transaction.put("reference_id","#001TRI"+randomNumber);
        db.collection("db_v1").document("barter_doc").collection("accepted_request").document().set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();
                SellingTransactionFragment.addFragment((BaseActivity) requestReceivedSpecificationFragment.getActivity());
            }
        });

    }
}
