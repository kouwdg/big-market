package org.example.infrastructure.repository;


import lombok.extern.slf4j.Slf4j;

import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.model.entity.StrategyRuleEntity;
import org.example.domain.strategy.model.vo.RuleWeightVo;
import org.example.domain.strategy.model.vo.StrategyAwardRuleModelVo;
import org.example.domain.strategy.model.vo.StrategyAwardStockKeyVo;
import org.example.domain.strategy.model.vo.ruleTree.*;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.example.infrastructure.persistent.dao.*;
import org.example.infrastructure.persistent.po.*;
import org.example.infrastructure.persistent.redis.IRedisService;

import org.example.types.common.Constants;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;


//仓储层 负责调用dao层进行实现
@Service
@Slf4j
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IRaffleActivityDao raffleActivityDao;

    @Resource
    private IStrategyAwardDao strategyAwardDao;
    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IRedisService redisService;

    @Resource
    private IRuleTreeNodeLineDao ruleTreeNodeLineDao;
    @Resource
    private IRuleTreeDao ruleTreeDao;

    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;


    /**
     *  查询策略对应的奖品信息
     * @param strategyId 策略ID
     * @return
     */
    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        //优先从数据库中获取 奖品列表
        String cacheKey = Constants.redisKey.STRATEGY_AWARD_LIST_KEY + strategyId;
        List<StrategyAwardEntity> entityList = redisService.getValue(cacheKey);
        if (entityList != null) {
            log.info("从redis中获取结果");
            return entityList;
        }

        //从数据库中读取数据
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);

        entityList = new ArrayList<>(strategyAwards.size());

        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity entity =
                    StrategyAwardEntity.builder()
                            .strategyId(strategyAward.getStrategyId())
                            .awardId(strategyAward.getAwardId())
                            .awardCount(strategyAward.getAwardCount())
                            .awardCountSurplus(strategyAward.getAwardCountSurplus())
                            .awardRate(strategyAward.getAwardRate())
                            .sort(strategyAward.getSort())
                            .awardTitle(strategyAward.getAwardTitle())
                            .ruleModels(strategyAward.getRuleModels())
                            .awardSubtitle(strategyAward.getAwardSubtitle())
                            .build();
            entityList.add(entity);
        }
        redisService.setValue(cacheKey, entityList);
        return entityList;
    }


    /**
     * 将 hashMap存入redis
     *
     * @param
     * @param rateRange
     * @param hashMap
     */
    @Override
    public void storeStrategyAwardSwarchRateTables(String strategyId, BigDecimal rateRange, HashMap hashMap) {

        redisService.setValue(Constants.redisKey.STRATEGY_RATE_RANGE_KEY + strategyId, rateRange.intValue());
        String cacheKey = Constants.redisKey.STRATEGY_RATE_TABLE_KEY + strategyId;
        RMap<Object, Object> map = redisService.getMap(cacheKey);
        //先清除
        map.clear();
        //再加入
        map.putAll(hashMap);
    }

    @Override
    public List<StrategyRuleEntity> queryStrategyRuleByStrategyId(Long strategyId) {
        return null;
    }


    @Override
    public StrategyEntity queryStrategyByStrategyId(Long strategyId) {
        Strategy strategies = strategyDao.queryByStrategyId(strategyId);
        return StrategyEntity.builder()
                .strategyId(strategies.getStrategyId())
                .strategyDesc(strategies.getStrategyDesc())
                .ruleModels(strategies.getRuleModels())
                .build();
    }

    /**
     * 查询strategyRule
     * @param strategyId
     * @param ruleModel
     * @return
     */
    @Override
    public StrategyRuleEntity queryStrategyRuleByStrategyIdAndRuleModel(Long strategyId, String ruleModel) {
        StrategyRule rule = strategyRuleDao.queryStrategyRuleByStrategyIdAndRuleModel(strategyId, ruleModel);
        if (rule==null)return null;
       return StrategyRuleEntity.builder()
                .strategyId(rule.getStrategyId())
                .awardId(rule.getAwardId())
                .ruleType(rule.getRuleType())
                .ruleModel(rule.getRuleModel())
                .ruleValue(rule.getRuleValue())
                .ruleDesc(rule.getRuleDesc())
                .build();
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel) {
        StrategyRule rule = new StrategyRule();
        rule.setStrategyId(strategyId);
        rule.setAwardId(awardId);
        rule.setRuleModel(ruleModel);
        StrategyRule result = strategyRuleDao.queryStrategyRuleValue(rule);
        if(result==null)return null;
        return result.getRuleValue();
    }


    //查询StrategyRule中的ruleValue值
    @Override
    public String queryStrategyRuleValue(Long strategyId, String s) {
        return queryStrategyRuleValue(strategyId,null,s);
    }

    @Override
    public int getRateRange(Long strategyId) {
        String cacheKey = Constants.redisKey.STRATEGY_RATE_RANGE_KEY + strategyId;
        return redisService.getValue(cacheKey);
    }

    @Override
    public int getStrategyAwardAssemble(Long strategyId, int rateKey) {
        String cacheKey = Constants.redisKey.STRATEGY_RATE_TABLE_KEY + strategyId;
        Object fromMap = redisService.getFromMap(cacheKey, rateKey);
        String string = fromMap.toString();
        return Integer.parseInt(string);
    }

    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, int rateKey, String ruleWeightValue) {
        //抽奖
        String cacheKey = Constants.redisKey.STRATEGY_RATE_TABLE_KEY + strategyId + "_" + ruleWeightValue;
        Object fromMap = redisService.getFromMap(cacheKey, rateKey);
        String string = fromMap.toString();
        return Integer.parseInt(string);
    }

    @Override
    public String getScoreRange(Long strategyId, Long userSoure) {
        String ruleWeightValue = String.valueOf(userSoure);

        // 1.1 查询 幸运值 的 分级
        String ruleModel = DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode();
        StrategyRule rule = new StrategyRule();
        rule.setRuleModel(ruleModel);
        rule.setStrategyId(strategyId);
        StrategyRule result = strategyRuleDao.queryStrategyRuleValue(rule);
        String ruleValue = result.getRuleValue();

        // 2 分隔 将string变成数组 4000:101,102,103,105 5000:106,107,108 6000:101,102,103,105,106,107,108,109  变成 [4000,5000,6000]
        List<Integer> Scores = new ArrayList<>();
        //4000:101,102,103,105
        String[] split = ruleValue.split(Constants.BLANK);
        for (String s : split) {
            //4000 101,102,103,105
            String[] split1 = s.split(Constants.COLON);
            String score = split1[0];
            Scores.add(Integer.parseInt(score));
        }

        //score=[4000,5000,6000]
        //3 排序 看ruleWeightValue属于哪一个
        Collections.sort(Scores);

        //没有到达幸运者要求
        if (Integer.parseInt(ruleWeightValue) < Scores.get(0)) {
            return null;
        }
        String RealRuleWeightValue = null;
        for (Integer score : Scores) {
            if (Integer.parseInt(ruleWeightValue) >= score) {
                RealRuleWeightValue = String.valueOf(score);
            }
        }
        return RealRuleWeightValue;
    }

    //根据幸运值获取抽奖范围
    @Override
    public int getRuleWeightRateRange(Long strategyId, String ruleWeightValue) {
        String cacheKey = Constants.redisKey.STRATEGY_RATE_RANGE_KEY + strategyId + "_" + ruleWeightValue;
        return redisService.getValue(cacheKey);
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        //优先去缓存里面去拿
        String cacheKey = Constants.redisKey.STRATEGY_ENTITY_KEY + strategyId;
        StrategyEntity entity = null;
        entity = redisService.getValue(cacheKey);
        if (entity != null) return entity;

        Strategy strategy = strategyDao.queryByStrategyId(strategyId);
        log.info("strategy:{}", strategy);
        entity = StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .strategyDesc(strategy.getStrategyDesc())
                .ruleModels(strategy.getRuleModels())
                .build();
        redisService.setValue(cacheKey, entity);
        return entity;
    }


    //查看奖品加锁次数
    @Override
    public Integer queryAwardLockCount(Long strategyId, Integer awardId) {
        String ruleModel = strategyAwardDao.queryRuleModel(strategyId, awardId);
        if (ruleModel == null) return null;
        String count = ruleTreeNodeDao.queryRuleLocksCount(ruleModel);
        if (count==null) return null;
        return Integer.parseInt(count);
    }


    //根据treeId查询并格式化 ruleTree
    @Override
    public RuleTreeVO queryRuleTreeVoByTreeId(String treeId) {
        RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId(treeId);
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDao.queryRuleTreeNodeListByTreeId(treeId);
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId(treeId);
        // 1. tree node line 转换Map结构

        Map<String, List<RuleTreeNodeLineVO>> ruleTreeNodeLineMap = new HashMap<>();
        for (RuleTreeNodeLine ruleTreeNodeLine : ruleTreeNodeLines) {
            RuleTreeNodeLineVO vo = RuleTreeNodeLineVO.builder()
                    .treeId(ruleTreeNodeLine.getTreeId())
                    .ruleNodeFrom(ruleTreeNodeLine.getRuleNodeFrom())
                    .ruleNodeTo(ruleTreeNodeLine.getRuleNodeTo())
                    .ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
                    .ruleLimitValue(RuleLogicCheckTypeVo.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
                    .build();
            //先查询map中是否有 vo from的key作为健的 如果没有就创建一个空列表
            List<RuleTreeNodeLineVO> ruleTreeNodeLineVOS =
                    ruleTreeNodeLineMap.computeIfAbsent(vo.getRuleNodeFrom(), k -> new ArrayList<>());
            ruleTreeNodeLineVOS.add(vo);
        }

        // 2. tree node 转换为Map结构
        Map<String, RuleTreeNodeVO> treeNodeMap = new HashMap<>();
        for (RuleTreeNode ruleTreeNode : ruleTreeNodes) {
            RuleTreeNodeVO ruleTreeNodeVO = RuleTreeNodeVO.builder()
                    .treeId(ruleTreeNode.getTreeId())
                    .ruleKey(ruleTreeNode.getRuleKey())
                    .ruleDesc(ruleTreeNode.getRuleDesc())
                    .ruleValue(ruleTreeNode.getRuleValue())
                    .treeNodeLineVOList(ruleTreeNodeLineMap.get(ruleTreeNode.getRuleKey()))
                    .build();
            treeNodeMap.put(ruleTreeNode.getRuleKey(), ruleTreeNodeVO);
        }
        log.info("数据库查询的ruleTree:{}", ruleTree);

        // 3. 构建 Rule Tree
        RuleTreeVO ruleTreeVODB = RuleTreeVO.builder()
                .treeId(ruleTree.getTreeId())
                .treeName(ruleTree.getTreeName())
                .treeDesc(ruleTree.getTreeDesc())
                .treeRootRuleNode(ruleTree.getTreeRootRuleKey())
                .treeNodeMap(treeNodeMap)
                .build();

        return ruleTreeVODB;
    }

    //扣减库存
    @Override
    public Boolean AbatementAwardStock(String cacheKey) {
        long decr = redisService.decr(cacheKey);
        if (decr<0){
            redisService.setValue(cacheKey,0);
            return false;
        }

        //加锁
        String lockKey=cacheKey+Constants.UNDERLINE+decr;
        Boolean lock = redisService.setNx(lockKey);
        if(!lock){
            //todo 抛出异常 加锁失败
            log.info("加锁失败");
        }
      return lock;
    }

    @Override
    public void cacheStrategyAwardCount(String cacheKey, Integer awardCount) {
        Long cacheAwardCount = redisService.getAtomicLong(cacheKey);
        if (cacheAwardCount != null) {
            redisService.setAtomicLong(cacheKey, awardCount);
        }
    }

    //写入延迟队列
    @Override
    public void awardStockConsumeSendQueue(StrategyAwardStockKeyVo build) {
        String cacheKey = Constants.redisKey.STRATEGY_AWARD_COUNT_QUERY_KEY;

        //创建队列
        RBlockingQueue<StrategyAwardStockKeyVo> blockingQueue = redisService.getBlockingQueue(cacheKey);
        //延迟队列
        RDelayedQueue<StrategyAwardStockKeyVo> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        //填入信息
        delayedQueue.offer(build,3, TimeUnit.SECONDS);
    }

    //获得延迟队列
    @Override
    public StrategyAwardStockKeyVo takeQueryValue() {
        String cacheKey = Constants.redisKey.STRATEGY_AWARD_COUNT_QUERY_KEY;
        RBlockingQueue<StrategyAwardStockKeyVo> blockingQueue = redisService.getBlockingQueue(cacheKey);
        return blockingQueue.poll();
    }

    @Override
    public void updateStrategyAwardStock(Long strategy, Integer award) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setAwardId(award);
        strategyAward.setStrategyId(strategy);
        strategyAwardDao.DescAwardStock(strategyAward);
    }

    //根据活动ID查询策略ID
    @Override
    public Long queryStrategyIdByActivityId(Long activityId) {
        return raffleActivityDao.queryStrategyIdByActivityId(activityId);
    }

    @Override
    public Map<String, Integer> queryAwardRuleLockCount(String[] treeIds) {
        if (treeIds == null || treeIds.length == 0) return new HashMap<>();
        List<RuleTreeNode> temList = ruleTreeNodeDao.queryRuleLocks(treeIds);
        Map<String, Integer> resultMap = new HashMap<>();
        for (RuleTreeNode tem : temList) {
            resultMap.put(tem.getTreeId(), Integer.parseInt(tem.getRuleValue()));
        }
        return resultMap;
    }

    @Override
    public StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId) {
        //优先从缓存中获取
        String cacheKey = Constants.redisKey.STRATEGY_AWARD_KEY + strategyId + Constants.UNDERLINE + awardId;
        log.info("cacheKey:{}", cacheKey);
        StrategyAwardEntity value = redisService.getValue(cacheKey);
        if (value != null) {
            log.info("test 缓存中获取 {}", value);
            return value;
        }
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setAwardId(awardId);
        strategyAward.setStrategyId(strategyId);
        StrategyAward entity = strategyAwardDao.queryStrategyAward(strategyAward);

        StrategyAwardEntity result = StrategyAwardEntity.builder()
                .strategyId(entity.getStrategyId())
                .awardId(entity.getAwardId())
                .awardCount(entity.getAwardCount())
                .awardCountSurplus(entity.getAwardCountSurplus())
                .awardRate(entity.getAwardRate())
                .sort(entity.getSort())
                .awardTitle(entity.getAwardTitle())
                .awardSubtitle(entity.getAwardSubtitle())
                .build();

        //放入缓存
        redisService.setValue(cacheKey, result);
        return result;
    }

    //查看奖品对应的ruleModel
    @Override
    public StrategyAwardRuleModelVo queryStrategyAwardRuleModel(Long strategyId, Integer awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setStrategyId(strategyId);
        strategyAward.setAwardId(awardId);
        String ruleModel = strategyAwardDao.queryStrategyAwardRuleModel(strategyAward);
        //这个奖品没有定义规则
        if (ruleModel == null) {
            return null;
        }
        log.info("查询出来的规则:{}", ruleModel);
        return StrategyAwardRuleModelVo.builder().ruleModel(ruleModel).build();
    }

    //查询 ruleWeight的信息
    @Override
    public List<RuleWeightVo> queryAwardRuleWeight(Long strategyId) {
        //优先从缓存中获取
        String cacheKey=Constants.redisKey.RULE_WEIGHT_VALUE+ strategyId;
        List<RuleWeightVo> value = redisService.getValue(cacheKey);
        if (value!=null){
            return value;
        }

        List<RuleWeightVo> resultList=new ArrayList<>();
        //查询rule_weight对应的值
        StrategyRule entity = new StrategyRule();
        entity.setStrategyId(strategyId);
        entity.setRuleModel(DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode());
        StrategyRule strategyRule = strategyRuleDao.queryStrategyRuleValue(entity);

        StrategyRuleEntity strategyRuleEntity = new StrategyRuleEntity();
        strategyRuleEntity.setRuleModel(DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode());
        strategyRuleEntity.setRuleValue(strategyRule.getRuleValue());
        Map<String, List<Integer>> ruleWeight = strategyRuleEntity.getRuleWeight();

        Set<String> ruleWeightKeyS = ruleWeight.keySet();


        Map<Integer, StrategyAward> awardMap = queryAllAwardByStrategyId(strategyId);

        for (String key : ruleWeightKeyS) {
            List<Integer> awardIds = ruleWeight.get(key);
            String stringAwardIds=convertListToString(awardIds);
            //根据awardIds获得奖品列表
                //1 查询对应策略的全部奖品
            List<RuleWeightVo.Award> awardList=new ArrayList<>();
            for (Integer awardId : awardIds) {
                RuleWeightVo.Award tem = RuleWeightVo.Award.builder()
                        .awardId(awardId)
                        .awardTitle(awardMap.get(awardId).getAwardTitle())
                        .build();
                awardList.add(tem);
            }
            RuleWeightVo result = RuleWeightVo.builder()
                    .ruleValue(key + ":" + stringAwardIds)
                    .awardIds(awardIds)
                    .weight(Integer.parseInt(key))
                    .awardList(awardList)
                    .build();
            resultList.add(result);
        }

        redisService.setValue(cacheKey,resultList);
        return resultList;
    }
    //查询分级

    @Override
    public String getScoreRangeByCount(Long strategyId, Integer count) {

        // 1.1 查询 幸运值 的 分级
        String ruleModel = DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode();
        StrategyRule rule = new StrategyRule();
        rule.setRuleModel(ruleModel);
        rule.setStrategyId(strategyId);
        StrategyRule result = strategyRuleDao.queryStrategyRuleValue(rule);
        String ruleValue = result.getRuleValue();

        // 2 分隔 将string变成数组 4000:101,102,103,105 5000:106,107,108 6000:101,102,103,105,106,107,108,109  变成 [4000,5000,6000]
        List<Integer> Scores = new ArrayList<>();
        //4000:101,102,103,105
        String[] split = ruleValue.split(Constants.BLANK);
        for (String s : split) {
            //4000 101,102,103,105
            String[] split1 = s.split(Constants.COLON);
            String score = split1[0];
            Scores.add(Integer.parseInt(score));
        }

        //3 Scres= [4000 5000 6000]
        for (Integer score : Scores) {
            if (count==score){
                return String.valueOf(count);
            }
        }
        return null;
    }

    private static String convertListToString(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    private  Map<Integer,StrategyAward> queryAllAwardByStrategyId(Long strategyId){
        //缓存到redis
        String cacheKey=Constants.redisKey.STRATEGY_AWARD_ALL_MAP+strategyId;
        Map<Integer,StrategyAward> value = redisService.getValue(cacheKey);
        if (value!=null){
            return value;
        }
        List<StrategyAward> strategyAwards = strategyAwardDao.queryByStrategyId(strategyId);
        if (strategyAwards==null){
            return null;
        }

        Map<Integer, StrategyAward> resultMap = new HashMap<>();
        for (StrategyAward strategyAward : strategyAwards) {
            resultMap.put(strategyAward.getAwardId(),strategyAward);
        }
        redisService.setValue(cacheKey,resultMap);
        return resultMap;
    }

}
