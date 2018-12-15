package com.zhongmei.bty.settings.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingBusinessType;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingOperateType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.bty.basemodule.trade.bean.TradeDealSettingVo;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.settings.adapter.DinnerAutoAccepAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * 微信订单控制
 */
@EFragment(R.layout.dinner_auto_order_fragment)
public class DinnerAutoOrderFragment extends Fragment {

    private static final String TAG = DinnerAutoOrderFragment.class.getSimpleName();

    //自动接单ListView
    @ViewById(R.id.auto_accept_list)
    ListView mList;

    @ViewById(R.id.receive_type)
    RelativeLayout receiveType;

    @ViewById(R.id.server_accept_swtich)
    TextView serverAcceptSwtich;

    @ViewById(R.id.btn_weixin_accept_transfer_kitchen)
    Button btn_weixin_accept_transfer_kitchen;

    //自动接单Adapter
    private DinnerAutoAccepAdapter mAdapter;

    //自动接受设置对象
    private TradeDealSettingVo mAcceptVo;

    private boolean mServerAccSw = false;

    @AfterViews
    void init() {
        mAdapter = new DinnerAutoAccepAdapter(getActivity());
        mList.setAdapter(mAdapter);
        queryOrderSetting();
        btn_weixin_accept_transfer_kitchen.setSelected(SpHelper.getDefault().getBoolean(Constant.WEIXIN_ACCEPT_TRANSFER_KITCHEN));
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
                    acceptVo = systemSettingDal.findTradeDealSetting(TradeDealSettingOperateType.SERVER_ACCEPT,
                            TradeDealSettingBusinessType.DINNER);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                mAcceptVo = acceptVo;
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            //设置自动接单
            if (mAcceptVo != null && mAcceptVo.getTradeDealSetting() != null
                /*&& mAcceptVo.getTradeDealSetting().getIsEnabled() == YesOrNo.YES*/) {
                mAdapter.setDataSet(mAcceptVo.getTradeDealSettingItems());
                receiveType.setVisibility(View.VISIBLE);
                mList.setVisibility(View.VISIBLE);
            } else {
                mList.setVisibility(View.GONE);
                receiveType.setVisibility(View.GONE);
            }

            if (mAcceptVo != null && mAcceptVo.getTradeDealSetting() != null)
                mServerAccSw = mAcceptVo.getTradeDealSetting().getIsEnabled() == YesOrNo.YES;
            if (mServerAccSw)
                serverAcceptSwtich.setText(R.string.server_auto_accept_open);
            else
                serverAcceptSwtich.setText(R.string.server_auto_accept_close);

        }

        ;
    };

    @Click(R.id.btn_weixin_accept_transfer_kitchen)
    public void click(View v) {
        boolean isSelected = !btn_weixin_accept_transfer_kitchen.isSelected();
        btn_weixin_accept_transfer_kitchen.setSelected(isSelected);
        SpHelper.getDefault().putBoolean(Constant.WEIXIN_ACCEPT_TRANSFER_KITCHEN, isSelected);
    }
}
