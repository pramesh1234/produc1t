package com.codestrela.product.adapters;

import com.codestrela.product.R;
import com.codestrela.product.base.recycler.RecyclerBaseAdapter;
import com.codestrela.product.viewmodels.RowGroupDetaill;

import java.util.ArrayList;

public class GroupDetailAdapter extends RecyclerBaseAdapter {
    ArrayList<RowGroupDetaill> list;

    public GroupDetailAdapter(ArrayList<RowGroupDetaill> list) {
        this.list = list;
    }

    @Override
    protected Object getObjForPosition(int position) {
        return list.get(position);
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_group_detail;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(RowGroupDetaill viewmodel) {
        this.list.add(viewmodel);
        notifyDataSetChanged();
    }
}
