package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentRequestBinding;
import com.codestrela.product.viewmodels.RequestViewModel;
import com.google.android.gms.common.util.DataUtils;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestFragment extends Fragment {
    FragmentRequestBinding binding;
    RequestViewModel vm;

    public static void addFragment(BaseActivity activity,Fragment fragment) {
        activity.replaceFragment(fragment, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm =new RequestViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_request, container, false);
        binding.setVm(vm);
        ViewPager viewPager = binding.getRoot().findViewById(R.id.event_view_pager);
        TabLayout tabLayout = binding.getRoot().findViewById(R.id.event_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        return binding.getRoot();
    }

}