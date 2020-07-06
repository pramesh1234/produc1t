package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codestrela.product.adapters.GroupCommodityListAdapter;
import com.codestrela.product.adapters.MyCommoditiesAdapter;
import com.codestrela.product.fragments.MyCommoditiesFragment;
import com.codestrela.product.util.BindableBoolean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyCommoditiesViewModel {
    private static final String TAG = "MyCommoditiesViewModel";
    public GroupCommodityListAdapter myCommoditiesAdapter;
    public FirebaseFirestore db;
    MyCommoditiesFragment myCommoditiesFragment;
    ArrayList<RowGroupCommodityList> viewModels;
    RowGroupCommodityList viewModel;
    public BindableBoolean progress=new BindableBoolean();
    public BindableBoolean noCommodity=new BindableBoolean();
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    FirebaseAuth mAuth;

    public MyCommoditiesViewModel(MyCommoditiesFragment myCommoditiesFragment) {
        this.myCommoditiesFragment = myCommoditiesFragment;
        db = FirebaseFirestore.getInstance();
        progress.set(true);
        noCommodity.set(false);
        mAuth = FirebaseAuth.getInstance();
        viewModels = new ArrayList<>();
        Log.e(TAG, "onComplete: joaioda"+loadData(myCommoditiesFragment.getActivity()));
        myCommoditiesAdapter = new GroupCommodityListAdapter(new ArrayList<RowGroupCommodityList>());
        db.collection("db_v1").document("barter_doc").collection("commodity_list").whereEqualTo("created_by_doc_id",loadData(myCommoditiesFragment.getActivity())).get()
    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    noCommodity.set(true);
                    progress.set(false);
                }

                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Log.e(TAG, "onComplete: joaioda");
                            String name = doc.getString("name");
                            String price = doc.getString("price");
                            String image = doc.getString("image");
                             progress.set(false);
                            viewModel = new RowGroupCommodityList();
                            viewModel.commodityName.set(name);
                            viewModel.commodityPrice.set(price);
                            viewModel.commodityImage.set(image);
                            viewModels.add(viewModel);
                            Log.e(TAG, "name: " + name);

                    }
                    myCommoditiesAdapter.addAll(viewModels);

                }
        }
    });

    }

    public void fillData(String name, String price) {

    }
    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

}
