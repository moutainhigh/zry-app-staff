package com.zhongmei.yunfu.db.entity.crm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.CommonEntityBase;
import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.context.base.BaseApplication;


@DatabaseTable(tableName = "customer_group")
public class CustomerGroupLevel extends CommonEntityBase {


    private static final long serialVersionUID = 1L;

    public interface $ extends CommonEntityBase.$ {

        String uuid = "uuid";

        String groupName = "group_name";

    }

    @DatabaseField(columnName = $.uuid)
    private String uuid;

    @DatabaseField(columnName = $.groupName)
    private String groupName = BaseApplication.sInstance.getString(R.string.customer_all_customer);



    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
