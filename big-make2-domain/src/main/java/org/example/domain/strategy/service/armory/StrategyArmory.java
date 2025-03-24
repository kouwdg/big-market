package org.example.domain.strategy.service.armory;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.ActivitySkuEntity;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.model.entity.StrategyRuleEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.types.common.Constants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 实现策略装配
 * @date 2025/2/14 9:59
 */
@Service
@Slf4j
public class StrategyArmory implements IStrategyArmory,IStrategyDispatch {


    @Resource
    private IStrategyRepository strategyRepository;
    @Resource
    private IActivityRepository activityRepository;

    //抽奖概率装配
    @Override
    public boolean assembleLotteryStrategy(Long StrategyId) {
        //校验数据
        if (StrategyId == null) {
            //todo 抛出异常
            return false;
        }
        //根据id查询奖品的信息
        List<StrategyAwardEntity> entityList = strategyRepository.queryStrategyAwardList(StrategyId);

        // 缓存奖品库存
        for (StrategyAwardEntity entity : entityList) {
            Integer awardId = entity.getAwardId();
            //奖品的库存总量
            Integer awardCount = entity.getAwardCount();
            //放到把库存放redis中
            cacheStrategyAwardCount(StrategyId, awardId, awardCount);
        }


        //创建全量抽奖概率
        assembleLotteryStrategy(StrategyId.toString(), entityList);

        //查询策略规则 看是否有 rule_weight规则
        StrategyEntity strategyEntity = strategyRepository.queryStrategyByStrategyId(StrategyId);
        if (strategyEntity == null) return true;
        if (strategyEntity.getRuleWeight() == null) return true;

        //从strategyRule 取出 rule_weight的具体数据
        StrategyRuleEntity ruleEntity = strategyRepository.queryStrategyRuleByStrategyIdAndRuleModel(StrategyId, strategyEntity.getRuleWeight());
        Map<String, List<Integer>> ruleWeightValue = ruleEntity.getRuleWeight();
        Set<String> set = ruleWeightValue.keySet();
        for (String string : set) {
            // 101,102,103,105
            List<Integer> integers = ruleWeightValue.get(string);
            ArrayList<StrategyAwardEntity> entityListCopy = new ArrayList<>(entityList);
            entityListCopy.removeIf(entity -> !integers.contains(entity.getAwardId()));
            System.out.println("entityListCopy:  " + entityListCopy);
            String cacheKey = StrategyId + "_" + string;
            System.out.println("cacheKey:  " + cacheKey);
            assembleLotteryStrategy(cacheKey, entityListCopy);
        }

        return true;
    }

    @Override
    public boolean assembleActivitySkuByActivityId(Long ActivityId) {
        //根据 活动ID 查询出所有对应的 sku
        List<ActivitySkuEntity> skuEntityList = activityRepository.queryActivitySkuByActivityId(ActivityId);
        for (ActivitySkuEntity tem : skuEntityList) {
            cacheActivitySkuStockCount(tem.getSku(), tem.getStockCountSurplus());
            //预热 对应的活动次数 到缓存中
            activityRepository.queryRaffleActivityCountByActivityCountId(tem.getActivityCountId());
        }
        //预热 对应的活动 到缓存中
        activityRepository.queryRaffleActivityByActivityId(ActivityId);
        return true;
    }

    @Override
    public boolean assembleLotteryStrategyByActivityId(Long activityId) {
        Long strategyId = strategyRepository.queryStrategyIdByActivityId(activityId);
        return assembleLotteryStrategy(strategyId);
    }

    private void assembleLotteryStrategy(String RedisKey, List<StrategyAwardEntity> entityList) {
        // 获取最小概率值
        BigDecimal minAwardRate = entityList.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        //获得概率总和
        BigDecimal totalAwardRate = entityList.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //获得概率表大小
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        // 5. 生成策略奖品概率查找表「这里指需要在list集合中，存放上对应的奖品占位即可，占位越多等于概率越高」
        List<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());

        for (StrategyAwardEntity strategyAward : entityList) {
            Integer awardId = strategyAward.getAwardId();
            BigDecimal awardRate = strategyAward.getAwardRate().divide(totalAwardRate, 3, RoundingMode.CEILING);

            // 计算出每个概率值需要存放到查找表的数量，循环填充
            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }

        //6 乱序
        Collections.shuffle(strategyAwardSearchRateTables);

        //7 移动到hashMap中
        HashMap<Object, Object> hashMap = new HashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            System.out.println(i);
            hashMap.put(i, strategyAwardSearchRateTables.get(i));
        }
        // 8 存储到redis中
        strategyRepository.storeStrategyAwardSwarchRateTables(RedisKey, rateRange, hashMap);
    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        //获得表格范围
        int rateRange = strategyRepository.getRateRange(strategyId);
        //传入表格范围获得 随机值
        int i = new SecureRandom().nextInt(rateRange);
        log.info("随机值: {}", i);
        //读取redis
        return strategyRepository.getStrategyAwardAssemble(strategyId, i);
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        //1 获得表格范围
        int rateRange = strategyRepository.getRuleWeightRateRange(strategyId, ruleWeightValue);

        //2 获得随机值
        int i = new SecureRandom().nextInt(rateRange);

        //3 抽奖
        return strategyRepository.getStrategyAwardAssemble(strategyId, i, ruleWeightValue);
    }

    //扣减库存
    @Override
    public Boolean AbatementAwardStock(Long strategyId, Integer awardId) {
        //查询redis中的key
        String cacheKey = Constants.redisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        return strategyRepository.AbatementAwardStock(cacheKey);
    }
    private void cacheStrategyAwardCount(Long strategy, Integer awardId, Integer awardCount) {
        String cacheKey = Constants.redisKey.STRATEGY_AWARD_COUNT_KEY + strategy + Constants.UNDERLINE + awardId;
        strategyRepository.cacheStrategyAwardCount(cacheKey, awardCount);
    }

    private void cacheActivitySkuStockCount(Long sku, Integer stockCount) {

        String cache = Constants.redisKey.ACTIVITY_SKU_STOCK_COUNT + sku;
        //只存储 库存 到redis中
        activityRepository.cacheActivitySkuStockCount(cache, stockCount);
    }


}
