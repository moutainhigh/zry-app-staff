package com.zhongmei.bty.mobilepay.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.MemberLoginTypeData;

import java.util.ArrayList;
import java.util.List;

import static com.zhongmei.bty.mobilepay.bean.MemberLoginTypeData.UI_TYPE_MOBILE;

/**
 *

 *
 */
public class MemberLoginTypeChooseAdapter extends BaseAdapter {

    private Context mContext;

    private List<MemberLoginTypeData> dataList;

    private int mCurrentUiType = UI_TYPE_MOBILE;


    public MemberLoginTypeChooseAdapter(Context context) {
        mContext = context;
        this.dataList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.member_login_type_spinner_item_layout, parent, false);
        }
        TextView mTvLoginType = (TextView) convertView.findViewById(R.id.tv_member_type);
        MemberLoginTypeData memberLoginTypeData = dataList.get(position);
        Drawable leftDrawable = mContext.getResources().getDrawable(memberLoginTypeData.getMemberTypeImgRes());
        mTvLoginType.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
        mTvLoginType.setText(memberLoginTypeData.getMemberTypeTextRes());
        if (memberLoginTypeData.getUiType() == mCurrentUiType) {
            mTvLoginType.setSelected(true);
        } else {
            mTvLoginType.setSelected(false);
        }
        if (position == 0) {
            mTvLoginType.setBackgroundResource(R.drawable.member_pay_login_type_popup_top_selector);
        }
        if (position == 1) {
            mTvLoginType.setBackgroundResource(R.drawable.member_pay_login_type_popup_bottom_selector);
        }
        return convertView;
    }


    public void refreshData(List<MemberLoginTypeData> dataList, int uiType) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        mCurrentUiType = uiType;
        notifyDataSetChanged();
    }

}
