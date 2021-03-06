package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentImageBinding;
import com.codestrela.product.viewmodels.ImageViewModel;

public class ImageFragment extends DialogFragment {
 FragmentImageBinding binding;
 ImageViewModel vm;

    public static void addFragment(BaseActivity activity,Fragment fragment) {
        activity.replaceFragment(fragment, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      vm=new ImageViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     binding= DataBindingUtil.inflate(inflater,R.layout.fragment_image, container, false);
     binding.setVm(vm);
        return binding.getRoot();
    }
}