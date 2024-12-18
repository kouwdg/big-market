package com.cheng.infrastructure.persistent.repository;

import com.cheng.domain.strategy.model.entity.StrategyAwardEntity;
import com.cheng.domain.strategy.model.entity.StrategyEntity;
import com.cheng.domain.strategy.model.entity.StrategyRuleEntity;
import com.cheng.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import com.cheng.domain.strategy.model.vo.StrategyAwardRuleModelVo;
import com.cheng.domain.strategy.model.vo.StrategyAwardStockKeyVo;
import com.cheng.domain.strategy.model.vo.ruleTree.RuleLimitTypeVO;
import com.cheng.domain.strategy.model.vo.ruleTree.RuleTreeNodeLineVO;
import com.cheng.domain.strategy.model.vo.ruleTree.RuleTreeNodeVO;
import com.cheng.domain.strategy.model.vo.ruleTree.RuleTreeVO;
import com.cheng.domain.strategy.repository.IStrategyRepository;
import com.cheng.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.cheng.infrastructure.persistent.dao.*;
import com.cheng.infrastructure.persistent.po.*;
import com.cheng.infrastructure.persistent.redis.IRedisService;
import com.cheng.types.common.Constants;
import com.cheng.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.print.attribute.standard.Destination;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

//仓储层 负责调用dao层进行实现
@Service
@Slf4j
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyRuleDao strategyRuleDao;
    @Resource
    private IRedisService redisService;

    @Resource
    private IRuleTreeDao ruleTreeDao;

    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;

    @Resource
    private IRuleTreeNodeLineDao ruleTreeNodeLineDao;


    //根据策略Id查询奖品列表
    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {

        //优先从数据库中获取 奖品列表
        String cacheKey=Constants.redisKey.STRATEGY_AWARD_LIST_KEY+strategyId;
        List<StrategyAwardEntity> entityList=redisService.getValue(cacheKey);
        if (entityList!=null&&!entityList.isEmpty())return  entityList;

        //从数据库中读取数据
        List<StrategyAward> strategyAwards=strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);

        entityList=new ArrayList<>(strategyAwards.size());

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
                      .awardSubtitle(strategyAward.getAwardSubtitle())
                      .build();
           entityList.add(entity);
        }
        redisService.setValue(cacheKey,entityList);
        return entityList;
    }

    @Override
    public void storeStrategyAwardSwarchRateTables(String strategyId, BigDecimal rateRange, HashMap<Object, Object> hashMap) {
        redisService.setValue(Constants.redisKey.STRATEGY_RATE_RANGE_KEY+strategyId,rateRange.intValue());

        String cacheKey=Constants.redisKey.STRATEGY_RATE_TABLE_KEY + strategyId;
        RMap<Object, Object> map = redisService.getMap(cacheKey);

        //先清除
        map.clear();
        //再加入
        map.putAll(hashMap);
    }

    @Override
    public int getRateRange(Long strategyId) {
        String cacheKey=Constants.redisKey.STRATEGY_RATE_RANGE_KEY + strategyId;
        return redisService.getValue(cacheKey);
    }


    //根据幸运值获取抽奖范围
    @Override
    public int getRuleWeightRateRange(Long strategyId, String ruleWeightValue) {
        String cacheKey=Constants.redisKey.STRATEGY_RATE_RANGE_KEY + strategyId+"_"+ruleWeightValue;
        return redisService.getValue(cacheKey);
    }

    //获得 rateKey上对应的奖品
    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, int rateKey) {

        String cacheKey=Constants.redisKey.STRATEGY_RATE_TABLE_KEY+strategyId;
        Object fromMap = redisService.getFromMap(cacheKey, rateKey);
        String string = fromMap.toString();
        return Integer.parseInt(string);
    }

    //根据权重选择抽奖的接口
    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, int rateKey, String ruleWeightValue) {
        //抽奖
        String cacheKey=Constants.redisKey.STRATEGY_RATE_TABLE_KEY+strategyId+"_"+ruleWeightValue;
        Object fromMap = redisService.getFromMap(cacheKey, rateKey);
        String string = fromMap.toString();
        return Integer.parseInt(string);
    }

    //查询Strategy
    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {

        //优先去缓存里面去拿
        String cacheKey=Constants.redisKey.STRATEGY_ENTITY_KEY+strategyId;
        StrategyEntity entity=null;
        entity= redisService.getValue(cacheKey);
        if (entity!=null) return entity;

        Strategy strategy=strategyDao.queryStrategyByStrategyId(strategyId);
        log.info("strategy:{}",strategy);
        entity=StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .strategyDesc(strategy.getStrategyDesc())
                .ruleModels(strategy.getRuleModels())
                .build();
        redisService.setValue(cacheKey,entity);
        return entity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRuleByStrategyIdAndRuleModel(Long strategyId, String ruleModel) {
        log.info("策略ID:{}",strategyId);
        log.info("规则名字:{}",ruleModel);
        strategyRule rule=strategyRuleDao.queryStrategyRuleByStrategyIdAndRuleModel(strategyId,ruleModel);
        if(rule==null){
           throw new AppException("2000","数据库数据错误");
        }
        System.out.println(rule);
        StrategyRuleEntity ruleEntity = StrategyRuleEntity.builder()
                .strategyId(rule.getStrategyId())
                .awardId(rule.getAwardId())
                .ruleType(rule.getRuleType())
                .ruleModel(rule.getRuleModel())
                .ruleValue(rule.getRuleValue())
                .ruleDesc(rule.getRuleDesc())
                .build();
        return ruleEntity;
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel) {

        strategyRule rule = new strategyRule();
        rule.setStrategyId(strategyId);
        rule.setAwardId(awardId);
        rule.setRuleModel(ruleModel);
        strategyRule result=strategyRuleDao.queryStrategyRuleValue(rule);
        return result.getRuleValue();
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, String ruleModel) {
        return queryStrategyRuleValue(strategyId,null,ruleModel);
    }

    @Override
    public String getScoreRange(Long strategyId,Long userSoure) {
        String ruleWeightValue=String.valueOf(userSoure);

        // 1.1 查询 幸运值 的 分级
        String ruleModel= DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode();
        strategyRule rule = new strategyRule();
        rule.setRuleModel(ruleModel);
        rule.setStrategyId(strategyId);
        strategyRule result = strategyRuleDao.queryStrategyRuleValue(rule);
        String ruleValue = result.getRuleValue();

        // 2 分隔 将string变成数组 4000:101,102,103,105 5000:106,107,108 6000:101,102,103,105,106,107,108,109  变成 [4000,5000,6000]
        List<Integer> Scores=new ArrayList<>();
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
        if(Integer.parseInt(ruleWeightValue)<Scores.get(0)){
            return null;
        }
        String RealRuleWeightValue=null;
        for (Integer score : Scores) {
            if(Integer.parseInt(ruleWeightValue)>=score){
                RealRuleWeightValue=String.valueOf(score);
            }
        }
        return RealRuleWeightValue;
    }

    @Override
    public StrategyAwardRuleModelVo queryStrategyAwardRuleModel(Long strategyId, Integer awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setStrategyId(strategyId);
        strategyAward.setAwardId(awardId);
        String ruleModel=strategyAwardDao.queryStrategyAwardRuleModel(strategyAward);
        //这个奖品没有定义规则
        if (ruleModel==null){
            return null;
        }
        log.info("查询出来的规则:{}",ruleModel);
        return StrategyAwardRuleModelVo.builder().ruleModel(ruleModel).build();

    }

    //装配RuleTreeVo
    @Override
    public RuleTreeVO queryRuleTreeVoByTreeId(String treeId) {

        //todo 考虑从redis中获取

        // 从数据库获取
        RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId(treeId);
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDao.queryRuleTreeNodeListByTreeId(treeId);
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId(treeId);

        // 1. tree node line 转换Map结构

        Map<String,List<RuleTreeNodeLineVO>> ruleTreeNodeLineMap = new HashMap<>();
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
        log.info("数据库查询的ruleTree:{}",ruleTree);

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

//    缓存奖品的库存
    @Override
    public void cacheStrategyAwardCount(String cacheKey, Integer awardCount) {
        Long cacheAwardCount = redisService.getAtomicLong(cacheKey);
        if(cacheAwardCount!=null){
            redisService.setAtomicLong(cacheKey,awardCount);
        }
    }

    //扣减库存
    @Override
    public Boolean subtractionAwardStock(String cacheKey) {
        //自减完剩余的库存
        long decr = redisService.decr(cacheKey);
        if (decr<0){
            redisService.setValue(cacheKey,0);
            return false;
        }
        // 例子 剩 99
        String lockKey=cacheKey+Constants.UNDERLINE+decr;
        //在redis中创建一个key_value ....99 lock
        Boolean lock = redisService.setNx(lockKey);
        if(!lock){
            log.info("策略奖品库存加锁失败：{}",lockKey);
        }
        return lock;
    }

    //写入延迟队列
    @Override
    public void awardStockConsumeSendQueue(StrategyAwardStockKeyVo build) {
        String cacheKey = Constants.redisKey.STRATEGY_AWARD_COUNT_QUERY_KEY;

        //创建队列消息
        RBlockingQueue<StrategyAwardStockKeyVo> blockingQueue = redisService.getBlockingQueue(cacheKey);
        //将队列 转换成 延迟队列
        RDelayedQueue<StrategyAwardStockKeyVo> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        //填入消息
        delayedQueue.offer(build,3, TimeUnit.SECONDS);

    }

    //获取队列值
    @Override
    public StrategyAwardStockKeyVo takeQueryValue() {
        String cacheKey = Constants.redisKey.STRATEGY_AWARD_COUNT_QUERY_KEY;
        RBlockingQueue<StrategyAwardStockKeyVo> blockingQueue = redisService.getBlockingQueue(cacheKey);
        return blockingQueue.poll();
    }

    //更新 数据库中的 库存表
    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setAwardId(awardId);
        strategyAward.setStrategyId(strategyId);
        strategyAwardDao.updateStrategyAwardStock(strategyAward);
    }

    @Override
    public StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId) {

        //优先从缓存中获取
        String cacheKey=Constants.redisKey.STRATEGY_AWARD_KEY+strategyId+Constants.UNDERLINE+awardId;
        log.info("cacheKey:{}",cacheKey);
        StrategyAwardEntity value = redisService.getValue(cacheKey);
        if (value!=null){
            log.info("test 缓存中获取 {}",value);
            return value;
        }
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setAwardId(awardId);
        strategyAward.setStrategyId(strategyId);
        StrategyAward entity=strategyAwardDao.queryStrategyAward(strategyAward);

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
        redisService.setValue(cacheKey,result);
        return result;
    }


}
