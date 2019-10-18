package com.zhongmei.bty.dinner.table.manager;

import android.support.v4.app.Fragment;

import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.buffet.table.view.BuffetUnionCancelVo;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.dinner.vo.DinnerConnectTablesVo;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class IUnionTrade {

    protected WeakReference<Fragment> mWeakReference;
    protected BusinessType businessType;
    protected OnInterceptRequest interceptRequest;

    public static IUnionTrade newUnion(Fragment fragment, BusinessType businessType) {
        IUnionTrade result;
        switch (businessType) {
            case BUFFET:
                result = new BuffetUnionTrade();
                break;
            case DINNER:
            default:
                result = new DinnerUnionTrade();
                break;
        }

        result.mWeakReference = new WeakReference<>(fragment);
        result.businessType = businessType;
        return result;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public IUnionTrade setInterceptRequest(OnInterceptRequest interceptRequest) {
        this.interceptRequest = interceptRequest;
        return this;
    }


    public abstract void create(List<DinnerConnectTablesVo> tablesVoList, UnionListener listener);


    public abstract void cancel(List<DinnerConnectTablesVo> tablesVoList, BuffetUnionCancelVo unionCancelVo, UnionListener listener);


    protected void setResponse(UnionListener listener, Throwable error, ResponseObject<?> result) {
        if (mWeakReference.get() != null) {
            if (listener != null) {
                listener.onResponse(error, result);
            }
        }
    }

    public interface UnionListener {
        void onResponse(Throwable error, ResponseObject<?> result);
    }

    public interface OnInterceptRequest {
        boolean onInterceptRequest(IUnionTrade target, TradeVo mainTradeVo, TradeVo subTradeVo);
    }
}
