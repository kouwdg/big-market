package org.example.domain.activity.service.armory;


import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.ActivitySkuEntity;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.types.common.Constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 活动sku预热
 * @date 2024/12/24 14:31
 */
@Component
public class ActivityArmory implements IActivityArmory, IActivityDispatch {
    @Resource
    private IActivityRepository activityRepository;

    @Override
    public boolean assembleActivitySku(Long sku) {
        //1  查询sku的信息
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(sku);

        //2 缓存sku库存到redis中
        cacheActivitySkuStockCount(sku, activitySkuEntity.getStockCount());

        //预热 对应的活动 到缓存中
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        //预热 对应的活动次数 到缓存中
        activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        return true;
    }

    /**
     * 根据 活动Id 装配Sku
     *
     * @param ActivityId
     * @return
     */
    @Override
    public boolean assembleActivitySkuByActivityId(Long ActivityId) {
        //根据 活动ID 查询出所有对应的 sku
        List<ActivitySkuEntity> skuEntityList = activityRepository.queryActivitySkuByActivityId(ActivityId);
        System.out.println(skuEntityList);
        for (ActivitySkuEntity tem : skuEntityList) {
            cacheActivitySkuStockCount(tem.getSku(), tem.getStockCountSurplus());
            //预热 对应的活动次数 到缓存中
            activityRepository.queryRaffleActivityCountByActivityCountId(tem.getActivityCountId());
        }
        //预热 对应的活动 到缓存中
        activityRepository.queryRaffleActivityByActivityId(ActivityId);
        return true;
    }


    //清空redis
    @Override
    public boolean redisFlush() {
        return activityRepository.redisFlush();
    }

    @Override
    public Integer getUsedRaffleCount(String userId, Long activityId) {
        return activityRepository.queryUsedDrawTotalCount(userId,activityId);
    }


    //扣减库存
    @Override
    public boolean subtractionActivitySkuStock(Long sku, Date endDateTime) {
        String cache = Constants.redisKey.ACTIVITY_SKU_STOCK_COUNT + sku;
        return activityRepository.subtractionActivitySkuStock(sku, cache, endDateTime);
    }

    private void cacheActivitySkuStockCount(Long sku, Integer stockCount) {

        String cache = Constants.redisKey.ACTIVITY_SKU_STOCK_COUNT + sku;
        //只存储 库存 到redis中
        activityRepository.cacheActivitySkuStockCount(cache, stockCount);
    }


}
