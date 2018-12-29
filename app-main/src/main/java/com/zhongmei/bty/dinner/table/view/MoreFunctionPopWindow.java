package com.zhongmei.bty.dinner.table.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.commonbusiness.message.BusinessChargeReq;
import com.zhongmei.bty.basemodule.commonbusiness.message.BusinessChargeResp;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.util.DinnerMobClickAgentEvent;
import com.zhongmei.bty.dinner.adapter.DinnerTableFunctionAdapter;
import com.zhongmei.bty.dinner.table.bean.FunctionBean;
import com.zhongmei.bty.dinner.table.event.EventFunctionChange;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.data.MindTransferResp;
import com.zhongmei.yunfu.resp.data.TransferReq;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * Created by demo on 2018/12/15
 */
public class MoreFunctionPopWindow extends PopupWindow implements View.OnTouchListener, AdapterView.OnItemClickListener, DinnerTableFunctionAdapter.OnFunctionChangeListener, View.OnClickListener {

    private Context mContext;
    private static MoreFunctionPopWindow mMoreFunctionPopuWindow;

    private SpreadListView slv_function;


    private LinearLayout layout_dataContainer;
    private TextView tv_checkoutFee;
    private TextView tv_unCheckoutFee;
    private TextView tv_peopleAvgFee;
    private TextView tv_tableAvgFee;
    private LinearLayout layout_controller;
    private ProgressBar pb_loading;
    private Button btn_getBussinessData;

    private DinnerTableFunctionAdapter mFunctionAdapter;
    private BusinessType mBusinessType;


    private void setContext(Context context) {
        this.mContext = context;
    }

    public int getCheckNum() {
        if (mFunctionAdapter != null) {
            return mFunctionAdapter.getCheckFunctionNum();
        }
        return 0;
    }

    public int getTotalNum() {
        if (mFunctionAdapter != null) {
            return mFunctionAdapter.getCount();
        }
        return 0;
    }

