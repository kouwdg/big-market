package org.example.domain.award.service.distribute;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.event.SendAwardMessageEvent;
import org.example.domain.activity.repository.IAwardRepository;
import org.example.domain.award.model.entity.DistributeAwardEntity;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 奖品发放的Service
 * @date 2025/3/27 8:29
 */
@Service
@Slf4j
public class AwardDistributionService implements IAwardDistributionService {

    private IAwardRepository awardRepository;

    private SendAwardMessageEvent sendAwardMessageEvent;
    private final Map<String,IDistributeAward> distributeAwardMap;

    public AwardDistributionService(
            IAwardRepository awardRepository,
            SendAwardMessageEvent sendAwardMessageEvent,
            Map<String, IDistributeAward> distributeAwardMap
            ) {
        this.awardRepository = awardRepository;
        this.sendAwardMessageEvent = sendAwardMessageEvent;
        this.distributeAwardMap = distributeAwardMap;
    }

    //查询奖品的key
    @Override
    public String queryAwardKey(Integer awardId) {
       String awardKey=awardRepository.queryAwardKey(awardId);
       if (awardKey==null){
           log.info("分发奖品 奖品ID存在");
           return null;
       }

        IDistributeAward distributeAward = distributeAwardMap.get(awardKey);
       if (distributeAward==null){
           log.error("分发奖品 对应的奖品key不存在");
           //todo 后续完善全部奖品开启异常
           //throw new RuntimeException("分发奖品，奖品："+awardId+" 对应的服务不存在");
       }
        return awardKey;
    }

    //发放奖品
    @Override
    public void distributeAward(DistributeAwardEntity entity) {
        //查询awardKey
        String awardKey=awardRepository.queryAwardKey(entity.getAwardId());
        if (awardKey==null){
            log.info("分发奖品 奖品ID存在");
            return;
        }
        IDistributeAward distributeAward = distributeAwardMap.get(awardKey);
        if (distributeAward==null){
            log.error("分发奖品 对应的奖品key不存在");
            //todo 后续完善全部奖品开启异常
            //throw new RuntimeException("分发奖品，奖品："+awardId+" 对应的服务不存在");
            return;
        }
        //发放具体类型的奖品
        distributeAward.distributeAward(entity);
    }
}
