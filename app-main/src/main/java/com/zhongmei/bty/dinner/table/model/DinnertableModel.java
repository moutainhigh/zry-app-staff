package com.zhongmei.bty.dinner.table.model;

import android.util.Log;

import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeModel;
import com.zhongmei.bty.basemodule.trade.bean.TableStateInfo;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.bean.IZone;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpType;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class DinnertableModel implements IDinnertable {
    private String uuid;    private Long id;     private String name;
    private int x;
    private int y;
    private int width;
    private int height;

    private int numberOfSeats;

    private DinnertableShape shape;

    private IZone zone;

    private boolean isReserved;
    private TableStatus tableStatus;
    private List<AsyncHttpRecord> mListHttpRecord;

    private Map<String, List<AsyncHttpRecord>> mListTradeHttpRecord;
    private List<TableSeat> tableSeats;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setTableStatus(TableStatus tableStatus) {
        this.tableStatus = tableStatus;
    }

    @Override
    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        if (numberOfSeats != null) {
            this.numberOfSeats = numberOfSeats;
        }
    }

    public DinnertableShape getShape() {
        return shape;
    }

    public void setShape(DinnertableShape shape) {
        this.shape = shape;
    }

    @Override
    public List<TableSeat> getTableSeats() {
        return tableSeats;
    }

    public void setTableSeats(List<TableSeat> tableSeats) {
        this.tableSeats = tableSeats;
    }

    @Override
    public IZone getZone() {
        return zone;
    }

    public void setZone(IZone zone) {
        this.zone = zone;
    }

    private TableStateInfo stateInfo;
    private List<IDinnertableTrade> dinnertableTrades;
    private int numberOfMeals;
    private int unprocessTradeCount;
    private IDinnertableTrade dinnerUnionMainTrade;
    private List<IDinnertableTrade> dinnerUnionSubTrades;

    public void setStateInfo(TableStateInfo stateInfo, List<IDinnertableTrade> dinnertableTrades) {
        this.stateInfo = stateInfo;
        int number = 0;
        int count = 0;
        for (IDinnertableTrade dinnertableTrade : dinnertableTrades) {
            number += dinnertableTrade.getNumberOfMeals();
            if (dinnertableTrade.getTradeStatus() == TradeStatus.UNPROCESSED) {
                count++;
            }
        }
                try {
            Collections.sort(dinnertableTrades, new Comparator<IDinnertableTrade>() {

                @Override
                public int compare(IDinnertableTrade lhs, IDinnertableTrade rhs) {
                    Integer value1 = Integer.valueOf(lhs.getSpendTime());
                    Integer value2 = Integer.valueOf(rhs.getSpendTime());
                    return value2.compareTo(value1);
                }

            });
        } catch (Exception e) {
            Log.e("DinnertableModel", e.getMessage());
        }
        this.dinnertableTrades = dinnertableTrades;
        numberOfMeals = number;
        unprocessTradeCount = count;
    }

    public List<IDinnertableTrade> getDinnertableTrades() {
        if (dinnertableTrades == null) {
            dinnertableTrades = new ArrayList<IDinnertableTrade>();
        }
        return dinnertableTrades;
    }

    public boolean isGroup() {
        if (stateInfo != null) {
            return stateInfo.tradeBusinessType == BusinessType.GROUP;
        }
        return false;
    }

    public boolean isBuffet() {
        if (stateInfo != null) {
            return stateInfo.tradeBusinessType == BusinessType.BUFFET;
        }
        return false;
    }

    public boolean isDinner() {
        if (stateInfo != null) {
            return stateInfo.tradeBusinessType == BusinessType.DINNER;
        }
        return false;
    }

    public List<AsyncHttpRecord> getmListHttpRecord() {
        return mListHttpRecord;
    }

    public synchronized void setmListHttpRecord(List<AsyncHttpRecord> mListHttpRecord) {

        if (Utils.isEmpty(mListHttpRecord)) {            this.mListHttpRecord = null;
            return;
        }

        try {            Collections.sort(mListHttpRecord, new Comparator<AsyncHttpRecord>() {
                @Override
                public int compare(AsyncHttpRecord lhs, AsyncHttpRecord rhs) {

                    int flag = lhs.getClientUpdateTime().compareTo(rhs.getClientUpdateTime());


                    return flag;

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        this.mListHttpRecord = mListHttpRecord;
    }

    public Map<String, List<AsyncHttpRecord>> getmListTradeHttpRecord() {
        return mListTradeHttpRecord;
    }

    public void setmListTradeHttpRecord(Map<String, List<AsyncHttpRecord>> mListTradeHttpRecord) {
        this.mListTradeHttpRecord = mListTradeHttpRecord;
    }

    public IDinnertableTrade getDinnerUnionMainTrade() {
        return dinnerUnionMainTrade;
    }

    public List<IDinnertableTrade> getDinnerUnionSubTrades() {
        return dinnerUnionSubTrades;
    }

    public void setUnionTableTradeInfo(IDinnertableTrade mainTrade, List<IDinnertableTrade> subTrades) {
        this.dinnerUnionMainTrade = mainTrade;
        this.dinnerUnionSubTrades = subTrades;
    }

    @Override
    public int getNumberOfMeals() {
        return numberOfMeals;
    }

    @Override
    public int getUnprocessTradeCount() {
        return unprocessTradeCount;
    }

    @Override
    public boolean isCurBusinessType(BusinessType businessType) {
        for (IDinnertableTrade iDinnertableTrade : getDinnertableTrades()) {
            if (iDinnertableTrade.getBusinessType() != businessType) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return Utils.isEmpty(dinnertableTrades);
    }

    public boolean asyncOpeningTable() {
        if (Utils.isEmpty(mListHttpRecord)) {
            return false;
        }

        for (AsyncHttpRecord record : mListHttpRecord) {
            if (record.getType() == AsyncHttpType.OPENTABLE) {
                return true;
            }
        }
        return false;
    }

    public boolean isDone() {
        return isEmpty() && getTableStatus() != TableStatus.EMPTY;
    }

    @Override
    public int getTradeCount() {
        if (dinnertableTrades == null) {
            return 0;
        }
        return dinnertableTrades.size();
    }

    @Override
    public Long getServerUpdateTime() {
        return stateInfo == null ? 0 : stateInfo.serverUpdateTime;
    }

    @Override
    public TableStatus getTableStatus() {
        return stateInfo == null ? TableStatus.EMPTY : stateInfo.tableStatus;
    }


    public TableStatus getPhysicsTableStatus() {
        return tableStatus;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DinnertableModel other = (DinnertableModel) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public AsyncHttpRecord getAsyncHttpRecord() {
        if (dinnertableTrades != null && dinnertableTrades.size() != 0) {
            AsyncHttpRecord recordResult = null;
            for (IDinnertableTrade dinnertableTrade : dinnertableTrades) {
                DinnertableTradeModel tradeModel = (DinnertableTradeModel) dinnertableTrade;
                AsyncHttpRecord record = tradeModel.getAsyncHttpRecord();
                if (record != null) {
                    if (recordResult == null) {
                        recordResult = record;
                    } else {
                        if (record.getStatus().value() < recordResult.getStatus().value()) {
                            recordResult = record;
                        }

                    }

                }

            }
            return recordResult;
        }
        return null;
    }


    public boolean hasAddDish() {
        if (dinnertableTrades != null) {
            for (IDinnertableTrade trade : dinnertableTrades) {
                DinnertableTradeModel tradeModel = (DinnertableTradeModel) trade;
                if (tradeModel.getAddItemVos() != null && tradeModel.getAddItemVos().size() != 0) {
                    return true;
                }
            }
        }
        return false;
    }


}
