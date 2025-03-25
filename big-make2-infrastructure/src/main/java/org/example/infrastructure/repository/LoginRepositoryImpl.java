package org.example.infrastructure.repository;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.login.model.Vo.UserLoginVo;
import org.example.domain.login.model.entity.UserAccountEntity;
import org.example.domain.login.repository.LoginRepository;
import org.example.infrastructure.persistent.dao.IUserAccountDao;
import org.example.infrastructure.persistent.po.UserAccount;
import org.example.types.Utils.JWTUtil;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/24 15:45
 */
@Repository
@Slf4j
public class LoginRepositoryImpl implements LoginRepository {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private IUserAccountDao userAccountDao;
    @Override
    public String Login(UserLoginVo userLoginVo) {
        //校验参数
        if (userLoginVo.getUserName() == null || userLoginVo.getPassword() == null) {
            return null;
        }


        UsernamePasswordAuthenticationToken userInfo =
                new UsernamePasswordAuthenticationToken(userLoginVo.getUserName(), userLoginVo.getPassword());
        //进行用户认证
        Authentication authenticate = authenticationManager.authenticate(userInfo);
        //认证未通过 给出对应的提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }
        //如果认证通过 生成jwt
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        String userId = userDetails.getUserAccount().getUserId();
        String jwt = JWTUtil.createJWT(userId, userId, null);


        //放到redis中
        RMapCache<Object, Object> loginToken = redissonClient.getMapCache("login_token");
        loginToken.put("userId:" + userId,JSON.toJSONString(userDetails.getUserAccount()), 30, TimeUnit.MINUTES);
        return jwt;

    }
    //判断redis中是否有对应的用户信息
    @Override
    public UserAccountEntity IsUserAccountInRedis(String userId) {
        RMapCache<Object, Object> loginToken = redissonClient.getMapCache("login_token");
        if (loginToken.containsKey("userId:"+userId)){

            String jsonString =(String) loginToken.get("userId:" + userId);
            UserAccount userAccount = JSON.parseObject(jsonString, UserAccount.class);
            if(userAccount==null)return null;
           return UserAccountEntity.builder()
                   .id(userAccount.getId())
                   .userId(userAccount.getUserId())
                   .userName(userAccount.getUserName())
                   .roles(userAccount.getRoles())
                   .build();
        }
        return null;
    }

    @Override
    public Boolean quitLogin(String userName) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = userAccountDao.queryByUseName(userName);

        RMapCache<Object, Object> mapCache = redissonClient.getMapCache("login_token");
        mapCache.remove("userId:" + userAccount.getUserId());
        return true;
    }
}
