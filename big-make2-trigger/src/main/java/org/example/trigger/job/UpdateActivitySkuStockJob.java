package org.example.trigger.job;


import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.vo.ActivitySkuStockKeyVo;
import org.example.domain.activity.service.skuStock.ISkuStock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 更新活动sku库存任务(处理延迟队列)
 * @create 2024-03-30 09:52
 */
@Slf4j
@Component()
public class UpdateActivitySkuStockJob {
    @Resource
    private ISkuStock skuStock;
    @Scheduled(cron = "0/10 * * * * ?")
    public void exec() {
        try {
            log.info("定时任务，更新活动sku库存");
            ActivitySkuStockKeyVo activitySkuStockKeyVO = skuStock.takeQueueValue();
            if (null == activitySkuStockKeyVO) {
                log.info("sku库存无需更新");
                return;
            };
            log.info("定时任务，更新活动sku库存 sku:{} activityId:{}", activitySkuStockKeyVO.getSku(), activitySkuStockKeyVO.getActivityId());
            skuStock.updateActivitySkuStock(activitySkuStockKeyVO.getSku());
        } catch (Exception e) {
            log.error("定时任务，更新活动sku库存失败", e);
        }
    }
}