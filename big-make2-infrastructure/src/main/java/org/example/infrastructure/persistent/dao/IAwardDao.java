package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/26 16:27
 */
@Mapper
public interface IAwardDao {
    String queryAwardConfigByAwardId(Integer awardId);

    String queryAwardKey(Integer awardId);
}
