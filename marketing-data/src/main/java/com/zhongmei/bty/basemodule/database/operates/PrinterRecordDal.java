package com.zhongmei.bty.basemodule.database.operates;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.commonmodule.database.entity.local.PrinterRecord;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 打印错误纪录操作接口
 */

public interface PrinterRecordDal extends IOperates {

    /**
     * 新增打印出错信息
     *
     * @return
     */
    boolean insertPrintRecords(List<PrinterRecord> printRecords);

    /**
     * 查询所有的打印出错纪录
     *
     * @return
     */
    List<PrinterRecord> queryAllPrintRecord();

    /**
     * 删除打印纪录
     *
     * @param uuid
     * @return
     */
    boolean deletePrintRecord(String uuid);

}
