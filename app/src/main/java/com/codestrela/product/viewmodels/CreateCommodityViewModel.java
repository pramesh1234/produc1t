package com.codestrela.product.viewmodels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.fragments.CreateCommodityFragment;
import com.codestrela.product.fragments.GroupListDialogFragment;
import com.codestrela.product.fragments.HomeFragment;
import com.codestrela.product.util.BindableString;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class CreateCommodityViewModel {
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    public BindableString name = new BindableString();
    public BindableString unit = new BindableString();
    public BindableString price = new BindableString();
    public BindableString spection = new BindableString();
    public BindableString mode = new BindableString();
    public BindableString type = new BindableString();
    public FirebaseFirestore db;
    public FirebaseAuth mAuth;
    FragmentManager fm;
    Map<String, Object> commodity;
    GroupListDialogFragment fragment;
    CreateCommodityFragment createCommodityFragment;

    public CreateCommodityViewModel(CreateCommodityFragment createCommodityFragment) {
        this.createCommodityFragment = createCommodityFragment;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        fragment = new GroupListDialogFragment();
        fm = createCommodityFragment.getActivity().getSupportFragmentManager();
    }

    public CreateCommodityViewModel() {
    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    public void onSubmit(View view) {
        if (createCommodityFragment.req == 1) {


            final ProgressDialog dialog = ProgressDialog.show(createCommodityFragment.getActivity(), "",
                    "Please wait...", true);
            dialog.show();
            commodity = new HashMap<>();
            commodity.put("name", name.get());
            commodity.put("unit", unit.get());
            commodity.put("price", price.get());
            commodity.put("spection", spection.get());
            commodity.put("mode", mode.get());
            commodity.put("type", type.get());
            commodity.put("image", createCommodityFragment.imageUrl);
            commodity.put("created_by_doc_id", loadData(createCommodityFragment.getContext()));
            commodity.put("created_date", FieldValue.serverTimestamp());
            if (type.get().equals("group")) {
                Bundle args = new Bundle();
                args.putSerializable("getdata", (Serializable) commodity);
                fragment.setArguments(args);
                fragment.show(fm, "fma");
            } else {
                db.collection("db_v1").document("barter_doc").collection("commodity_list").document().set(commodity).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();
                                Toast.makeText(createCommodityFragment.getActivity(), "Commodity is Created", Toast.LENGTH_SHORT).show();
                                HomeFragment.addFragment((BaseActivity) createCommodityFragment.getActivity());
                            }
                        }
                );
            }
            dialog.dismiss();
        } else {
            Toast.makeText(createCommodityFragment.getActivity(), "please upload image", Toast.LENGTH_SHORT).show();
        }
    }

}
