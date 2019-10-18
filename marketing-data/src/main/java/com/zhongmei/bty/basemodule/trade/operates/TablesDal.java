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


public interface TablesDal extends IOperates {

    List<TableType> listTableType() throws Exception;


    List<CommercialArea> listTableArea() throws Exception;

    List<Tables> listTables() throws Exception;

    List<TableSeat> listTableSeats() throws Exception;

    List<TableSeat> listTableSeatsByTableId(Long tableId) throws Exception;


    List<Tables> listAllPhysicalLayoutTables() throws SQLException;


    List<CommercialArea> listDinnerArea() throws Exception;


    List<Tables> listDinnerEmptyTablesByStatus(TableStatus tableStatus) throws Exception;


    List<Tables> listTablesByAreaId(Long areaId) throws Exception;

    List<Tables> listTables(Long tableTypeId) throws Exception;

    Collection<DinnertableWrapper> listDinnertables() throws Exception;


    void clearDinnertable(IDinnertable dinnertable, ResponseListener<ClearDinnertableResp> listener);


    CommercialArea listArea(Long tableId) throws Exception;

    Map<CommercialArea, List<Tables>> listAreaByTableId(Long... tableId) throws Exception;


    Tables findTablesById(Long tabelId) throws Exception;


    CommercialArea findAreaById(Long areaId) throws Exception;

    List<CommercialArea> findAreaById(List<Long> areaId) throws Exception;


    int countTotalPersonCount(Long tabelId) throws Exception;


    DinnerTableInfo getDinnerTableByTradeVo(TradeVo tradeVo) throws Exception;


    List<TableAreaPopupVo> findAllTableAreaVo() throws Exception;


    List<SnackTableVo> findSnackTableVo(boolean haveLimitServiceTime) throws Exception;


    List<Trade> findTradesInTable(TradeTable tradeTable) throws SQLException;


        public List<Tables> findTablesByIds(List<Long> tableIds, TableStatus status) throws Exception;


    List<Tables> listTables(TableStatus tableStatus) throws Exception;

    List<Tables> listValidTables() throws Exception;

    List<TradeTable> listTradeTable(List<Long> tradeIdList) throws Exception;

    long countOfTablesByPeopleCount(int minPeopleCount, int maxPeopleCount) throws SQLException;


    class DinnertableWrapper {

        public static final int MIN_WIDTH = 105;

        public static final int MIN_HEIGHT = 105;

        private String uuid;
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

