package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentGroupTabBinding;
import com.codestrela.product.viewmodels.GroupTabViewModel;

public class GroupTabFragment extends Fragment {
    GroupTabViewModel vm;
    FragmentGroupTabBinding binding;
    private static final String TAG = "GroupTabFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new GroupTabViewModel(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group_tab, container, false);
        binding.setVm(vm);
        ((BaseActivity) getActivity()).setToolbarVisibility(false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: " );
    }
}