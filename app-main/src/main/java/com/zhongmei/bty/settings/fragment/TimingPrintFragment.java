package com.zhongmei.bty.settings.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.basemodule.database.db.CashHandoverConfig;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.basemodule.shopmanager.handover.manager.ServerSettingManager;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.yunfu.resp.ResponseObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.settings_print_timing)
public class TimingPrintFragment extends BasicFragment {
    private static final String TAG = TimingPrintFragment.class.getSimpleName();

    private SystemSettingDal dal = OperatesFactory.create(SystemSettingDal.class);

    @ViewById(R.id.ll_selected_timing_type)
    LinearLayout llSelectedTimingType;

    @ViewById(R.id.tv_selected_timing_type)
    TextView tvSelectedTimingType;

    @ViewById(R.id.btn_expand)
    ToggleButton btnExpand;

    @ViewById(R.id.interval_type)
    LinearLayout intervalType;

    @ViewById(R.id.interval_min_30)
    TextView tvMin30;

    @ViewById(R.id.interval_min_60)
    TextView tvMin60;

    @ViewById(R.id.interval_min_90)
    TextView tvMin90;

    @ViewById(R.id.interval_min_120)
    TextView tvMin120;


    @AfterViews
    void init() {
        getTiminPrintMin();
        btnExpand.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    intervalType.setVisibility(View.VISIBLE);
                    if (tvMin60.isSelected()) {
                        setTimingPrintMin(2);
                        tvSelectedTimingType.setText(R.string.timing_print_60);
                    } else if (tvMin90.isSelected()) {
                        setTimingPrintMin(3);
                        tvSelectedTimingType.setText(R.string.timing_print_90);
                    } else if (tvMin120.isSelected()) {
                        setTimingPrintMin(4);
                        tvSelectedTimingType.setText(R.string.timing_print_120);
                    } else {
                        setTimingPrintMin(1);
                        selectTimingType(tvMin30);
                        tvSelectedTimingType.setText(R.string.timing_print_30);
                    }
                } else {
                    intervalType.setVisibility(View.GONE);
                    tvSelectedTimingType.setText(R.string.unselect);
                    setTimingPrintMin(0);
                }
            }
        });
    }

    @SuppressLint("InflateParams")
    @Click({R.id.interval_min_30, R.id.interval_min_60, R.id.interval_min_90, R.id.interval_min_120})
    protected void click(View v) {
        switch (v.getId()) {
            case R.id.interval_min_30:
                if (!tvMin30.isSelected()) {
                    setSelectTimingType(1);
                    setTimingPrintMin(1);
                }
                break;
            case R.id.interval_min_60:
                if (!tvMin60.isSelected()) {
                    setSelectTimingType(2);
                    setTimingPrintMin(2);
                }
                break;
            case R.id.interval_min_90:
                if (!tvMin90.isSelected()) {
                    setSelectTimingType(3);
                    setTimingPrintMin(3);
                }
                break;
            case R.id.interval_min_120:
                if (!tvMin120.isSelected()) {
                    setSelectTimingType(4);
                    setTimingPrintMin(4);
                }
                break;
        }
    }

    /**
     * 选中时间间隔，30min，60min，90min和120min
     *
     * @param tv
     */
    private void selectTimingType(TextView tv) {
        tv.setSelected(true);
        Drawable drawable = getActivity().getResources().getDrawable(R.drawable.print_cashbox_select);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(null, null, drawable, null);
        tv.setTextColor(getActivity().getResources().getColor(R.color.color_32ADF6));
    }

    /**
     * 取消选中时间间隔
     *
     * @param tv
     */
    private void cancelSelectTimingType(TextView tv) {
        tv.setSelected(false);
        tv.setCompoundDrawables(null, null, null, null);
        tv.setTextColor(getActivity().getResources().getColor(R.color.color_333333));
    }

    /**
     * 读取定时打印配置信息
     */
    private void getTiminPrintMin() {
        try {
            CashHandoverConfig config = dal.findCashHandoverConfig(3);
            if (config != null) {
                int flag = config.getConfigValue();
                if (flag != 0) {
                    setSelectTimingType(flag);
                    btnExpand.setChecked(true);
                } else {
                    btnExpand.setChecked(false);
                    intervalType.setVisibility(View.GONE);
                }
            } else {
                btnExpand.setChecked(false);
                intervalType.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    /**
     * 设置定时打印
     *
     * @param value
     */
    private void setTimingPrintMin(int value) {
        ResponseListener<CashHandoverConfig> listener = new ResponseListener<CashHandoverConfig>() {

            @Override
            public void onResponse(ResponseObject<CashHandoverConfig> response) {
                if (ResponseObject.isOk(response)) {
                    ToastUtil.showLongToast(R.string.setting_success);

                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }

        };

        try {
            CashHandoverConfig config = dal.findCashHandoverConfig(3);
            if (config == null) {
                config = new CashHandoverConfig();
                config.setUuid(SystemUtils.genOnlyIdentifier());
                config.setConfigKey(3);
            }
            config.setConfigValue(value);
            ServerSettingManager.updateHandoverSet(config, LoadingResponseListener.ensure(listener, getFragmentManager()));
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }

    }

    /**
     * 设置定时打印选中显示效果
     *
     * @param flag
     */
    private void setSelectTimingType(int flag) {
        TextView[] tvMin = {tvMin30, tvMin60, tvMin90, tvMin120};
        int[] timingPrint =
                {R.string.timing_print_30, R.string.timing_print_60, R.string.timing_print_90, R.string.timing_print_120};

        for (int i = 0; i < tvMin.length; i++) {
            if (i == flag - 1) {
                selectTimingType(tvMin[i]);
                tvSelectedTimingType.setText(timingPrint[i]);
            } else {
                cancelSelectTimingType(tvMin[i]);
            }
        }
        intervalType.setVisibility(View.VISIBLE);
    }
}
