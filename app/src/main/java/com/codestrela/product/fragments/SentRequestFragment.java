package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;
import com.codestrela.product.databinding.FragmentSentRequestBinding;
import com.codestrela.product.viewmodels.SentRequestViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SentRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SentRequestFragment extends Fragment {

FragmentSentRequestBinding binding;
SentRequestViewModel vm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         vm=new SentRequestViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_sent_request, container, false);
        binding.setVm(vm);
        return binding.getRoot();
    }
}