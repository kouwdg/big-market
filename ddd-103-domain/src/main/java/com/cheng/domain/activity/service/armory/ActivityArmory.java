package com.cheng.domain.activity.service.armory;

import com.cheng.domain.activity.model.entity.ActivitySkuEntity;
import com.cheng.domain.activity.repository.IActivityRepository;
import com.cheng.types.common.Constants;
import com.cheng.types.enums.ResponseCode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 活动sku预热
 * @date 2024/12/24 14:31
 */
@Component
public class ActivityArmory implements IActivityArmory,IActivityDispatch{
    @Resource
    private IActivityRepository activityRepository;
    @Override
    public boolean assembleActivitySku(Long sku) {
        //1  查询sku的信息
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(sku);

        //2 缓存sku库存到redis中
        cacheActivitySkuStockCount(sku,activitySkuEntity.getStockCount());
        return false;
    }


    //扣减库存
    @Override
    public boolean subtractionActivitySkuStock(Long sku, Date endDateTime) {
        String cache= Constants.redisKey.ACTIVITY_SKU_STOCK_COUNT+sku;
        return activityRepository.subtractionActivitySkuStock(sku,cache,endDateTime);
    }

    private void cacheActivitySkuStockCount(Long sku,Integer stockCount){

        String cache= Constants.redisKey.ACTIVITY_SKU_STOCK_COUNT+sku;
        //只存储 库存 到redis中
        activityRepository.cacheActivitySkuStockCount(cache,stockCount);
    }


}
