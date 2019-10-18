package com.zhongmei.bty.common.util;

import java.io.Serializable;
import java.util.Comparator;

import com.zhongmei.bty.splash.login.UserGridItem;


public class PinyinComparator implements Comparator<UserGridItem>, Serializable {

    public int compare(UserGridItem o1, UserGridItem o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
