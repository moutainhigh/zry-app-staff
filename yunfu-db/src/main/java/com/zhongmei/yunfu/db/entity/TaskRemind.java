package com.zhongmei.yunfu.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;


@DatabaseTable(tableName = "task_remind")
public class TaskRemind extends DataEntityBase implements ICreator, IUpdator {

    public interface $ extends DataEntityBase.${
        public static final String title="title";

        public static final String content="content";

        public static final String remindTime="remind_time";

        public static final String type="type";

        public static final String status="status";

        public static final String customerDocId="customer_doc_id";

        public static final String taskResult="task_result";

        public static final String customerId="customer_id";

        public static final String customerName="customer_name";

        public static final String customerMobile="customer_mobile";

        public static final String userId="user_id";

        public static final String userName="user_name";

    }

    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "content")
    private String content;
    @DatabaseField(columnName = "remind_time")
    private Long remindTime;
    @DatabaseField(columnName = "type")
    private Integer type;
    @DatabaseField(columnName = "status")
    private Integer status=1;
    @DatabaseField(columnName = "customer_doc_id")
    private Long customerDocId;
    @DatabaseField(columnName = "task_result")
    private String taskResult;
    @DatabaseField(columnName = "customer_id")
    private Long customerId;
    @DatabaseField(columnName = "customer_name")
    private String customerName;
    @DatabaseField(columnName = "customer_mobile")
    private String customerMobile;
    @DatabaseField(columnName = "user_id")
    private Long userId;
    @DatabaseField(columnName = "user_name")
    private String userName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Long remindTime) {
        this.remindTime = remindTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCustomerDocId() {
        return customerDocId;
    }

    public void setCustomerDocId(Long customerDocId) {
        this.customerDocId = customerDocId;
    }

    public String getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(String taskResult) {
        this.taskResult = taskResult;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public Long getCreatorId() {
        return getCreatorId();
    }

    @Override
    public void setCreatorId(Long creatorId) {
        setCreatorId(creatorId);
    }

    @Override
    public String getCreatorName() {
        return getCreatorName();
    }

    @Override
    public void setCreatorName(String creatorName) {
        setCreatorName(creatorName);
    }

    @Override
    public Long getUpdatorId() {
        return getUpdatorId();
    }

    @Override
    public void setUpdatorId(Long updatorId) {
        setUpdatorId(updatorId);
    }

    @Override
    public String getUpdatorName() {
        return getUpdatorName();
    }

    @Override
    public void setUpdatorName(String updatorName) {
        setUpdatorName(updatorName);
    }
}
