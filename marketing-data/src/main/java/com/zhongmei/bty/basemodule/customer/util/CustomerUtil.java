package com.zhongmei.bty.basemodule.customer.util;

import android.content.res.Resources;
import android.text.TextUtils;

import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.data.R;
import com.zhongmei.bty.basemodule.devices.mispos.enums.CardStatus;
import com.zhongmei.yunfu.context.base.BaseApplication;

public class CustomerUtil {

    public static final String SP_MEMBER_NUMBER = "sp_member_number";


    public static String getStatusName(CardStatus cardStatus) {
        Resources res = BaseApplication.sInstance.getResources();
        if (cardStatus == null) {
            return res.getString(R.string.eccard_status_all);
        }
        switch (cardStatus) {
            case UNMAKECARD:
                return res.getString(R.string.eccard_not_make_card);
            case UNSELL:
                return res.getString(R.string.eccard_unsale);
            case UNACTIVATED:
                return res.getString(R.string.eccard_unactive);
            case ACTIVATED:
                return res.getString(R.string.eccard_actived);
            case ISDISABLE:
                return res.getString(R.string.eccard_disabled);
            case ISCANCEL:
                return res.getString(R.string.eccard_invalided);
        }
        return res.getString(R.string.eccard_not_make_card);
    }


    public static int getRegistMemberNumber() {
                String memberDataStr = SharedPreferenceUtil.getSpUtil().getString(SP_MEMBER_NUMBER, "");

        if (TextUtils.isEmpty(memberDataStr)) {
            return 0;
        }

        String[] memberDataArray = memberDataStr.split(":");

        if (TextUtils.equals(memberDataArray[0], DateUtil.format(System.currentTimeMillis(), DateUtil.DEFAULT_DATE_FORMAT))) {
            return Integer.parseInt(memberDataArray[1]);
        }

        return 0;
    }


    public static void setRegistMemberNumber(int memberNumber) {
        String curDate = DateUtil.format(System.currentTimeMillis(), DateUtil.DEFAULT_DATE_FORMAT);

        SharedPreferenceUtil.getSpUtil().putString(SP_MEMBER_NUMBER, curDate + ":" + memberNumber);
    }


    public static void addRegistMemberNumber(int newMemberNumber) {
        int numberNumber = getRegistMemberNumber();
        numberNumber = numberNumber + newMemberNumber;
        setRegistMemberNumber(numberNumber);
    }

}
