package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.codestrela.product.adapters.GroupListAdapter;
import com.codestrela.product.data.Contact;
import com.codestrela.product.data.Group;
import com.codestrela.product.fragments.GroupTabFragment;
import com.codestrela.product.fragments.ListDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class GroupTabViewModel {
    public static final String GROUP_LIST = "group_list";
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    private static final String TAG = "GroupTabViewModel";
    public GroupListAdapter adapter;
    FirebaseFirestore db;
    GroupTabFragment groupTabFragment;
    ArrayList<Contact> contacts;
    FragmentManager fm;
    ListDialogFragment tv;
    RowGroupListViewModel viewModel;
    ArrayList<Group> group;
    ArrayList<RowGroupListViewModel> groupList;

    public GroupTabViewModel(GroupTabFragment groupTabFragment) {
        this.groupTabFragment = groupTabFragment;
        fm = groupTabFragment.getActivity().getSupportFragmentManager();
        db = FirebaseFirestore.getInstance();

        adapter = new GroupListAdapter(new ArrayList<RowGroupListViewModel>());
        tv = new ListDialogFragment();
        groupList();
    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    public void groupList() {
        db.collection("db_v1").document("barter_doc").collection("groups").whereArrayContains("groupMembers", loadData(groupTabFragment.getContext())).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                            }
                            groupList = new ArrayList<>();
                            group = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String name = doc.getString("groupName");
                                String groupId = doc.getString("groupId");
                                viewModel = new RowGroupListViewModel(groupTabFragment, groupId);
                                viewModel.groupName.set(name);
                                group.add(new Group(viewModel.groupName.get(), groupId));
                                groupList.add(viewModel);
                            }
                        }
                        SharedPreferences sharedPreferences = groupTabFragment.getActivity().getSharedPreferences("shared preference", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(group);
                        editor.putString(GROUP_LIST, json);
                        editor.apply();
                        adapter.addAll(groupList);
                    }
                });
    }

    public void onCreateGroup(View view) {
        tv.show(fm, "fma");
    }
}