package com.codestrela.product.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentMyOrdersBinding;
import com.codestrela.product.viewmodels.MyOrdersViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrdersFragment extends Fragment {
    FragmentMyOrdersBinding binding;
    MyOrdersViewModel vm;

    public static void addFragment(BaseActivity activity) {
        activity.replaceFragment(new MyOrdersFragment(), true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new MyOrdersViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_orders, container, false);
        ((BaseActivity) getActivity()).setToolbarVisibility(true);
        binding.setVm(vm);
        return binding.getRoot();
    }
}