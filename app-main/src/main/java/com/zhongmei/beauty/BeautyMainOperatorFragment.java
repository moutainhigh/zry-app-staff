package com.zhongmei.beauty;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.beauty.entity.BeautyNotifyEntity;
import com.zhongmei.beauty.interfaces.IBeautyOperator;
import com.zhongmei.beauty.operates.BeautyNotifyCache;
import com.zhongmei.util.AsyncImage;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.ui.base.BasicFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.beauty_main_fragment_operator)
public class BeautyMainOperatorFragment extends BasicFragment implements BeautyNotifyCache.BeautyNotifyListener {

    @ViewById(R.id.tv_version)
    TextView tv_version;

    @ViewById(R.id.iv_shop_logo)
    protected ImageView iv_shopLogo;

    @ViewById(R.id.tv_shop_name)
    protected TextView tv_shopName;

    @ViewById(R.id.tv_shopinfo)
    protected TextView tv_shopinfo;

    @ViewById(R.id.tv_customer_number)
    protected TextView tv_customerNumber;//到店人数

    @ViewById(R.id.tv_reserver_number)
    protected TextView tv_reserverNumber;//预约单数

    @ViewById(R.id.tv_trade_number)
    protected TextView tv_tradeNumber;//订单数

    @ViewById(R.id.tv_member_number)
    protected TextView tv_memberNumber;//新增会员数

    private BeautyNotifyCache mBeautyNotifyCache;

    private IBeautyOperator iBeautyOperatorListener;

    @AfterViews
    public void init() {
        showShopInfo();
        setVersion(SystemUtils.getVersionName());
        initEnvrionment();
    }


    public void setiBeautyOperatorListener(IBeautyOperator iBeautyOperatorListener) {
        this.iBeautyOperatorListener = iBeautyOperatorListener;
    }

    /**
     * 加载门店信息
     */
    private void showShopInfo() {
        String shopName = ShopInfoManager.getInstance().shopInfo.getShopName();
        tv_shopName.setText(shopName);
        tv_shopinfo.setText(String.format(getString(R.string.beauty_shopinfo), ShopInfoManager.getInstance().shopInfo.getShopAddress()));

        String logoUrl = ShopInfoManager.getInstance().shopInfo.getShopLogo();
        if (!TextUtils.isEmpty(logoUrl)) {
            /*ImageLoader imageLoader = new ImageLoader.Builder()
                    .imageView(iv_shopLogo)
                    .url(logoUrl)
                    .errorImage(R.drawable.shop_icon).build();
            ImageHandler.getInstance().loadImage(getActivity().getApplicationContext(), imageLoader);*/
            AsyncImage.showImg(getActivity(), iv_shopLogo, logoUrl, R.drawable.shop_icon);
        }

    }

    /**
     * 设置版本号
     * @param versionName
     */
    private void setVersion(String versionName){
        if(TextUtils.isEmpty(versionName)){
            tv_version.setVisibility(View.GONE);
            return;
        }

        tv_version.setText("V"+versionName);

    }


    private void initEnvrionment() {
        mBeautyNotifyCache = BeautyNotifyCache.getInstance();
        mBeautyNotifyCache.addNotifyListener(this);
        mBeautyNotifyCache.start();
    }

    @Click({R.id.btn_create_trade, R.id.btn_create_card, R.id.btn_charge, R.id.btn_create_member, R.id.btn_create_reserver})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_trade://开单
                if (iBeautyOperatorListener != null) {
                    iBeautyOperatorListener.toCreateTrade();
                }
                break;
            case R.id.btn_create_card://办卡
                if (iBeautyOperatorListener != null) {
                    iBeautyOperatorListener.toCreateCrad();
                }
                break;
            case R.id.btn_charge://充值
                if (iBeautyOperatorListener != null) {
                    iBeautyOperatorListener.toCharge();
                }
                break;
            case R.id.btn_create_member://添加会员
                if (iBeautyOperatorListener != null) {
                    iBeautyOperatorListener.toCreateMember();
                }
                break;
            case R.id.btn_create_reserver://添加预约
                if (iBeautyOperatorListener != null) {
                    iBeautyOperatorListener.toCreateReserver();
                }
                break;
        }
    }

    @UiThread
    @Override
    public void refreshNotifyNumbers(BeautyNotifyEntity notifyEntity) {
        tv_customerNumber.setText(String.valueOf(notifyEntity.getCustomerNumber()));
        tv_reserverNumber.setText(String.valueOf(notifyEntity.getReserverNumber()));
        tv_tradeNumber.setText(String.valueOf(notifyEntity.getTradeNumber()));
        tv_memberNumber.setText(String.valueOf(notifyEntity.getMemberNumber()));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBeautyNotifyCache!=null){
            mBeautyNotifyCache.removeNotifyListener(this);
        }
    }
}
