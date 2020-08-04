package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.codestrela.product.adapters.SentRequestAdapter;
import com.codestrela.product.fragments.SentRequestFragment;
import com.codestrela.product.util.BindableBoolean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SentRequestViewModel {

    private static final String TAG = "RequestTabViewModel";
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    public SentRequestAdapter adapter;
    public BindableBoolean loading = new BindableBoolean();
    public BindableBoolean emptyList = new BindableBoolean();
    FirebaseFirestore db;
    ArrayList<RowRequestViewModel> arrayList;
    SentRequestFragment sentRequestFragment;

    public SentRequestViewModel(SentRequestFragment sentRequestFragment) {
        this.sentRequestFragment = sentRequestFragment;
        db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        loading.set(true);
        emptyList.set(false);
        adapter = new SentRequestAdapter(new ArrayList<RowRequestViewModel>());
        displayRequest();
    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    public void displayRequest() {
        Log.e(TAG, "onComplete: wpwpempty " + loadData(sentRequestFragment.getActivity()));

        db.collection("db_v1").document("barter_doc").collection("requests").whereEqualTo("requested_to", loadData(sentRequestFragment.getActivity())).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()) {
                            emptyList.set(true);
                            loading.set(false);
                            Log.e(TAG, "onComplete: empty " + loadData(sentRequestFragment.getActivity()));
                        }
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                final String requestedBy = doc.getString("requested_by");
                                final String requestedTo = doc.getString("requested_to");
                                final String commodityName = doc.getString("commodity_name");
                                final String quantity = doc.getString("quantity");
                                final String requestRef = doc.getId();
                                final String mode = doc.getString("mode");
                                final String requestId = doc.getId();
                                final String specification = doc.getString("specification");
                                db.collection("db_v1").document("barter_doc").collection("users").document(requestedTo).get().addOnCompleteListener(
                                        new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot documentSnapshot = task.getResult();
                                                    String user = documentSnapshot.getString("name");
                                                    Log.e(TAG, "onComplete: yy " + user);
                                                    loading.set(false);
                                                    RowRequestViewModel viewModel = new RowRequestViewModel(sentRequestFragment, commodityName, quantity, mode, specification, requestId, requestedTo, requestedBy, requestRef);
                                                    viewModel.commodityName.set(commodityName);
                                                    viewModel.quantity.set(quantity);
                                                    viewModel.requester.set(user);
                                                    adapter.add(viewModel);
                                                }
                                            }
                                        }
                                );

                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
