package com.zhongmei.bty.dinner.table.view;

import android.view.MotionEvent;

import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.dinner.table.TableFragment;
import com.zhongmei.bty.dinner.table.TableInfoFragment;
import com.zhongmei.bty.dinner.vo.LoadingFinish;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.dinner.vo.SwitchTableTradeVo;


public abstract class TableFragmentBase extends BasicFragment {
    private static final String TAG = TableFragment.class.getSimpleName();

    public static final String TAG_TABLE_FRAGMENT = "tag_table_fragment";

    private LoadingFinish mLoadingFinish;


    public void init() {
        if (mLoadingFinish != null) {
            mLoadingFinish.loadingFinish();
        }
    }


    public void registerLoadingListener(LoadingFinish mLoadingFinish) {
        this.mLoadingFinish = mLoadingFinish;
    }


    protected abstract TableInfoFragment initInfoFragment();

    public abstract TableInfoFragment getInfoFragment();


    public abstract void enableDinnertableClick(boolean enable);



    public abstract void selectTrade(SwitchTableTradeVo switchTableTradeVo);


    public abstract boolean hideMoreMenuPop(MotionEvent event);


    public abstract boolean hideComboSelectView(MotionEvent event);


    public abstract BusinessType getBussinessType();
}
