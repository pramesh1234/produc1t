package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;
import com.codestrela.product.databinding.FragmentRequestReceivedSpecificationBinding;
import com.codestrela.product.viewmodels.RequestReceivedSpecificationViewModel;

public class RequestReceivedSpecificationFragment extends Fragment {
FragmentRequestReceivedSpecificationBinding binding;
RequestReceivedSpecificationViewModel vm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm=new RequestReceivedSpecificationViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_request_received_specification, container, false);
        binding.setVm(vm);
        return binding.getRoot();
    }
}