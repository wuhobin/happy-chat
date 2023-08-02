package com.wuhobin.utils.http;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/8/1.
 */
@Slf4j
public class CookieUtil {

    /**
     * 查询cookie值
     * @param request
     * @param name
     * @return 查不到返回null
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        if (Strings.isNullOrEmpty(name)) {
            return null;
        }
        String result = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return result;
    }

    public static void addCookie(HttpServletResponse response, String name, String value){
        try {
            Cookie cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
            cookie.setPath("/");
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
        }catch (Exception e){
            log.error("设置cookie异常",e);
        }
    }
}
