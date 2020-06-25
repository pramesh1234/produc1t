package com.codestrela.product.viewmodels;

import android.content.Intent;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.fragments.CreateCommodityFragment;
import com.codestrela.product.fragments.ListDialogFragment;
import com.codestrela.product.fragments.MyAccountFragment;
import com.codestrela.product.fragments.MyCommoditiesFragment;
import com.codestrela.product.fragments.MyContactListFragment;
import com.codestrela.product.fragments.PhoneSignInFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MyAccountViewModel {
    FirebaseAuth firebaseAuth;
    MyAccountFragment myAccountFragment;
    ListDialogFragment tv;
    FragmentManager fm;

    public MyAccountViewModel(MyAccountFragment myAccountFragment) {
        this.myAccountFragment = myAccountFragment;
        firebaseAuth = FirebaseAuth.getInstance();
        fm = myAccountFragment.getActivity().getSupportFragmentManager();
        tv = new ListDialogFragment();
    }

    public void onCreateCommodity(View view) {
        CreateCommodityFragment.addFragment((BaseActivity) myAccountFragment.getActivity());
    }

    public void onMyCommodity(View view) {
        MyCommoditiesFragment.addFragment((BaseActivity) myAccountFragment.getActivity());
    }

    public void onMyContact(View view) {
        MyContactListFragment.addFragment((BaseActivity) myAccountFragment.getActivity());
    }

    public void onSignOut(View view) {
        firebaseAuth.signOut();
        PhoneSignInFragment.addFragment((BaseActivity) myAccountFragment.getActivity());
    }

    public void onCreateGroup(View view) {
        tv.show(fm, "fma");
    }

}
