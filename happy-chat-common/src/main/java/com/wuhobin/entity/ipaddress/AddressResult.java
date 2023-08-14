package com.wuhobin.entity.ipaddress;

import lombok.Data;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/14 12:06
 * @description
 */
@Data
public class AddressResult {

    private String address;

    private ContentInfo content;

    private String status;

}
