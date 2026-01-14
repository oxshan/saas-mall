package cc.oxshan.api.user.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 系统角色 DTO
 */
@Data
public class SysRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long shopId;
    private String code;
    private String name;
    private Integer type;
    private Integer status;
}
