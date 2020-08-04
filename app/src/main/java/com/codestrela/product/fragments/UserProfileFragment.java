package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentUserProfileBinding;
import com.codestrela.product.util.BindableString;
import com.codestrela.product.viewmodels.UserProfileViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {
 FragmentUserProfileBinding binding;
 UserProfileViewModel vm;

    public static void addFragment(BaseActivity activity) {
        activity.replaceFragment(new UserProfileFragment(), true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           vm=new UserProfileViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_user_profile, container, false);
        ((BaseActivity) getActivity()).setToolbarVisibility(true);
        binding.setVm(vm);
        return binding.getRoot();
    }
}