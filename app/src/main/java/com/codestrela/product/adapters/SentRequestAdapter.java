package com.codestrela.product.adapters;

import com.codestrela.product.R;
import com.codestrela.product.base.recycler.RecyclerBaseAdapter;
import com.codestrela.product.viewmodels.RowRequestViewModel;

import java.util.ArrayList;

public class SentRequestAdapter extends RecyclerBaseAdapter {
ArrayList<RowRequestViewModel> arrayList;
    public SentRequestAdapter(ArrayList<RowRequestViewModel> arrayList) {
        this.arrayList=arrayList;
    }

    @Override
    protected Object getObjForPosition(int position) {
        return arrayList.get(position);
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_sent_request;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public void addAll(ArrayList<RowRequestViewModel> viewModels) {
        this.arrayList.addAll(viewModels);
        notifyDataSetChanged();
    }
    public void add(RowRequestViewModel rowRequestViewModel) {
        this.arrayList.add(rowRequestViewModel);
        notifyDataSetChanged();
    }
}
