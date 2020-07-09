package com.codestrela.product.viewmodels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codestrela.product.MainActivity;
import com.codestrela.product.adapters.ContactAdapter;
import com.codestrela.product.adapters.HomeAdapter;
import com.codestrela.product.R;
import com.codestrela.product.adapters.MyCommoditiesAdapter;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.data.Contact;
import com.codestrela.product.fragments.HomeFragment;
import com.codestrela.product.fragments.MyAccountFragment;
import com.codestrela.product.fragments.PhoneSignInFragment;
import com.codestrela.product.fragments.PublicFragment;
import com.codestrela.product.util.BindableString;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class HomeViewModel {
    public static final String CONTACT_LIST = "contact_list";
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    private static final String TAG = "HomeViewModel";
    public BindableString cusName = new BindableString();
    public BindableString cusNumber = new BindableString();
    public BindableString cusEmail = new BindableString();
    public HomeAdapter mViewPagerAdapter;
    FirebaseAuth mAuth;
    HomeFragment homeFragment;
    FirebaseFirestore db;
    DocumentReference documentReference;
    GoogleSignInClient mGoogleSignInClient;
    ProgressDialog dialog;
    ArrayList<Contact> contacts;

    public HomeViewModel(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String userId = mAuth.getUid();
        documentReference = db.collection("users").document(userId);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(homeFragment.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(homeFragment.getActivity(), gso);
        showData();
        mViewPagerAdapter = new HomeAdapter(homeFragment.getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //     Toast.makeText(homeFragment.getActivity(), "g "+loadData(homeFragment.getContext()), Toast.LENGTH_SHORT).show();

    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    public void onSignOut(View view) {
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(homeFragment.getActivity(),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
        PhoneSignInFragment.addFragment((BaseActivity) homeFragment.getActivity());
    }

    public void showData() {

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            String number = documentSnapshot.getString("phone_number");
                            cusName.set(name);
                            cusEmail.set(email);
                            cusNumber.set(number);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void onAccountClicked(View view) {
        MyAccountFragment.addFragment((BaseActivity) homeFragment.getActivity());
    }

    public void onPublicClicked(View view) {
        PublicFragment.addFragment((BaseActivity) homeFragment.getActivity());
    }

    public void onBottomSheet(View view) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(homeFragment.getActivity());
        bottomSheetDialog.setContentView(R.layout.dialog_fragment);

        final MyCommoditiesAdapter myCommoditiesAdapter = new MyCommoditiesAdapter(new ArrayList<RowCommodityViewModel>());
        RecyclerView recyclerView = (RecyclerView) bottomSheetDialog.findViewById(R.id.recyclerView);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        final ArrayList<RowCommodityViewModel> viewModels = new ArrayList<>();
        firebaseFirestore.collection("Commodities").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String name = doc.getDocument().getString("name");
                        String price = doc.getDocument().getString("price");
                        RowCommodityViewModel viewModel = new RowCommodityViewModel();
                        viewModel.name.set(name);
                        viewModel.price.set(price);
                        Log.e(TAG, "bottomsheet: " + name);
                        viewModels.add(viewModel);
                    }
                }
                myCommoditiesAdapter.addAll(viewModels);
            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(homeFragment.getActivity()));
        recyclerView.setAdapter(myCommoditiesAdapter);

        bottomSheetDialog.show();
    }
    public void delay(int seconds) {
        final int milliseconds = seconds * 1000;
        homeFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //add your code here
dialog.dismiss();
                    }
                }, milliseconds);
            }
        });

    }
}
