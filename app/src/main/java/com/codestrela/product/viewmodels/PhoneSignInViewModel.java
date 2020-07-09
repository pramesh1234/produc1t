package com.codestrela.product.viewmodels;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.fragments.EmailSignInFragment;
import com.codestrela.product.fragments.PhoneLoginFragment;
import com.codestrela.product.fragments.PhoneRegisterFragment;
import com.codestrela.product.fragments.PhoneSignInFragment;
import com.codestrela.product.util.BindableBoolean;
import com.codestrela.product.util.BindableString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PhoneSignInViewModel {
    public BindableString phoneNumber = new BindableString();
    public BindableBoolean loading=new BindableBoolean();
    PhoneSignInFragment phoneSignInFragment;
    FirebaseFirestore db;

    public PhoneSignInViewModel(PhoneSignInFragment phoneSignInFragment) {
        this.phoneSignInFragment = phoneSignInFragment;
        loading.set(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && phoneSignInFragment.getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            phoneSignInFragment.getActivity().requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            //     getContacts();
        }
        db = FirebaseFirestore.getInstance();
    }

    public void onSubmitClicked(View view) {
        loading.set(true);
        checkUser();

    }

    public void onSignInClicked(View view) {
        EmailSignInFragment.addFragment((BaseActivity) phoneSignInFragment.getActivity());
    }

    public void checkUser() {
        String number = "+91" + phoneNumber.get();

        db.collection("db_v1").document("barter_doc").collection("users").whereEqualTo("phone_number", number)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                loading.set(false);
                                PhoneRegisterFragment fragment = new PhoneRegisterFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("phoneNumber", phoneNumber.get());
                                fragment.setArguments(bundle);
                                fragment.addFragment((BaseActivity) phoneSignInFragment.getActivity(), fragment);
                            } else {
                                loading.set(false);
                                PhoneLoginFragment fragment = new PhoneLoginFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("phoneNumber", phoneNumber.get());
                                fragment.setArguments(bundle);
                                fragment.addFragment((BaseActivity) phoneSignInFragment.getActivity(), fragment);
                            }
                        }
                    }
                });
    }

}
