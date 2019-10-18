package com.zhongmei.bty.settings.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.resp.data.MindTransferResp;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialCustomSettings;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.basemodule.shopmanager.handover.manager.ServerSettingManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.fragment_handover_type)
public class HandoverTypeSettingFragment extends Fragment {
    private final static int HANDOVER_TYPE_SHOW_DATA = 1;    private final static int HANDOVER_TYPE_HIDE_DATA = 2;    @ViewById(R.id.show_data_type)
    ImageButton showDataHandoverType;
    @ViewById(R.id.hide_data_type)
    ImageButton hideDataHandoverType;
    private int currentType = HANDOVER_TYPE_SHOW_DATA;

    @AfterViews
    public void init() {
        currentType = ServerSettingManager.getHandoverType();
        setViewByType(currentType);
    }

    private void setHandoverType(final int type) {
        SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);
        systemSettingDal.setHandoverType(type, LoadingResponseListener.ensure(new ResponseListener<MindTransferResp<CommercialCustomSettings>>() {
            @Override
            public void onResponse(ResponseObject<MindTransferResp<CommercialCustomSettings>> response) {
                if (ResponseObject.isOk(response)) {
                    MindTransferResp<CommercialCustomSettings> content = response.getContent();
                    if (MindTransferResp.isOk(content) && content.isSuccess() && content.getData() != null) {
                        setViewByType(type);
                    }
                    ToastUtil.showShortToast(content.getMessage());
                } else {
                    ToastUtil.showShortToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        }, getFragmentManager()));
    }

    @Click({R.id.show_data_type_rl, R.id.show_data_type, R.id.hide_data_type_rl, R.id.hide_data_type})
    public void onClick(View view) {
        int type;
        if (view.getId() == R.id.hide_data_type_rl || view.getId() == R.id.hide_data_type) {
            type = HANDOVER_TYPE_HIDE_DATA;
        } else {
            type = HANDOVER_TYPE_SHOW_DATA;
        }
        if (currentType == type) {
            return;
        }
        currentType = type;
                setHandoverType(currentType);
    }

    private void setViewByType(int type) {
        if (type == HANDOVER_TYPE_SHOW_DATA) {
            showDataHandoverType.setImageResource(R.drawable.cashier_popup_check_checked);
            hideDataHandoverType.setImageResource(R.drawable.cashier_popup_check_normal);
        } else if (type == HANDOVER_TYPE_HIDE_DATA) {
            showDataHandoverType.setImageResource(R.drawable.cashier_popup_check_normal);
            hideDataHandoverType.setImageResource(R.drawable.cashier_popup_check_checked);
        }
    }
}
