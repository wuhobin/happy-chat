package com.wuhobin.service.user.impl;

import com.wuhobin.cache.UserOnlineCache;
import com.wuhobin.service.user.MemberService;
import com.wuhobin.service.user.UserInfoService;
import com.wuhobin.vo.dto.UserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/11 18:29
 * @description
 */
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserOnlineCache userOnlineCache;

    @Override
    public List<UserInfoDto> onlineMemberList() {
        Set<Long> members = userOnlineCache.getMembers();
        if (CollectionUtils.isEmpty(members)) {
            return Collections.emptyList();
        }
        List<UserInfoDto> list = userInfoService.selectOnlineUserList(members);
        return list;
    }
}
