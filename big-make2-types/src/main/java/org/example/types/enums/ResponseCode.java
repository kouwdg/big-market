package org.example.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "成功"),
    UN_ERROR("0001", "未知失败"),
    ILLEGAL_PARAMETER("0002", "非法参数"),
    RULE_WRIGHT("0003","rule_weight格式不正确"),
    ACTIVITY_DATE_ERROR("ERR_BIZ_004","非活动日期范围"),
    ACTIVITY_SKU_STOCK_ERROR("ERR_BIZ_005","活动库存不足"),
    ACTIVITY_STATUS_OPEN("ERR_BIZ_006","活动未开启"),
    ACCOUNT_QUOTA_ERROR("ERR_BIZ_006","账户总额度不足"),

    ACCOUNT_MONTH_QUOTA_ERROR("ERR_BIZ_007","账户月额度不足"),
    ACCOUNT_DAY_QUOTA_ERROR("ERR_BIZ_008","账户日额度不足"),
    ACTIVITY_ORDER_ERROR("ERROR_BIZ_009","用户抽奖单已使用"),
    ACCOUNT_UN_EXIST("ERR_BIZ_0010","账户不存在"),
    USER_OR_ACTIVITY_NOT_EXIST("ERR_BIZ_0011","用户或活动不存在"),
    ;

    private String code;
    private String info;

}
