package com.codestrela.product.adapters;

import android.inputmethodservice.Keyboard;

import com.codestrela.product.R;
import com.codestrela.product.base.recycler.RecyclerBaseAdapter;
import com.codestrela.product.viewmodels.RowAdminOrder;
import com.codestrela.product.viewmodels.RowRequestViewModel;

import java.util.ArrayList;

public class AdminOrderAdapter extends RecyclerBaseAdapter {
    ArrayList<RowAdminOrder> arrayList;

    public AdminOrderAdapter(ArrayList<RowAdminOrder> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    protected Object getObjForPosition(int position) {
        return arrayList.get(position);
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_admin_order;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public void addAll(ArrayList<RowAdminOrder> viewModels) {
        this.arrayList.addAll(viewModels);
        notifyDataSetChanged();
    }
    public void add(RowAdminOrder rowAdminOrder) {
        this.arrayList.add(rowAdminOrder);
        notifyDataSetChanged();
    }
}
