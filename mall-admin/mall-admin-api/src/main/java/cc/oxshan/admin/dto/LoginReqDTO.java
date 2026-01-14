package cc.oxshan.admin.dto;

import lombok.Data;

/**
 * 登录请求
 */
@Data
public class LoginReqDTO {
    private String username;
    private String password;
}
