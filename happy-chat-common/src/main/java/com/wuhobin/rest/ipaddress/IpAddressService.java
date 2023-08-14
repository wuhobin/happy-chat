package com.wuhobin.rest.ipaddress;

import com.wuhobin.entity.ipaddress.AddressResult;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/14 12:02
 * @description
 */
public interface IpAddressService {

    /**
     * 根据ip地址 获取归属地信息
     * @param ip
     * @return
     */
    AddressResult getIpAddress(String ip);

}
