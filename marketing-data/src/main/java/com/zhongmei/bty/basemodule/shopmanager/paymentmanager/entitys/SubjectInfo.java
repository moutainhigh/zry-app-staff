package com.zhongmei.bty.basemodule.shopmanager.paymentmanager.entitys;


import com.zhongmei.bty.commonmodule.data.db.AccountSubject;

import java.io.Serializable;
import java.util.List;


public class SubjectInfo implements Serializable {

    private AccountSubject mSubject;

    private List<PaymentsPayInfo> mPayInfos;

    private boolean isParent;

    private List<SubjectInfo> childSubInfo;

    public AccountSubject getmSubject() {
        return mSubject;
    }

    public void setmSubject(AccountSubject mSubject) {
        this.mSubject = mSubject;
    }


    public List<PaymentsPayInfo> getmPayInfos() {
        return mPayInfos;
    }

    public void setmPayInfos(List<PaymentsPayInfo> mPayInfos) {
        this.mPayInfos = mPayInfos;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public List<SubjectInfo> getChildSubInfo() {
        return childSubInfo;
    }

    public void setChildSubInfo(List<SubjectInfo> childSubInfo) {
        this.childSubInfo = childSubInfo;
    }
}
