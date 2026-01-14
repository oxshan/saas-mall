package cc.oxshan.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举
 */
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {

    // 通用错误码 1xxx
    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(1001, "系统异常"),
    PARAM_ERROR(1002, "参数错误"),
    DATA_NOT_FOUND(1003, "数据不存在"),

    // 认证错误码 2xxx
    AUTH_FAILED(2001, "用户名或密码错误"),
    ACCOUNT_DISABLED(2002, "账号已被禁用"),
    TOKEN_INVALID(2003, "Token无效"),
    TOKEN_EXPIRED(2004, "Token已过期"),
    UNAUTHORIZED(2005, "未登录或登录已过期"),

    // 权限错误码 3xxx
    PERMISSION_DENIED(3001, "没有操作权限"),
    ROLE_NOT_FOUND(3002, "角色不存在");

    private final int code;
    private final String msg;
}
