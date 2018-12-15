package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.bty.basemodule.orderdish.entity.AddItemBatch;
import com.zhongmei.bty.basemodule.orderdish.entity.AddItemRecord;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class AddItemVo implements java.io.Serializable {

    //微信加菜批次信息
    private AddItemBatch mAddItemBatch;

    //微信加菜该批次下的家在信息
    private List<AddItemRecord> mAddItemRecords;

    public AddItemBatch getmAddItemBatch() {
        return mAddItemBatch;
    }

    public void setmAddItemBatch(AddItemBatch mAddItemBatch) {
        this.mAddItemBatch = mAddItemBatch;
    }

    public List<AddItemRecord> getmAddItemRecords() {
        return mAddItemRecords;
    }

    public void setmAddItemRecords(List<AddItemRecord> mAddItemRecords) {
        this.mAddItemRecords = mAddItemRecords;
    }
}
