package org.example.infrastructure.repository;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain.login.model.Vo.UserLoginVo;
import org.example.domain.login.model.Vo.UserRegisterVo;
import org.example.domain.login.model.entity.UserAccountEntity;
import org.example.domain.login.repository.LoginRepository;
import org.example.infrastructure.persistent.dao.IRaffleActivityAccountDao;
import org.example.infrastructure.persistent.dao.IUserAccountDao;
import org.example.infrastructure.persistent.po.RaffleActivityAccount;
import org.example.infrastructure.persistent.po.UserAccount;
import org.example.types.Utils.JWTUtil;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.UUID;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private IUserAccountDao userAccountDao;

    @Autowired
    private IRaffleActivityAccountDao raffleActivityAccountDao;

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
        loginToken.put("userId:" + userId, JSON.toJSONString(userDetails.getUserAccount()), 30, TimeUnit.MINUTES);
        return jwt;

    }

    //判断redis中是否有对应的用户信息
    @Override
    public UserAccountEntity IsUserAccountInRedis(String userId) {
        RMapCache<Object, Object> loginToken = redissonClient.getMapCache("login_token");
        if (loginToken.containsKey("userId:" + userId)) {

            String jsonString = (String) loginToken.get("userId:" + userId);
            UserAccount userAccount = JSON.parseObject(jsonString, UserAccount.class);
            if (userAccount == null) return null;
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

    @Override
    public Boolean register(UserRegisterVo request) {
        //1 查询用户名是否已经存在
        UserAccount userAccount = userAccountDao.queryByUseName(request.getUserName());
        if (userAccount != null) {
            return false;
        }
        //2 添加到数据库
        String password = passwordEncoder.encode(request.getPassword());
        String userName = request.getUserName();

        //3 随机生成用户ID
        String userId = RandomStringUtils.randomNumeric(12);
        RaffleActivityAccount account = RaffleActivityAccount.builder()
                .userId(userName)
                .activityId(101L)
                .totalCount(0)
                .totalCountSurplus(0)
                .dayCount(0)
                .dayCountSurplus(0)
                .monthCount(0)
                .monthCountSurplus(0).build();
        //创建用户抽奖次数账户
        raffleActivityAccountDao.insert(account);

        UserAccount build = UserAccount.builder().userId(userId)
                .password(password)
                .userName(userName).build();
        int i = userAccountDao.insert(build);
        return i == 1;
    }
}
