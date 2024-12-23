package com.cheng.domain.activity.service;

import com.cheng.domain.activity.model.aggregate.CreateOrderAggregate;
import com.cheng.domain.activity.model.entity.*;
import com.cheng.domain.activity.repository.IActivityRepository;
import com.cheng.domain.activity.service.rule.chain.IActionChain;
import com.cheng.domain.activity.service.rule.chain.factory.DefaultActivityChainFactory;
import com.cheng.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/23 12:52
 */

@Slf4j
@Service
public abstract class AbstractRaffleActivity extends RaffleActivitySupport implements IRaffleOrder{

    protected DefaultActivityChainFactory defaultActivityChainFactory;
    protected IActivityRepository activityRepository;

    public AbstractRaffleActivity(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory, IActivityRepository activityRepository1) {
        super(activityRepository);
        this.defaultActivityChainFactory = defaultActivityChainFactory;
        this.activityRepository = activityRepository1;
    }

    @Override
    public ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity entity) {
        return null;
    }

    @Override
    public String createSkuRechargeOrder(skuRechargeEntity entity) {
        // 1 参数校验
        Long sku = entity.getSku();
        String userId = entity.getUserId();
        String outBusinessNo = entity.getOutBusinessNo();
        log.info("{}",StringUtils.isNotBlank(userId));
        if (sku==null|| !StringUtils.isNotBlank(userId)||outBusinessNo==null){
            //todo 返回正确的信息
            throw new AppException("200","参数无效");
        }

        // 2 查询基础信息

        //2.1 通过sku查询活动信息
        ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
        //2.2 查询活动信息
        ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        //2.3 查询次数信息
        ActivityCountEntity activityCountEntity = queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());


        // 3 活动动作规则校验 //todo 后续继续处理规则过滤流程
        IActionChain actionChain = defaultActivityChainFactory.openActionChain();
        boolean success = actionChain.action(activitySkuEntity, activityEntity, activityCountEntity);

        // 4 构建订单聚合对象
        CreateOrderAggregate orderAggregate=buildOrderAggregate(entity,activitySkuEntity,activityEntity, activityCountEntity);
        // 5 保存订单
        doSaveOrder(orderAggregate);
        // 6 返回单号
        return orderAggregate.getActivityOrderEntity().getOrderId();
    }

    protected abstract CreateOrderAggregate buildOrderAggregate(skuRechargeEntity entity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);

    protected abstract void doSaveOrder(CreateOrderAggregate orderAggregate);


}
