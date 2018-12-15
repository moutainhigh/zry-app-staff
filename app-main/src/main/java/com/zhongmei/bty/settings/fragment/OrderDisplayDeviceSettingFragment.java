package com.zhongmei.bty.settings.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialCustomSettings;
import com.zhongmei.bty.basemodule.commonbusiness.message.CommercialCustomSettingsReq;
import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.bty.basemodule.shopmanager.handover.manager.ServerSettingManager;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.common.adpter.ViewHolder;
import com.zhongmei.bty.common.adpter.abslistview.CommonAdapter;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.data.MindTransferResp;
import com.zhongmei.bty.commonmodule.database.entity.InitSystem;
import com.zhongmei.bty.commonmodule.database.enums.InitSystemDeviceType;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

@EFragment(R.layout.fragment_order_display_device_setting)
public class OrderDisplayDeviceSettingFragment extends BasicFragment {
    private static final String TAG = OrderDisplayDeviceSettingFragment.class.getSimpleName();

    @ViewById(R.id.fragment_order_display_device_setting_content_lv)
    ListView lvContent;

    private SystemSettingDal mSystemSettingDal;

    private OrderDisplayDeviceListAdapter mOrderDisplayDeviceListAdapter;

    @AfterViews
    void initView() {
        mSystemSettingDal = OperatesFactory.create(SystemSettingDal.class);

        //加载当前门店下的设备列表
        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, OrderDisplayDeviceSettingItem>() {
            @Override
            protected OrderDisplayDeviceSettingItem doInBackground(Void... params) {
                OrderDisplayDeviceSettingItem item = new OrderDisplayDeviceSettingItem();
                try {
                    List<InitSystem> initSystems = mSystemSettingDal.queryInitSystem(InitSystemDeviceType.PAD);
                    item.setInitSystems(initSystems);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                String deviceId = ServerSettingManager.getOrderDisplayDevice();
                item.setDeviceId(deviceId);
                return item;
            }

            @Override
            protected void onPostExecute(OrderDisplayDeviceSettingItem item) {
                if (isAdded() && item.getInitSystems() != null) {
                    mOrderDisplayDeviceListAdapter = new OrderDisplayDeviceListAdapter(getActivity(), R.layout.order_display_device_setting_list_item, item.getInitSystems(), item.getDeviceId());
                    lvContent.setAdapter(mOrderDisplayDeviceListAdapter);
                }
            }
        });
    }

    private class OrderDisplayDeviceListAdapter extends CommonAdapter<InitSystem> {
        //选中的设备
        private String mSelectedDeviceId;

        public OrderDisplayDeviceListAdapter(Context context, int layoutId, List<InitSystem> datas, String defaultSelectedDeviceId) {
            super(context, layoutId, datas);
            mSelectedDeviceId = defaultSelectedDeviceId;
        }

        @Override
        public void convert(ViewHolder holder, final InitSystem initSystem) {
            CheckBox cbState = (CheckBox) holder.getConvertView().findViewById(R.id.cb_state);
            if (!TextUtils.isEmpty(mSelectedDeviceId) && mSelectedDeviceId.equals(initSystem.getDeviceID())) {
                cbState.setChecked(true);
            } else {
                cbState.setChecked(false);
            }

            //设置名称
            TextView tvName = (TextView) holder.getConvertView().findViewById(R.id.tv_name);
            String name = getString(R.string.fragment_order_display_device_setting_device_name, initSystem.getPadNo().toString());
            String mainPos = "";
            if (initSystem.getIsMainPos() == 0) {
                mainPos = getString(R.string.fragment_order_display_device_setting_main_pos);
            }
            TextView tvPosMain = (TextView) holder.getConvertView().findViewById(R.id.tv_name_pos_main);
            tvPosMain.setText(mainPos);
            tvName.setText(name);

            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateSaveSetting(initSystem.getDeviceID());
                }
            });
        }
    }

    private void updateSaveSetting(final String value) {
        CommercialCustomSettingsReq req = new CommercialCustomSettingsReq();
        req.setBrandIdenty(MainApplication.getInstance().getBrandIdenty());
        req.setShopIdenty(MainApplication.getInstance().getShopIdenty());
        req.setKey("shop.fast.support.deviceNo");
        req.setValue(value);

        ResponseListener<MindTransferResp<CommercialCustomSettings>> listener = new ResponseListener<MindTransferResp<CommercialCustomSettings>>() {
            @Override
            public void onResponse(ResponseObject<MindTransferResp<CommercialCustomSettings>> response) {
                if (MindTransferResp.isOk(response.getContent())
                        && mOrderDisplayDeviceListAdapter != null) {//为200表示操作成功
                    mOrderDisplayDeviceListAdapter.mSelectedDeviceId = value;
                    mOrderDisplayDeviceListAdapter.notifyDataSetChanged();
                }
                ToastUtil.showShortToast(response.getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        };

        mSystemSettingDal.updateSaveSetting(req, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

    /**
     * 该页面设置数据
     */
    private class OrderDisplayDeviceSettingItem {
        private String deviceId;
        private List<InitSystem> initSystems;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public List<InitSystem> getInitSystems() {
            return initSystems;
        }

        public void setInitSystems(List<InitSystem> initSystems) {
            this.initSystems = initSystems;
        }
    }
}
