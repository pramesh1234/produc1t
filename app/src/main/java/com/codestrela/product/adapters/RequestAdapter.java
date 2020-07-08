package com.codestrela.product.adapters;

import com.codestrela.product.R;
import com.codestrela.product.base.recycler.RecyclerBaseAdapter;
import com.codestrela.product.viewmodels.RowContactViewModel;
import com.codestrela.product.viewmodels.RowGroupListViewModel;
import com.codestrela.product.viewmodels.RowRequestViewModel;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerBaseAdapter {
    ArrayList<RowRequestViewModel> arrayList;
    public RequestAdapter(ArrayList<RowRequestViewModel> arrayList) {
        this.arrayList=arrayList;
    }

    @Override
    protected Object getObjForPosition(int position) {
        return arrayList.get(position);
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_request;
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
