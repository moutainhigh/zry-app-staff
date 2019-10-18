package com.zhongmei.bty.basemodule.shopmanager.paymentmanager.entitys;

import java.io.Serializable;
import java.util.List;


public class AccountSubjectVo implements Serializable {

    private List<SubjectInfo> mInComeSubject;
    private List<SubjectInfo> mExpensesSubject;
    public List<SubjectInfo> getmInComeSubject() {
        return mInComeSubject;
    }

    public void setmInComeSubject(List<SubjectInfo> mInComeSubject) {
        this.mInComeSubject = mInComeSubject;
    }

    public List<SubjectInfo> getmExpensesSubject() {
        return mExpensesSubject;
    }

    public void setmExpensesSubject(List<SubjectInfo> mExpensesSubject) {
        this.mExpensesSubject = mExpensesSubject;
    }
}
