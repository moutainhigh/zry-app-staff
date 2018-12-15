package com.zhongmei.bty.dinner.adapter;

import java.util.List;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.discount.bean.DinnerExtraChargeVo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * 附加费的adpater
 *
 * @date:2016年3月10日下午3:13:49
 */
public class DinnerExtraAdapter extends BaseAdapter {

    private Context mContext;

    private List<DinnerExtraChargeVo> mExtraChargeList;

    private OnStatusChangeListner mStatusChangeListener;

    public DinnerExtraAdapter(Context context, List<DinnerExtraChargeVo> extraChargeList) {
        mContext = context;
        mExtraChargeList = extraChargeList;
    }

    public void setmStatusChangeListener(OnStatusChangeListner listener) {
        this.mStatusChangeListener = listener;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (mExtraChargeList == null)
            return 0;
        return mExtraChargeList.size();
    }

    @Override
    public DinnerExtraChargeVo getItem(int position) {
        // TODO Auto-generated method stub

        return mExtraChargeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dinner_extra_item, null);
            viewHolder = new ViewHolder();
            viewHolder.pview = convertView;
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.dinner_extra_name);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.dinner_extra_content);
            viewHolder.cb_status = (CheckBox) convertView.findViewById(R.id.dinner_extra_anchor);
            convertView.setTag(viewHolder);
        }
        DinnerExtraChargeVo extraChargeVo = getItem(position);
        viewHolder = (ViewHolder) convertView.getTag();
        bindData(viewHolder, extraChargeVo, position);
        return convertView;
    }

    private void bindData(ViewHolder viewHolder, DinnerExtraChargeVo extraChargeVo, int position) {
        // TODO Auto-generated method stub
        viewHolder.cb_status.setOnCheckedChangeListener(null);
        ExtraCharge extraCharge = extraChargeVo.getExtrageCharge();
        //服务费不允许操作
        if (extraChargeVo.isServiceCharge()) {
            viewHolder.cb_status.setEnabled(false);
            viewHolder.pview.setAlpha(0.3f);
        } else {
            viewHolder.pview.setAlpha(1f);
        }
        viewHolder.tv_name.setText(extraCharge.getName());
        //根据不同计算方式设置附加费说明
        viewHolder.tv_content.setText(getStringByCalcWay(extraCharge));

        viewHolder.cb_status.setChecked(extraChargeVo.isSelected());

        viewHolder.cb_status.setOnCheckedChangeListener(new CheckChangeListener(position));

//		if (extraChargeVo.isSelected()) {
//			viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.text_blue));
//			viewHolder.tv_content.setTextColor(mContext.getResources().getColor(R.color.text_blue));
//			viewHolder.cb_status.setVisibility(View.VISIBLE);
//			viewHolder.pview.setBackgroundResource(R.drawable.bg_dinner_discount_pressed);
//		} else {
//			viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.orderdish_text_black));
//			viewHolder.tv_content.setTextColor(mContext.getResources().getColor(R.color.orderdish_text_black));
//			viewHolder.cb_status.setVisibility(View.GONE);
//			viewHolder.pview.setBackgroundResource(R.drawable.bg_dinner_discount_normal);
//		}
    }

    public void resetData(List<DinnerExtraChargeVo> extraChargeList) {
        mExtraChargeList = extraChargeList;
        notifyDataSetChanged();
    }

    public void changeSelected(int position) {
        if (mExtraChargeList == null) {
            return;
        }
        DinnerExtraChargeVo extraVo = mExtraChargeList.get(position);
        if (extraVo != null)
            extraVo.setSelected(!extraVo.isSelected());
        notifyDataSetChanged();
    }

    class CheckChangeListener implements CompoundButton.OnCheckedChangeListener {
        private int mPosition;

        public CheckChangeListener(int position) {
            this.mPosition = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getItem(mPosition).setSelected(isChecked);
            if (mStatusChangeListener != null) {
                mStatusChangeListener.onStatusChange(mPosition, isChecked);
            }
        }
    }

    class ViewHolder {
        View pview;

        TextView tv_name;

        TextView tv_content;

        CheckBox cb_status;
    }

    /**
     * 根据附加费计算方式返回具体说明格式
     */
    private String getStringByCalcWay(ExtraCharge extraChage) {
        String calcWayDesc = "";
        switch (extraChage.getCalcWay()) {
            case FIXED_AMOUNT:
                calcWayDesc = String.format(mContext.getString(R.string.fixed_price_str), ShopInfoCfg.formatCurrencySymbol(extraChage.getContent()));
                break;
            case RATE:
                calcWayDesc = String.format(mContext.getString(R.string.all_price_rate_str), extraChage.getContent() + "%");
                break;
            case NUMBER_OF_PEOPLE:
                calcWayDesc = String.format(mContext.getString(R.string.guest_num_price_str), ShopInfoCfg.formatCurrencySymbol(extraChage.getContent()));
                break;
            default:
                break;
        }
        return calcWayDesc;

    }

    public interface OnStatusChangeListner {
        void onStatusChange(int position, boolean isCheck);
    }
}
