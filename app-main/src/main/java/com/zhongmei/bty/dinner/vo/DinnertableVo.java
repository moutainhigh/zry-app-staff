package com.zhongmei.bty.dinner.vo;

import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.IZone;

import java.util.List;


public class DinnertableVo {

    private final IDinnertable dinnertable;


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


    public Long getTableId() {
        return dinnertable.getId();
    }


    public String getTableName() {
        return dinnertable.getName();
    }


    public IZone getZone() {
        return dinnertable.getZone();
    }


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
