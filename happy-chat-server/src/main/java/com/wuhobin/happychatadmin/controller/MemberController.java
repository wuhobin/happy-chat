package com.wuhobin.happychatadmin.controller;

import com.wuhobin.api.CommonResult;
import com.wuhobin.cache.UserOnlineCache;
import com.wuhobin.service.user.MemberService;
import com.wuhobin.service.user.UserInfoService;
import com.wuhobin.vo.dto.UserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/11 18:15
 * @description
 */
@RestController
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserOnlineCache userOnlineCache;
    /**
     * 获取在线用户列表
     * @return
     */
    @PostMapping("/onlineMemberList")
    public CommonResult getOnlineMemberList(){
        try {
            Map<String, Object> map = new HashMap<>(8);
            List<UserInfoDto> onlineList =  memberService.onlineMemberList();
            map.put("onlineList",onlineList);
            map.put("onlineCount",userOnlineCache.getOnlineUserCount());
            return CommonResult.success(map);
        }catch (Exception e){
            log.info("【用户相关接口】 获取在线用户列表异常",e);
            return CommonResult.failed();
        }
    }

}
