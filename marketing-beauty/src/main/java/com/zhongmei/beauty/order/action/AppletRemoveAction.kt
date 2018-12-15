package com.zhongmei.beauty.order.action

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase

/**

 */
class AppletRemoveAction {
    var mShopcartItem: IShopcartItemBase

    constructor(shpcartItem: IShopcartItemBase) {
        mShopcartItem = shpcartItem
    }
}