package org.example.domain.login.repository;

import org.example.domain.login.model.Vo.UserLoginVo;
import org.example.domain.login.model.Vo.UserRegisterVo;
import org.example.domain.login.model.entity.UserAccountEntity;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/24 15:45
 */
public interface LoginRepository {



    String Login( UserLoginVo userLoginVo) throws Exception;
    //判断redis中是否有对应的用户信息
    UserAccountEntity IsUserAccountInRedis(String userId);

    Boolean quitLogin(String userName);

    Boolean register(UserRegisterVo request);
}
