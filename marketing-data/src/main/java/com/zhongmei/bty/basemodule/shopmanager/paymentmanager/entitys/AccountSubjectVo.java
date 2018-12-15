package com.zhongmei.bty.basemodule.shopmanager.paymentmanager.entitys;

import java.io.Serializable;
import java.util.List;

/**
 * @Date： 2016/7/20
 * @Description:收支管理Vo
 * @Version: 1.0
 */
public class AccountSubjectVo implements Serializable {

    private List<SubjectInfo> mInComeSubject;//收入科目项

    private List<SubjectInfo> mExpensesSubject;//支出科目项

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
