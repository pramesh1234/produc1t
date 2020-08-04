package com.codestrela.product.adapters;

import com.codestrela.product.R;
import com.codestrela.product.base.recycler.RecyclerBaseAdapter;
import com.codestrela.product.viewmodels.RowOrderListViewModel;
import com.codestrela.product.viewmodels.RowRequestViewModel;

import java.util.ArrayList;

public class MyOrderAdapter extends RecyclerBaseAdapter {
    ArrayList<RowOrderListViewModel> arrayList;

    public MyOrderAdapter(ArrayList<RowOrderListViewModel> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    protected Object getObjForPosition(int position) {
        return arrayList.get(position);
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_order_list;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public void addAll(ArrayList<RowOrderListViewModel> viewModels) {
        this.arrayList.addAll(viewModels);
        notifyDataSetChanged();
    }
    public void add(RowOrderListViewModel rowOrderListViewModel) {
        this.arrayList.add(rowOrderListViewModel);
        notifyDataSetChanged();
    }
}
