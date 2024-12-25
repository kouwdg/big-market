package com.cheng.domain.activity.service.armory;

import java.util.Date;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: sku活动库存扣减
 * @date 2024/12/24 14:43
 */
public interface IActivityDispatch {

    boolean subtractionActivitySkuStock(Long sku, Date endDateTime);
}
