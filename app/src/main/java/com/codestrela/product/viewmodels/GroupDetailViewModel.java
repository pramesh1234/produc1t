package com.codestrela.product.viewmodels;

import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.codestrela.product.adapters.GroupDetailAdapter;
import com.codestrela.product.fragments.GroupDetailFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class GroupDetailViewModel {
    private static final String TAG = "GroupDetailViewModel";
    public GroupDetailAdapter adapter;
    FirebaseFirestore db;
    GroupDetailFragment groupDetailFragment;

    public GroupDetailViewModel(GroupDetailFragment groupDetailFragment) {
        this.groupDetailFragment = groupDetailFragment;
        db = FirebaseFirestore.getInstance();
        Bundle bundle = groupDetailFragment.getArguments();
        String groupId = bundle.getString("groupId");

        adapter = new GroupDetailAdapter(new ArrayList<RowGroupDetaill>());
        db.collection("db_v1").document("barter_doc").collection("groups").document(groupId).addSnapshotListener(
                new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        ArrayList<String> groupMembers = (ArrayList<String>) documentSnapshot.get("groupMembers");
                        Log.e(TAG, "onEvent: " + groupMembers.size());
                        for (int i = 0; i < groupMembers.size(); i++) {
                            db.collection("db_v1").document("barter_doc").collection("users").document(groupMembers.get(i)).addSnapshotListener(
                                    new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                            String name = documentSnapshot.getString("Name");
                                            String number = documentSnapshot.getString("Phone Number");
                                            Log.e(TAG, "onEvent: " + name + " " + number);
                                            RowGroupDetaill viewmodel = new RowGroupDetaill();
                                            viewmodel.name.set(name);
                                            viewmodel.number.set(number);
                                            adapter.addAll(viewmodel);
                                        }
                                    });

                        }

                    }
                }
        );
        Toast.makeText(groupDetailFragment.getContext(), "" + groupId, Toast.LENGTH_SHORT).show();
    }
}
