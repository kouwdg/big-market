package org.example.infrastructure.repository;

import org.example.infrastructure.persistent.dao.IUserAccountDao;
import org.example.infrastructure.persistent.po.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author 程宇乐
 * @version 1.0
 * @description:
 * @date 2025/3/24 11:58
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserAccountDao userDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1 查询用户信息
        System.out.println("查询用户信息");
        UserAccount user = userDao.queryByUseName(username);
        if (user==null){
            //抛出异常
            throw new UsernameNotFoundException(username);
        }

        //todo 查询对应的权限信息

        //把数据封装成UserDetails对象进行返回
        return new UserDetailsImpl(user);
    }
}
