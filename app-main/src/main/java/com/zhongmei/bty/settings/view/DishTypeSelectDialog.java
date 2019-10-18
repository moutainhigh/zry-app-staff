package com.zhongmei.bty.settings.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.DishBrandTypes;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.ui.base.BaseDialog;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.settings.adapter.DishTypeAdapter;
import com.zhongmei.bty.snack.orderdish.view.OnCloseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class DishTypeSelectDialog extends BaseDialog implements View.OnClickListener, AdapterView.OnItemClickListener {


    private DishTypeAdapter mDishTypeAdapter;
    private List<DishBrandType> mDishTypeList = new ArrayList<DishBrandType>();
    private HashMap<String, Integer> mDishTypeListBackup = new HashMap();

    private OnCloseListener mListener = null;
    private boolean isSelectedAll = false;

    private LinearLayout mGoodsTitle;
    private ImageView mSetClose;
    private TextView mSetTitle;
    private Button mBtnSave;
    private TextView mIsSelectAll;
    private LineGridView mGoodsView;

    private void assignViews() {
        mGoodsTitle = (LinearLayout) findViewById(R.id.goods_title);
        mSetClose = (ImageView) findViewById(R.id.set_close);
        mSetTitle = (TextView) findViewById(R.id.set_title);
        mBtnSave = (Button) findViewById(R.id.btn_save);
        mIsSelectAll = (TextView) findViewById(R.id.is_select_all);
        mGoodsView = (LineGridView) findViewById(R.id.goods_view);
    }


    private static DishTypeSelectDialog mInstance;
    public Activity activity;

    public DishTypeSelectDialog(Context context, DishBrandTypes dishBrandTypes, OnCloseListener listener) {
        super(context, R.style.Transparent);
        activity = (Activity) context;
        mListener = listener;
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        DisplayMetrics metric = new DisplayMetrics();
        dialogWindow.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;         int height = metric.heightPixels;

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = width - DensityUtil.dip2px(MainApplication.getInstance(), 260);
        lp.height = height - DensityUtil.dip2px(MainApplication.getInstance(), 15);
        dialogWindow.setAttributes(lp);
        setContentView(R.layout.settings_dinner_dishtype_select);
        assignViews();
        mSetClose.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        mIsSelectAll.setOnClickListener(this);
        mDishTypeList.addAll(dishBrandTypes.dishTypeList);
        mDishTypeListBackup.clear();
        for (DishBrandType type : mDishTypeList) {
            mDishTypeListBackup.put(type.getUuid(), type.isShow());
        }
        mDishTypeAdapter = new DishTypeAdapter(context, mDishTypeList);
        mGoodsView.setAdapter(mDishTypeAdapter);
        mGoodsView.setOnItemClickListener(this);
        setAllBtnText();
    }

    public static void show(Context context, DishBrandTypes dishBrandTypes, OnCloseListener listener) {
        Activity activity = (Activity) context;
        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
            return;
        }
        close();
        mInstance = new DishTypeSelectDialog(context, dishBrandTypes, listener);
        mInstance.setCanceledOnTouchOutside(false);
        mInstance.setCancelable(false);
        mInstance.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                mListener.onClose(true, null);
                dismiss();
                break;
            case R.id.set_close:
                for (DishBrandType type : mDishTypeList) {
                    type.setShow(mDishTypeListBackup.get(type.getUuid()));
                }
                mListener.onClose(false, null);
                dismiss();
                break;
            case R.id.is_select_all:
                allSelectSwitch();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mDishTypeList.get(position).isShow() == Bool.YES.value())
            mDishTypeList.get(position).setShow(Bool.NO.value());
        else
            mDishTypeList.get(position).setShow(Bool.YES.value());
        mDishTypeAdapter.notifyDataSetChanged();
        setAllBtnText();
    }

    private void setAllBtnText() {
        isSelectedAll = true;
        for (DishBrandType type : mDishTypeList) {
            if (type.isShow() != Bool.YES.value()) {
                isSelectedAll = false;
                break;
            }
        }
        if (isSelectedAll) {
            mIsSelectAll.setText(R.string.unselect_all);
            mIsSelectAll.setTextColor(activity.getResources().getColor(R.color.print_font_color));
        } else {
            mIsSelectAll.setText(R.string.select_all);
            mIsSelectAll.setTextColor(activity.getResources().getColor(R.color.settings_grayword));
        }
    }

    private void allSelectSwitch() {
        for (DishBrandType type : mDishTypeList) {
            if (isSelectedAll)
                type.setShow(Bool.NO.value());
            else
                type.setShow(Bool.YES.value());
        }
        mDishTypeAdapter.notifyDataSetChanged();
        setAllBtnText();
    }


    public static void close() {
        if (mInstance != null) {
                        Activity activity = mInstance.activity;
            if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            mInstance.dismiss();
            mInstance = null;
        }
    }

}
