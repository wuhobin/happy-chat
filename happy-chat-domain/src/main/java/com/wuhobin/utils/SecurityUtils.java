package com.wuhobin.utils;


import com.wuhobin.api.ResultCode;
import com.wuhobin.config.exception.ServiceException;
import com.wuhobin.vo.UserInfoVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全服务工具类
 * 
 * @author huxiaohui
 */
public class SecurityUtils
{
    /**
     * 用户ID
     **/
    public static Long getUserId()
    {
        try
        {
            return getLoginUser().getId();
        }
        catch (Exception e)
        {
            throw new ServiceException("获取用户ID异常", (int) ResultCode.UNAUTHORIZED.getCode());
        }
    }



    /**
     * 获取用户
     **/
    public static UserInfoVO getLoginUser()
    {
        try
        {
            return (UserInfoVO) getAuthentication().getPrincipal();
        }
        catch (Exception e)
        {
            throw new ServiceException("获取用户信息异常", (int) ResultCode.UNAUTHORIZED.getCode());
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    /**
     * 是否为管理员
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }
}
