package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.codestrela.product.adapters.GroupCommodityListAdapter;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.fragments.GroupCommodityListFragment;
import com.codestrela.product.fragments.GroupDetailFragment;
import com.codestrela.product.util.BindableBoolean;
import com.codestrela.product.util.BindableString;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class GroupCommodityListViewModel {
    private static final String TAG = "GroupCommodityListViewM";
    public BindableString groupName = new BindableString();
    public GroupCommodityListAdapter adapter;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    public BindableBoolean load=new BindableBoolean();
    public BindableBoolean noCommodity=new BindableBoolean();
    FirebaseFirestore db;
    RowGroupCommodityList viewModel;
    ArrayList<RowGroupCommodityList> arrayList;
    GroupCommodityListFragment groupCommodityListFragment;
    String groupId;

    public GroupCommodityListViewModel(GroupCommodityListFragment groupCommodityListFragment) {
        this.groupCommodityListFragment = groupCommodityListFragment;
        load.set(true);
        noCommodity.set(false);
        db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<>();
        adapter = new GroupCommodityListAdapter(new ArrayList<RowGroupCommodityList>());
        getCommodityList();
    }

    public void getCommodityList() {
        Bundle bundle = groupCommodityListFragment.getArguments();
        groupId = bundle.getString("groupId");
        String title = bundle.getString("groupName");
        groupName.set(title);
        db.collection("db_v1").document("barter_doc").collection("groups").document(groupId).addSnapshotListener(
                new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "error :" + e.getMessage());
                        }

                        final List<String> commodityId = (List<String>) documentSnapshot.get("commodities");
                        if(commodityId.size()==0){
                            noCommodity.set(true);
                            load.set(false);
                            Log.e(TAG, "onEvent: kuch bhi7" );
                        }else {
                            for (int i = 0; i < commodityId.size(); i++) {
                                final int finalI = i;
                                db.collection("db_v1").document("barter_doc").collection("commodity_list").document(commodityId.get(i)).addSnapshotListener(
                                        new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                if (e != null) {
                                                    Log.e(TAG, "error :" + e.getMessage());
                                                }
                                                load.set(false);
                                                String createdBy=documentSnapshot.getString("created_by_doc_id");
                                                String name = documentSnapshot.getString("name");
                                                String price = documentSnapshot.getString("price");
                                                String quanity=documentSnapshot.getString("unit");
                                                String image = documentSnapshot.getString("image");
                                                viewModel = new RowGroupCommodityList(commodityId.get(finalI),createdBy,loadData(groupCommodityListFragment.getContext()),groupCommodityListFragment,quanity);
                                                viewModel.commodityName.set(name);
                                                viewModel.commodityPrice.set(price);
                                                viewModel.commodityImage.set(image);
                                                arrayList.add(viewModel);
                                                Log.e(TAG, "name: " + name);
                                                adapter.addAll(viewModel);
                                                Log.e(TAG, "onEvent: " + arrayList.size());
                                            }


                                        }

                                );


                            }
                        }
                    }

                }
        );

    }

    public void onDetailClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("groupId", groupId);
        GroupDetailFragment fragment = new GroupDetailFragment();
        fragment.setArguments(bundle);
        fragment.addFragment((BaseActivity) groupCommodityListFragment.getActivity(), fragment);
    }
    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }
}
