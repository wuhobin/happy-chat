package com.wuhobin.api;

/**
 * 枚举了一些常用API操作码
 *
 * @author whb
 */
public enum ResultCode implements IErrorCode {

    /**
     * 返回信息
     */
    SUCCESS(200, "操作成功"),
    FAILED(-500, "系统异常"),
    VALIDATE_FAILED(400, "参数检验失败"),
    UNAUTHORIZED(401, "未登录"),
    FORBIDDEN(403, "无权限,请先登录"),

    /**
     * 用户相关
     */
    USER_NO_REGISTER(-1001,"用户暂未注册"),
    VERIFY_CODE_ERROR(-1002,"验证码错误"),
    VERIFY_CODE_NOT_CLICK(-1003,"请先获取验证码"),
    MSG_NOT_LOGIN(-1004,"未登录，请重新登录！"),
    ASTRICT_LOGIN(-1005, "验证码连续错误三次，限制登录两小时"),
    LOGIN_FAIL(-1006,"登录失败，token生成失败"),
    PLEASE_TRY_LATER(-1007, "还未到限制发送时间，稍后再试"),
    SEND_VERIFY_CODE_ERR(-1008, "生成验证码异常，请稍后再试");




    ;

	private long code;
    private String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
