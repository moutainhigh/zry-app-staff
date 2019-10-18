package com.zhongmei.bty.dinner.table.view;

import java.util.List;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.model.ZoneModel;
import com.zhongmei.bty.basemodule.trade.bean.IZone;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.util.LongSparseArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ZonesIndicator extends LinearLayout {

    private OnControlListener mOnControlListener;

    private final LongSparseArray<ViewGroup> viewFinder;
    private IZone mCurr;
    private List<ZoneModel> zones;

    public List<ZoneModel> getZones() {
        return zones;
    }

    public ZonesIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewFinder = new LongSparseArray<ViewGroup>();
        this.setBackgroundColor(Color.WHITE);
    }

    public void setOnControlListener(OnControlListener listener) {
        mOnControlListener = listener;
    }

    public void setZones(List<ZoneModel> zones) {
        this.zones = zones;
        mCurr = null;
        for (int i = getChildCount() - 1; i >= 0; i--) {
            getChildAt(i).setOnClickListener(null);
            removeViewAt(i);
        }
        for (ZoneModel vo : zones) {
            ViewGroup viewGroup = ViewUtils.inflateZoneIndicatorItem(getContext());
            viewGroup.setBackgroundResource(R.drawable.dinnertable_zone_indicator_item_bg_nomal);
            LayoutParams layoutParams = new LayoutParams(
                    0, LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            viewGroup.setLayoutParams(layoutParams);
            addView(viewGroup);
            TextView textView =
                    (TextView) viewGroup.findViewById(R.id.name);
            textView.setTextColor(ViewUtils.COLOR_ZONE_INDICATOR);
            textView.setText(vo.getName());
            viewGroup.setTag(vo);
            refreshWechatSign(viewGroup);
            viewGroup.setOnClickListener(mOnClickListener);

            viewFinder.put(vo.getId(), viewGroup);
        }
        if (zones.size() <= 1) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }
    }


    public boolean switchZone(IZone zone) {
        if (zone != mCurr) {
            if (mCurr != null) {
                ViewGroup viewGroup = viewFinder.get(mCurr.getId());
                viewGroup.setBackgroundResource(R.drawable.dinnertable_zone_indicator_item_bg_nomal);
                TextView textView = (TextView) viewGroup.findViewById(R.id.name);
                textView.setTextColor(ViewUtils.COLOR_ZONE_INDICATOR);
            }
            mCurr = zone;
            ViewGroup viewGroup = viewFinder.get(mCurr.getId());
            viewGroup.setBackgroundResource(R.drawable.dinnertable_zone_indicator_item_bg_pressed);
            TextView textView = (TextView) viewGroup.findViewById(R.id.name);
            textView.setTextColor(ViewUtils.COLOR_ZONE_INDICATOR_SELECTED);
            mOnControlListener.onSwitchZone(mCurr);
            return true;
        }
        return false;
    }


    public boolean switchZone(Long zoneId) {
        if (zoneId != null) {
            if (mCurr != null && zoneId.equals(mCurr.getId())) {
                return true;
            }
            if (zones != null) {
                for (ZoneModel zone : zones) {
                    if (zoneId.equals(zone.getId())) {
                        return switchZone(zone);
                    }
                }
            }
        }
        return false;
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            if (mOnControlListener != null && !ClickManager.getInstance().isClicked()) {
                IZone zone = (IZone) view.getTag();
                switchZone(zone);
            }
        }

    };


    public void refreshZone() {
        if (zones != null) {
            for (ZoneModel vo : zones) {
                ViewGroup viewGroup = viewFinder.get(vo.getId());
                refreshWechatSign(viewGroup);
            }
        }
    }


    private void refreshWechatSign(ViewGroup viewGroup) {
                boolean isShowWechatImage = false;
        ZoneModel model = (ZoneModel) viewGroup.getTag();
        List<DinnertableModel> dinnerTableModels = model.getDinnertableModels();
        if (dinnerTableModels != null && dinnerTableModels.size() > 0) {
            for (DinnertableModel tableModel : model.getDinnertableModels()) {
                if (tableModel.getUnprocessTradeCount() > 0 || tableModel.hasAddDish()) {
                    isShowWechatImage = true;
                    break;
                }
            }
        }
        ImageView wechatImage = (ImageView) viewGroup.findViewById(R.id.zone_unprocess_wechat);
        if (isShowWechatImage) {
            wechatImage.setVisibility(View.VISIBLE);
        } else {
            wechatImage.setVisibility(View.GONE);
        }
    }

}
