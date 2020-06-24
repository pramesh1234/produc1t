package com.codestrela.product.adapters;

import com.codestrela.product.R;
import com.codestrela.product.base.recycler.RecyclerBaseAdapter;
import com.codestrela.product.viewmodels.RowSelectGroupViewModel;

import java.util.ArrayList;

public class GroupSelectAdapter extends RecyclerBaseAdapter {
    ArrayList<RowSelectGroupViewModel> groupList;

    public GroupSelectAdapter(ArrayList<RowSelectGroupViewModel> groupList) {
        this.groupList = groupList;
    }

    @Override
    protected Object getObjForPosition(int position) {
        return groupList.get(position);
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_group_select;
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public void addAll(ArrayList<RowSelectGroupViewModel> viewModels) {
        this.groupList.addAll(viewModels);
        notifyDataSetChanged();
    }
}
