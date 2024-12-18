package com.cheng.domain.strategy.service.armory;

import com.cheng.domain.strategy.model.entity.StrategyAwardEntity;
import com.cheng.domain.strategy.model.entity.StrategyEntity;
import com.cheng.domain.strategy.model.entity.StrategyRuleEntity;
import com.cheng.domain.strategy.repository.IStrategyRepository;
import com.cheng.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

//策略 装配的库(工厂)

@Slf4j
@Service
public class StrategyArmory implements IStrategyArmory,IStrategyDispatch{

    @Resource
    private IStrategyRepository repository;


    @Override
    public boolean assembleLotteryStrategy(Long strategyId){
        log.info("a");
        //查询策略中包含的抽奖奖品的信息
        List<StrategyAwardEntity> entityList=repository.queryStrategyAwardList(strategyId);

        //todo 新加的东西  缓存奖品库存
        for (StrategyAwardEntity entity : entityList) {
            Integer awardId = entity.getAwardId();
            //奖品的库存总量
            Integer awardCount = entity.getAwardCount();

            //放到把库存放redis中
            cacheStrategyAwardCount(strategyId,awardId,awardCount);
        }



        //根据策略id 与 抽奖奖品的信息 创建原始奖品表 (全量抽奖概率)
        assembleLotteryStrategy(strategyId.toString(),entityList);


        //查询策略配置 主要是将 ruleModel查出来
        StrategyEntity strategyEntity=repository.queryStrategyEntityByStrategyId(strategyId);
        log.info("StrategyEntity:{}",strategyEntity);
        if(strategyEntity==null) return true;

        if(strategyEntity.getRuleWeight()==null) return true;

        //查看具体的策略规则
        StrategyRuleEntity ruleEntity=repository.queryStrategyRuleByStrategyIdAndRuleModel(strategyId,strategyEntity.getRuleWeight());

        //提取出 ruleEntity中的ruleModel的RuleWeight规则
        Map<String, List<Integer>> ruleWeightValue = ruleEntity.getRuleWeight();
        Set<String> set = ruleWeightValue.keySet();
        for (String string : set) {
            // 101,102,103,105
            List<Integer> integers = ruleWeightValue.get(string);
            ArrayList<StrategyAwardEntity>entityListCopy  = new ArrayList<>(entityList);
            entityListCopy.removeIf(entity->!integers.contains(entity.getAwardId()));
            System.out.println("entityListCopy:  "+entityListCopy);
            String cacheKey=strategyId+"_"+string;
            System.out.println("cacheKey:  "+cacheKey);
            assembleLotteryStrategy(cacheKey,entityListCopy);
        }
        return true;
    }

    //库存扣减
    @Override
    public Boolean subtractionAwardStock(Long strategyId, Integer awardId) {
        String cacheKey=Constants.redisKey.STRATEGY_AWARD_COUNT_KEY+strategyId+Constants.UNDERLINE+awardId;
        System.out.println(cacheKey);
        return repository.subtractionAwardStock(cacheKey);
    }


    //将 抽奖表格放到redis中
    private void assembleLotteryStrategy(String key, List<StrategyAwardEntity> entityList){


        // 获取最小概率值
        BigDecimal minAwardRate=entityList.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        //获取概率总和
        BigDecimal totalAwardRate=entityList.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO,BigDecimal::add);


        // 4. 用 1 % 0.0001 获得概率范围，百分位、千分位、万分位
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);
        // 5. 生成策略奖品概率查找表「这里指需要在list集合中，存放上对应的奖品占位即可，占位越多等于概率越高」
        List<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAward : entityList) {
            Integer awardId = strategyAward.getAwardId();
            BigDecimal awardRate = strategyAward.getAwardRate().divide(totalAwardRate,3,RoundingMode.CEILING);

            // 计算出每个概率值需要存放到查找表的数量，循环填充
            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }

        //6 乱序
        Collections.shuffle(strategyAwardSearchRateTables);

        //7
        HashMap<Object, Object> hashMap = new HashMap<>();
        for(int i=0;i<strategyAwardSearchRateTables.size();i++){
            System.out.println(i);
            hashMap.put(i,strategyAwardSearchRateTables.get(i));
        }

        // 8 存储到redis中
        repository.storeStrategyAwardSwarchRateTables(key,rateRange,hashMap);
    }

    //普通抽奖
    @Override
    public Integer getRandomAwardId(Long strategyId) {
        //获得表格范围
        int rateRange=repository.getRateRange(strategyId);
        //传入表格范围获得 随机值
        int i = new SecureRandom().nextInt(rateRange);
        log.info("随机值: {}",i);
        //读取redis
        return repository.getStrategyAwardAssemble(strategyId,i);
    }

    //待权重的抽奖
    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        //1 获得表格范围
        int rateRange=repository.getRuleWeightRateRange(strategyId,ruleWeightValue);

        //2 获得随机值
        int i = new SecureRandom().nextInt(rateRange);

        //3 抽奖
        return repository.getStrategyAwardAssemble(strategyId,i,ruleWeightValue);
    }


    private void cacheStrategyAwardCount(Long strategy,Integer awardId,Integer awardCount){
        String cacheKey=Constants.redisKey.STRATEGY_AWARD_COUNT_KEY+strategy+Constants.UNDERLINE+awardId;
        repository.cacheStrategyAwardCount(cacheKey,awardCount);
    }

}
