package com.zhongmei.bty.customer.util;

import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.db.enums.Sex;

public class SexUtils {
    public static final String MALE_INT_FLAG = "1";

    public static final String FEMALE_INT_FLAG = "0";

    public static final String MALE_FLAG = "male";

    public static final String FEMALE_FLAG = "female";

    public static final String UNKNOWN_INT_FLAG = "-1";

    public static final int MALE = 1;

    public static final int FEMALE = 0;

    public static final int UNKNOWN = -1;

    public static int getSexStringId(String sex) {

        switch (getSex(sex)) {

            case MALE:
                return R.string.customer_sex_male;
            case FEMALE:
                return R.string.customer_sex_female;
            default:
                return R.string.customer_sex_unknown;
        }
    }

    public static int getSex(String sex) {
        if (!TextUtils.isEmpty(sex)) {
            if (sex.equals(MALE_INT_FLAG) || sex.equals(MALE_FLAG)) {
                return MALE;
            } else if (sex.equals(FEMALE_INT_FLAG) || sex.equals(FEMALE_FLAG)) {
                return FEMALE;
            }
        }
        return UNKNOWN;
    }

    public static String getSex(boolean isFemale) {
        return isFemale ? FEMALE_INT_FLAG + "" : MALE_INT_FLAG + "";
    }
	
	/*public static String getHonorific(Customer customer) {
		if (TextUtils.isEmpty(customer.get(Customer.NAME_KEY))) {
			return "()";
		}
		String sex;
		switch (getSex(customer.get(Customer.SEX_KEY))) {
			case MALE:
				sex = MainApplication.getInstance().getResources().getString(R.string.customer_sex_male);
				break;
			
			case FEMALE:
				sex = MainApplication.getInstance().getResources().getString(R.string.customer_sex_female);
				break;
			default:
				return "(" + customer.get(Customer.NAME_KEY) + ")";
				
		}
		return "(" + customer.get(Customer.NAME_KEY).substring(0, 1) + sex + ")";
	}*/

    public static void setSex(CustomerResp customer, CompoundButton male, CompoundButton female) {
        if (male.isChecked()) {
            customer.sex = Integer.valueOf(MALE_INT_FLAG);
//			customer.set(Customer.SEX_KEY, MALE_INT_FLAG);
        } else if (female.isChecked()) {
            customer.sex = Integer.valueOf(FEMALE_INT_FLAG);
//			customer.set(Customer.SEX_KEY, FEMALE_INT_FLAG);
        } else {
            customer.sex = Integer.valueOf(UNKNOWN_INT_FLAG);
//			customer.set(Customer.SEX_KEY, UNKNOWN_INT_FLAG);
        }
    }

    public static void setSexCompoundButton(String sex, CompoundButton male, CompoundButton female) {
        switch (getSex(sex)) {
            case MALE:
                male.setChecked(true);
                female.setChecked(false);
                return;
            case FEMALE:
                female.setChecked(true);
                male.setChecked(false);
                return;
            default:
                female.setChecked(false);
                male.setChecked(false);
                return;
        }
    }

    public static void setSex(CustomerResp customer, View male, View female) {
        if (male.isSelected()) {
            customer.sex = Integer.valueOf(MALE_INT_FLAG);
        } else if (female.isSelected()) {
            customer.sex = Integer.valueOf(FEMALE_INT_FLAG);
        } else {
            customer.sex = Integer.valueOf(UNKNOWN_INT_FLAG);
        }
    }

    public static void setSex(CustomerResp customer, Sex sex) {
        if (sex == Sex.MALE) {
            customer.sex = Integer.valueOf(MALE_INT_FLAG);
        } else if (sex == Sex.FEMALE) {
            customer.sex = Integer.valueOf(FEMALE_INT_FLAG);
        } else {
            customer.sex = Integer.valueOf(UNKNOWN_INT_FLAG);
        }
    }

    public static Sex getSexEnum(String sex) {
        if (FEMALE_INT_FLAG.equals(sex)) {
            return Sex.FEMALE;
        } else {
            return Sex.MALE;
        }
    }
}
