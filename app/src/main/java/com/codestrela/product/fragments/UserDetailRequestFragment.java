package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codestrela.product.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserDetailRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserDetailRequestFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_detail_request, container, false);
    }
}