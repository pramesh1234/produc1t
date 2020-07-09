package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.codestrela.product.adapters.GroupCommodityListAdapter;
import com.codestrela.product.fragments.PublicFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class PublicViewModel {
    public GroupCommodityListAdapter adapter;
    FirebaseFirestore db;
    RowGroupCommodityList viewModel;
    ArrayList<RowGroupCommodityList> arrayList;
    PublicFragment publicFragment;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";

    public PublicViewModel(PublicFragment publicFragment) {
        this.publicFragment = publicFragment;
        db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<>();
        adapter = new GroupCommodityListAdapter(new ArrayList<RowGroupCommodityList>());
        getCommodity();
    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    public void getCommodity() {
        db.collection("db_v1").document("barter_doc").collection("commodity_list").whereEqualTo("type", "public").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String commodityId=document.getId();
                                String requestedTo=document.getString("created_by_doc_id");
                                String requestedBy=loadData(publicFragment.getActivity());
                                String name = document.getString("name");
                                String price = document.getString("price");
                                String image = document.getString("image");
                                viewModel = new RowGroupCommodityList(commodityId,requestedTo,requestedBy,publicFragment);
                                viewModel.commodityName.set(name);
                                viewModel.commodityPrice.set(price);
                                viewModel.commodityImage.set(image);
                                arrayList.add(viewModel);
                                adapter.addAll(viewModel);
                            }
                        }
                    }
                });
    }
}
