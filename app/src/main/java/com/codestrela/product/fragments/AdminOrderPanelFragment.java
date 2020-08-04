package com.codestrela.product.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentAdminOrderListBinding;
import com.codestrela.product.databinding.FragmentAdminOrderPanelBinding;
import com.codestrela.product.viewmodels.AdminOrderPanelViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AdminOrderPanelFragment extends Fragment {
    AdminOrderPanelViewModel vm;
FragmentAdminOrderPanelBinding binding;
FirebaseFirestore db;
    String documentId,requestedBy,referenceId;

    public static void addFragment(BaseActivity activity,Fragment fragment) {
        activity.replaceFragment(fragment, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new AdminOrderPanelViewModel(this);
        db=FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_admin_order_panel, container, false);
        binding.setVm(vm);
        Bundle bundle=getArguments();
        documentId=bundle.getString("documentId");
        requestedBy=bundle.getString("requestedBy");
        referenceId=bundle.getString("referenceId");
        ((BaseActivity)getActivity()).setToolbarVisibility(true);
        RadioGroup typeRadioGroup = (RadioGroup) binding.getRoot().findViewById(R.id.rgStatus);
        typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbProcessing:
                 db.collection("db_v1").document("barter_doc").collection("accepted_request").document(documentId).update("order_status","Processing")
                         .addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                                 Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                                 HashMap<String,Object> notification=new HashMap<>();
                                 notification.put("title","Your order is in process");
                                 notification.put("order_doc_reference",documentId);
                                 notification.put("receiver",requestedBy);
                                 notification.put("order_no",referenceId);
                                 db.collection("db_v1").document("barter_doc").collection("notifications").document().set(notification)
                                         .addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                             public void onSuccess(Void aVoid) {
                                                 BottomNavigationFragment.addFragment((BaseActivity) getActivity());
                                             }
                                         })
                                         .addOnFailureListener(new OnFailureListener() {
                                             @Override
                                             public void onFailure(@NonNull Exception e) {
                                                 Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                                             }
                                         });
                             }
                         })
                         .addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Toast.makeText(getContext(), "non success", Toast.LENGTH_SHORT).show();
                             }
                         });
                        break;
                    case R.id.rbShipped:
                        db.collection("db_v1").document("barter_doc").collection("accepted_request").document(documentId).update("order_status","Ordered shipped")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        HashMap<String,Object> notification=new HashMap<>();
                                        notification.put("title","Your order is shipped");
                                        notification.put("order_doc_reference",documentId);
                                        notification.put("receiver",requestedBy);
                                        notification.put("order_no",referenceId);
                                        db.collection("db_v1").document("barter_doc").collection("notifications").document().set(notification)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        BottomNavigationFragment.addFragment((BaseActivity) getActivity());
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "non success", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        break;
                    case R.id.rbWay:
                        db.collection("db_v1").document("barter_doc").collection("accepted_request").document(documentId).update("order_status","Order on the way")
.addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            HashMap<String,Object> notification=new HashMap<>();
                            notification.put("title","Your order is on the way");
                            notification.put("order_doc_reference",documentId);
                            notification.put("receiver",requestedBy);
                            notification.put("order_no",referenceId);
                            db.collection("db_v1").document("barter_doc").collection("notifications").document().set(notification)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            BottomNavigationFragment.addFragment((BaseActivity) getActivity());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                                        }
                                    });                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "non success", Toast.LENGTH_SHORT).show();
                                }
                            });
                        break;
                    case R.id.rbDelivered:
                        db.collection("db_v1").document("barter_doc").collection("accepted_request").document(documentId).update("order_status","Order delivered")
.addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            HashMap<String,Object> notification=new HashMap<>();
                            notification.put("title","Your order is delievered");
                            notification.put("order_doc_reference",documentId);
                            notification.put("receiver",requestedBy);
                            notification.put("order_no",referenceId);
                            db.collection("db_v1").document("barter_doc").collection("notifications").document().set(notification)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            BottomNavigationFragment.addFragment((BaseActivity) getActivity());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                                        }
                                    });                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "non success", Toast.LENGTH_SHORT).show();
                                }
                            });
                        break;
                }
            }
        });
        return binding.getRoot();
    }
}