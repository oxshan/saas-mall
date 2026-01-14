package cc.oxshan.api.user.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 系统菜单 DTO
 */
@Data
public class SysMenuDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long parentId;
    private String name;
    private String path;
    private String component;
    private String perms;
    private Integer type;
    private String icon;
    private Integer sort;
    private Integer visible;
    private Integer status;
    
    /**
     * 子菜单列表（用于树形结构）
     */
    private List<SysMenuDTO> children;
}
