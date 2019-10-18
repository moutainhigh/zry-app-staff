package com.zhongmei.yunfu.data.util;

import java.util.ArrayList;
import java.util.Collections;



public class Lists {

    public static <E> ArrayList<E> newArrayList(E... elements) {
        ArrayList<E> list = new ArrayList();
        Collections.addAll(list, elements);
        return list;
    }
}
