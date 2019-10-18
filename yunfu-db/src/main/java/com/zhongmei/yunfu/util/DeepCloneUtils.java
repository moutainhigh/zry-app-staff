package com.zhongmei.yunfu.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DeepCloneUtils {


    public static <T extends Serializable> T deepClone(T obj) {
        T cloneObj = null;
        try {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        ObjectOutputStream obs = new ObjectOutputStream(out);
            obs.writeObject(obj);
            obs.close();

                        ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
                        ObjectInputStream ois = new ObjectInputStream(ios);
            cloneObj = (T) ois.readObject();

            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloneObj;
    }


    public static <T> List<T> deepClone(List<T> objList) {
        List<T> copyObjList = new ArrayList<>();
        if (EmptyUtils.isEmpty(objList)) return copyObjList;
        try {
            T[] objArray = (T[]) objList.toArray();
            T[] copyObjArray = deepClone(objArray);
            if (copyObjArray != null) {
                copyObjList = Arrays.asList(copyObjArray);
            }
        } catch (Exception e) {
            copyObjList = new ArrayList<>();
        }
        return copyObjList;
    }


}
