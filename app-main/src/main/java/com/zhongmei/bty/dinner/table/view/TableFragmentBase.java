package com.zhongmei.bty.dinner.table.view;

import android.view.MotionEvent;

import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.dinner.table.TableFragment;
import com.zhongmei.bty.dinner.table.TableInfoFragment;
import com.zhongmei.bty.dinner.vo.LoadingFinish;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.dinner.vo.SwitchTableTradeVo;

/**
 * Created by demo on 2018/12/15
 * tableFragment基础类
 */
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

    /**
     * @Description:是否允许桌台点击
     */
    public abstract void enableDinnertableClick(boolean enable);


    /**
     * 选中指定订单
     *
     * @Title: selectTrade
     * @Return void 返回类型
     */
    public abstract void selectTrade(SwitchTableTradeVo switchTableTradeVo);

    /**
     * 隐藏更多功能菜单
     *
     * @param event
     * @return
     */
    public abstract boolean hideMoreMenuPop(MotionEvent event);

    /**
     * 隐藏套餐选择框
     *
     * @param event
     * @return
     */
    public abstract boolean hideComboSelectView(MotionEvent event);

    /**
     * 获取当前的业务类型
     *
     * @return
     */
    public abstract BusinessType getBussinessType();
}
