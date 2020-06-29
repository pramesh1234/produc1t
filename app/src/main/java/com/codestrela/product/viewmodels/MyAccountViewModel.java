package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.fragments.CreateCommodityFragment;
import com.codestrela.product.fragments.ListDialogFragment;
import com.codestrela.product.fragments.MyAccountFragment;
import com.codestrela.product.fragments.MyCommoditiesFragment;
import com.codestrela.product.fragments.MyContactListFragment;
import com.codestrela.product.fragments.PhoneSignInFragment;
import com.codestrela.product.util.BindableBoolean;
import com.codestrela.product.util.BindableString;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;

public class MyAccountViewModel {
    FirebaseAuth firebaseAuth;
    public BindableBoolean loading=new BindableBoolean();
    MyAccountFragment myAccountFragment;
    ListDialogFragment tv;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    public BindableString nameStr = new BindableString();
    public BindableString emailStr = new BindableString();
    public BindableString photoUrlStr = new BindableString();
    GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore db;
    FragmentManager fm;

    public MyAccountViewModel(MyAccountFragment myAccountFragment) {
        this.myAccountFragment = myAccountFragment;
        firebaseAuth = FirebaseAuth.getInstance();
        fm = myAccountFragment.getActivity().getSupportFragmentManager();
        tv = new ListDialogFragment();
        db = FirebaseFirestore.getInstance();
        loading.set(true);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(myAccountFragment.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(myAccountFragment.getActivity(), gso);

    }

    public void onCreateCommodity(View view) {
        CreateCommodityFragment.addFragment((BaseActivity) myAccountFragment.getActivity());
    }

    public void onMyCommodity(View view) {
        MyCommoditiesFragment.addFragment((BaseActivity) myAccountFragment.getActivity());
    }

    public void onMyContact(View view) {
        MyContactListFragment.addFragment((BaseActivity) myAccountFragment.getActivity());
    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    public void onCreateGroup(View view) {
        tv.show(fm, "fma");
    }

    public void onSignOut(View view) {
        firebaseAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(myAccountFragment.getActivity(),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
        PhoneSignInFragment.addFragment((BaseActivity) myAccountFragment.getActivity());
    }

    public void getProfileDetail() {
        db.collection("db_v1").document("barter_doc").collection("users").document(loadData(myAccountFragment.getContext())).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                String name = documentSnapshot.getString("name");
                                String email = documentSnapshot.getString("email");
                                String imageUrl = documentSnapshot.getString("photo_url");
                                loading.set(false);
                                nameStr.set(name);
                                emailStr.set(email);
                                if (imageUrl.equals("")) {
                                    photoUrlStr.set("https://firebasestorage.googleapis.com/v0/b/project-d7d51.appspot.com/o/profile_images%2Fdefault-profile.png?alt=media&token=f19f9724-71dc-48ac-9f1a-ecf64c703d2b");
                                } else {
                                    photoUrlStr.set(imageUrl);
                                }
                            }
                        }

                    }
                });
    }
}
