package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codestrela.product.adapters.GroupSelectAdapter;
import com.codestrela.product.data.Group;
import com.codestrela.product.fragments.GroupListDialogFragment;
import com.codestrela.product.fragments.GroupListMemberDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupListMemberDialogViewModel {
    public static final String GROUP_LIST = "group_list";
    private static final String TAG = "GroupListMemberDialogViewModel";
    public GroupSelectAdapter adapter;
    public ArrayList<Group> group;
    public ArrayList<String> list;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ArrayList<RowSelectGroupViewModel> grouplist;
    String contactId;
    Bundle args;
    GroupListMemberDialogFragment groupListMemberDialogFragment;

    public GroupListMemberDialogViewModel(GroupListMemberDialogFragment groupListMemberDialogFragment) {
        this.groupListMemberDialogFragment = groupListMemberDialogFragment;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        args = groupListMemberDialogFragment.getArguments();
        list = new ArrayList<>();
        group = new ArrayList<>();
        contactId = args.getString("contactId");
        Toast.makeText(groupListMemberDialogFragment.getContext(), "" + contactId, Toast.LENGTH_SHORT).show();
        adapter = new GroupSelectAdapter(new ArrayList<RowSelectGroupViewModel>());
        groupListUpdate();
    }

    public void groupListUpdate() {
        final RowSelectGroupViewModel m = new RowSelectGroupViewModel(this);
        grouplist = new ArrayList<>();
        SharedPreferences sharedPreferences = groupListMemberDialogFragment.getActivity().getSharedPreferences("shared preference", Context.MODE_PRIVATE);
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

    public void onSubmit(View view) {
        for (int i = 0; i < grouplist.size(); i++) {
            db.collection("db_v1").document("barter_doc").collection("groups").document(grouplist.get(i).groupId.get()).update("groupMembers", FieldValue.arrayUnion(contactId));
        }
        groupListMemberDialogFragment.dismiss();
    }
}
