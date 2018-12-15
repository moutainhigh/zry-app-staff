package com.zhongmei.bty.settings.fragment;

import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingBusinessType;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingOperateType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.basemodule.shopmanager.message.OpenAndCloseReq;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.settings.adapter.AutoAccepAdapter;
import com.zhongmei.bty.basemodule.trade.bean.TradeDealSettingVo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * 微信订单控制
 */
@EFragment(R.layout.weixin_order_control_fragment)
public class WeiXinOrderControlFragment extends Fragment {

    private static final String TAG = WeiXinOrderControlFragment.class.getSimpleName();

    //接受微信订单开关
    @ViewById(R.id.settings_openorclose_swtich)
    ToggleButton mSwich;

    @ViewById(R.id.settings_openorclose_descrption)
    TextView mDesceiption;

    //自动接单ListView
    @ViewById(R.id.auto_accept_list)
    ListView mList;

    @ViewById(R.id.receive_type)
    RelativeLayout receiveType;

    //自动接单Adapter
    private AutoAccepAdapter mAdapter;

    //自动拒绝开关
    @ViewById(R.id.settings_reject_swtich)
    ToggleButton mRejectSwtich;

    //自动拒绝时间
    @ViewById(R.id.rejectTime)
    TextView rejectTime;

    @ViewById(R.id.autoreject_layout)
    RelativeLayout autorejectLayout;

    @ViewById(R.id.tv_auto_reject_title)
    TextView tvAutoRejectTitle;

    //自动接受设置对象
    private TradeDealSettingVo mAcceptVo;

    //自动拒绝设置对象
    private TradeDealSettingVo mRefuseVo;

    @AfterViews
    void init() {
        //获取是否接受微信订单
        boolean openOrClose = SharedPreferenceUtil.getSpUtil().getBoolean("openorclose", true);
        mSwich.setChecked(openOrClose);
//		request();

        //获取自动拒绝值
        boolean isAutoRefuse = SpHelper.getDefault().getLong(Constant.SP_AUTO_REFUSE_SWITCH, -1L) != -1;
        mRejectSwtich.setTag(isAutoRefuse);
        setRejectBg(isAutoRefuse);

        mAdapter = new AutoAccepAdapter(getActivity());
        mList.setAdapter(mAdapter);

        queryOrderSetting();

    }


    /**
     * 查询订单处理设置数据
     */
    private void queryOrderSetting() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                TradeDealSettingVo acceptVo = null;
                try {
                    SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);
                    acceptVo = systemSettingDal.findTradeDealSetting(TradeDealSettingOperateType.ACCEPT,
                            TradeDealSettingBusinessType.TAKEAWAY);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                mAcceptVo = acceptVo;

                TradeDealSettingVo refuseVo = null;
                try {
                    SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);
                    refuseVo = systemSettingDal.findTradeDealSetting(TradeDealSettingOperateType.REFUSE,
                            TradeDealSettingBusinessType.TAKEAWAY);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                mRefuseVo = refuseVo;
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            //设置自动接单
            if (mAcceptVo != null && mAcceptVo.getTradeDealSetting() != null
                    && mAcceptVo.getTradeDealSetting().getIsEnabled() == YesOrNo.YES) {
                mAdapter.setDataSet(mAcceptVo.getTradeDealSettingItems());
                mList.setVisibility(View.VISIBLE);
            } else {
                mList.setVisibility(View.GONE);
                receiveType.setVisibility(View.GONE);
            }

            //设置自动拒绝
            if (mRefuseVo != null && mRefuseVo.getTradeDealSetting() != null
                    && mRefuseVo.getTradeDealSetting().getIsEnabled() == YesOrNo.YES) {
                autorejectLayout.setVisibility(View.VISIBLE);

                if (isAdded()) {
                    rejectTime.setText(getString(R.string.waiting_time, mRefuseVo.getTradeDealSetting().getWaitTime().toString()));
                }

            } else {
                autorejectLayout.setVisibility(View.GONE);
                tvAutoRejectTitle.setVisibility(View.GONE);
            }
        }

        ;
    };

    @Click(R.id.settings_openorclose_swtich)
    void clickSwich() {
        request();
    }

    @Click(R.id.settings_reject_swtich)
    void click() {
        if (!ClickManager.getInstance().isClicked()) {
            if (!(Boolean) mRejectSwtich.getTag()) {
                mRejectSwtich.setTag(true);
                SpHelper.getDefault().putLong(Constant.SP_AUTO_REFUSE_SWITCH, System.currentTimeMillis());
            } else {
                mRejectSwtich.setTag(false);
                SpHelper.getDefault().putLong(Constant.SP_AUTO_REFUSE_SWITCH, -1L);
            }
            setRejectBg((Boolean) mRejectSwtich.getTag());
        }
    }

    /**
     * 设置自动拒绝背景
     */
    private void setRejectBg(boolean isOpen) {
        if (isOpen) {
            mRejectSwtich.setChecked(true);
        } else {
            mRejectSwtich.setChecked(false);
        }
    }

    /**
     * @Title: isChecked
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    private Boolean isChecked() {
        // TODO Auto-generated method stub
        if (mSwich.isChecked() == true) {
            if (isAdded()) {
                mDesceiption.setText(getString(R.string.accept_network_order_str));
            }
            return true;
        } else {
            if (isAdded()) {
                mDesceiption.setText(getString(R.string.not_accept_network_order_str));
            }
            return false;
        }
    }

    /**
     * 进行网络请求，如果ToggleButton是开启的，则开启状态设为0（0为可以接受网络订单状态）， 否则，则为-1（-1为拒绝接受）
     *
     * @Title: request
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    private void request() {
        OpenAndCloseReq mOpenAndCloseReq = new OpenAndCloseReq();
        if (isChecked()) {
            mOpenAndCloseReq.setStatus(0);
            requestNet(mOpenAndCloseReq);
        } else {
            mOpenAndCloseReq.setStatus(-1);
            requestNet(mOpenAndCloseReq);
        }
    }

    private void requestNet(OpenAndCloseReq req) {
        ResponseListener<Object> listener = new ResponseListener<Object>() {

            @Override
            public void onResponse(ResponseObject<Object> response) {
                if (ResponseObject.isOk(response)) {
                    SharedPreferenceUtil.getSpUtil().putBoolean("openorclose", isChecked());

                } else {
                    if (isAdded()) {
                        ToastUtil.showShortToast(getString(R.string.return_error));
                        mDesceiption.setText(getString(R.string.not_accept_network_order_str));
                    }
                    mSwich.setChecked(false);
                }
            }

            @Override
            public void onError(VolleyError error) {
                mSwich.setChecked(false);
                if (isAdded()) {
                    mDesceiption.setText(getString(R.string.not_accept_network_order_str));
                    ToastUtil.showShortToast("error message = " + error.getMessage());
                }
            }
        };

        SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);
        systemSettingDal.openAndCloseBusiness(req, listener);
    }

}
