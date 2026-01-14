package cc.oxshan.user.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统用户实体
 */
@Data
public class SysUser {

    private Long id;
    private Long shopId;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
