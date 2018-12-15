package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 时段类型
 */
public enum PeriodType implements ValueEnum<Integer> {
    /**
     * 早餐
     */
    BREAKFAST(0),

    /**
     * 午餐
     */
    LUNCH(1),

    /**
     * 下午茶
     */
    AFTERNOONTEA(2),

    /**
     * 晚餐
     */
    DINNER(3),

    /**
     * 夜宵
     */
    SUPPPER(4),

    /**
     * 其他
     */
    OTHERS(5),

    /**
     * 全天
     */
    ALLDAY(6),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PeriodType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PeriodType() {
        helper = Helper.unknownHelper();
    }

    @Override
    public Integer value() {
        return helper.value();
    }

    @Override
    public boolean equalsValue(Integer value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public boolean isUnknownEnum() {
        return helper.isUnknownEnum();
    }

    @Override
    public void setUnknownValue(Integer value) {
        helper.setUnknownValue(value);
    }

    @Override
    public String toString() {
        return "" + value();
    }

//	/**
//	 * 获得 时段的字符串显示
//	 * @return
//	 */
//	public static EnumMap<PeriodType, String> getValueStrings(){
//		Resources res = MainApplication.getInstance().getResources();
//		EnumMap<PeriodType,String> peridNames=new EnumMap<PeriodType, String>(PeriodType.class);
//		peridNames.put(BREAKFAST, res.getString(R.string.dinner_breakfast));
//		peridNames.put(LUNCH, res.getString(R.string.dinner_lunch));
//		peridNames.put(AFTERNOONTEA, res.getString(R.string.dinner_afternoon_tea));
//		peridNames.put(DINNER, res.getString(R.string.dinner_dinner));
//		peridNames.put(SUPPPER, res.getString(R.string.dinner_night_snack));
//		peridNames.put(OTHERS, res.getString(R.string.commonmodule_dialog_other));
//		peridNames.put(ALLDAY, res.getString(R.string.dinner_full_day));
//		return peridNames;
//	}
}
