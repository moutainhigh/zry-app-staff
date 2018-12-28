package com.zhongmei.beauty.booking.order

import android.text.TextUtils
import com.zhongmei.beauty.booking.bean.DishBrandTypeVo
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager.DISHNAME

/**
 * 服务选择处理工具类

 */
class BeautySelectManager {

    var dishCache: HashMap<Long, DishVo>? = HashMap()

    /**
     * 添加选择的服务到缓存
     * @param dishVo 当前处理的dishVo
     */
    fun changeDishCache(dishVo: DishVo) {
        dishVo.isSelected = !dishVo.isSelected
        if (isDishSelected(dishVo.brandDishId)) {
            dishCache!!.remove(dishVo.brandDishId)
        } else {
            dishCache!!.put(dishVo.brandDishId, dishVo)
        }
    }

    fun isDishSelected(dishId: Long): Boolean {
        return dishCache!![dishId] != null
    }


    /**
     * 加载所有的服务类别，包括构建全部类
     */
    fun loadBrandTypeData(mDishManager: DishManager): ArrayList<DishBrandTypeVo> {
        val dishBrandTypes = mDishManager.loadData().dishTypeList
        var allVo: DishBrandTypeVo = DishBrandTypeVo()
        allVo.isAll = true
        var typeVoList = ArrayList<DishBrandTypeVo>()
//        typeVoList.add(allVo)
        dishBrandTypes.forEach { type ->
            var typeVo = DishBrandTypeVo()
            typeVo.brandType = type
            typeVo.isAll = false
            typeVoList.add(typeVo)
        }
        return typeVoList
    }

    /**
     * 初始化全部菜品的选中状态
     */
    fun initAllDishVo(mDishManager: DishManager): ArrayList<DishVo> {
        var dishVoList = mDishManager.allDishVo
        return initDishVoSelected(dishVoList)
    }

    /**
     * 根据类型初始化dishVo的选中状态
     */
    fun initDishVoByType(mDishManager: DishManager, dishBrandType: DishBrandTypeVo): ArrayList<DishVo> {
        var dishVoList = mDishManager.switchType(dishBrandType.brandType).dishList
        return initDishVoSelected(dishVoList)
    }

    fun initDishVoSelected(dishVoList: List<DishVo>): ArrayList<DishVo> {
        dishVoList!!.forEach { dishVo ->
            if (isDishSelected(dishVo.brandDishId)) {
                dishVo.isSelected = true
            } else {
                dishVo.isSelected = false
            }
        }
        return ArrayList(dishVoList)
    }

    /**
     * 搜索菜品
     */
    fun doSearch(mDishManager: DishManager, keyword: String): ArrayList<DishVo> {
        if (TextUtils.isEmpty(keyword)) {
            return ArrayList()
        }
        var dishList = mDishManager.search(keyword, DISHNAME)!!.dishList
        dishList.forEach { dishVo ->
            if (isDishSelected(dishVo.brandDishId)) {
                dishVo.isSelected = true
            } else {
                dishVo.isSelected = false
            }
        }
        return ArrayList(dishList)
    }

}