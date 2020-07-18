package com.codestrela.product.adapters;

import com.codestrela.product.R;
import com.codestrela.product.base.recycler.RecyclerBaseAdapter;
import com.codestrela.product.viewmodels.RowContactViewModel;
import com.codestrela.product.viewmodels.RowSelectContactViewModel;
import com.codestrela.product.viewmodels.RowTransactionViewModel;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerBaseAdapter {
    ArrayList<RowTransactionViewModel> arrayList;

    public TransactionAdapter(ArrayList<RowTransactionViewModel> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    protected Object getObjForPosition(int position) {
        return arrayList.get(position);
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_transaction;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public void addAll(ArrayList<RowTransactionViewModel> viewmodel) {
        this.arrayList.addAll(viewmodel);
        notifyDataSetChanged();
    }
    public void add(RowTransactionViewModel rowTransactionViewModel) {
        this.arrayList.add(rowTransactionViewModel);
        notifyDataSetChanged();
    }
}
