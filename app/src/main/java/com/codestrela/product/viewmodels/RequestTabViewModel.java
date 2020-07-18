package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.codestrela.product.adapters.HomeAdapter;
import com.codestrela.product.adapters.RequestAdapter;
import com.codestrela.product.adapters.RequestViewPagerAdapter;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.fragments.RequestTabFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class RequestTabViewModel {
    private static final String TAG = "RequestTabViewModel";
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    public RequestAdapter adapter;
    public RequestViewPagerAdapter mViewPagerAdapter;
    FirebaseFirestore db;
    String requesterName,commodityName,quantity;
    ArrayList<RowRequestViewModel> arrayList;
    RequestTabFragment requestTabFragment;

    public RequestTabViewModel(RequestTabFragment requestTabFragment) {
        this.requestTabFragment=requestTabFragment;
        mViewPagerAdapter = new RequestViewPagerAdapter(requestTabFragment.getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }
}
