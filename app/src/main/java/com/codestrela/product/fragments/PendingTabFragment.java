package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentPendingTabBinding;
import com.codestrela.product.viewmodels.PendingTabViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingTabFragment extends Fragment {
    PendingTabViewModel vm;
    FragmentPendingTabBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new PendingTabViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pending_tab, container, false);
        binding.setVm(vm);
        ((BaseActivity) getActivity()).setToolbarVisibility(false);
        return binding.getRoot();
    }
}
