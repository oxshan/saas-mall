package cc.oxshan.api.user.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 系统用户 DTO
 */
@Data
public class SysUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long shopId;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;
}
