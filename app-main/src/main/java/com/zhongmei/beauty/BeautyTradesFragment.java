package com.zhongmei.beauty;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.zhongmei.beauty.BeautyMainReserverFragment_;
import com.zhongmei.beauty.BeautyMainUnPaidFragment_;
import com.zhongmei.beauty.BeautyTableFragment_;
import com.zhongmei.beauty.entity.BeautyNotifyEntity;
import com.zhongmei.beauty.operates.BeautyNotifyCache;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ui.base.BasicFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.beauty_trades_fragment)
public class BeautyTradesFragment extends BasicFragment implements CompoundButton.OnCheckedChangeListener, BeautyNotifyCache.BeautyNotifyListener {

    @ViewById(R.id.rb_table)
    protected RadioButton rb_tables;

    @ViewById(R.id.rb_reserver)
    protected RadioButton rb_reserverTrades;

    @ViewById(R.id.rb_trades)
    protected RadioButton rb_unPaidTrades;

    private BeautyNotifyCache mBeautyNotifyCache;

    private BeautyMainUnPaidFragment mUnpaidTradeFragment;

    private BeautyTableFragment mTableFragment;

    @AfterViews
    public void init() {
        initEnvironment();
        rb_reserverTrades.setOnCheckedChangeListener(this);
        rb_unPaidTrades.setOnCheckedChangeListener(this);
        rb_tables.setOnCheckedChangeListener(this);

        rb_tables.setChecked(true);
    }

    private void initEnvironment() {
        mBeautyNotifyCache = BeautyNotifyCache.getInstance();
        mBeautyNotifyCache.addNotifyListener(this);
        mBeautyNotifyCache.start();
    }


    private void toUnpaidTradeFragment() {
        if (mUnpaidTradeFragment == null) {
            mUnpaidTradeFragment = BeautyMainUnPaidFragment_.builder().build();
        }
        replaceChildFragment(R.id.layout_trades_contain, mUnpaidTradeFragment, "unPaidTradesFragment");
    }

    private void toTableFragment() {
        if (mTableFragment == null) {
            mTableFragment = BeautyTableFragment_.builder().build();
        }
        replaceChildFragment(R.id.layout_trades_contain, mTableFragment, "tableFragment");
    }


    private void refreshCompoundButtonTextColor(CompoundButton combtn, boolean isChecked) {
        Context mContext = getActivity().getBaseContext();
        if (combtn != null) {
            String text = combtn.getText().toString();
            SpannableString textSpan = new SpannableString(text);
            int colorRes = isChecked ? R.color.beauty_color_333333 : R.color.beauty_text_gray;
            if (!TextUtils.isEmpty(text) && text.indexOf(" ") > -1) {
                                textSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(colorRes)), 0, text.indexOf(" "), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.beauty_color_FFB729)), text.indexOf(" "), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                textSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(colorRes)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            combtn.setText(textSpan);
        }
    }

    @UiThread
    @Override
    public void refreshNotifyNumbers(BeautyNotifyEntity notifyEntity) {
        String todyReserverStr = getString(R.string.beauty_today_reserver) + " " + notifyEntity.getTodayReserverNumber();
        rb_reserverTrades.setText(todyReserverStr);

        String unPaidStr = getString(R.string.beauty_unpaid_trade) + " " + notifyEntity.getUnpaidTradeNumber();
        rb_unPaidTrades.setText(unPaidStr);
    }


    private void toReserverFragment() {
        BeautyMainReserverFragment mReserverFragment = BeautyMainReserverFragment_.builder().build();
        replaceChildFragment(R.id.layout_trades_contain, mReserverFragment, "reserverFragment");
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.rb_reserver:
                if (isChecked) {
                    toReserverFragment();
                }
                break;
            case R.id.rb_trades:
                if (isChecked) {
                    toUnpaidTradeFragment();
                }
                break;
            case R.id.rb_table:
                if (isChecked) {
                    toTableFragment();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mBeautyNotifyCache != null) {
            mBeautyNotifyCache.removeNotifyListener(this);
            Log.e("TradesFragment", "mBeautyNotifyCache.onDestory()");
        }
        super.onDestroy();
    }
}
