package com.codestrela.product.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentNotificationListBinding;
import com.codestrela.product.viewmodels.NotificationListViewModel;

public class NotificationListFragment extends Fragment {
    FragmentNotificationListBinding binding;
    NotificationListViewModel vm;

    public static void addFragment(BaseActivity activity) {
        activity.replaceFragment(new NotificationListFragment(), true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new NotificationListViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         binding= DataBindingUtil.inflate(inflater,R.layout.fragment_notification_list, container, false);
        ((BaseActivity)getActivity()).setToolbarVisibility(true);
         binding.setVm(vm);
        return binding.getRoot();
    }
}