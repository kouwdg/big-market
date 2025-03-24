package org.example.domain.activity.service.skuStock;

import org.example.domain.activity.model.vo.ActivitySkuStockKeyVo;
import org.example.domain.activity.repository.IActivityRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/21 14:11
 */
@Service
public class SkuStock implements ISkuStock{
    @Resource
    private IActivityRepository activityRepository;
    @Override
    public ActivitySkuStockKeyVo takeQueueValue() throws InterruptedException {
        return activityRepository.takeQueueValue();
    }

    @Override
    public void clearQueueValue() {
        activityRepository.clearQueueValue();
    }

    @Override
    public void updateActivitySkuStock(Long sku) {
        activityRepository.updateActivitySkuStock(sku);
    }

    @Override
    public void clearActivitySkuStock(Long sku) {

    }
}
