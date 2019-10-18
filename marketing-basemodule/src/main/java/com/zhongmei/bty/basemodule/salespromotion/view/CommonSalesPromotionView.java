package com.zhongmei.bty.basemodule.salespromotion.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRuleVo;
import com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConvertHelper;
import com.zhongmei.bty.basemodule.salespromotion.adapter.SalesPromotionExpandableListAdapter;
import com.zhongmei.yunfu.util.ViewFinder;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CommonSalesPromotionView extends LinearLayout implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener {
    private ExpandableListView lvSalesPromotion;

    private Context mContext;
    private FragmentManager mFragmentManager;
    private LayoutInflater mLayoutInflater;
    private SalesPromotionExpandableListAdapter mAdapter;

    private Callback mCallback;

    public CommonSalesPromotionView(Context context, FragmentManager fragmentManager) {
        super(context);
        mContext = context;
        mFragmentManager = fragmentManager;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void initView() {
        View view = mLayoutInflater.inflate(R.layout.common_sales_promotion_layout, this, true);
        initSalesPromotionExpandableListView(view);
    }

    private void initSalesPromotionExpandableListView(View view) {
        lvSalesPromotion = ViewFinder.findViewById(view, R.id.lv_sales_promotion);
        lvSalesPromotion.setGroupIndicator(null);
        lvSalesPromotion.setOnGroupClickListener(this);
        lvSalesPromotion.setOnChildClickListener(this);

        mAdapter = new SalesPromotionExpandableListAdapter(mContext, mFragmentManager);
        mAdapter.setDataSet(listSalesPromotionRuleVo(), getSelectedSalesPromotionRules(), getCustomerNew());
        lvSalesPromotion.setAdapter(mAdapter);

                int groupCount = lvSalesPromotion.getCount();
        for (int i = 0; i < groupCount; i++) {
            lvSalesPromotion.expandGroup(i);
        }
    }


    public List<SalesPromotionRuleVo> listSalesPromotionRuleVo() {
        return reSort(SalesPromotionConvertHelper.getInstance().getSalesPromotionRuleVos());
    }


    private List<SalesPromotionRuleVo> reSort(List<SalesPromotionRuleVo> salesPromotionRuleVos) {
        if (Utils.isNotEmpty(salesPromotionRuleVos)) {
            List<SalesPromotionRuleVo> sortedSalesPromotionRuleVos = new ArrayList<SalesPromotionRuleVo>();
            int size = salesPromotionRuleVos.size();
            List<SalesPromotionRuleVo> list1 = new ArrayList<SalesPromotionRuleVo>();
            List<SalesPromotionRuleVo> list2 = new ArrayList<SalesPromotionRuleVo>();
            for (int i = 0; i < size; i++) {
                SalesPromotionRuleVo salesPromotionRuleVo = salesPromotionRuleVos.get(i);
                if (salesPromotionRuleVo != null) {
                    if (salesPromotionRuleVo.isCurrentEnable(getCustomerNew())) {
                        list1.add(salesPromotionRuleVo);
                    } else {
                        list2.add(salesPromotionRuleVo);
                    }
                }
            }

            sortedSalesPromotionRuleVos.addAll(list1);
            sortedSalesPromotionRuleVos.addAll(list2);
            return sortedSalesPromotionRuleVos;
        }

        return Collections.emptyList();
    }

    public void refreshView() {
        if (mAdapter != null) {
            mAdapter.setDataSet(listSalesPromotionRuleVo(), getSelectedSalesPromotionRules(), getCustomerNew());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mCallback != null) {
            mCallback.exitManualAddSalesPromotionMode();
        }
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View convertView, int groupPosition, long id) {
        boolean groupExpanded = expandableListView.isGroupExpanded(groupPosition);
        if (groupExpanded) {
            expandableListView.collapseGroup(groupPosition);
        } else {
            expandableListView.expandGroup(groupPosition, true);
        }
        mAdapter.setIndicatorState(groupExpanded, convertView);
        return true;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
        if (mAdapter != null) {
            SalesPromotionRuleVo salesPromotionRuleVo = mAdapter.getChild(groupPosition, childPosition);
            if (salesPromotionRuleVo != null && salesPromotionRuleVo.isCurrentEnable(getCustomerNew()) && mCallback != null) {
                List<Long> selectedSalesPromotionRules = mAdapter.getSelectedSalesPromotionRules();
                if (selectedSalesPromotionRules.contains(salesPromotionRuleVo.getRule().getId())) {
                    mCallback.removeSalesPromotion(salesPromotionRuleVo);
                } else {
                    mCallback.enterManualAddSalesPromotionMode(salesPromotionRuleVo);
                }
            }
        }

        return true;
    }

    public Callback getCallback() {
        return mCallback;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }


    public abstract CustomerResp getCustomerNew();


    public abstract List<Long> getSelectedSalesPromotionRules();


    public interface Callback {

        void enterManualAddSalesPromotionMode(@NonNull SalesPromotionRuleVo salesPromotionRuleVo);


        void exitManualAddSalesPromotionMode();


        void removeSalesPromotion(@NonNull SalesPromotionRuleVo salesPromotionRuleVo);
    }
}
