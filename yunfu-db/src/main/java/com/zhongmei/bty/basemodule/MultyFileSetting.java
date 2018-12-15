package com.zhongmei.bty.basemodule;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "multy_file_setting")
public class MultyFileSetting extends BasicEntityBase implements ICreator, IUpdator {


    public Integer getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Integer enableFlag) {
        this.enableFlag = enableFlag;
    }

    public interface $ extends BasicEntityBase.$ {
        String multiFileUrl = "multi_file_url";
        String sort = "sort";
        String fileType = "file_type";
        String carouselInterval = "carousel_interval";
        String groupFlag = "group_flag";
        /**
         * 是否启用（1-是，2-否）
         */
        String enableFlag = "enable_flag";
    }


    @DatabaseField(columnName = "multi_file_url", canBeNull = false)
    private String multiFileUrl;

    @DatabaseField(columnName = "sort")
    private int sort;

    @DatabaseField(columnName = "file_type", canBeNull = false)
    private int fileType;

    @DatabaseField(columnName = "carousel_interval", canBeNull = false)
    private int carouselInterval;

    @DatabaseField(columnName = "group_flag", canBeNull = false)
    private int groupFlag;
    @DatabaseField(columnName = "enable_flag", canBeNull = false)
    private Integer enableFlag;

    public String getMultiFileUrl() {
        return multiFileUrl;
    }

    public void setMultiFileUrl(String multiFileUrl) {
        this.multiFileUrl = multiFileUrl;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getCarouselInterval() {
        return carouselInterval;
    }

    public void setCarouselInterval(int carouselInterval) {
        this.carouselInterval = carouselInterval;
    }

    public int getGroupFlag() {
        return groupFlag;
    }

    public void setGroupFlag(int groupFlag) {
        this.groupFlag = groupFlag;
    }

    @Override
    public Long getCreatorId() {

        return null;
    }

    @Override
    public void setCreatorId(Long creatorId) {

    }

    @Override
    public String getCreatorName() {
        return null;
    }

    @Override
    public void setCreatorName(String creatorName) {

    }

    @Override
    public Long getUpdatorId() {
        return null;
    }

    @Override
    public void setUpdatorId(Long updatorId) {

    }

    @Override
    public String getUpdatorName() {
        return null;
    }

    @Override
    public void setUpdatorName(String updatorName) {

    }
}
