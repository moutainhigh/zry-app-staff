package com.zhongmei.bty.dinner.vo;

import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.IZone;

import java.util.List;

/**
 * @version: 1.0
 * @date 2015年9月20日
 */
public class DinnertableVo {

    private final IDinnertable dinnertable;

    /**
     * 桌台上的所有单据列表
     */
    private List<DinnertableTradeVo> dinnertableTradeVos;

    private DinnertableTradeVo unionMainTradeVo;

    private List<DinnertableTradeVo> unionSubTradeVos;

    public DinnertableVo(IDinnertable dinnertable) {
        this.dinnertable = dinnertable;
    }

    public IDinnertable getDinnertable() {
        return dinnertable;
    }

    public DinnertableTradeVo getUnionMainTradeVo() {
        return unionMainTradeVo;
    }

    public void setUnionMainTradeVo(DinnertableTradeVo unionMainTradeVo) {
        this.unionMainTradeVo = unionMainTradeVo;
    }

    public List<DinnertableTradeVo> getUnionSubTradeVos() {
        return unionSubTradeVos;
    }

    public void setUnionSubTradeVos(List<DinnertableTradeVo> unionSubTradeVos) {
        this.unionSubTradeVos = unionSubTradeVos;
    }

    /**
     * 返回桌台ID
     *
     * @return
     */
    public Long getTableId() {
        return dinnertable.getId();
    }

    /**
     * 返回桌台名称
     *
     * @return
     */
    public String getTableName() {
        return dinnertable.getName();
    }

    /**
     * 返回桌台所属区域
     *
     * @return
     */
    public IZone getZone() {
        return dinnertable.getZone();
    }

    /**
     * 返回桌台的座位数
     *
     * @return
     */
    public Integer getNumberOfSeats() {
        return dinnertable.getNumberOfSeats();
    }

    public List<DinnertableTradeVo> getDinnertableTradeVos() {
        return dinnertableTradeVos;
    }

    public void setDinnertableTradeVos(List<DinnertableTradeVo> dinnertableTradeVos) {
        this.dinnertableTradeVos = dinnertableTradeVos;
    }


}
