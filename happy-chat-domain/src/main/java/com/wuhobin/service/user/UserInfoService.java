package com.wuhobin.service.user;

import com.wuhobin.dataobject.UserInfoDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhobin.vo.UserInfoVO;
import com.wuhobin.vo.dto.UserInfoDto;

import java.util.List;
import java.util.Set;

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


    /**
     * 根据用户id获取
     * @param userId
     * @return
     */
    UserInfoVO getUserById(Long userId);

    /**
     * 更新用户头像
     * @param userId
     * @param downloadUrl
     */
    void updateUserAvatar(Long userId, String downloadUrl);

    /**
     * 获取在线用户列表
     * @param members
     * @return
     */
    List<UserInfoDto> selectOnlineUserList(Set<Long> members);
}
