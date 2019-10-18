package com.zhongmei.bty.dinner.table.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.dinner.vo.DinnerConnectTablesVo;

import java.util.List;



public class BatchOperationTableDialogFragment extends BasicDialogFragment
        implements View.OnClickListener {

    private ImageView mClose;

    private BusinessType mBusinessType;


    DinnerAllTableListFragment listFragment;

    DinnerTablesOperationFragment operationFragment;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_LAYOUT_FLAGS | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        Window window = dialog.getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        return dialog;
    }


    public BatchOperationTableDialogFragment setmBusinessType(BusinessType mBusinessType) {
        this.mBusinessType = mBusinessType;
        return this;
    }

    public BusinessType getmBusinessType() {
        return mBusinessType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.batch_operation_table_dialog_fragment, container, false);
        initView(view);
        return view;
    }


    private void initView(View v) {
        listFragment = new DinnerAllTableListFragment_();
        replaceChildFragment(R.id.tables_list_fragment, listFragment, DinnerAllTableListFragment.TAG);
        operationFragment = new DinnerTablesOperationFragment_();
        replaceChildFragment(R.id.tables_operation_fragment, operationFragment, DinnerTablesOperationFragment.TAG);
        listFragment.setOnItemSelectedListener(new DinnerAllTableListFragment.OnItemSelectedListener() {
            @Override
            public void onSelected(DinnerConnectTablesVo vo) {
                operationFragment.refreshView(vo);
            }

            @Override
            public void onSelectedList(List<DinnerConnectTablesVo> tablesVoList) {
                operationFragment.refreshView(tablesVoList);
            }
        });
        operationFragment.setFreshViewListener(new DinnerTablesOperationFragment.FreshViewListener() {
            public void freshView(List<DinnerConnectTablesVo> vos) {
                if (Utils.isNotEmpty(vos)) {
                    listFragment.removeSelectedTable(vos.get(0));
                }
            }

            public void closeView() {
                dismiss();
            }

            @Override
            public void refreshView() {
                listFragment.refreshView();
            }
        });
        mClose = (ImageView) v.findViewById(R.id.btn_close);
        mClose.setOnClickListener(this);

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (manager != null && !manager.isDestroyed()) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                dismiss();
                break;
            default:
                break;
        }
    }
}
