package com.codestrela.product.viewmodels;

import android.util.Log;

import androidx.annotation.Nullable;

import com.codestrela.product.adapters.MyCommoditiesAdapter;
import com.codestrela.product.fragments.MyCommoditiesFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyCommoditiesViewModel {
    private static final String TAG = "MyCommoditiesViewModel";
    public MyCommoditiesAdapter myCommoditiesAdapter;
    public FirebaseFirestore db;
    MyCommoditiesFragment myCommoditiesFragment;
    ArrayList<RowCommodityViewModel> viewModels;
    RowCommodityViewModel viewModel;
    FirebaseAuth mAuth;

    public MyCommoditiesViewModel(MyCommoditiesFragment myCommoditiesFragment) {
        this.myCommoditiesFragment = myCommoditiesFragment;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getUid();
        viewModels = new ArrayList<>();
        myCommoditiesAdapter = new MyCommoditiesAdapter(new ArrayList<RowCommodityViewModel>());
        db.collection("users").document(userId).collection("commodity_list").addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "error :" + e.getMessage());
                        }
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                String name = doc.getDocument().getString("name");
                                String price = doc.getDocument().getString("price");
                                String type = doc.getDocument().getString("type");
                                String qty = doc.getDocument().getString("unit");
                                String mode = doc.getDocument().getString("mode");

                                viewModel = new RowCommodityViewModel();
                                viewModel.name.set(name);
                                viewModel.price.set(price);
                                viewModel.type.set(type);
                                viewModel.qty.set(qty);
                                viewModel.mode.set(mode);
                                viewModels.add(viewModel);
                                Log.e(TAG, "name: " + name);
                            }
                        }
                        myCommoditiesAdapter.addAll(viewModels);
                    }
                }
        );

    }

    public void fillData(String name, String price) {

    }

}
