package com.codestrela.product.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentSellingTransactionBinding;
import com.codestrela.product.databinding.FragmentTransactionBinding;
import com.codestrela.product.viewmodels.SellingTransactionViewModel;

public class SellingTransactionFragment extends Fragment {
    FragmentSellingTransactionBinding binding;
    SellingTransactionViewModel vm;

    public static void addFragment(BaseActivity activity) {
        activity.replaceFragment(new SellingTransactionFragment(), true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new SellingTransactionViewModel(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_selling_transaction, container, false);
        ((BaseActivity) getActivity()).setToolbarVisibility(true);
        binding.setVm(vm);
        return binding.getRoot();
    }
}