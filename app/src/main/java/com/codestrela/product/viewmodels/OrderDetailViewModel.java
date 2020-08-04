package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.codestrela.product.fragments.OrderDetailFragment;
import com.codestrela.product.util.BindableString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.Context.MODE_PRIVATE;

public class OrderDetailViewModel {
    private static final String TAG = "OrderDetailViewModel";
    public BindableString commodityNameBindable=new BindableString();
    public BindableString orderNoBindable=new BindableString();
    public BindableString orderStatusBindable=new BindableString();
    public BindableString orderQuantityBindable=new BindableString();
    OrderDetailFragment orderDetailFragment;
    FirebaseFirestore db;
    String orderReference;
    public OrderDetailViewModel(OrderDetailFragment orderDetailFragment) {
        this.orderDetailFragment=orderDetailFragment;
        Bundle bundle=orderDetailFragment.getArguments();
         orderReference=bundle.getString("orderReference");
        db=FirebaseFirestore.getInstance();
        Log.e(TAG, "OrderDetailViewModel: "+orderReference );
          getOrders();
    }
    public void getOrders() {
        db.collection("db_v1").document("barter_doc").collection("accepted_request").document(orderReference).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String commodityName=document.getString("commodity_name");
                                String orderStatus=document.getString("order_status");
                                String orderNo=document.getString("reference_id");
                                String orderQuantity=document.getString("quantity");
                                commodityNameBindable.set(commodityName);
                                orderNoBindable.set(orderNo);
                                orderStatusBindable.set(orderStatus);
                                orderQuantityBindable.set(orderQuantity);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }

}
