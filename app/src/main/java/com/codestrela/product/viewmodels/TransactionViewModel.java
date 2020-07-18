package com.codestrela.product.viewmodels;

import androidx.fragment.app.FragmentStatePagerAdapter;

import com.codestrela.product.adapters.TransactionTabAdapter;
import com.codestrela.product.fragments.TransactionFragment;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class TransactionViewModel {
    public TransactionTabAdapter transactionTabAdapter;
    TransactionFragment transactionFragment;
    public TransactionViewModel(TransactionFragment transactionFragment) {
        this.transactionFragment=transactionFragment;
        transactionTabAdapter=new TransactionTabAdapter(transactionFragment.getChildFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }
}
