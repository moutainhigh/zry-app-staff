package com.zhongmei.bty.settings.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialCustomSettings;
import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.data.MindTransferResp;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 * 基本功能，菜品打印顺序
 */

@EFragment(R.layout.setting_basic_setting)
public class BasicSettingFragment extends BasicFragment {
    @ViewById(R.id.iv_timer_order)
    protected ImageView timeImage;//下单时间排序
    @ViewById(R.id.iv_type_order)
    protected ImageView typeImage;//中类排序
    @ViewById(R.id.tb_enable_printer_open_short_name)
    protected ToggleButton toggleOpenShortName;//开启短名称

    private ImageView[] orderImages;

    @AfterViews
    protected void initView() {
        orderImages = new ImageView[]{timeImage, typeImage};

        SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);
        CommercialCustomSettings customSetting = null;
        CommercialCustomSettings customSettingIsOpenShortName = null;

        try {
            customSetting = systemSettingDal.findCommercialCustomSettings("ticket.order.type");
            customSettingIsOpenShortName = systemSettingDal.findCommercialCustomSettings("ticket_kitchen_is_open_short_name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (customSetting != null && "2".equals(customSetting.getValue())) {
            changeOrderSelected(1);
        } else {
            changeOrderSelected(0);
        }
        if (null != customSettingIsOpenShortName && "1".equals(customSettingIsOpenShortName.getValue())) {//开启
            toggleOpenShortName.setChecked(true);
        } else {//关闭
            toggleOpenShortName.setChecked(false);
        }
    }

    @Click({R.id.ly_setting_time_order, R.id.ly_setting_type_order, R.id.tb_enable_printer_open_short_name})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_setting_time_order:
                changeOrderSelected(0);
                saveSettingConfig(1);
                break;
            case R.id.ly_setting_type_order:
                changeOrderSelected(1);
                saveSettingConfig(2);
                break;
            case R.id.tb_enable_printer_open_short_name:
                saveIsOpenShortName();
                break;
        }
    }

    private void changeOrderSelected(int selectIndex) {
        for (int i = 0; i < orderImages.length; i++) {
            if (i == selectIndex) {
                orderImages[i].setImageResource(R.drawable.setting_content_item_selected);
                orderImages[i].setTag("select");
            } else {
                orderImages[i].setImageResource(R.drawable.setting_content_item_unselected);
                orderImages[i].setTag(null);
            }
        }
    }

    //sort：打印顺序  1表示下单时间，2表示中类顺序
    private void saveSettingConfig(int sort) {
        SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);
        systemSettingDal.updatePrintSort(sort, LoadingResponseListener.ensure(new ResponseListener<MindTransferResp<CommercialCustomSettings>>() {
            @Override
            public void onResponse(ResponseObject<MindTransferResp<CommercialCustomSettings>> response) {
                ToastUtil.showShortToast(response.getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        }, getFragmentManager()));
    }

    //保存是否开启短名称
    private void saveIsOpenShortName() {
        int sort = toggleOpenShortName.isChecked() ? 1 : 2;

        SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);
        systemSettingDal.updateIsOpenShortName(sort, LoadingResponseListener.ensure(new ResponseListener<MindTransferResp<CommercialCustomSettings>>() {
            @Override
            public void onResponse(ResponseObject<MindTransferResp<CommercialCustomSettings>> response) {
                ToastUtil.showShortToast(response.getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        }, getFragmentManager()));

    }
}
