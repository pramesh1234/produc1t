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
import com.codestrela.product.databinding.FragmentRequestDetailBinding;
import com.codestrela.product.databinding.FragmentSentRequestBinding;
import com.codestrela.product.databinding.FragmentSentRequestDetailBinding;
import com.codestrela.product.viewmodels.SentRequestDetailViewModel;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SentRequestDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SentRequestDetailFragment extends Fragment {
FragmentSentRequestDetailBinding binding;
SentRequestDetailViewModel vm;

    public static void addFragment(BaseActivity activity,Fragment fragment) {
        activity.replaceFragment(fragment, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     vm=new SentRequestDetailViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_sent_request_detail, container, false);
        binding.setVm(vm);
        ((BaseActivity)getActivity()).setToolbarVisibility(true);
        ViewPager viewPager = binding.getRoot().findViewById(R.id.event_view_pager);
        TabLayout tabLayout = binding.getRoot().findViewById(R.id.event_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        return inflater.inflate(R.layout.fragment_sent_request_detail, container, false);
    }
}