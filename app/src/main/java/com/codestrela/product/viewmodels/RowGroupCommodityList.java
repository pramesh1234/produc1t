package com.codestrela.product.viewmodels;

import android.util.Log;
import android.view.View;

import com.codestrela.product.util.BindableString;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RowGroupCommodityList {
    private static final String TAG = "RowGroupCommodityList";
    FirebaseFirestore db;
    HashMap<String,Object> request=new HashMap<>();
    public BindableString commodityName = new BindableString();
    public BindableString commodityPrice = new BindableString();
    public BindableString commodityImage = new BindableString();
    String commodityId,requestedTo,requestedBy;

    public RowGroupCommodityList(String commodityId, String requestedTo, String requestedBy) {
        this.commodityId=commodityId;
        this.requestedTo=requestedTo;
        this.requestedBy=requestedBy;
        db=FirebaseFirestore.getInstance();
    }
    public RowGroupCommodityList() {

    }


    public void onRequestClick(View view){
        request.put("requested_by",requestedBy);
        request.put("requestedTo",requestedTo);
        request.put("commodityId",commodityId);
        //Log.e(TAG, "onRequestClick: commodity id "+commodityId+" created by "+createdBy);
        db.collection("db_v1").document("barter_doc").collection("requests").document().set(request);
    }

}
