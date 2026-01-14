package cc.oxshan.user.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统菜单实体
 */
@Data
public class SysMenu {

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
