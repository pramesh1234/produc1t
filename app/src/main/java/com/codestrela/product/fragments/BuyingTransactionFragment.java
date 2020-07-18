package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentBuyingTransactionBinding;
import com.codestrela.product.viewmodels.BuyingTransactionViewModel;

public class BuyingTransactionFragment extends Fragment {
    FragmentBuyingTransactionBinding binding;
    BuyingTransactionViewModel vm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm=new BuyingTransactionViewModel(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding= DataBindingUtil.inflate(inflater,R.layout.fragment_buying_transaction, container, false);
        ((BaseActivity) getActivity()).setToolbarVisibility(true);
       binding.setVm(vm);
        return binding.getRoot();
    }
}