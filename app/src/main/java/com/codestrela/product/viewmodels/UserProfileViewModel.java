package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.codestrela.product.fragments.UserProfileFragment;
import com.codestrela.product.util.BindableString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;

public class UserProfileViewModel {
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    public BindableString profileImage=new BindableString();
    public BindableString profileName=new BindableString();
    public BindableString profileContact=new BindableString();
    public BindableString profileEmail=new BindableString();
    public BindableString profileLocality=new BindableString();
    FirebaseFirestore db;
    UserProfileFragment userProfileFragment;
    public UserProfileViewModel(UserProfileFragment userProfileFragment) {
        this.userProfileFragment=userProfileFragment;
        db=FirebaseFirestore.getInstance();
         getProfileDetail();
    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    public void getProfileDetail() {
        db.collection("db_v1").document("barter_doc").collection("users").document(loadData(userProfileFragment.getContext())).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                String name = documentSnapshot.getString("name");
                                String email = documentSnapshot.getString("email");
                                String imageUrl = documentSnapshot.getString("photo_url");
                                profileName.set(name);
                                profileEmail.set(email);
                                assert imageUrl != null;
                                if (imageUrl.equals("")) {
                                    profileImage.set("https://firebasestorage.googleapis.com/v0/b/project-d7d51.appspot.com/o/profile_images%2FUntitle.png?alt=media&token=e600e509-1c1f-42a1-a7c1-be5cce7dcd0e");
                                } else {
                                    profileImage.set(imageUrl);
                                }
                            }
                        }

                    }
                });
    }
}
