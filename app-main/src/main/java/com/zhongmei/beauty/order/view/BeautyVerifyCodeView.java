package com.zhongmei.beauty.order.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhongmei.beauty.operates.BeautyCustomerOperates;
import com.zhongmei.beauty.operates.message.BeautyActivityBuyRecordResp;
import com.zhongmei.beauty.order.util.BeautyAppletTool;
import com.zhongmei.beauty.order.util.IChangeMiddlePageListener;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.data.LoadingYFResponseListener;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.beauty_order_middle_view_verify_code)
public class BeautyVerifyCodeView extends LinearLayout {

    @ViewById(R.id.et_code)
    protected EditText et_code;

    @ViewById(R.id.btn_check)
    protected Button btn_check;


    private FragmentManager mFragmentManager;

    private ChangePageListener mChangePageListener;
    private IChangeMiddlePageListener mChangeMiddlePageListener;

    public BeautyVerifyCodeView(Context context,ChangePageListener pageListener,IChangeMiddlePageListener middlePageListener,FragmentManager fm) {
        super(context);
        this.mChangePageListener=pageListener;
        this.mChangeMiddlePageListener=middlePageListener;
        this.mFragmentManager=fm;
    }

    @Click({R.id.btn_check})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_check:
                if(!ClickManager.getInstance().isClicked(R.id.btn_check)){
                    checkCode();
                }
                break;
        }

    }


    private void checkCode(){
        String couponCode=et_code.getText().toString().trim();

        if(TextUtils.isEmpty(couponCode)){
            ToastUtil.showShortToast(R.string.input_coupon_code);
            return;
        }


        checkCode(couponCode);
    }


    private void checkCode(String code){
                YFResponseListener listener=new YFResponseListener<YFResponse<BeautyActivityBuyRecordResp>>(){

            @Override
            public void onResponse(YFResponse<BeautyActivityBuyRecordResp> response) {
                if(response.isOk()){
                                        addActivityToCart(response.getContent());
                }else{
                    ToastUtil.showShortToast(response.getMessage());
                }

            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(R.string.rquest_error);
            }
        };

        BeautyCustomerOperates beautyOperate = OperatesFactory.create(BeautyCustomerOperates.class);
        beautyOperate.getActivityByCode(code, LoadingYFResponseListener.ensure(listener,mFragmentManager));
    }


    private void addActivityToCart(BeautyActivityBuyRecordResp activity){
        if(activity==null){
            ToastUtil.showShortToast(R.string.obtain_no_coupons);
            return;
        }

        if(!BeautyAppletTool.Companion.isAddtoShopcart(activity)){
            BeautyAppletTool.Companion.addDishToShopcart(activity, mChangePageListener, mChangeMiddlePageListener, getContext());
            ToastUtil.showShortToast(R.string.activity_in_cart);
        }else{
            ToastUtil.showShortToast(R.string.coupon_be_used);
        }

        et_code.setText("");
    }

}
