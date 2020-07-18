package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentTransactionDetailsBinding;
import com.codestrela.product.viewmodels.TransactionDetailsViewModel;

public class TransactionDetailsFragment extends Fragment {
    TransactionDetailsViewModel vm;
    FragmentTransactionDetailsBinding binding;

    public static void addFragment(BaseActivity activity,Fragment fragment) {
        activity.replaceFragment(fragment, true);
    }

  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm=new TransactionDetailsViewModel(this);
        getActivity().setTitle("Transactions");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_transaction_details, container, false);
        binding.setVm(vm);
        ((BaseActivity)getActivity()).setToolbarVisibility(true);
        return binding.getRoot();
    }
}