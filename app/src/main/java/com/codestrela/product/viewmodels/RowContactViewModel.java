package com.codestrela.product.viewmodels;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.codestrela.product.fragments.GroupListDialogFragment;
import com.codestrela.product.fragments.GroupListMemberDialogFragment;
import com.codestrela.product.fragments.MyContactListFragment;
import com.codestrela.product.util.BindableBoolean;
import com.codestrela.product.util.BindableString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;

public class RowContactViewModel {
    private static final String TAG = "RowContactViewModel";
    public BindableString contactName = new BindableString();
    public BindableString contactNumber = new BindableString();
    public BindableBoolean visiblity = new BindableBoolean();
    FragmentManager fm;
    GroupListMemberDialogFragment fragment;
    MyContactListFragment myContactListFragment;
    FirebaseFirestore db;

    public RowContactViewModel() {
    }

    public RowContactViewModel(GroupListMemberDialogFragment fragment, MyContactListFragment myContactListFragment) {

        this.fragment = fragment;
        this.myContactListFragment = myContactListFragment;
        db = FirebaseFirestore.getInstance();
    }


    public void onJoinClicked(View view) {
        String contact;
        if (contactNumber.get().length() == 10) {
            contact = "+91" + contactNumber.get();
        } else {
            contact = contactNumber.get();
        }
        db.collection("db_v1").document("barter_doc").collection("users").whereEqualTo("Phone Number", contact).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                Log.e(TAG, "onComplete: empty");
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Bundle args = new Bundle();
                                    args.putString("contactId", document.getId());
                                    fragment = new GroupListMemberDialogFragment();
                                    fragment.setArguments(args);
                                    fm = myContactListFragment.getActivity().getSupportFragmentManager();
                                    fragment.show(fm, "fma");
                                }
                            }
                        }
                    }
                });


    }

    public void onShareClicked(View view) {

        String link = "pramesh has invited you to install this application";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, link);

        myContactListFragment.startActivity(Intent.createChooser(intent, "Share Link"));
    }
}
