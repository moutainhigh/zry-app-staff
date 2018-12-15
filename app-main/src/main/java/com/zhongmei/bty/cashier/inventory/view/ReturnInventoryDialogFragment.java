package com.zhongmei.bty.cashier.inventory.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryItem;
import com.zhongmei.bty.cashier.inventory.adapter.InventoryDishItemAdapter;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class ReturnInventoryDialogFragment extends BasicDialogFragment implements View.OnClickListener {

    protected ListView dishListView;

    protected Button submitOk;

    protected ImageButton ibClose;

    protected TextView tvTitle;

    private List<InventoryItem> inventoryItemList;


    private InventoryDishItemAdapter adapter;

    private ReturnDishDataListener returnDishDataListener;

    private ReturnCloseListener returnCloseListener;

    private int titleId;

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

    public void setInventoryItemList(List<InventoryItem> inventoryItemList) {
        this.inventoryItemList = inventoryItemList;
    }

    public void setReturnDishDataListener(ReturnDishDataListener returnDishDataListener) {
        this.returnDishDataListener = returnDishDataListener;
    }

    public void setReturnCloseListener(ReturnCloseListener returnCloseListener) {
        this.returnCloseListener = returnCloseListener;
    }

    public void setTitle(int titleId) {
        this.titleId = titleId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.return_inventory_list_dialog, container, false);
        dishListView = (ListView) view.findViewById(R.id.dish_list_view);
        submitOk = (Button) view.findViewById(R.id.btn_return_inventory);
        ibClose = (ImageButton) view.findViewById(R.id.ib_close);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        if (titleId != 0) {
            tvTitle.setText(titleId);
        }
        submitOk.setOnClickListener(this);
        ibClose.setOnClickListener(this);
        adapter = new InventoryDishItemAdapter(getActivity(), inventoryItemList);
        dishListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_return_inventory:
                if (returnDishDataListener != null) {
                    for (InventoryItem item : inventoryItemList) {
                        if (item.getTempReturnInventoryNum() != null) {
                            item.setReturnInventoryNum(item.getTempReturnInventoryNum());
                            item.setTempReturnInventoryNum(null);
                        }
                    }
                    returnDishDataListener.setDishData(inventoryItemList);
                }
                dismiss();
                break;
            case R.id.ib_close:
                if (returnDishDataListener != null) {
                    for (InventoryItem item : inventoryItemList) {
                        if (item.getTempReturnInventoryNum() != null) {
                            item.setTempReturnInventoryNum(null);
                        }
                    }
                }

                if (returnCloseListener != null) {
                    returnCloseListener.onClose(inventoryItemList);
                }
                dismiss();
                break;
        }
    }

    public interface ReturnDishDataListener {
        void setDishData(List<InventoryItem> inventoryItemList);
    }

    public interface ReturnCloseListener {
        void onClose(List<InventoryItem> inventoryItemList);
    }

}
