package com.codestrela.product.viewmodels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.fragments.GmailRegisterTwoFragment;
import com.codestrela.product.fragments.HomeFragment;
import com.codestrela.product.fragments.PhoneRegisterFragment;
import com.codestrela.product.util.AppUtil;
import com.codestrela.product.util.BindableBoolean;
import com.codestrela.product.util.BindableString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class PhoneRegisterViewModel {
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    public BindableString name = new BindableString();
    public BindableString email = new BindableString();
    FirebaseFirestore db;
    public BindableBoolean loading=new BindableBoolean();
    private static final String TAG = "PhoneRegisterViewModel";
    public BindableString smsCode = new BindableString();
    ProgressDialog progressDialog;
    String documentId;
    private boolean mVerificationInProgress = false;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    Map<String, Object> userData = new HashMap<>();
    PhoneRegisterFragment phoneRegisterFragment;

    FirebaseAuth mAuth;
    String phoneNumber;
    private String verficationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verficationId = s;
            mResendToken = forceResendingToken;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                smsCode.set(code);
                Toast.makeText(phoneRegisterFragment.getActivity(), "code sent successfully", Toast.LENGTH_SHORT).show();
                loading.set(false);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            loading.set(false);
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(phoneRegisterFragment.getContext(), "The entered number is invalid", Toast.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast.makeText(phoneRegisterFragment.getActivity(), "The SMS quota for the project has been exceeded", Toast.LENGTH_SHORT).show();
            }

        }
    };

    public PhoneRegisterViewModel(PhoneRegisterFragment phoneRegisterFragment, String phoneNumber) {
        this.phoneRegisterFragment = phoneRegisterFragment;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        this.phoneNumber = phoneNumber;
        loading.set(false);
        startPhoneNumberVerification(phoneNumber);
        Log.e(TAG, "PhoneRegisterViewModel: " + phoneNumber);

    }

    public static void saveData(Context context, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY, id);
        editor.apply();
    }

    public void onPhoneRegister(View view) {
        if (email.get().isEmpty() && email.get().equals("")) {
            AppUtil.showToast(phoneRegisterFragment.getActivity(), "please don't leave email field empty");
        } else if (name.get().isEmpty() && name.get().equals("")) {
            AppUtil.showToast(phoneRegisterFragment.getActivity(), "please don't leave name field empty");
        } else if (smsCode.get().isEmpty() && smsCode.get().equals("")) {
            AppUtil.showToast(phoneRegisterFragment.getActivity(), "please don't leave otp field empty");
        } else {
            verifycode(smsCode.get());
        }
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                phoneRegisterFragment.getActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void SignInWithCredentials(PhoneAuthCredential credential) {
        progressDialog = new ProgressDialog(phoneRegisterFragment.getActivity());
        progressDialog.setMessage("Creating profile..");
        progressDialog.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userData.put("name", name.get());
                            userData.put("email", email.get());
                            userData.put("phone_number", phoneNumber);
                            userData.put("photo_url", "");
                            documentId = db.collection("db_v1").document("barter_doc").collection("users").document().getId();
                            db.collection("db_v1").document("barter_doc").collection("users").document(documentId).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    checkUser();
                                    loading.set(false);

                                }
                            });
                        } else {
                            loading.set(false);
                            Toast.makeText(phoneRegisterFragment.getActivity(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void onResendClicked(View view) {
        resendVerificationCode(phoneNumber, mResendToken);
        loading.set(true);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                phoneRegisterFragment.getActivity(),               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void verifycode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verficationId, code);
        SignInWithCredentials(credential);
    }

    public void checkUser() {

        db.collection("db_v1").document("barter_doc").collection("users").document(documentId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Log.e(TAG, "onSuccess");
                            saveData(phoneRegisterFragment.getContext(), documentId);
                            AppUtil.showToast(phoneRegisterFragment.getActivity(), "Profile Created");
                            progressDialog.dismiss();
                            HomeFragment.addFragment((BaseActivity) phoneRegisterFragment.getActivity());
                        } else {
                        }
                    }
                });
    }
}
