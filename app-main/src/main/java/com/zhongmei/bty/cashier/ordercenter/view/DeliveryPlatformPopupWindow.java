package com.zhongmei.bty.cashier.ordercenter.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.common.adpter.ViewHolder;
import com.zhongmei.bty.common.adpter.abslistview.CommonAdapter;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;

import java.util.List;


/**
 * 配送平台展示框
 */

public class DeliveryPlatformPopupWindow extends PopupWindow {
    /**
     * 配送平台弹出层回调接口
     */
    public interface OnDeliveryPlatformSelectedListener {
        /**
         * 当某一个配送平台被选择是调用
         */
        void OnDeliveryPlatformSelected(PartnerShopBiz partnerShopBiz);
    }

    //istView子项高度
    private static final int LIST_ITEM_HEIGHT = DensityUtil.dip2px(MainApplication.getInstance(), 64);
    public Context mContext;
    private int mWidth;
    private List<PartnerShopBiz> mDataSet;
    private OnDeliveryPlatformSelectedListener mListener;

    public DeliveryPlatformPopupWindow(Context context, int width, List<PartnerShopBiz> dataSet, OnDeliveryPlatformSelectedListener listener) {
        super(context);
        mContext = context;
        mWidth = width;
        mDataSet = dataSet;
        mListener = listener;
        init();
    }

    private void init() {
        setContentView(inflateContentView());
        setWidth(mWidth);//设置宽度
        setHeight(getHeight());//设置高度
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0));
    }

    private View inflateContentView() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.view_delivery_platform, null);
        ListView lvContent = (ListView) contentView.findViewById(R.id.delivery_platform_content_lv);
        lvContent.setAdapter(new CommonAdapter<PartnerShopBiz>(mContext, R.layout.delivery_platform_list_item, mDataSet) {
            @Override
            public void convert(ViewHolder holder, final PartnerShopBiz partnerShopBiz) {
                holder.setOnClickListener(R.id.delivery_platform_list_item_name_tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            mListener.OnDeliveryPlatformSelected(partnerShopBiz);
                        }
                        dismiss();
                    }
                });
                holder.setText(R.id.delivery_platform_list_item_name_tv, partnerShopBiz.getSourceName());
                setListItemHeight(holder);
            }
        });
        return contentView;
    }

    /**
     * 设置子项高度
     */
    private void setListItemHeight(ViewHolder holder) {
        View contentView = holder.getConvertView();
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.height = LIST_ITEM_HEIGHT;
        contentView.setLayoutParams(layoutParams);
    }

    /**
     * @return 返回popupwindow高度
     */
    public int getHeight() {
        return LIST_ITEM_HEIGHT * (mDataSet.size() > 5 ? 5 : mDataSet.size());
    }

    public int getWidth() {
        return mWidth;
    }

    /**
     * 设置显示在v上方(以v的左边距为开始位置)
     *
     * @param v
     */
    public void show(View v) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - getWidth() / 2,
                location[1] - getHeight() - DensityUtil.dip2px(MainApplication.getInstance(), 5));
    }

    /**
     * 设置显示在v上方,对齐v的右边。
     *
     * @param v
     */
    public void showAlignRight(View v) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth()) - getWidth(),
                location[1] - getHeight() - DensityUtil.dip2px(MainApplication.getInstance(), 5));
    }
}
