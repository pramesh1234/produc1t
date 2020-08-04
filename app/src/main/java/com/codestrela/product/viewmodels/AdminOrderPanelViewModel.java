package com.codestrela.product.viewmodels;

import android.os.Bundle;
import android.widget.Toast;

import com.codestrela.product.fragments.AdminOrderPanelFragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminOrderPanelViewModel {
    AdminOrderPanelFragment adminOrderPanelFragment;
    FirebaseFirestore db;
    public AdminOrderPanelViewModel(AdminOrderPanelFragment adminOrderPanelFragment) {
        this.adminOrderPanelFragment=adminOrderPanelFragment;
        db=FirebaseFirestore.getInstance();
         }
}
