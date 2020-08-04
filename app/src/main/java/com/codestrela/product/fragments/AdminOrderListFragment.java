package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentAdminOrderListBinding;
import com.codestrela.product.viewmodels.AdminOrderListViewModel;

public class AdminOrderListFragment extends Fragment {

FragmentAdminOrderListBinding binding;
AdminOrderListViewModel vm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm=new AdminOrderListViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_admin_order_list, container, false);
        binding.setVm(vm);
        ((BaseActivity)getActivity()).setToolbarVisibility(true);
        return binding.getRoot();
    }
}