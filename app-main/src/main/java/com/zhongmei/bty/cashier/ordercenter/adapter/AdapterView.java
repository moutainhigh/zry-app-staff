package com.zhongmei.bty.cashier.ordercenter.adapter;

import android.view.View;

import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;

/**
 * 用于{@link EsayAdapter}的View, 其实不是真正的View, 类似ViewHolder<br>
 * 用interface是方便子类使用不同的layout, 尽可能减少不必要的布局嵌套<br>
 *
 * @param <T> 与View绑定的数据类型
 * @date 2014-7-29
 */
public interface AdapterView<T extends TradeItemVo> {

    /**
     * 返回真正的View对象
     *
     * @return
     */
    public abstract View getView();

    /**
     * 将具体数据绑定到View
     *
     * @param position
     * @param data
     */
    public abstract void bindData(int position, T data);

}
