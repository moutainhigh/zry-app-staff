package com.zhongmei.yunfu.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Object的默认克隆方法主要以下3点：
 * 1、 基本类型: 如果变量是基本很类型，则拷贝其值，比如int、float等。
 * 2、 对象类型: 如果变量是一个实例对象，则拷贝其地址引用，也就是说此时新对象与原来对象是公用该实例变量
 * 3、 字符串: 若变量为String字符串，则拷贝其地址引用。但是在修改时，它会从字符串池中重新生成一个新的字符串，原有的字符串对象内容保持不变
 * 所以在类中的Clone方法里，针对对象的拷贝，需要单独进行新建赋值。
 * <p>
 * 通过Java默认序列化和反序列化的方法来进行深复制的工具类
 * 经过实际几组数据测试，发现通过json序列化和反序列化速度要快些，所以暂时采用通过json序列化来深度复制
 * 01-07 17:27:49.844  E/fanQinLin: Json深度拷贝的开始时间为:1483781269844
 * 01-07 17:27:49.848  E/fanQinLin: Json深度拷贝的结束时间为:1483781269848
 * 01-07 17:27:49.848  E/fanQinLin: Java默认深度拷贝的开始时间为:1483781269848
 * 01-07 17:27:49.859  E/fanQinLin: Java默认深度拷贝的结束时间为:1483781269859
 * 01-07 17:28:04.569  E/fanQinLin: Json深度拷贝的开始时间为:1483781284569
 * 01-07 17:28:04.587  E/fanQinLin: Json深度拷贝的结束时间为:1483781284587
 * 01-07 17:28:04.587  E/fanQinLin: Java默认深度拷贝的开始时间为:1483781284587
 * 01-07 17:28:04.650  E/fanQinLin: Java默认深度拷贝的结束时间为:1483781284650
 */
public class DeepCloneUtils {

    /**
     * @param <T>
     * @param obj
     * @return
     */
    public static <T extends Serializable> T deepClone(T obj) {
        T cloneObj = null;
        try {
            // 构造Byte输出流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 将对象写入到输出流
            ObjectOutputStream obs = new ObjectOutputStream(out);
            obs.writeObject(obj);
            obs.close();

            // 从Byte输出流中获取数据,构造输入流
            ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
            // 根据输入流转化成对象
            ObjectInputStream ois = new ObjectInputStream(ios);
            cloneObj = (T) ois.readObject();

            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloneObj;
    }

    /**
     * @param objList
     * @return
     */
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
