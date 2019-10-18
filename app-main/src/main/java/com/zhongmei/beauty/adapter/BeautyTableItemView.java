package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;


@EViewGroup(R.layout.beauty_tablechoice_item_view)
public class BeautyTableItemView extends LinearLayout {

    @ViewById(R.id.cb_table)
    protected CheckBox cb_table;

    private DinnertableModel mTableModel;

    public BeautyTableItemView(Context context) {
        super(context);
    }

    public BeautyTableItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BeautyTableItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BeautyTableItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void refreshUI(DinnertableModel tableModel, boolean isCheck, OnClickListener listener) {
        this.mTableModel = tableModel;
        if (tableModel.getPhysicsTableStatus().equalsValue(TableStatus.EMPTY.value())) {
            cb_table.setEnabled(true);
        } else {
            cb_table.setEnabled(false);
        }

        cb_table.setChecked(isCheck);

        cb_table.setText(tableModel.getName());

        cb_table.setTag(tableModel);

        cb_table.setOnClickListener(listener);
    }
}
