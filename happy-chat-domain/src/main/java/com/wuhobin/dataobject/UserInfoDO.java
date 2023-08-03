package com.wuhobin.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author wuhongbin
 * @since 2023-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user_info")
@ApiModel(value = "UserInfoDO对象", description = "用户信息表")
public class UserInfoDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("手机号码")
    private String phoneNum;

    @ApiModelProperty("用户性别（0男 1女 2未知）")
    private Integer sex;

    @ApiModelProperty("头像地址")
    private String avatar;

    @ApiModelProperty("帐号状态（0正常 1停用）")
    private Integer status;

    @ApiModelProperty("最后登录IP")
    private String loginIp;

    @ApiModelProperty("用户所在城市")
    private String city;

    @ApiModelProperty("最后登录时间")
    private Date loginDate;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
