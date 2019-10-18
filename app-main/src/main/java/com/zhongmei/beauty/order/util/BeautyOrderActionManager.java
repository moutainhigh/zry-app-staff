package com.zhongmei.beauty.order.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.util.SettingManager;
import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.beauty.order.vo.BeautyOpActionVo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class BeautyOrderActionManager {

    public interface IOrderActionListener {
        void onDataChanged();

        void onActionClicked(View view, BeautyOpActionVo orderActionVo);
    }

    private List<BeautyOpActionVo> mOrderActionVos = new ArrayList<BeautyOpActionVo>();
        private SparseArray<View> allChildViews = new SparseArray<View>();
    private Context mContext;
    private IOrderActionListener mListener;
    private View mSelectedView;

    private int mDefaultPageSize;

    private int mPageSize;

    private LayoutInflater mLayoutInflater;

    public BeautyOrderActionManager(Context context, IOrderActionListener listener) {
        mContext = context;
        mDefaultPageSize = 5;
        mPageSize = mDefaultPageSize;
        mListener = listener;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    public void loadData() {
        AsyncTask<Void, Void, List<BeautyOpActionVo>> asyncTask = new AsyncTask<Void, Void, List<BeautyOpActionVo>>() {
            @Override
            protected List<BeautyOpActionVo> doInBackground(Void... params) {
                Resources resources = mContext.getResources();
                IPanelItemSettings iPanelItemSettings = SettingManager.getSettings(IPanelItemSettings.class);
                List<BeautyOpActionVo> orderActionVoList = new ArrayList<BeautyOpActionVo>();

                                addIf(true, orderActionVoList,
                        new BeautyOpActionVo(BeautyOpActionVo.TYPE_CUSTOMER,
                                mContext.getString(R.string.beauty_card), resources.getDrawable(R.drawable.beauty_m_card_selector)));

                addIf(true,
                        orderActionVoList,
                        new BeautyOpActionVo(BeautyOpActionVo.TYPE_INTEGRAL,
                                mContext.getString(R.string.customer_integral),
                                resources.getDrawable(R.drawable.beauty_m_integral_selector)));
                                addIf(true,
                        orderActionVoList,
                        new BeautyOpActionVo(BeautyOpActionVo.TYPE_COUPONS,
                                mContext.getString(R.string.beauty_coupon),
                                resources.getDrawable(R.drawable.beauty_m_coupon_selector)));


                                addIf(true,
                        orderActionVoList,
                        new BeautyOpActionVo(BeautyOpActionVo.TYPE_DISCOUNT,
                                mContext.getString(R.string.discount),
                                resources.getDrawable(R.drawable.beauty_m_discount_selector)));


                                addIf(true,
                        orderActionVoList,
                        new BeautyOpActionVo(BeautyOpActionVo.TYPE_ACTIVITY,
                                mContext.getString(R.string.beauty_activity_title),
                                resources.getDrawable(R.drawable.beauty_m_market_selector)));


                                addIf(true,
                        orderActionVoList,
                        new BeautyOpActionVo(BeautyOpActionVo.TYPE_WEIXINCODE,
                                mContext.getString(R.string.coupon_code_WeChat_label),
                                resources.getDrawable(R.drawable.beauty_m_weixincode_selector)));
                return orderActionVoList;
            }

            private <T> void addIf(boolean condition, Collection<T> collection, T t) {
                if (collection != null && t != null && condition) {
                    collection.add(t);
                }
            }

            @Override
            protected void onPostExecute(List<BeautyOpActionVo> orderActionVos) {
                mOrderActionVos = orderActionVos;
                if (mListener != null) {
                    mListener.onDataChanged();
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else {
            asyncTask.execute();
        }
    }

    private int getPageCount() {
        return mOrderActionVos == null ? 0 : (mOrderActionVos.size() + mPageSize - 1) / mPageSize;
    }


    public boolean showSwitchPage() {
        return mOrderActionVos != null && mOrderActionVos.size() > mDefaultPageSize;
    }

    public List<View> inflateView() {
        List<View> views = new ArrayList<View>();
        LinearLayout linearLayout = null;

        for (int i = 0; i < mOrderActionVos.size(); i++) {
            if (i % mPageSize == 0) {
                linearLayout = new LinearLayout(mContext);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
            }

            View view = mLayoutInflater.inflate(R.layout.beauty_middle_action, null);
            view.setId(i);
            view.setTag(mOrderActionVos.get(i));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onActionClicked(v, mOrderActionVos.get(v.getId()));
                    }
                }
            });

            ImageView ivOrderAction = (ImageView) view.findViewById(R.id.iv_order_action);
            Drawable drawable = mOrderActionVos.get(i).getDrawable();
            if (drawable != null) {
                ivOrderAction.setImageDrawable(drawable);
                ivOrderAction.setVisibility(View.VISIBLE);
            } else {
                ivOrderAction.setVisibility(View.GONE);
            }

            TextView tvOrderAction = (TextView) view.findViewById(R.id.tv_order_action);
            String name = mOrderActionVos.get(i).getName();
            if (TextUtils.isEmpty(name)) {
                tvOrderAction.setVisibility(View.GONE);
            } else {
                tvOrderAction.setText(name);
                tvOrderAction.setVisibility(View.VISIBLE);
            }

            linearLayout.addView(view, i % mPageSize, getItemLayoutParams(i % mPageSize));
            if (i % mPageSize == mPageSize - 1 || i == mOrderActionVos.size() - 1) {
                views.add(linearLayout);
            }

            allChildViews.put(mOrderActionVos.get(i).getType(), view);
        }

        int pageCount = getPageCount();
        if (pageCount * mPageSize > mOrderActionVos.size()) {
            for (int j = mOrderActionVos.size(); j < pageCount * mPageSize; j++) {
                View view = new View(mContext);
                LinearLayout parentView = (LinearLayout) views.get(views.size() - 1);
                parentView.addView(view,
                        j % mPageSize,
                        getItemLayoutParams(j % mPageSize));
            }
        }
        return views;
    }


    private LinearLayout.LayoutParams getItemLayoutParams(int position) {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;
        params.gravity = Gravity.CENTER;
        params.topMargin = DensityUtil.dip2px(MainApplication.getInstance(), 8);
        params.leftMargin = DensityUtil.dip2px(MainApplication.getInstance(), 13);
        params.rightMargin = DensityUtil.dip2px(MainApplication.getInstance(), 13);
        if (position == mPageSize - 1) {
            params.bottomMargin = DensityUtil.dip2px(MainApplication.getInstance(), 8);
        } else {
            params.bottomMargin = DensityUtil.dip2px(MainApplication.getInstance(), 0);
        }

        return params;
    }

    public void setSelectedView(View selectedView) {
        clearSelectedView();
        if (selectedView != null) {
            selectedView.setSelected(true);
            mSelectedView = selectedView;
        }
    }

    public View getNeedSelectedView(int type) {
        return allChildViews.get(type);
    }

    public void clearSelectedView() {
        if (mSelectedView != null) {
            mSelectedView.setSelected(false);
            mSelectedView = null;
        }
    }

    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }


    public void refreshCustomer(boolean isEnabled) {
        View view = getNeedSelectedView(BeautyOpActionVo.TYPE_CUSTOMER);
        setViewEnabled(view, isEnabled);
        View couponView = getNeedSelectedView(BeautyOpActionVo.TYPE_COUPONS);
        setViewEnabled(couponView, isEnabled);
        View integralView = getNeedSelectedView(BeautyOpActionVo.TYPE_INTEGRAL);
        setViewEnabled(integralView, isEnabled);
    }

    private void setViewEnabled(View view, boolean isEnabled) {
        view.setEnabled(isEnabled);
        if (isEnabled) {
            view.setAlpha(1f);
        } else {
            view.setAlpha(0.4f);
        }
    }

}
