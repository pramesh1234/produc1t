package com.codestrela.product.viewmodels;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.codestrela.product.fragments.SentRequestDialogFragment;
import com.codestrela.product.util.AppUtil;
import com.codestrela.product.util.BindableString;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SentRequestDialogViewModel {
 Bundle args;
 FirebaseFirestore db;
    ProgressDialog dialog;
 HashMap<String,Object> request=new HashMap<>();
 public BindableString mode=new BindableString();
 public BindableString name=new BindableString();
 public BindableString specification=new BindableString();
 public BindableString quantity=new BindableString();
 public BindableString unit=new BindableString();
    private static final String TAG = "SentRequestDialogViewMo";
 String commodityName,commodityId,requestedBy,requestedTo,commodityUnit;
    SentRequestDialogFragment sentRequestDialogFragment;
    public SentRequestDialogViewModel(SentRequestDialogFragment sentRequestDialogFragment) {
        this.sentRequestDialogFragment=sentRequestDialogFragment;
        args=sentRequestDialogFragment.getArguments();
        db=FirebaseFirestore.getInstance();
        commodityName=args.getString("data");
        Log.e(TAG, "SentRequestDialogViewModel: "+commodityName );
        commodityName=args.getString("commodityName");
        commodityId=args.getString("commodityId");
        requestedBy=args.getString("requestedBy");
        requestedTo=args.getString("requestedTo");
        commodityUnit=args.getString("quantity");
        unit.set(commodityUnit);
        name.set(commodityName);
    }
    public void onSendRequest(View view){
         dialog=new ProgressDialog(sentRequestDialogFragment.getActivity());
        dialog.setMessage("Sending Request...");

        request.put("requested_by",requestedBy);
        request.put("requested_to",requestedTo);
        request.put("commodity_id",commodityId);
        request.put("commodity_name",commodityName);
        request.put("mode",mode.get());
        request.put("specification",specification.get());
        request.put("quantity",quantity.get());
        int sellerQuant=Integer.parseInt(quantity.get());
        int buyerQuant=Integer.parseInt(commodityUnit);
        if(buyerQuant>sellerQuant){
            dialog.show();
            db.collection("db_v1").document("barter_doc").collection("requests").document().set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    AppUtil.showToast(sentRequestDialogFragment.getActivity(),"Some error occured");
                    dialog.dismiss();
                }
            });
        }else{
            AppUtil.showToast(sentRequestDialogFragment.getActivity(),"entered unit is invalid");
        }


    }

}
