package org.example.domain.login.service;

import org.example.domain.login.model.Vo.UserLoginVo;
import org.example.domain.login.model.Vo.UserRegisterVo;
import org.example.domain.login.model.entity.UserAccountEntity;
import org.example.domain.login.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 登录注册Serviec
 * @date 2025/3/24 15:42
 */

@Service
public class LoginService implements ILoginService{



    @Autowired
    private LoginRepository loginRepository;
    @Override
    public String login(UserLoginVo userLoginVo) throws Exception {
       return loginRepository.Login(userLoginVo);
    }

    //退出登录
    @Override
    public Boolean quitLogin(String userName) {
        return loginRepository.quitLogin(userName);

    }

    //判断redis中是否有对应的用户信息
    @Override
    public UserAccountEntity IsUserAccountInRedis(String userId) {
        return loginRepository.IsUserAccountInRedis(userId);
    }

    //注册
    @Override
    public Boolean register(UserRegisterVo request) {
        //参数校验
        if (request.getUserName()==null||request.getPassword()==null) return false;
        //1 查看用户名是否已经存在
       return loginRepository.register(request);
    }
}
