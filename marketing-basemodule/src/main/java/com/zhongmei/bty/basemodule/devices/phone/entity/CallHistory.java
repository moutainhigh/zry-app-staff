package com.zhongmei.bty.basemodule.devices.phone.entity;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.devices.phone.CallDBHelper;
import com.zhongmei.bty.basemodule.devices.phone.CallService;
import com.zhongmei.bty.basemodule.devices.phone.utils.Lg;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.bty.basemodule.devices.phone.bean.AdapterData;


/**
 * 通话记录
 *
 * @date 2014-8-14
 */
@DatabaseTable(tableName = "call_history")
public class CallHistory extends EntityBase<Integer> implements AdapterData {

    public static final String TABLE_NAME = "call_history";
    public static final String COL_ID = "id";
    public static final String COL_PHONE_NUM = "phone_num";
    public static final String COL_START_TIME = "start_time";
    public static final String COL_END_TIME = "end_time";
    public static final String COL_TYPE = "type";
    public static final String COL_STATUS = "status";
    public static final String COL_CONTACTS_NAME = "contacts_name";
    public static final String COL_CONTACTS_LEVEL_ID = "levelid";

    /**
     * 未设置状态
     */
    public static final int STATUS_NONE = -1;
    /**
     * 正常通话
     */
    public static final int STATUS_NORMAL = 0;
    /**
     * 未接
     */
    public static final int STATUS_NO_ANSWER = 1;


    /**
     * 未设置类型
     */
    public static final int TYPE_NONE = -1;
    /**
     * 来电
     */
    public static final int TYPE_INCALL = 0;
    /**
     * 去电
     */
    public static final int TYPE_OUTCALL = 1;

    /**
     * 自增长ID
     */
    @DatabaseField(columnName = COL_ID, generatedId = true)
    private int id;

    /**
     * 开始时间
     */
    @DatabaseField(columnName = COL_START_TIME, dataType = DataType.DATE)
    public Date startTime;

    /**
     * 结束时间
     */
    @DatabaseField(columnName = COL_END_TIME, dataType = DataType.DATE)
    public Date endTime;

    /**
     * 类型,  0:来电  1:去电
     */
    @DatabaseField(columnName = COL_TYPE)
    public int type = TYPE_NONE;

    /**
     * 状态 0:正常 1:未接
     */
    @DatabaseField(columnName = COL_STATUS)
    public int status = STATUS_NONE;

    /**
     * 对方号码
     */
    @DatabaseField(columnName = COL_PHONE_NUM)
    public String number;

    /**
     * 联系人姓名, 需要从外部读取
     */
    @DatabaseField(columnName = COL_CONTACTS_NAME)
    public String contactsName;

    /**
     * 联系人等级
     */
    @DatabaseField(columnName = COL_CONTACTS_LEVEL_ID)
    public String levelId;

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public CallHistory() {

    }

    public CallHistory(Date startTime, int type) {
        setStartTime(startTime);
        setType(type);
    }


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getType() {
        return type;
    }

    public CallHistory setType(int type) {
        this.type = type;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public CallHistory setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public CallHistory setNumber(String number) {
        this.number = number;
        return this;
    }


    public void save() {
        if (isAvailable()) {
            Lg.d("save history", this.toString());
//            int sss= getStatus();
            // TODO 保存通话记录, 可能需要从外部读取联系人
            // 暂时在当前线程保存.........   嗯... 应该.. 不会太影响吧..
            CallDBHelper.getService(CallService.class).insert(this);
        }
    }

    private boolean isAvailable() {
        return number != null && status != STATUS_NONE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    @Override
    public String toString() {
        return "CallHistory [id=" + id + ", startTime=" + startTime
                + ", endTime=" + endTime + ", type=" + type + ", status="
                + status + ", number=" + number + ", contactsName="
                + contactsName + "]";
    }


    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Integer pkValue() {
        return id;
    }

    @Override
    public Long verValue() {
        return Long.valueOf(id);
    }
}
