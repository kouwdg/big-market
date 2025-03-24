package org.example.domain.activity.service.RaffleOrder;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.activity.model.aggregate.CreateOrderAggregate;
import org.example.domain.activity.model.entity.*;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.RaffleOrder.rule.chain.IActionChain;
import org.example.domain.activity.service.RaffleOrder.rule.chain.factory.DefaultActivityChainFactory;
import org.example.domain.rebate.model.entity.skuRechargeEntity;
import org.example.types.exception.AppException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/21 11:24
 */

@Service
@Slf4j
public class RalffleOrder implements IRaffleOrder{
    @Resource
    private DefaultActivityChainFactory defaultActivityChainFactory;

    @Resource
    private IActivityRepository activityRepository;
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
        log.info("{}", StringUtils.isNotBlank(userId));
        if (sku==null|| !StringUtils.isNotBlank(userId)||outBusinessNo==null){
            //todo 返回正确的信息
            throw new AppException("200","参数无效");
        }

        // 2 查询基础信息

        //2.1 通过sku查询活动信息
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(sku);
        //2.2 查询活动信息
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        //2.3 查询次数信息
        ActivityCountEntity activityCountEntity = activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());


        // 3 活动动作规则校验 //todo 后续继续处理规则过滤流程
        IActionChain actionChain = defaultActivityChainFactory.openActionChain();
        actionChain.action(activitySkuEntity, activityEntity, activityCountEntity);

        // 4 构建订单聚合对象
        CreateOrderAggregate orderAggregate= activityRepository.buildOrderAggregate(entity,activitySkuEntity,activityEntity, activityCountEntity);
        // 5 保存订单
        activityRepository.doSaveOrder(orderAggregate);
        // 6 返回单号
        return orderAggregate.getActivityOrderEntity().getOrderId();
    }
}
