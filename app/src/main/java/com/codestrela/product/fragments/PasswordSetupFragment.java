package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentPasswordSetupBinding;
import com.codestrela.product.viewmodels.PassWordSetUpViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PasswordSetupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PasswordSetupFragment extends Fragment {
    FragmentPasswordSetupBinding binding;
    PassWordSetUpViewModel vm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new PassWordSetUpViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_password_setup, container, false);
        ((BaseActivity) getActivity()).setToolbarVisibility(false);
        return binding.getRoot();
    }
}