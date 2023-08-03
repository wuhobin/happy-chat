package com.wuhobin.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author wuhongbin
 * @description: UserDetailService
 * @datetime 2023/01/12 16:58
 */
public interface UserDetailService extends UserDetailsService {

    /**
     * 根据电话号码查询用户
     *
     * @param mobile 手机号
     * @return 详情
     */
    UserDetails loadUserByMobile(String mobile);

}
