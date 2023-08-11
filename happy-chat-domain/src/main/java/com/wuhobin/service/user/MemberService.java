package com.wuhobin.service.user;

import com.wuhobin.vo.dto.UserInfoDto;

import java.util.List;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/11 18:29
 * @description
 */
public interface MemberService {

    /**
     * 获取在线用户列表
     * @return
     */
    List<UserInfoDto> onlineMemberList();
}
