package com.wuhobin.service.user.impl;

import com.wuhobin.cache.UserNoCache;
import com.wuhobin.dataobject.UserInfoDO;
import com.wuhobin.mapper.UserInfoMapper;
import com.wuhobin.service.user.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhobin.utils.BeanCopyUtil;
import com.wuhobin.utils.http.WebUtils;
import com.wuhobin.vo.UserInfoVO;
import com.wuhobin.vo.dto.UserInfoDto;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author wuhongbin
 * @since 2023-08-03
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoDO> implements UserInfoService {

    @Autowired
    private UserNoCache userNoCache;

    @Override
    public UserInfoVO selectUserByName(String username) {
        UserInfoDO one = lambdaQuery().eq(UserInfoDO::getNickName, username).last("limit 0,1").one();
        return BeanCopyUtil.copy(one,UserInfoVO.class);
    }

    @Override
    public UserInfoVO selectUserByMobile(String mobile) {
        UserInfoDO one = lambdaQuery().eq(UserInfoDO::getPhoneNum, mobile).last("limit 0,1").one();
        return BeanCopyUtil.copy(one,UserInfoVO.class);
    }

    @Override
    public UserInfoVO saveUserInfo(String mobile) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setPhoneNum(mobile);
        userInfoDO.setCreateTime(new Date());
        userInfoDO.setUpdateTime(new Date());
        userInfoDO.setLoginIp(WebUtils.getClientRealIp(request));
        userInfoDO.setNickName("星球用户" + userNoCache.getNewUserNo(mobile));
        userInfoDO.setLoginDate(new Date());
        save(userInfoDO);
        return BeanCopyUtil.copy(userInfoDO, UserInfoVO.class);
    }

    @Override
    public UserInfoVO getUserById(Long userId) {
        UserInfoDO one = lambdaQuery().eq(UserInfoDO::getId, userId).last("limit 0,1").one();
        return BeanCopyUtil.copy(one, UserInfoVO.class);
    }

    @Override
    public void updateUserAvatar(Long userId, String downloadUrl) {
        lambdaUpdate()
                .set(UserInfoDO::getAvatar,downloadUrl)
                .set(UserInfoDO::getUpdateTime,new Date())
                .eq(UserInfoDO::getId,userId)
                .update();
    }

    @Override
    public List<UserInfoDto> selectOnlineUserList(Set<Long> members) {
        List<UserInfoDO> list = lambdaQuery()
                .in(UserInfoDO::getId, members)
                .orderByDesc(UserInfoDO::getLoginDate)
                .list();
        return BeanCopyUtil.copy(list, UserInfoDto.class);
    }
}
