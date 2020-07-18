package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;
import com.codestrela.product.databinding.FragmentAcceptRequestBinding;
import com.codestrela.product.viewmodels.AcceptRequestViewModel;

public class AcceptRequestFragment extends Fragment {
FragmentAcceptRequestBinding binding;
AcceptRequestViewModel vm;
   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                 vm=new AcceptRequestViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_accept_request, container, false);
        binding.setVm(vm);
        return binding.getRoot();
    }
}