package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentRequestReceivedSpecificationBinding;
import com.codestrela.product.databinding.FragmentRequestSentSpecificationBinding;
import com.codestrela.product.viewmodels.RequestSentSpecificationViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestSentSpecificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestSentSpecificationFragment extends Fragment {
    RequestSentSpecificationViewModel vm;
    FragmentRequestSentSpecificationBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       vm=new RequestSentSpecificationViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_request_sent_specification, container, false);
        binding.setVm(vm);
        ((BaseActivity)getActivity()).setToolbarVisibility(true);
        return binding.getRoot();
    }
}