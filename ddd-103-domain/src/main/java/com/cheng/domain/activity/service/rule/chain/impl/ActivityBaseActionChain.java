package com.cheng.domain.activity.service.rule.chain.impl;


import com.cheng.domain.activity.model.entity.ActivityCountEntity;
import com.cheng.domain.activity.model.entity.ActivityEntity;
import com.cheng.domain.activity.model.entity.ActivitySkuEntity;
import com.cheng.domain.activity.model.vo.ActivityStateVO;
import com.cheng.domain.activity.service.rule.chain.AbstractActionChain;
import com.cheng.types.enums.ResponseCode;
import com.cheng.types.exception.AppException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 活动规则过滤【日期、状态】
 * @create 2024-03-23 10:23
 */
@Slf4j
@Component("activity_base_action")
public class ActivityBaseActionChain extends AbstractActionChain {
    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {

        log.info("活动责任链-基础信息【有效期、状态】校验开始。");
        log.info("tem:{}",activityEntity);
        //1 校验活动状态
        if(!ActivityStateVO.open.getCode().equals(activityEntity.getState().getCode())){
            throw new AppException(ResponseCode.ACTIVITY_STATUS_OPEN.getCode(),ResponseCode.ACTIVITY_STATUS_OPEN.getInfo());
        }
        //2 校验活动时间
        Date date = new Date();
        if(activityEntity.getBeginDateTime().after(date)||activityEntity.getEndDateTime().before(date)){
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(),ResponseCode.ACTIVITY_DATE_ERROR.getInfo());
        }
        //3 校验活动库存
        if( activitySkuEntity.getStockCountSurplus()<=0){
            throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode(),ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getInfo());
        }
        return next().action(activitySkuEntity, activityEntity, activityCountEntity);
    }
}
