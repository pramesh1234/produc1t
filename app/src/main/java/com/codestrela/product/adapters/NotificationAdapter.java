package com.codestrela.product.adapters;

import com.codestrela.product.R;
import com.codestrela.product.base.recycler.RecyclerBaseAdapter;
import com.codestrela.product.viewmodels.RowNotificationListViewModel;
import com.codestrela.product.viewmodels.RowOrderListViewModel;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerBaseAdapter{
    ArrayList<RowNotificationListViewModel> arrayList;

    public NotificationAdapter(ArrayList<RowNotificationListViewModel> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    protected Object getObjForPosition(int position) {
        return arrayList.get(position);
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_notification_list;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public void addAll(ArrayList<RowNotificationListViewModel> viewModels) {
        this.arrayList.addAll(viewModels);
        notifyDataSetChanged();
    }
    public void add(RowNotificationListViewModel rowNotificationListViewModel) {
        this.arrayList.add(rowNotificationListViewModel);
        notifyDataSetChanged();
    }
}