    public MoreFunctionPopWindow(Context context, BusinessType businessType) {
        super(context);
        //初始化popuwindow
        this.mContext = context;
        this.mBusinessType = businessType;
        initView(context);
        initData(context);
    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popuwindow_dinner_morefunction, null);
        setContentView(view);
        setWidth(DensityUtil.dip2px(MainApplication.getInstance(), 210f));
        setHeight(DensityUtil.dip2px(MainApplication.getInstance(), 240f));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_dinner_table_function));
        setFocusable(false);

        slv_function = (SpreadListView) view.findViewById(R.id.slv_function);
        slv_function.setOnItemClickListener(this);
        layout_dataContainer = (LinearLayout) view.findViewById(R.id.layout_data_contain);
        tv_checkoutFee = (TextView) view.findViewById(R.id.tv_checkout_fee);
        tv_unCheckoutFee = (TextView) view.findViewById(R.id.tv_uncheckout_fee);
        tv_peopleAvgFee = (TextView) view.findViewById(R.id.tv_people_avgfee);
        tv_tableAvgFee = (TextView) view.findViewById(R.id.tv_table_avgfee);
        layout_controller = (LinearLayout) view.findViewById(R.id.layout_controller);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        btn_getBussinessData = (Button) view.findViewById(R.id.btn_get_business_data);
        RelativeLayout gotoSearchTableTV = (RelativeLayout) view.findViewById(R.id.tv_goto_search_table);
        btn_getBussinessData.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        gotoSearchTableTV.setOnClickListener(this);
        btn_getBussinessData.setOnClickListener(this);

        setOutsideTouchable(false);
    }

    private void initData(Context context) {
        String[] functions = context.getResources().getStringArray(R.array.dinner_table_fnction);
        List<FunctionBean> mList = new ArrayList<>();
        for (String functionItem : functions) {
            String[] function = functionItem.split(":");
            mList.add(new FunctionBean(Integer.parseInt(function[0]), function[1]));
        }
        mFunctionAdapter = new DinnerTableFunctionAdapter(context, mList);
        mFunctionAdapter.setFunctionChangeListener(this);
        slv_function.setAdapter(mFunctionAdapter);

//       EventBus.getDefault().post(new EventFunctionChange(null,false,mFunctionAdapter.getCheckFunctionNum(),mFunctionAdapter.getCount()));
    }

    public void initItemChecked(Set<Integer> checkItems) {
        if (mFunctionAdapter != null) {
            mFunctionAdapter.setCacheItems(checkItems);
            mFunctionAdapter.notifyDataSetChanged();
        }

        setLoading(false);
    }


    public void show(View parentView) {
        if (parentView != null) {
            if (!isShowing()) {
                showAsDropDown(parentView, DensityUtil.dip2px(MainApplication.getInstance(), -62), 0);
            }

        }
    }

    public void hide() {
        if (isShowing()) {
            dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mFunctionAdapter.onItemClick(position);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isShowing()) {
            dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onFunctionChange(FunctionBean function, boolean isCheck) {
        EventFunctionChange event = new EventFunctionChange(function, isCheck, mFunctionAdapter.getCheckFunctionNum(), mFunctionAdapter.getCount());
        EventBus.getDefault().post(event);
    }


    /**
     * 数据获取
     */
    private void getBusinessData() {
        setLoading(true);
        //数据获取
        TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        ResponseListener<MindTransferResp<BusinessChargeResp>> listener = new ResponseListener<MindTransferResp<BusinessChargeResp>>() {
            @Override
            public void onResponse(ResponseObject<MindTransferResp<BusinessChargeResp>> response) {
                if (ResponseObject.isOk(response) && MindTransferResp.isOk(response.getContent())) {
                    showBusinessData(response.getContent().getData());
                } else {
                    ToastUtil.showShortToast(response.getContent() != null ? response.getContent().getMessage() : response.getMessage());
                    setLoading(false);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
                setLoading(false);
            }
        };

        TransferReq<BusinessChargeReq> businessChargeReq = createGoodlsSellRankReq();
        tradeOperates.getBusinessCharge(businessChargeReq, listener);
    }

    /**
     * 设置家在UI
     *
     * @param loading
     */
    private void setLoading(boolean loading) {
        layout_controller.setVisibility(View.VISIBLE);
        layout_dataContainer.setVisibility(View.GONE);
        if (loading) {
            btn_getBussinessData.setVisibility(View.GONE);
            pb_loading.setVisibility(View.VISIBLE);
        } else {
            btn_getBussinessData.setVisibility(View.VISIBLE);
            pb_loading.setVisibility(View.GONE);
        }
    }


    /**
     * 显示试试概况数据
     */
    private void showBusinessData(BusinessChargeResp resp) {
        layout_dataContainer.setVisibility(View.VISIBLE);
        layout_controller.setVisibility(View.GONE);

        String checkoutFee = String.format(mContext.getString(R.string.dinner_table_checkout_fee), Utils.formatPrice(resp.getBusinessAmount().doubleValue()));
        String uncheckoutFee = String.format(mContext.getString(R.string.dinner_table_uncheckout_fee), Utils.formatPrice(resp.getUnGetAmount().doubleValue()));
        String peopleAvgFee = String.format(mContext.getString(R.string.dinner_table_people_avgfee), Utils.formatPrice(resp.getPersonAvg().doubleValue()));
        String tableAvgFee = String.format(mContext.getString(R.string.dinner_table_table_avgfee), Utils.formatPrice(resp.getTableAvg().doubleValue()));

        tv_checkoutFee.setText(checkoutFee);
        tv_unCheckoutFee.setText(uncheckoutFee);
        tv_peopleAvgFee.setText(peopleAvgFee);
        tv_tableAvgFee.setText(tableAvgFee);
    }

    /**
     * 创建实时概况请求参数
     *
     * @return
     */
    private TransferReq<BusinessChargeReq> createGoodlsSellRankReq() {
        TransferReq<BusinessChargeReq> businessChargeReq = new TransferReq<BusinessChargeReq>();

        BusinessChargeReq req = new BusinessChargeReq();
        //设置请求数据
        req.setBrandId(MainApplication.getInstance().getBrandIdenty());
        req.setShopId(MainApplication.getInstance().getShopIdenty());
        businessChargeReq.setPostData(req);
        businessChargeReq.setUrl("/mind/innerApi/fs/desktopBusinessStat");


        return businessChargeReq;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_goto_search_table:
                MobclickAgentEvent.onEvent(mContext, DinnerMobClickAgentEvent.tableMoreFunctionSearchTable);
                //Intent intent = new Intent(mContext, SearchTableActivity_.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("BusinessType", mBusinessType);
                //intent.putExtras(bundle);
                //mContext.startActivity(intent);
                this.dismiss();
                break;
            case R.id.btn_get_business_data:
                //权限验证
                MobclickAgentEvent.onEvent(mContext, DinnerMobClickAgentEvent.tableMoreFunctionBusinessInfo);
                VerifyHelper.verifyAlert((FragmentActivity) mContext, DinnerApplication.PERMISSION_DINNER_BUSINESS_CHARGE,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                getBusinessData();
                            }
                        });
                break;
            default:
                break;
        }
    }
}
