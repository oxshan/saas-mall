package cc.oxshan.user.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统角色实体
 */
@Data
public class SysRole {

    private Long id;
    private Long shopId;
    private Long templateId;
    private String code;
    private String name;
    private Integer type;
    private Integer isSystem;
    private Integer status;
    private Integer sort;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
