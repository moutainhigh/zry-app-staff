package com.zhongmei.bty.splash.check;

import android.content.Context;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.init.sync.Check;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryCacheUtil;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryInfo;
import com.zhongmei.bty.data.operates.message.content.InventoryInfoResp;
import com.zhongmei.bty.data.operates.message.content.InventorySetResp;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.manager.InventoryManager;

import java.util.List;
import java.util.Set;

/**
 * @Date： 2017/3/1
 * @Description:检查库存开关
 * @Version: 1.0
 */
public class InventoryCheck extends Check {

    private InventoryManager mInventoryManager;

    private InventoryCacheUtil mInventoryCacheUtil;

    public InventoryCheck(Context context) {
        super(context, context.getString(R.string.check_inventory_data), true);
        mInventoryManager = InventoryManager.getInstance();
        mInventoryCacheUtil = InventoryCacheUtil.getInstance();
    }

    @Override
    public void running(Set<String> modules) {
        update(mContext.getString(R.string.update_inventory_data));
        mInventoryManager.getInventorySet(setRespResponseListener);
    }

    /**
     * 库存开关responseListener
     */
    private ResponseListener<InventorySetResp> setRespResponseListener = new EventResponseListener<InventorySetResp>(UserActionEvent.INIT_PROCESS) {
        public boolean isOk(ResponseObject<InventorySetResp> response) {
            if (ResponseObject.isOk(response)) {
                InventorySetResp content = response.getContent();
                if (content != null && content.isSuccess()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onResponse(ResponseObject<InventorySetResp> response) {
            if (isOk(response)) {
                InventorySetResp content = response.getContent();
                if (content != null && content.getSaleNumOpen() != null) {
                    mInventoryCacheUtil.setSaleNumOpenSwitch(content.getSaleNumOpen().intValue());
                }
                if (content.getShowSaleVal() == InventoryCacheUtil.SWITCH_OPEN) {//实时库存开关打开
                    if (!mInventoryCacheUtil.getSaleSwitch()) {//缓存中实时库存没有打开，说明开关发生改变
                        mInventoryManager.getInentoryInfo(null, infoRespResponseListener);
                    } else {
                        //开关没发生改变、判断缓存中数据是否还存在
                        if (mInventoryCacheUtil.getAllInventoryData() != null
                                && mInventoryCacheUtil.getAllInventoryData().size() == 0) {
                            mInventoryManager.getInentoryInfo(null, infoRespResponseListener);
                        } else {
                            success(mContext.getString(R.string.check_inventory_finish));
                        }
                    }
                    //缓存实时库存开关
                    mInventoryCacheUtil.setSaleSwitch(content.getShowSaleVal());
                    mInventoryCacheUtil.setAutoClearSwitch(content.getAutoClearStatus());
                } else {
                    if (mInventoryCacheUtil.getSaleSwitch()) {//缓存中实时库存打开，说明开关发生改变
                        mInventoryCacheUtil.clear();
                    }
                    mInventoryCacheUtil.setSaleSwitch(content.getShowSaleVal());
                    success(mContext.getString(R.string.check_inventory_finish));
                }
            } else {
                error(mContext.getString(R.string.check_inventory_error));
            }
        }

        @Override
        public void onError(VolleyError error) {
            error(mContext.getString(R.string.check_inventory_error) + error.getMessage());
        }
    };

    /**
     * 库存数据responseListener
     */
    private ResponseListener<InventoryInfoResp> infoRespResponseListener = new EventResponseListener<InventoryInfoResp>(UserActionEvent.INIT_PROCESS) {
        @Override
        public void onResponse(ResponseObject<InventoryInfoResp> response) {
            if (ResponseObject.isOk(response)) {
                InventoryInfoResp content = response.getContent();
                if (content != null && content.isSuccess()) {
                    List<InventoryInfo> inventoryInfos = content.getData();
                    mInventoryCacheUtil.setInventoryData(inventoryInfos);
                    mInventoryCacheUtil.setUpdateTime(content.getUpdateTime());//缓存最新时间
                }
                success(mContext.getString(R.string.check_inventory_finish));
            } else {
                error(mContext.getString(R.string.check_inventory_error));
            }
        }

        @Override
        public void onError(VolleyError error) {
            error(mContext.getString(R.string.check_inventory_error) + error.getMessage());
        }
    };
}
