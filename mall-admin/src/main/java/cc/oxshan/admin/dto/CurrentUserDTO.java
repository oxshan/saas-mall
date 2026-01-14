package cc.oxshan.admin.dto;

import lombok.Data;
import java.util.List;

/**
 * 当前用户信息 DTO
 */
@Data
public class CurrentUserDTO {

    private Long id;
    private Long shopId;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;
    
    /** 角色编码列表 */
    private List<String> roles;
    
    /** 权限标识列表 */
    private List<String> permissions;
}
