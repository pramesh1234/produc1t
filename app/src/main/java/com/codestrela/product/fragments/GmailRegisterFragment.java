package com.codestrela.product.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentGmailRegisterBinding;
import com.codestrela.product.util.AppUtil;
import com.codestrela.product.viewmodels.GmailRegisterViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GmailRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GmailRegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    GmailRegisterViewModel vm;
    FragmentGmailRegisterBinding binding;
    TextInputLayout etPhone;
    String phoneNo;

    public static void addFragment(BaseActivity activity) {
        activity.replaceFragment(new GmailRegisterFragment(), true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isConnected(Objects.requireNonNull(getActivity()))){
            buildDialog(getContext()).show(); }
        vm = new GmailRegisterViewModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gmail_register, container, false);
        ((BaseActivity) getActivity()).setToolbarVisibility(true);
        binding.setVm(vm);
        etPhone = (TextInputLayout) binding.getRoot().findViewById(R.id.etPhone);
        phoneNo = etPhone.getEditText().toString();
        Button submitBtn = (Button) binding.getRoot().findViewById(R.id.subBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("phoneNo", phoneNo);
                GmailRegisterFragment gmailRegisterFragment = new GmailRegisterFragment();
                gmailRegisterFragment.setArguments(bundle);
                AppUtil.showToast(getActivity(), "hhh");
                GmailRegisterTwoFragment.addFragment((BaseActivity) getActivity(), gmailRegisterFragment);

            }
        });
        return binding.getRoot();

    }
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null&&netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                getActivity().finish();
            }
        });

        return builder;
    }
}
