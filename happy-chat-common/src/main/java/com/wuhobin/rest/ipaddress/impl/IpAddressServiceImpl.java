package com.wuhobin.rest.ipaddress.impl;

import cn.hutool.json.JSONUtil;
import com.wuhobin.config.base.BasePlatformConfig;
import com.wuhobin.entity.ipaddress.AddressResult;
import com.wuhobin.rest.ipaddress.IpAddressService;
import com.wuhobin.utils.http.WebUtils;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/14 12:02
 * @description
 */
@Slf4j
@Component
public class IpAddressServiceImpl implements IpAddressService {

    @Autowired
    private BasePlatformConfig basePlatformConfig;


    @Override
    public AddressResult getIpAddress(String ip) {
        String url = "";
        try {
            url = new StringBuilder().append("https://api.map.baidu.com/location/ip?ip=").append(ip).append("&ak=").append(basePlatformConfig.getBaiduAk()).toString();
            String result = WebUtils.doGet(url, null);
            log.info("获取ip归属地, url={},result={}",url,result);
            AddressResult addressResult = JSONUtil.toBean(result, AddressResult.class);
            if (!addressResult.getStatus().equals("0")){
                return null;
            }
            return addressResult;
        } catch (Exception e) {
            log.error("获取ip归属异常 url={}", url, e);
        }
        return null;
    }
}
