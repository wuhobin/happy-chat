package com.wuhobin.service.user;

import com.wuhobin.dataobject.UserInfoDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhobin.vo.UserInfoVO;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author wuhongbin
 * @since 2023-08-03
 */
public interface UserInfoService extends IService<UserInfoDO> {

    /**
     * 根据username查找
     * @param username
     * @return
     */
    UserInfoVO selectUserByName(String username);

    /**
     * 根据手机号查找
     * @param mobile
     * @return
     */
    UserInfoVO selectUserByMobile(String mobile);

    /**
     * 保持用户
     * @param mobile
     * @return
     */
    UserInfoVO saveUserInfo(String mobile);

}
