package com.cheng.domain.activity.service.rule.chain.impl;

import com.cheng.domain.activity.model.entity.ActivityCountEntity;
import com.cheng.domain.activity.model.entity.ActivityEntity;
import com.cheng.domain.activity.model.entity.ActivitySkuEntity;
import com.cheng.domain.activity.model.vo.ActivitySkuStockKeyVo;
import com.cheng.domain.activity.repository.IActivityRepository;
import com.cheng.domain.activity.service.armory.IActivityDispatch;
import com.cheng.domain.activity.service.rule.chain.AbstractActionChain;
import com.cheng.types.enums.ResponseCode;
import com.cheng.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.xml.ws.RequestWrapper;

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
            //写入延迟队列(目的让 数据库 进行扣减)
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
