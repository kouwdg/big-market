package org.example.domain.activity.service.RaffleOrder.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.ActivityCountEntity;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.ActivitySkuEntity;
import org.example.domain.activity.model.vo.ActivitySkuStockKeyVo;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.RaffleOrder.rule.chain.AbstractActionChain;
import org.example.domain.activity.service.armory.IActivityDispatch;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 商品库存规则节点
 * @create 2024-03-23 10:25
 */
@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {
    @Resource
    private IActivityDispatch activityDispatch;
    @Resource
    private IActivityRepository activityRepository;

    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品库存处理【校验&扣减】开始。");

        //1 扣减库存(目的是在redis中扣减)
        boolean status = activityDispatch.subtractionActivitySkuStock(activitySkuEntity.getSku(), activityEntity.getEndDateTime());

        if (status){
            log.info("sku库存扣减成功");
            //写入延迟队列(目的 让数据库 进行扣减)
            activityRepository.activitySkuStockSendQueue(ActivitySkuStockKeyVo.builder()
                    .sku(activitySkuEntity.getSku())
                    .activityId(activitySkuEntity.getActivityId())
                    .build());
            return true;
        }
        log.info("sku库存不足");
        throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode(),ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getInfo());

    }
}
