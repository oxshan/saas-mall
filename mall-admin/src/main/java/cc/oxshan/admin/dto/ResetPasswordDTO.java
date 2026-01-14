package cc.oxshan.admin.dto;

import lombok.Data;

/**
 * 重置密码请求 DTO
 */
@Data
public class ResetPasswordDTO {
    
    private Long userId;
    private String newPassword;
}
