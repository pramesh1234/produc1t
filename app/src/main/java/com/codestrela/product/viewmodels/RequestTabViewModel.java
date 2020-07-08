package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.codestrela.product.adapters.RequestAdapter;
import com.codestrela.product.fragments.RequestTabFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class RequestTabViewModel {
    private static final String TAG = "RequestTabViewModel";
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    public RequestAdapter adapter;
    String requesterName,commodityName,quantity;
    ArrayList<RowRequestViewModel> arrayList;
    FirebaseFirestore db;
    RequestTabFragment requestTabFragment;

    public RequestTabViewModel(RequestTabFragment requestTabFragment) {
        this.requestTabFragment=requestTabFragment;
        db=FirebaseFirestore.getInstance();
        arrayList=new ArrayList<>();
        adapter=new RequestAdapter(new ArrayList<RowRequestViewModel>());
        displayRequest();
    }
    public void displayRequest(){
        Log.e(TAG, "onComplete: wpwpempty "+loadData(requestTabFragment.getActivity()));

        db.collection("db_v1").document("barter_doc").collection("requests").whereEqualTo("requested_to",loadData(requestTabFragment.getActivity())).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty()){
                            Log.e(TAG, "onComplete: empty "+loadData(requestTabFragment.getActivity()));
                        }
                     if(task.isSuccessful()){
                         for(QueryDocumentSnapshot doc : task.getResult()){
                             RowRequestViewModel viewModel=new RowRequestViewModel();
                              commodityName=doc.getString("commodity_name");
                              quantity=doc.getString("quantity");
                             Log.e(TAG, "onComplete: pty "+commodityName);
                             viewModel.commodityName.set(commodityName);
                              viewModel.quantity.set(quantity);
                              arrayList.add(viewModel);

                             Log.e(TAG, "onComplete: "+commodityName);
                         }
                     }
                     adapter.addAll(arrayList);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }
}
