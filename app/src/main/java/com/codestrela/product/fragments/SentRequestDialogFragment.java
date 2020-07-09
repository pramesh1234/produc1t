package com.codestrela.product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentSentRequestDialogBinding;
import com.codestrela.product.viewmodels.SentRequestDialogViewModel;

import java.util.ArrayList;

public class SentRequestDialogFragment extends Fragment {
    FragmentSentRequestDialogBinding binding;
    SentRequestDialogViewModel vm;
    ArrayAdapter<String> adapter;
    ArrayList<String> modeList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm=new SentRequestDialogViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_sent_request_dialog, container, false);
        binding.setVm(vm);
        RadioGroup typeRadioGroup = (RadioGroup) binding.getRoot().findViewById(R.id.radioGroupRequest);
        typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.requestRentId:
                        vm.mode.set("Rent");
                        break;
                    case R.id.buyId:
                        vm.mode.set("Buy");
                        break;
                }
            }
        });
        modeList=new ArrayList<String>();
       return binding.getRoot();

    }
    public static void addFragment(BaseActivity activity,Fragment fragment) {
        activity.replaceFragment(fragment, true);
    }

}