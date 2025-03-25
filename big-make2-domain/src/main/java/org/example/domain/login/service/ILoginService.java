package org.example.domain.login.service;

import org.example.domain.login.model.Vo.UserLoginVo;
import org.example.domain.login.model.entity.UserAccountEntity;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/24 15:42
 */
public interface ILoginService {

    //登录
    String login(UserLoginVo userLoginVo) throws Exception;

    //退出登录
    Boolean quitLogin(String userName);


    //判断redis中是否有对应的用户信息
    UserAccountEntity IsUserAccountInRedis(String userId);

}
