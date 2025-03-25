package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.infrastructure.persistent.po.UserAccount;


/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/24 11:27
 */
@Mapper
public interface IUserAccountDao {

    //查询用户
    UserAccount queryByUseName(@Param("userName") String userName);

}
