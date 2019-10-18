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




public class DeliveryPlatformPopupWindow extends PopupWindow {

    public interface OnDeliveryPlatformSelectedListener {

        void OnDeliveryPlatformSelected(PartnerShopBiz partnerShopBiz);
    }

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
        setWidth(mWidth);        setHeight(getHeight());        setFocusable(true);
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


    private void setListItemHeight(ViewHolder holder) {
        View contentView = holder.getConvertView();
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.height = LIST_ITEM_HEIGHT;
        contentView.setLayoutParams(layoutParams);
    }


    public int getHeight() {
        return LIST_ITEM_HEIGHT * (mDataSet.size() > 5 ? 5 : mDataSet.size());
    }

    public int getWidth() {
        return mWidth;
    }


    public void show(View v) {
                int[] location = new int[2];
        v.getLocationOnScreen(location);
                showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - getWidth() / 2,
                location[1] - getHeight() - DensityUtil.dip2px(MainApplication.getInstance(), 5));
    }


    public void showAlignRight(View v) {
                int[] location = new int[2];
        v.getLocationOnScreen(location);
                showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth()) - getWidth(),
                location[1] - getHeight() - DensityUtil.dip2px(MainApplication.getInstance(), 5));
    }
}
