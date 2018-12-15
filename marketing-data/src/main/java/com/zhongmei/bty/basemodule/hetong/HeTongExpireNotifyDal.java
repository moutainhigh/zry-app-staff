
package com.zhongmei.bty.basemodule.hetong;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.commonmodule.database.entity.local.ContractOverdue;

import java.sql.SQLException;
import java.util.List;

public interface HeTongExpireNotifyDal extends IOperates {


    /**
     * 插入合同到期的数据
     *
     * @param contractOverdue
     * @throws SQLException
     */
    void insertMsgs(ContractOverdue contractOverdue) throws SQLException;

    /**
     * 取得未清除的合同到期消息
     *
     * @return
     */
    List<ContractOverdue> getUnClearMsg() throws SQLException;

//	/**
//	 * 设置状态为清除
//	 * @param list
//	 * @throws SQLException
//	 */
//	void clear( List<ContractOverdue> list) throws SQLException ;

    /**
     * 物理清除所有数据;
     *
     * @throws SQLException
     */
    void clearAll() throws SQLException;


    /**
     * 插入新数据或者更新
     *
     * @throws SQLException
     */
    void update(ContractOverdue item) throws SQLException;


    /**
     * 过滤掉已有数据;
     *
     * @param list
     * @return
     * @throws SQLException
     */
    void filterData(List<ContractOverdue> list) throws SQLException;


    /**
     * 删除数据
     *
     * @param contractOverdue
     * @throws SQLException
     */
    void deleteData(ContractOverdue contractOverdue) throws SQLException;
}

