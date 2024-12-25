package com.cheng.domain.activity.service;

import com.cheng.domain.activity.model.vo.ActivitySkuStockKeyVo;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 处理sku库存接口
 * @date 2024/12/25 15:28
 */
public interface ISkuStock {

    /**
     *
     * @return
     * @throws InterruptedException
     */
    ActivitySkuStockKeyVo takeQueueValue()throws InterruptedException;

    /**
     * 清空队列
     */
    void clearQueueValue();

    /**
     * 根据延迟队列更新sku库存
     * @param sku
     */
    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);
}
