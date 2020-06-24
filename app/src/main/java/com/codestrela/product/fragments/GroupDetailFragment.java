package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentGroupDetailBinding;
import com.codestrela.product.viewmodels.GroupDetailViewModel;

public class GroupDetailFragment extends Fragment {
    GroupDetailViewModel vm;
    FragmentGroupDetailBinding binding;

    public static void addFragment(BaseActivity activity, Fragment fragment) {
        activity.replaceFragment(fragment, true);
    }

    // TODO: Rename and change types of parameters
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new GroupDetailViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group_detail, container, false);
        ((BaseActivity) getActivity()).setToolbarVisibility(false);
        binding.setVm(vm);
        return binding.getRoot();
    }
}