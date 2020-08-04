package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentBottomNavigationBinding;
import com.codestrela.product.viewmodels.BottomNavigationViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class BottomNavigationFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {
FragmentBottomNavigationBinding binding;
BottomNavigationViewModel vm;

    public static void addFragment(BaseActivity activity) {
        activity.replaceFragment(new BottomNavigationFragment(), false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm=new BottomNavigationViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loadFragment(new HomeFragment());

        //getting bottom navigation view and attaching the listener
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_bottom_navigation, container, false);
        binding.setVm(vm);
        BottomNavigationView navigation = binding.getRoot().findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        return binding.getRoot();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_profile:
                fragment = new MyAccountFragment();
                break;
            case R.id.navigation_dashboard:
                fragment = new RequestTabFragment();
                break;
            case R.id.navigation_notifications:
                fragment = new AdminOrderListFragment();
                break;


        }

        return loadFragment(fragment);

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}