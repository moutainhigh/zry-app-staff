package com.zhongmei.yunfu.db.entity.dish;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;

@DatabaseTable(tableName = "dish_describe")
public class DishDescribe extends BasicEntityBase implements ICreator, IUpdator {

    public interface $ extends BasicEntityBase.$ {
        public static final String dishId = "dish_id";
        public static final String dishPic = "dish_pic";
        public static final String dishDescribe = "dish_describe";
        public static final String creatorId = "creator_id";
        public static final String creatorName = "creator_name";
        public static final String updatorId = "updator_id";
        public static final String updatorName = "updator_name";
    }

    @DatabaseField(columnName = "dish_id")
    private Long dishID;

    @DatabaseField(columnName = "dish_pic")
    private String dishPic;

    @DatabaseField(columnName = "dish_describe")
    private String dishDescribe;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;


    public Long getDishID() {
        return dishID;
    }

    public void setDishID(Long dishID) {
        this.dishID = dishID;
    }

    public String getDishPic() {
        return dishPic;
    }

    public void setDishPic(String dishPic) {
        this.dishPic = dishPic;
    }

    public String getDishDescribe() {
        return dishDescribe;
    }

    public void setDishDescribe(String dishDescribe) {
        this.dishDescribe = dishDescribe;
    }

    @Override
    public Long getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String getCreatorName() {
        return creatorName;
    }

    @Override
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public Long getUpdatorId() {
        return getUpdatorId();
    }

    @Override
    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    @Override
    public String getUpdatorName() {
        return updatorName;
    }

    @Override
    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }
}
