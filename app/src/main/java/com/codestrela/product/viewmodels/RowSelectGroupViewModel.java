package com.codestrela.product.viewmodels;

import android.util.Log;
import android.widget.CompoundButton;

import com.codestrela.product.util.BindableBoolean;
import com.codestrela.product.util.BindableString;

public class RowSelectGroupViewModel {
    private static final String TAG = "RowSelectGroupViewModel";
    public BindableString groupName = new BindableString();
    public BindableBoolean selectGroup = new BindableBoolean();
    public BindableString groupId = new BindableString();
    GroupListDialogViewModel viewModel;
    GroupListMemberDialogViewModel groupViewmodel;
    String group;
    public CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.isChecked()) {
                if (groupViewmodel == null) {
                    group = buttonView.getText().toString();
                    viewModel.list.add(group);
                    Log.e(TAG, "clicked " + viewModel.list.size());
                    buttonView.getText();
                } else {
                    group = buttonView.getText().toString();
                    groupViewmodel.list.add(group);
                    Log.e(TAG, "clicked " + groupViewmodel.list.size());
                    buttonView.getText();
                }
            }

        }
    };

    public RowSelectGroupViewModel(GroupListDialogViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public RowSelectGroupViewModel() {
    }

    public RowSelectGroupViewModel(GroupListMemberDialogViewModel groupViewmodel) {
        this.groupViewmodel = groupViewmodel;
    }

}
