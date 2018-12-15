package com.zhongmei.bty.cashier.shoppingcart;

import java.util.ArrayList;
import java.util.List;

import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.ExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

/**
 * @Date：2015年7月15日 下午5:47:45
 * @Description: TODO
 * @Version: 1.0
 */
public class CreateDishTool {

    /**
     * @Title: tradeToDish
     * @Description: 将tradeVo转换成List<ShopcartItem>
     * @Param @param mTradeVo
     * @Param @return TODO
     * @Return List<ShopcartItem> 返回类型
     */
    public static List<ShopcartItem> tradeToDish(TradeVo mTradeVo) {
        List<ShopcartItem> listShopcartItem = new ArrayList<ShopcartItem>();
        List<TradeItemVo> listTradeItemVo = mTradeVo.getTradeItemList();
//		for(TradeItemVo tradeItem : listTradeItemVo){
//			if(TextUtils.isEmpty(tradeItem.getTradeItem().getParentUuid())){
//				
//				ShopcartItemBase mShopcartItemBase = buildShopcartItem(tradeItem);
//				ShopcartItem mShopcartItem = new ShopcartItem();
//				
//				if(mShopcartItemBase instanceof ShopcartItem){
//					mShopcartItem = (ShopcartItem)mShopcartItemBase;
//				}
//				
//				//设置加料
//				List<ExtraShopcartItem> extraList = new ArrayList<ExtraShopcartItem>();
//				//设置子菜
//				List<SetmealShopcartItem> setmealList = new ArrayList<SetmealShopcartItem>();
//				
//				for(TradeItemVo item : listTradeItemVo){
//					if(tradeItem.getTradeItem().getTradeUuid().equals(item.getTradeItem().getParentUuid())){
//						//构建加料数据
//						if(item.getTradeItem().getType().value() == DishType.EXTRA.value()){
//							ExtraShopcartItem mExtraItem = (ExtraShopcartItem)buildShopcartItem(item);
//							extraList.add(mExtraItem);
//						}else{
//							//构建子菜数据
//							SetmealShopcartItem mSetmealItem = (SetmealShopcartItem)buildShopcartItem(item);
//							//构建子菜中的加料
//							List<ExtraShopcartItem> listExtraDishItem = new ArrayList<ExtraShopcartItem>();
//							for(TradeItemVo extraTradeItem : listTradeItemVo){
//								if(extraTradeItem.getTradeItem().getParentUuid().equals(mSetmealItem.getUuid())){
//									ExtraShopcartItem mExtraDishItem = (ExtraShopcartItem)buildShopcartItem(extraTradeItem);
//									listExtraDishItem.add(mExtraDishItem);
//								}
//							}
//							mSetmealItem.setExtraList(listExtraDishItem);
//							setmealList.add(mSetmealItem);
//						}
//						
//					}
//				}
//				mShopcartItem.setExtraList(extraList);
//				mShopcartItem.setSetmealItems(setmealList);
//				listShopcartItem.add(mShopcartItem);
//			}else{
//				
//			}
//		}

        return listShopcartItem;
    }

    private static ShopcartItemBase buildShopcartItem(TradeItemVo tradeItem) {
//		ShopcartItemBase mShopcartItem = new ShopcartItemBase<OrderDish>() {};
//		
//		mShopcartItem.setUuid(tradeItem.getTradeItem().getTradeUuid());
//		mShopcartItem.setParentUuid(tradeItem.getTradeItem().getParentUuid());
//		mShopcartItem.setMemo(tradeItem.getTradeItem().getTradeMemo());
//		//明细的特征列表
//		List<OrderProperty> selectedProperties = new ArrayList<OrderProperty>();
//		List<TradeItemProperty> listProperties = tradeItem.getTradeItemPropertyList();
//		for(TradeItemProperty property : listProperties){
//			OrderProperty shopItemPropertyVo = new OrderProperty();
//			
//			shopItemPropertyVo.setProperty(getDishProperty(property));
//			selectedProperties.add(shopItemPropertyVo);
//		}
//		mShopcartItem.setSelectedProperties(selectedProperties);
//		//设置备注
//		mShopcartItem.setMemo(tradeItem.getTradeItem().getTradeMemo());
//		//设置优惠信息
//		TradePrivilege mTradePrivilege = tradeItem.getTradeItemPrivilege();
//		mShopcartItem.setPrivilege(mTradePrivilege);
//		
//		DishShop dishShop = new DishShop();
//		
//		return mShopcartItem;
        return null;
    }

    /**
     * @Title: buildSetmealShopcartItem
     * @Description: 将tradeItem转换为SetmealShopcartItem
     * @Param @param tradeItem
     * @Param @return TODO
     * @Return SetmealShopcartItem 返回类型
     */
    private static SetmealShopcartItem buildSetmealShopcartItem(TradeItemVo tradeItem) {
//		SetmealShopcartItem mSetmealShopcartItem = new SetmealShopcartItem();
//		mSetmealShopcartItem.setUuid(tradeItem.getTradeItem().getTradeUuid());
//		mSetmealShopcartItem.setParentUuid(tradeItem.getTradeItem().getParentUuid());
//		
//		return mSetmealShopcartItem;
        return null;
    }

    private static ExtraShopcartItem buildExtraDishItem(TradeItemVo tradeItem) {
//		ExtraShopcartItem mExtraDishItem = new ExtraShopcartItem();
//		
//		return mExtraDishItem;
        return null;
    }

    /**
     * @Title: getDishProperty
     * @Description: 构建菜品已选属性信息
     * @Param @param property
     * @Param @return TODO
     * @Return DishProperty 返回类型
     */
    private static DishProperty getDishProperty(TradeItemProperty property) {
        DishProperty dishProperty = new DishProperty();
//		dishProperty.setAliasName();
        dishProperty.setCreatorId(property.getCreatorId());
        dishProperty.setCreatorName(property.getCreatorName());
        dishProperty.setName(property.getPropertyName());
//		dishProperty.setPropertyKind();
//		dishProperty.setPropertyTypeId();
        dishProperty.setReprice(property.getAmount());
//		dishProperty.setSort();
        dishProperty.setUuid(property.getPropertyUuid());

        return dishProperty;
    }

}
