package com.codestrela.product.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentChatBinding;
import com.codestrela.product.viewmodels.ChatViewModel;

public class ChatFragment extends Fragment {
    FragmentChatBinding binding;
    ChatViewModel vm;

    public static void addFragment(BaseActivity activity) {
        activity.replaceFragment(new ChatFragment(), true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ChatViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((BaseActivity) getActivity()).setToolbarVisibility(true);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
        binding.setVm(vm);
        return binding.getRoot();
    }
}