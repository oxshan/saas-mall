package cc.oxshan.admin.dto;

import lombok.Data;

/**
 * 登录响应
 */
@Data
public class LoginRspDTO {
    private String token;
    private Long userId;
    private Long shopId;
    private String username;
    private String nickname;
}
