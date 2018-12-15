package com.zhongmei.bty.customer.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import android.text.TextUtils;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;

public class EnjoyManager {
    private static final String[] sEnjoyName = MainApplication.getInstance().getResources().getStringArray(R.array.taste);

    private static EnjoyManager sInstance = new EnjoyManager();

    HashMap<String, String> mEnjoys = new HashMap<String, String>();

    HashMap<String, String> mReverseEnjoys = new HashMap<String, String>();

    private EnjoyManager() {
        for (int i = 0; i < sEnjoyName.length; i++) {
            mEnjoys.put(String.format("%04d", i), sEnjoyName[i]);
            mReverseEnjoys.put(sEnjoyName[i], String.format("%04d", i));
        }
    }

    public static EnjoyManager getInstance() {
        return sInstance;
    }

    public HashMap<String, String> getAllEnjoys() {
        return mEnjoys;
    }

    public HashMap<String, String> getReverseEnjoys() {
        return mReverseEnjoys;
    }

    public ArrayList<String> getAllList() {

        ArrayList<String> list = new ArrayList<String>();
        for (String key : getAllEnjoys().keySet()) {
            list.add(getAllEnjoys().get(key));
        }
        return list;

    }

    public String getDtailEnjoyString(String hoddy) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(hoddy)) {
            String[] enjoyIds = hoddy.split(",");
            for (int i = 0; i < enjoyIds.length; i++) {
                sb.append(mEnjoys.get(enjoyIds[i])).append(",");
            }
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    public HashSet<String> getDtailEnjoy(String hoddy) {
        HashSet<String> enjoys = new HashSet<String>();

        if (!TextUtils.isEmpty(hoddy)) {
            String[] enjoyIds = hoddy.split(",");
            for (int i = 0; i < enjoyIds.length; i++) {
                enjoys.add(mEnjoys.get(enjoyIds[i]));
            }
        }
        return enjoys;
    }

    public String tohoddyString(ArrayList<String> enjoys) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < enjoys.size(); i++) {
            sb.append(mReverseEnjoys.get(enjoys.get(i)) + ",");
        }
        if (enjoys.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }

        return sb.toString();
    }

    public ArrayList<String> getEnjoylist(String hoddy) {
        ArrayList<String> enjoys = new ArrayList<String>();

        if (!TextUtils.isEmpty(hoddy)) {
            String[] enjoyIds = hoddy.split(",");
            for (int i = 0; i < enjoyIds.length; i++) {
                enjoys.add(mEnjoys.get(enjoyIds[i]));
            }
        }
        return enjoys;
    }

    public String hoddytoString(ArrayList<String> enjoys) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < enjoys.size(); i++) {
            sb.append(enjoys.get(i) + ",");
        }
        if (enjoys.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }

        return sb.toString();
    }
}
