package com.zyun.auth.service.impl;

import com.zyun.auth.client.UserControllerClient;
import com.zyun.auth.dto.UserJwt;
import com.zyun.model.system.user.ext.UserExt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UserDetailServiceImpl
 * @Author: zsp
 * @Date 2021/3/16 12:21
 * @Description: 用户认证类
 * @Version 1.0
 */
@Component("UserDetailServiceImpl")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private UserControllerClient userControllerClient;

    /**
     * 获取用户信息
     * @param username 用户名
     * @return UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 如果没有认证消息就去数据库或内存中查询客户端ZYunWebApp是否存在
        if(authentication == null){
            // 因为在配置中使用了JdbcClientDetailsService, 所以此处会去数据库查询校验配置的客户端名称和密码
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if(clientDetails!=null){
                //密码
                String clientSecret = clientDetails.getClientSecret();
                return new User(username,clientSecret,AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }
        // 账号为空 认证失败
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        // 请求用户中心，查询账号信息,获取用户信息，权限，等信息
        UserExt userExt = userControllerClient.getUserByUsername(username);
        // 为空表示用户不存在
        if(userExt == null)
            return null;
        // 取出正确密码
        String password = userExt.getPassword();

        // 用户权限，这里暂时使用静态数据，最终会从数据库读取
        List<String> permissions = userExt.getPermissions();
        List<String> user_permission = new ArrayList<>();
        if (permissions.size() > 0)
            permissions.forEach(item-> user_permission.add(item));
        // 添加权限到auth
        String user_permission_string  = StringUtils.join(user_permission.toArray(), ",");
        UserJwt userDetails = new UserJwt(username,
                password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(user_permission_string));
        userDetails.setId(userExt.getId());
        userDetails.setName(userExt.getName());//用户名称
        userDetails.setRoleId(userExt.getRoleId());
        return userDetails;
    }
}
