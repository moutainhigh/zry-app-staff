package com.zhongmei.bty.data.operates;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;

import com.zhongmei.bty.takeout.sender.vo.SenderSearcheVo;
import com.zhongmei.bty.entity.vo.SenderVo;

/**
 * @Date：2015-5-28 上午9:51:28
 * @Description: TODO
 * @Version: 1.0
 */
public interface SenderDal extends IOperates {

    public List<SenderVo> getAllSenderOffUser();//获取所有外卖员

    //public double getUnSquareUpAmount();//获取本机未清账金额

    List<SenderSearcheVo> getSearchVoList();//获取搜索列表

}
