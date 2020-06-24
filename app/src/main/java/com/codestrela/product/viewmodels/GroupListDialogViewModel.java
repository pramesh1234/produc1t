package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codestrela.product.adapters.GroupSelectAdapter;
import com.codestrela.product.data.Contact;
import com.codestrela.product.data.Group;
import com.codestrela.product.fragments.GroupListDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupListDialogViewModel {
    public static final String GROUP_LIST = "group_list";
    private static final String TAG = "GroupListDialogViewMode";
    public GroupSelectAdapter adapter;
    public ArrayList<Group> group;
    public ArrayList<String> list;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ArrayList<RowSelectGroupViewModel> grouplist;
    Bundle args;
    GroupListDialogFragment groupListDialogFragment;
    Map<String, Object> getData = new HashMap<>();
    CreateCommodityViewModel viewModel = new CreateCommodityViewModel();


    public GroupListDialogViewModel(GroupListDialogFragment groupListDialogFragment) {
        this.groupListDialogFragment = groupListDialogFragment;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        args = groupListDialogFragment.getArguments();
        list = new ArrayList<>();
        group = new ArrayList<>();
        getData = (Map<String, Object>) args.getSerializable("getdata");
        Log.e(TAG, "eege" + getData.toString());
        adapter = new GroupSelectAdapter(new ArrayList<RowSelectGroupViewModel>());
        GroupListUpdate();

    }

    public void GroupListUpdate() {

        final RowSelectGroupViewModel m = new RowSelectGroupViewModel(this);
        grouplist = new ArrayList<>();
        SharedPreferences sharedPreferences = groupListDialogFragment.getActivity().getSharedPreferences("shared preference", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(GROUP_LIST, null);
        Type type = new TypeToken<ArrayList<Group>>() {
        }.getType();
        group = gson.fromJson(json, type);
        for (int i = 0; i < group.size(); i++) {
            RowSelectGroupViewModel viewModel = new RowSelectGroupViewModel(this);
            viewModel.groupId.set(group.get(i).groupId);
            viewModel.groupName.set(group.get(i).groupName);
            grouplist.add(viewModel);
        }
        adapter.addAll(grouplist);
    }

    public void onSubmitGroup(View view) {
        DocumentReference doc = db.collection("db_v1").document("barter_doc").collection("commodity_list").document();
        doc.set(getData);
        for (int i = 0; i < list.size(); i++) {
            db.collection("db_v1").document("barter_doc").collection("groups").document(list.get(i)).update("commodities", FieldValue.arrayUnion(doc.getId()));
        }
        groupListDialogFragment.dismiss();
    }

    public void onCancelGroup(View view) {
        groupListDialogFragment.dismiss();
    }
}
