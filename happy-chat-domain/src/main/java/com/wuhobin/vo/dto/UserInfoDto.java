package com.wuhobin.vo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/11 18:16
 * @description
 */
@Data
public class UserInfoDto {

    private Long id;

    private String nickName;

    @ApiModelProperty("用户性别（0男 1女 2未知）")
    private Integer sex;

    @ApiModelProperty("头像地址")
    private String avatar;

    @ApiModelProperty("用户所在城市")
    private String city;

    @ApiModelProperty("最后登录时间")
    private Date loginDate;

}
