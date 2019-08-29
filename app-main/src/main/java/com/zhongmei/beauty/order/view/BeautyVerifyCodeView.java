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

    /**
     * 检查卷码
     */
    private void checkCode(){
        String couponCode=et_code.getText().toString().trim();

        if(TextUtils.isEmpty(couponCode)){
            ToastUtil.showShortToast("请输入券码！");
            return;
        }

//       不验证长度的原因，怕后期长度更改
//        if(couponCode.length()!=6){
//            ToastUtil.showShortToast("请输入正确的券码！");
//            return;
//        }

        checkCode(couponCode);
    }

    /**
     * 根据券码获取活动详情
     * @param code
     */
    private void checkCode(String code){
        //网络请求
        YFResponseListener listener=new YFResponseListener<YFResponse<BeautyActivityBuyRecordResp>>(){

            @Override
            public void onResponse(YFResponse<BeautyActivityBuyRecordResp> response) {
                if(response.isOk()){
                    //需要将活动加入到购物车
                    addActivityToCart(response.getContent());
                }
                ToastUtil.showShortToast(response.getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast("请求数据异常！");
            }
        };

        BeautyCustomerOperates beautyOperate = OperatesFactory.create(BeautyCustomerOperates.class);
        beautyOperate.getActivityByCode(code, LoadingYFResponseListener.ensure(listener,mFragmentManager));
    }

    /**
     * 添加活动到购物车
     * @param activity
     */
    private void addActivityToCart(BeautyActivityBuyRecordResp activity){
        if(activity==null){
            ToastUtil.showShortToast("没有获取到可用的券！");
            return;
        }

        if(!BeautyAppletTool.Companion.isAddtoShopcart(activity)){
            BeautyAppletTool.Companion.addDishToShopcart(activity, mChangePageListener, mChangeMiddlePageListener, getContext());
        }else{
            ToastUtil.showShortToast("该券已使用！");
        }

        et_code.setText("");
    }

}
