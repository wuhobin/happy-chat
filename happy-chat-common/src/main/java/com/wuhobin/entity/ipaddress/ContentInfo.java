package com.wuhobin.entity.ipaddress;

import lombok.Data;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/14 12:07
 * @description
 */
@Data
public class ContentInfo {

    private String address;

    private AddressInfo address_detail;
}
