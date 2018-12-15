package com.zhongmei.bty.basemodule.trade.operates;

import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.trade.bean.DinnerTableInfo;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.SnackTableVo;
import com.zhongmei.bty.basemodule.trade.bean.TableAreaPopupVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TableType;
import com.zhongmei.bty.basemodule.trade.message.ClearDinnertableResp;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.resp.ResponseListener;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @version: 1.0
 * @date 2015年7月27日
 */
public interface TablesDal extends IOperates {

    List<TableType> listTableType() throws Exception;

    /**
     * 捞取所有有效的桌台区域
     *
     * @return
     * @throws Exception
     */
    List<CommercialArea> listTableArea() throws Exception;

    List<Tables> listTables() throws Exception;

    List<TableSeat> listTableSeats() throws Exception;

    List<TableSeat> listTableSeatsByTableId(Long tableId) throws Exception;

    /**
     * 查询有对应物理布局信息的桌台(不限制桌台是否可预订)
     *
     * @Title: listPhysicalLayoutTables
     * @Param @return
     * @Return List<Tables> 返回类型
     */
    List<Tables> listAllPhysicalLayoutTables() throws SQLException;

    /**
     * 查询符合正餐展示条件的区域
     *
     * @return 返回对应的区域列表
     * @throws Exception
     */
    List<CommercialArea> listDinnerArea() throws Exception;

    /**
     * 根据桌台状态查询符合正餐展示条件的桌台
     *
     * @return 返回对应的台列表
     * @throws Exception
     */
    List<Tables> listDinnerEmptyTablesByStatus(TableStatus tableStatus) throws Exception;

    /**
     * 列出某区域下的所有桌台
     *
     * @param areaId 区域ID
     * @return
     * @throws Exception
     */
    List<Tables> listTablesByAreaId(Long areaId) throws Exception;

    List<Tables> listTables(Long tableTypeId) throws Exception;

    Collection<DinnertableWrapper> listDinnertables() throws Exception;

    /**
     * 清台。此方法不阻塞调用线程
     *
     * @param dinnertable
     * @param listener
     */
    void clearDinnertable(IDinnertable dinnertable, ResponseListener<ClearDinnertableResp> listener);

    /**
     * 通过tableID查找区域
     *
     * @param tableId
     * @return
     */
    CommercialArea listArea(Long tableId) throws Exception;

    Map<CommercialArea, List<Tables>> listAreaByTableId(Long... tableId) throws Exception;

    /**
     * @param tabelId
     * @return
     */
    Tables findTablesById(Long tabelId) throws Exception;

    /**
     * @param areaId
     * @return
     * @throws Exception
     */
    CommercialArea findAreaById(Long areaId) throws Exception;

    List<CommercialArea> findAreaById(List<Long> areaId) throws Exception;

    /**
     * @param tabelId
     * @return
     * @throws Exception
     */
    int countTotalPersonCount(Long tabelId) throws Exception;

    /**
     * 通过单据获取桌台数据
     *
     * @return
     */
    DinnerTableInfo getDinnerTableByTradeVo(TradeVo tradeVo) throws Exception;

    /**
     * 查询桌台区域
     *
     * @return
     */
    List<TableAreaPopupVo> findAllTableAreaVo() throws Exception;

    /**
     * 查询快餐桌台(包括桌台区域、桌台、桌台上Trade)
     */
    List<SnackTableVo> findSnackTableVo(boolean haveLimitServiceTime) throws Exception;

    /**
     * 获取桌台上的所有订单
     *
     * @return
     * @throws SQLException
     * @Title: findTradesInTable
     * @Return void 返回类型
     */
    List<Trade> findTradesInTable(TradeTable tradeTable) throws SQLException;


    //根据桌台id及状态查询桌台
    public List<Tables> findTablesByIds(List<Long> tableIds, TableStatus status) throws Exception;

    /**
     * 通过状态查找桌台
     */
    List<Tables> listTables(TableStatus tableStatus) throws Exception;

    List<Tables> listValidTables() throws Exception;

    List<TradeTable> listTradeTable(List<Long> tradeIdList) throws Exception;

    long countOfTablesByPeopleCount(int minPeopleCount, int maxPeopleCount) throws SQLException;

    /**
     * @version: 1.0
     * @date 2015年9月19日
     */
    class DinnertableWrapper {

        public static final int MIN_WIDTH = 105;

        public static final int MIN_HEIGHT = 105;

        private String uuid;//桌子uuid

        private Long id;

        private String name;

        private Long serverUpdateTime;

        private int x;

        private int y;

        private int width;

        private int height;

        private Integer numberOfSeats;

        private Long zoneId;

        private String zoneCode;

        private String zoneName;

        private TableStatus tableStatus;

        private List<TableSeat> tableSeats;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getServerUpdateTime() {
            return serverUpdateTime;
        }

        public void setServerUpdateTime(Long serverUpdateTime) {
            this.serverUpdateTime = serverUpdateTime;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public Integer getNumberOfSeats() {
            return numberOfSeats;
        }

        public void setNumberOfSeats(Integer numberOfSeats) {
            this.numberOfSeats = numberOfSeats;
        }

        public Long getZoneId() {
            return zoneId;
        }

        public void setZoneId(Long zoneId) {
            this.zoneId = zoneId;
        }

        public String getZoneCode() {
            return zoneCode;
        }

        public void setZoneCode(String zoneCode) {
            this.zoneCode = zoneCode;
        }

        public String getZoneName() {
            return zoneName;
        }

        public void setZoneName(String zoneName) {
            this.zoneName = zoneName;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public TableStatus getTableStatus() {
            return tableStatus;
        }

        public void setTableStatus(TableStatus tableStatus) {
            this.tableStatus = tableStatus;
        }

        public List<TableSeat> getTableSeats() {
            return tableSeats;
        }

        public void setTableSeats(List<TableSeat> tableSeats) {
            this.tableSeats = tableSeats;
        }
    }

}

