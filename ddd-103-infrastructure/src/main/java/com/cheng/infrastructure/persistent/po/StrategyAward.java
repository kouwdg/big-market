package com.cheng.infrastructure.persistent.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class StrategyAward {


            //自增Id
        private Long id;
            //抽奖策略Id
        private Long strategyId;
            //奖品Id
        private Integer awardId;
            //抽奖奖品的标题
        private String awardTitle;
            //抽奖奖品的副标题
        private String awardSubtitle;
            //奖品的库存总量
        private Integer awardCount;
            //奖品的库存
        private Integer awardCountSurplus;
            //奖品的中奖概率
        private BigDecimal awardRate;
            //rule里面的规则目录
        private String ruleModels;
            //排序
        private Integer sort;
            //创建时间
        private Date createTime;
            //更新时间
        private Date updateTime;

}
