package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.basemodule.erp.bean.ErpMessagePushDetail;

import java.util.List;

/**
 * Desc
 *
 * @created 2017/9/14
 */
public interface ErpMessagePushDetailDal extends IOperates {

    List<ErpMessagePushDetail> findAll();

    List<ErpMessagePushDetail> findByMsgNotice();

    void updateReadLocal(Long id);

    void update(ErpMessagePushDetail erpMessagePushDetail);
}
