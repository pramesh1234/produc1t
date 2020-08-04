package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.codestrela.product.adapters.MyOrderAdapter;
import com.codestrela.product.fragments.MyOrdersFragment;
import com.codestrela.product.util.BindableBoolean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyOrdersViewModel {
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    public MyOrderAdapter orderAdapter;
    public BindableBoolean emptyList=new BindableBoolean();
    public BindableBoolean loading=new BindableBoolean();
    MyOrdersFragment myOrdersFragment;
    FirebaseFirestore db;
    public MyOrdersViewModel(MyOrdersFragment myOrdersFragment) {
        this.myOrdersFragment=myOrdersFragment;
        loading.set(true);
        orderAdapter=new MyOrderAdapter(new ArrayList<RowOrderListViewModel>());
        db=FirebaseFirestore.getInstance();
        getOrders();
        myOrdersFragment.getActivity().setTitle("My Orders");

    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    public void getOrders() {
        db.collection("db_v1").document("barter_doc").collection("accepted_request").whereEqualTo("requested_by", loadData(myOrdersFragment.getActivity())).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()) {
                            emptyList.set(true);
                            loading.set(false);
                        }
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot doc : task.getResult()) {
                             loading.set(false);
                                final String requestedBy = doc.getString("requested_by");
                                final String requestedTo = doc.getString("requested_to");
                                final String commodityName = doc.getString("commodity_name");
                                final String quantity = doc.getString("quantity");
                                final String mode = doc.getString("mode");
                                final String referenceId=doc.getString("reference_id");
                                final String requestId = doc.getId();
                                final String price=doc.getString("price");
                                final String specification = doc.getString("specification");
                                RowOrderListViewModel viewModel=new RowOrderListViewModel(myOrdersFragment,requestId);
                                viewModel.commodityName.set(commodityName);
                                viewModel.orderId.set(referenceId);
                                orderAdapter.add(viewModel);


                            }


                        }

                    }
                });

    }
}
